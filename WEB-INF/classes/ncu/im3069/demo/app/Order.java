package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.json.*;

public class Order {

    /** id，訂單編號 */
    private int id;
	
	private String  stateOforder;

    /** first_name，會員姓名 */
    private int id_Member;

    /** last_name，會員姓 */
    private String phoneNumber_Member;

    /** email，會員電子郵件信箱 */
    private String name_Member;

    /** address，會員地址 */
    private String sn_Coupon;


    /** list，訂單列表 */
    private ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();

    /** create，訂單創建時間 */
    private Timestamp createofOrder;

    /** modify，訂單修改時間 */
    private Timestamp modifyOforder;

    /** oph，OrderItemHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private OrderDetailHelper oph = OrderDetailHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單資料時，產生一個新的訂單
     *
     * @param first_name 會員名
     * @param last_name 會員姓
     * @param email 會員電子信箱
     * @param address 會員地址
     * @param phone 會員姓名
     */
    			
    public Order(String stateOforder, int id_Member, String phoneNumber_Member, String name_Member, String sn_Coupon) {
        this.stateOforder = stateOforder;
        this.id_Member = id_Member;
        this.phoneNumber_Member = phoneNumber_Member;
        this.name_Member = name_Member;
        this.sn_Coupon = sn_Coupon;
        this.createofOrder = Timestamp.valueOf(LocalDateTime.now());
        this.modifyOforder = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單資料時，新改資料庫已存在的訂單
     *
     * @param first_name 會員名
     * @param last_name 會員姓
     * @param email 會員電子信箱
     * @param address 會員地址
     * @param phone 會員姓名
     * @param create 訂單創建時間
     * @param modify 訂單修改時間
     */
    public Order(int id, String stateOforder, int id_Member, String phoneNumber_Member, String name_Member, String sn_Coupon, Timestamp createofOrder, Timestamp modifyOforder) {
        this.id = id;
        this.stateOforder = stateOforder;
        this.id_Member = id_Member;
        this.phoneNumber_Member = phoneNumber_Member;
        this.name_Member = name_Member;
        this.sn_Coupon = sn_Coupon;
        this.createofOrder = createofOrder;
        this.modifyOforder = modifyOforder;
        //getOrderProductFromDB();
    }

    /**
     * 新增一個訂單產品及其數量
     */
    public void addOrderProduct(Product pd, int quantity) {
        this.list.add(new OrderDetail(pd, quantity));
    }

    /**
     * 新增一個訂單產品
     */
    public void addOrderProduct(OrderDetail op) {
        this.list.add(op);
    }

    /**
     * 設定訂單編號
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * 取得訂單會員的名
     *
     * @return String 回傳訂單會員的名
     */
    public String getStateOforder() {
        return this.stateOforder;
    }

    /**
     * 取得訂單會員的姓
     *
     * @return String 回傳訂單會員的姓
     */
    public int getId_Member() {
        return this.id_Member;
    }

    /**
     * 取得訂單信箱
     *
     * @return String 回傳訂單信箱
     */
    public String getPhoneNumber_Member() {
        return this.phoneNumber_Member;
    }
    
    
    public String getName_Member() {
    	return this.name_Member;
    }
    
    public String getSn_Coupon() {
    	return this.sn_Coupon;
    }

    /**
     * 取得訂單創建時間
     *
     * @return Timestamp 回傳訂單創建時間
     */
    public Timestamp getCreateTime() {
        return this.createofOrder;
    }
    
    

    /**
     * 取得訂單修改時間
     *
     * @return Timestamp 回傳訂單修改時間
     */
    public Timestamp getModifyTime() {
        return this.modifyOforder;
    }

    
    /**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public ArrayList<OrderDetail> getOrderProduct() {
        return this.list;
    }

    /**
     * 從 DB 中取得訂單產品
     */
    private void getOrderProductFromDB() {
        ArrayList<OrderDetail> data = oph.getOrderProductByOrderId(this.id);
        this.list = data;
    }
    
    /**
     * 取得訂單基本資料
     *
     * @return JSONObject 取得訂單基本資料
     */
    public JSONObject getOrderData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("stateOforder", getStateOforder());
        jso.put("id_Member", getId_Member());
        jso.put("phoneNumber_Member", getPhoneNumber_Member());
        jso.put("name_Member", getName_Member());
        jso.put("sn_Coupon", getSn_Coupon());
        jso.put("createofOrder", getCreateTime());
        jso.put("modifyOforder", getModifyTime());

        return jso;
    }

    /**
     * 取得訂單產品資料
     *
     * @return JSONArray 取得訂單產品資料
     */
    public JSONArray getOrderProductData() {
        JSONArray result = new JSONArray();  
   
        for(int i=0 ; i < this.list.size() ; i++) {
   
            result.put(this.list.get(i).getData());
            System.out.println(i);
        }

        return result;
    }

    /**
     * 取得訂單所有資訊
     *
     * @return JSONObject 取得訂單所有資訊
     */
    public JSONObject getOrderAllInfo() {
        JSONObject jso = new JSONObject();
        jso.put("order_info", getOrderData());
        jso.put("product_info", getOrderProductData());
        

        return jso;
    }

    /**
     * 設定訂單產品編號
     */
    public void setOrderProductId(JSONArray data) {
        for(int i=0 ; i < this.list.size() ; i++) {
            this.list.get(i).setId((int) data.getLong(i));
        }
    }

}
