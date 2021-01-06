package ncu.im3069.demo.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Order;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.demo.util.DBMgr;
import ncu.im3069.demo.app.OrderHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/order.do")
public class OrderController extends HttpServlet {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	private Connection conn = null;
	private PreparedStatement pres = null;
    /** ph，ProductHelper 之物件與 Product 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /** oh，OrderHelper 之物件與 order 相關之資料庫方法（Sigleton） */
	private OrderHelper oh =  OrderHelper.getHelper();

    public OrderController() {
        super();
    }

    /**
     * 處理 Http Method 請求 GET 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JsonReader 之 Request 參數 */
        String id = jsr.getParameter("id");

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
        if (!id.isEmpty()) {
          /** 透過 orderHelper 物件的 getByID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = oh.getById(id);
          resp.put("status", "200");
          resp.put("message", "單筆訂單資料取得成功");
          resp.put("response", query);
        }
        else {
          /** 透過 orderHelper 物件之 getAll() 方法取回所有訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = oh.getAll();
          resp.put("status", "200");
          resp.put("message", "所有訂單資料取得成功");
          resp.put("response", query);
        }

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}

    /**
     * 處理 Http Method 請求 POST 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到 JSONObject 之 Request 參數 */
        String phone_number = jso.getString("phone_number");
        //String name = jso.getString("name");
        String state_of_order = jso.getString("state_of_order");
        JSONArray item = jso.getJSONArray("drinkname");
        JSONArray pid = jso.getJSONArray("pid");
        JSONArray ice = jso.getJSONArray("ice");
        JSONArray sugar = jso.getJSONArray("sugar");
        JSONArray price = jso.getJSONArray("price");
        JSONArray amount = jso.getJSONArray("amount");
        JSONArray cup = jso.getJSONArray("cup");
        String exexcute_sql = "";
        //JSONArray quantity = jso.getJSONArray("quantity");

        /** 建立一個新的訂單物件 */
        Order od = new Order(phone_number, state_of_order);
        JSONObject result = oh.create(od);
        /** 將每一筆訂單細項取得出來 */
        for(int i=0 ; i < item.length() ; i++) {
        	 conn = DBMgr.getConnection();
        	 try {
                 /** 取得資料庫之連線 */
                 conn = DBMgr.getConnection();
                 /** SQL指令 */
                 String sql = "INSERT INTO `missa`.`orderdetails`(`id_Order`,`id_Product`, `specification_Product`, `quantity_Product`, `price_Product`, `id_Cup`,`quantity_Cup`,`price_Cup`,`price_Coupon`)"
                         + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                 
                 /** 將參數回填至SQL指令當中 */
                 pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                 pres.setInt(1, (int)result.getLong("order_id"));
                 pres.setInt(2,Integer.parseInt( pid.getString(i)));
                 pres.setString(3, ice.getString(i)+sugar.getString(i));
                 pres.setInt(4, Integer.parseInt(amount.getString(i)));
                 //String test =price.getString(i);
                 //String[] strprice=test.split("$");
                 pres.setInt(5, Integer.parseInt(price.getString(i)));
                 pres.setString(6, cup.getString(i));
                 pres.setInt(7, Integer.parseInt(amount.getString(i)));
                 pres.setInt(8, 30);
                 pres.setInt(9, 35);
                 
                 /** 執行新增之SQL指令並記錄影響之行數 */
                 pres.executeUpdate();
                 
                 /** 紀錄真實執行的SQL指令，並印出 **/
                 exexcute_sql = pres.toString();
                 System.out.println(exexcute_sql);
                 
                 ResultSet rs = pres.getGeneratedKeys();

                 if (rs.next()) {
                     //long id = rs.getLong(1);
                     //jsa.put(id);
                 }
             } catch (SQLException e) {
                 /** 印出JDBC SQL指令錯誤 **/
                 System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
             } catch (Exception e) {
                 /** 若錯誤則印出錯誤訊息 */
                 e.printStackTrace();
             } finally {
                 /** 關閉連線並釋放所有資料庫相關之資源 **/
                 DBMgr.close(pres, conn);
             }
        	 
            String product_id = item.getString(i);
            //int amount = quantity.getInt(i);

            /** 透過 ProductHelper 物件之 getById()，取得產品的資料並加進訂單物件裡 /
            Product pd = ph.getById(product_id);
            od.addOrderProduct(pd, amount);
        }

        /** 透過 orderHelper 物件的 create() 方法新建一筆訂單至資料庫 */
        }
        
        System.out.println(result.getLong("order_id"));

        /** 設定回傳回來的訂單編號與訂單細項編號 */
        //od.setId((int) result.getLong("order_id"));
        od.setOrderProductId(result.getJSONArray("order_product_id"));

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "訂單新增成功！");
        resp.put("response", od.getOrderAllInfo());

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}

}
