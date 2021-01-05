package ncu.im3069.demo.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import ncu.im3069.demo.util.DBMgr;

public class CouponHelper {

	 private CouponHelper() {
	        
	 }
	    
	 private static CouponHelper ch;
	 private Connection conn = null;
	 private PreparedStatement pres = null;
	    
	 public static CouponHelper getHelper() {
	 /** Singleton檢查是否已經有CouponHelper物件，若無則new一個，若有則直接回傳 */
		 if(ch == null) ch = new CouponHelper();
		 return ch;
	 }
	 
	 public JSONObject create(Coupon coupon) {
	        /** 記錄實際執行之SQL指令 */
	        String exexcute_sql = "";
	        long id = -1;
	        JSONArray opa = new JSONArray();
	        
	        try {
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "INSERT INTO `missa`.`coupon`( `sn`, `price`, `id_OrderGet`, `id_Member`, `id_OrderUsed`)"
	                    + " VALUES( ?, ?, ?, ?, ?)";
	            
	            /** 取得所需之參數 */
	           
	            String sn = coupon.getSN();
	            int price = coupon.getPrice();
	            int id_OrderGet = coupon.getId_OrderGet();
	            int id_Member = coupon.getId_Member();
	            int id_OrderUsed = coupon.getId_OrderUsed();
	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            pres.setString(1, sn);
	            pres.setInt(2, price);
	            pres.setInt(3, id_OrderGet);
	            pres.setInt(4, id_Member);
	            pres.setInt(5, id_OrderUsed);
	           
	            
	            /** 執行新增之SQL指令並記錄影響之行數 */
	            pres.executeUpdate();
	            
	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
	            
	         

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

	        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
	        JSONObject response = new JSONObject();
	        response.put("order_id", id);
	        response.put("order_product_id", opa);

	        return response;
	    }
	 
	 public JSONObject getAll() {
	        /** 新建一個 Coupon 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
	    	Coupon c = null;
	        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
	        JSONArray jsa = new JSONArray();
	        /** 記錄實際執行之SQL指令 */
	        String exexcute_sql = "";
	        /** 紀錄程式開始執行時間 */
	        long start_time = System.nanoTime();
	        /** 紀錄SQL總行數 */
	        int row = 0;
	        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
	        ResultSet rs = null;
	        
	        try {
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "SELECT * FROM `missa`.`coupon`";
	            
	            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
	            pres = conn.prepareStatement(sql);
	            /** 執行查詢之SQL指令並記錄其回傳之資料 */
	            rs = pres.executeQuery();

	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
	            
	            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
	            while(rs.next()) {
	                /** 每執行一次迴圈表示有一筆資料 */
	                row += 1;
	                
	                /** 將 ResultSet 之資料取出 */
	                int id_Coupon = rs.getInt("id");
	                String sn = rs.getString("sn");
	                int price = rs.getInt("price");
	                int id_OrderGet = rs.getInt("id_OrderGet");
	                int id_Member = rs.getInt("id_Member");
	                int id_OrderUsed = rs.getInt("id_OrderUsed"); 
	                
	                /** 將每一筆商品資料產生一名新Product物件 */
	                c = new Coupon(id_Coupon, sn, price, id_OrderGet, id_Member, id_OrderUsed);
	                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
	                jsa.put(c.getCouponData());
	            }

	        } catch (SQLException e) {
	            /** 印出JDBC SQL指令錯誤 **/
	            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
	        } catch (Exception e) {
	            /** 若錯誤則印出錯誤訊息 */
	            e.printStackTrace();
	        } finally {
	            /** 關閉連線並釋放所有資料庫相關之資源 **/
	            DBMgr.close(rs, pres, conn);
	        }
	        
	        /** 紀錄程式結束執行時間 */
	        long end_time = System.nanoTime();
	        /** 紀錄程式執行時間 */
	        long duration = (end_time - start_time);
	        
	        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
	        JSONObject response = new JSONObject();
	        response.put("sql", exexcute_sql);
	        response.put("row", row);
	        response.put("time", duration);
	        response.put("data", jsa);

	        return response;
	    }
	    

	 /**
	     * 透過會員編號（ID）取得會員資料
	     *
	     * @param id 會員編號
	     * @return the JSON object 回傳SQL執行結果與該會員編號之會員資料
	     */
	    public JSONObject getByID(String id) {
	        /** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
	        Coupon c = null;
	        /** 用於儲存所有檢索回之會員，以JSONArray方式儲存 */
	        JSONArray jsa = new JSONArray();
	        /** 記錄實際執行之SQL指令 */
	        String exexcute_sql = "";
	        /** 紀錄程式開始執行時間 */
	        long start_time = System.nanoTime();
	        /** 紀錄SQL總行數 */
	        int row = 0;
	        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
	        ResultSet rs = null;
	        
	        try {
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "SELECT * FROM `missa`.`coupon` WHERE `id_Member` = ? LIMIT 1";
	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql);
	            pres.setString(1, id);
	            /** 執行查詢之SQL指令並記錄其回傳之資料 */
	            rs = pres.executeQuery();

	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
	            
	            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
	            /** 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈 */
	            while(rs.next()) {
	                /** 每執行一次迴圈表示有一筆資料 */
	                row += 1;
	                
	                /** 將 ResultSet 之資料取出 */
	                int id_coupon = rs.getInt("id");
	                String sn = rs.getString("sn");
	                int price = rs.getInt("price");
	                int id_OrderGet = rs.getInt("id_OrderGet");
	               
	                int id_OrderUsed = rs.getInt("id_OrderUsed"); 
	                
	                /** 將每一筆會員資料產生一名新Member物件 */
	                c = new Coupon(id_coupon, sn, price, id_OrderGet, id_OrderUsed);
	                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
	               jsa.put(c.getCouponData());
	            }
	            
	        } catch (SQLException e) {
	            /** 印出JDBC SQL指令錯誤 **/
	            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
	        } catch (Exception e) {
	            /** 若錯誤則印出錯誤訊息 */
	            e.printStackTrace();
	        } finally {
	            /** 關閉連線並釋放所有資料庫相關之資源 **/
	            DBMgr.close(rs, pres, conn);
	        }
	        
	        /** 紀錄程式結束執行時間 */
	        long end_time = System.nanoTime();
	        /** 紀錄程式執行時間 */
	        long duration = (end_time - start_time);
	        
	        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
	        JSONObject response = new JSONObject();
	        response.put("sql", exexcute_sql);
	        response.put("row", row);
	        response.put("time", duration);
	        response.put("data", jsa);

	        return response;
	    }
	    
