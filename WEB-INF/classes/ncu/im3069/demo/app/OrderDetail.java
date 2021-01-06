package ncu.im3069.demo.app;

import org.json.JSONObject;
import ncu.im3069.demo.util.Arith;

public class OrderDetail {
   
	/** 訂單細項編號 */
    private int id;
    
    /** 訂單編號 */
    private int id_Order;
    
    /** 產品細項編號 */
    private int id_Product;
    
    /** 副商品編號 */
    private int id_Cup;

    /** pd，產品 */
    private Product pd;
    
    /** 產品規格 */
    private String specification_Product ;

    /** 產品數量 */
    private int quantity_Product;
    
    /** 副商品數量 */
    private int quantity_Cup;

    /** 產品價格 */
    private int price_Product;
    
    /** 副商品價格 */
    private int price_Cup;
    
    /** 優惠券面額 */
    private int price_Coupon;

    /** ph，ProductHelper 之物件與 OrderDetail 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();
    
    public OrderDetail(Product pd, int quantity_Product ) {
        this.pd = pd;
        this.quantity_Product = quantity_Product;
        
    }
    /**
     * 實例化（Instantiates）一個新的（new）OrderItem 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單細項時
     *
     */
    public OrderDetail(Product pd, int quantity_Product, String specification,int id_Cup, int quantity_Cup, int price_Coupon ) {
        this.pd = pd;
        this.quantity_Product = quantity_Product;
        this.specification_Product = specification;
        this.id_Cup = id_Cup;
        this.quantity_Cup = quantity_Cup;
        this.price_Coupon = price_Coupon;
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）OrderItem 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單細項時
     *
     */
    
    public OrderDetail(int orderdetail_id, int id_Order, int id_Product, String specification, int quantity_Product,
    		int price_Product, int id_Cup, int quantity_Cup, int price_Cup, int price_Coupon ) {
    	
    	this.id = orderdetail_id;
        this.id_Order = id_Order;
        this.id_Product = id_Product;
        this.specification_Product = specification;
        this.quantity_Product = quantity_Product;
        this.price_Product = price_Product;
        this.id_Cup = id_Cup;
        this.quantity_Cup = quantity_Cup;
        this.price_Cup = price_Cup;
        this.price_Coupon = price_Coupon;
        getProductFromDB(id_Product);
    }
    
    public OrderDetail(int id_Order, int id_Product, String specification, int quantity_Product,
    		int price_Product, int id_Cup, int quantity_Cup, int price_Cup, int price_Coupon ) {
    	
    
        this.id_Order = id_Order;
        this.id_Product = id_Product;
        this.specification_Product = specification;
        this.quantity_Product = quantity_Product;
        this.price_Product = price_Product;
        this.id_Cup = id_Cup;
        this.quantity_Cup = quantity_Cup;
        this.price_Cup = price_Cup;
        this.price_Coupon = price_Coupon;
        getProductFromDB(id_Product);
    }
    
    public OrderDetail(int orderdetail_id, int id_Order, int id_Product, int price_Product, int quantity_Product) {
        this.id = orderdetail_id;
        this.id_Order = id_Order;
        this.price_Product = price_Product;
        this.quantity_Product = quantity_Product;
        getProductFromDB(id_Product);
    }

    /**
     * 從 DB 中取得產品
     */
    private void getProductFromDB(int id_Product) {
        String id = String.valueOf(id_Product);
        this.pd = ph.getById(id);
    }
    
    /**
     * 取得產品
     *
     * @return Product 回傳產品
     */
    public Product getProduct() {
        return this.pd;
    }
    
    /**
     * 設定訂單細項編號
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * 取得訂單細項編號
     *
     * @return int 回傳訂單細項編號
     */
    public int getId() {
        return this.id;
    }
    
    
    public int getId_Order() {
        return this.id_Order;
    }
    
    public int getId_Product() {
        return this.id_Product;
    }
    
    public int getId_Cup() {
        return this.id_Cup;
    }
    public String getSpecification() {
        return this.specification_Product;
    }
 
    public int getPrice_Product() {
        return this.price_Product;
    }

    public int getPrice_Cup() {
        return this.price_Cup;
    }
    
    public int getPrice_Coupon() {
        return this.price_Coupon;
    }

    public int getQuantity_Product() {
        return this.quantity_Product;
    }
    
    public int getQuantity_Cup() {
        return this.quantity_Cup;
    }
    
    /**
     * 取得產品細項資料
     *
     * @return JSONObject 回傳產品細項資料
     */
    public JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("id", getId());
        data.put("product", getProduct().getData());
        data.put("specification_Product",  getSpecification());
        data.put("price_Product", getPrice_Product());
        data.put("quantity_Product", getQuantity_Product());
        data.put("id_Cup", getId_Cup());
        data.put("id_Order", getId_Order());
       // data.put("id_Product", getId_Product());
        data.put("price_Cup", getPrice_Cup());
        data.put("quantity_Cup", getQuantity_Cup());
        data.put("price_Coupon", getPrice_Coupon());

        return data;
    }
}
