package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.json.*;

public class Order {

    /** id，訂單編號 */
    private int id;

    private String item;

    private String paymentMethod;

    private Timestamp dateOforder;

    private String stateOforder;

    private int id_Member;
    
    private String phoneNumber_Member;
    
    private String name_Member;
    
    /** list，訂單列表 */
    private ArrayList<OrderItem> list = new ArrayList<OrderItem>();

    /** oph，OrderItemHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private OrderItemHelper oph = OrderItemHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單資料時，產生一個新的訂單
     *
     * @param phoneNumber_Member
     * @param name_Member
     * @param paymentMethod
     * @param stateOforder
     */
    
    public Order(String phoneNumber_Member, String name_Member, String paymentMethod, String stateOforder) {
        this.phoneNumber_Member = phoneNumber_Member;
        this.name_Member = name_Member;
        this.paymentMethod = paymentMethod;
        this.stateOforder = stateOforder;
        this.dateOforder = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單資料時，新改資料庫已存在的訂單
     *
     * @param phoneNumber_Member
     * @param name_Member
     * @param paymentMethod
     * @param dateOforder 
     * @param stateOforder
     */
    
    public Order(int id, String phoneNumber_Member, String name_Member, String paymentMethod, Timestamp dateOforder, String stateOforder) {
        this.id = id;
        this.phoneNumber_Member = phoneNumber_Member;
        this.name_Member = name_Member;
        this.paymentMethod = paymentMethod;
        this.stateOforder = stateOforder;
        this.dateOforder = Timestamp.valueOf(LocalDateTime.now());
        getOrderProductFromDB();
    }

    //新增一個訂單產品及其數量
    public void addOrderProduct(Product pd, int quantity) {
        this.list.add(new OrderItem(pd, quantity));
    }

    //新增一個訂單產品
    public void addOrderProduct(OrderItem op) {
        this.list.add(op);
    }

    //設定訂單編號
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
     * 取得訂單會員的姓名
     *
     * @return String 回傳訂單會員的姓名
     */
    public String getName() {
        return this.name_Member;
    }
    
    /**
     * 取得訂單電話
     *
     * @return String 回傳訂單電話
     */
    public String getPhone() {
        return this.phoneNumber_Member;
    }
    
    /**
     * 取得付款方式
     *
     * @return String 回傳付款方式
     */
    public String getPaymentMethod() {
        return this.paymentMethod;
    }
    
    /**
     * 取得訂單狀態
     *
     * @return String 回傳訂單追蹤
     */
    public String getStateOforder() {
        return this.stateOforder;
    }

    /**
     * 取得訂單創建時間
     *
     * @return String 回傳訂單創建時間
     */
    public Timestamp getCreateTime() {
        return this.dateOforder;
    }

    /**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public ArrayList<OrderItem> getOrderProduct() {
        return this.list;
    }

    /**
     * 從 DB 中取得訂單產品
     */
    private void getOrderProductFromDB() {
        ArrayList<OrderItem> data = oph.getOrderProductByOrderId(this.id);
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
        jso.put("name", getName());
        jso.put("phone", getPhone());
        jso.put("payment_method", getPaymentMethod());
        jso.put("state_of_order", getStateOforder());
        jso.put("create_time", getCreateTime());

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
