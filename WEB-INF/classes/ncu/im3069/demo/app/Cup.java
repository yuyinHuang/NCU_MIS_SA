package ncu.im3069.demo.app;

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
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     */
	public Cup(String name, int price, String image) {
		this.name = name;
		this.price = price;
		this.image = image;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     * @param id 產品編號
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param quantity 產品數量
     */
	public Cup(int id, String name, int price, String image, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.quantity = quantity;
	}

    /**
     * 取得產品編號
     * @return int 回傳產品編號
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
     * @return double 回傳產品價格
     */
	public int getPrice() {
		return this.price;
	}

    /**
     * 取得產品圖片
     * @return String 回傳產品圖片
     */
	public String getImage() {
		return this.image;
	}

	 /**
     * 取得產品數量
     * @return Int 回傳產品數量
     */
	public int getQuantity() {
		return this.quantity;
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
