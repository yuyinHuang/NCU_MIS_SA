package ncu.im3069.demo.app;

import java.util.Calendar;

import org.json.*;

public class Cup {

    /** id，會員編號 */
    private int id;

    /** id，會員編號 */
    private String name;

    /** id，會員編號 */
    private int price;

    /** id，會員編號 */
    private String image;
	
	 /** id，會員編號 */
	private int quantity;
	
	private CupHelper ch =  CupHelper.getHelper();
	
    /**
     * 實例化（Instantiates）一個新的（new）Cup 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     * @param id 產品編號
     */
	public Cup(int id) {
		this.id = id;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Cup 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增副商品時
     * @param name 副商品名稱
     * @param price 副商品價格
     * @param image 副商品圖片
     */
	public Cup(String name,String image, int price ) {
		this.name = name;
		this.price = price;
		this.image = image;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Cup 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     * @param id 副商品編號
     * @param name 副商品名稱
     * @param price 副商品價格
     * @param image 副商品圖片
     * @param quantity 副商品數量
     */
	public Cup(int id, String name, String image, int price,  int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.quantity = quantity;
	}

	public Cup( String name, String image, int price,  int quantity) {
		
		this.name = name;
		this.price = price;
		this.image = image;
		this.quantity = quantity;
	}

    /**
     * 取得產品編號
     * @return int 回傳副商品編號
     */
	public int getID() {
		return this.id;
	}

    /**
     * 取得產品名稱
     * @return String 回傳產品名稱
     */
	public String getName() {
		return this.name;
	}

    /**
     * 取得產品價格
     * @return int 回傳副商品價格
     */
	public int getPrice() {
		return this.price;
	}

    /**
     * 取得產品圖片
     * @return String 回傳副商品圖片
     */
	public String getImage() {
		return this.image;
	}

	 /**
     * 取得產品數量
     * @return int 回傳副商品數量
     */
	public int getQuantity() {
		return this.quantity;
	}
	
	public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        /** 取得更新資料時間（即現在之時間）之分鐘數 */
        
        
        /** 檢查該名會員是否已經在資料庫 */
        if(this.id != 0) {
            
            /** 透過MemberHelper物件，更新目前之會員資料置資料庫中 */
            data = ch.update(this);
        }
        
        return data;
    }
	
    /**
     * 取得產品資訊
     * @return JSONObject 回傳產品資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("price", getPrice());
        jso.put("image", getImage());
        jso.put("quantity", getQuantity());

        return jso;
    }
}
