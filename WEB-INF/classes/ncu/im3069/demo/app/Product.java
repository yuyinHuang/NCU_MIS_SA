package ncu.im3069.demo.app;

import org.json.*;

public class Product {

    /** id，商品編號 */
    private int id;

    /** name，商品名稱 */
    private String name;

    /** price，商品價格 */
    private int price;

    /** image，商品圖片 */
    private String image;

    /** description，商品描述 */
 private String description;

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 商品編號
     */
 public Product(int id) {
  this.id = id;
 }

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param name 商品名稱
     * @param price 商品價格
     * @param image 商品圖片
     */
 public Product(String name, int price, String image) {
  this.name = name;
  this.price = price;
  this.image = image;
 }

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     * @param id 商品編號
     * @param name 商品名稱
     * @param price 商品價格
     * @param image 商品圖片
     * @param description 商品描述
     */
 public Product(int id, String name, int price, String image, String description) {
  this.id = id;
  this.name = name;
  this.price = price;
  this.image = image;
  this.description = description;
 }

    /**
     * 取得商品編號
     *
     * @return int 回傳商品編號
     */
 public int getID() {
  return this.id;
 }

    /**
     * 取得商品名稱
     *
     * @return String 回傳商品名稱
     */
 public String getName() {
  return this.name;
 }

    /**
     * 取得商品價格
     *
     * @return int 回傳商品價格
     */
 public int getPrice() {
  return this.price;
 }

    /**
     * 取得商品圖片
     *
     * @return String 回傳商品圖片
     */
 public String getImage() {
  return this.image;
 }

    /**
     * 取得商品描述
     *
     * @return String 回傳商品描述
     */
 public String getDescription() {
  return this.description;
 }

    /**
     * 取得商品資訊
     *
     * @return JSONObject 回傳商品資訊
     */
 public JSONObject getData() {
        /** 透過JSONObject將該項商品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("price", getPrice());
        jso.put("image", getImage());
        jso.put("description", getDescription());

        return jso;
    }
}