	    /**
	     * 檢查該名會員之電子郵件信箱是否重複註冊
	     *
	     * @param m 一名會員之Member物件
	     * @return boolean 若重複註冊回傳False，若該信箱不存在則回傳True
	     */
	    public boolean checkDuplicate(Coupon c){
	        /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
	        int row = -1;
	        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
	        ResultSet rs = null;
	        
	        try {
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "SELECT count(*) FROM `missa`.`coupon` WHERE `sn` = ?";
	            
	            /** 取得所需之參數 */
	            String sn = c.getSN();
	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql);
	            pres.setString(1, sn);
	            /** 執行查詢之SQL指令並記錄其回傳之資料 */
	            rs = pres.executeQuery();

	            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
	            rs.next();
	            row = rs.getInt("count(*)");
	            System.out.print(row);

	        } catch (SQLException e) {
	            /** 印出JDBC SQL指令錯誤 **/
	            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
	        } catch (Exception e) {
	            /** 若錯誤則印出錯誤訊息 */
	            e.printStackTrace();
	        } finally {
	            /** 關閉連線並釋放所有資料庫相關之資源 **/
	            DBMgr.close(rs, pres, conn);
	        }
	        
	        /** 
	         * 判斷是否已經有一筆該電子郵件信箱之資料
	         * 若無一筆則回傳False，否則回傳True 
	         */
	        return (row == 0) ? false : true;
	    }
	    
	    public JSONObject update(Coupon c) {
	        /** 紀錄回傳之資料 */
	        JSONArray jsa = new JSONArray();
	        /** 記錄實際執行之SQL指令 */
	        String exexcute_sql = "";
	        /** 紀錄程式開始執行時間 */
	       
	        /** 紀錄SQL總行數 */
	        int row = 0;
	        
	        try {
	            /** 取得資料庫之連線 */
	            conn = DBMgr.getConnection();
	            /** SQL指令 */
	            String sql = "Update `missa`.`coupon` SET `id_OrderUsed` = ?  WHERE `id` = ?";
	            /** 取得所需之參數 */
	            int id_OrderUsed = c.getId_OrderUsed();
	            
	            
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql);
	            pres.setInt(6, id_OrderUsed);
	            
	            /** 執行更新之SQL指令並記錄影響之行數 */
	            row = pres.executeUpdate();

	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);

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
	        
	       
	        
	        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
	        JSONObject response = new JSONObject();
	        response.put("sql", exexcute_sql);
	        response.put("row", row);
	       
	        response.put("data", jsa);

	        return response;
	    }
}
