package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class OrderDetailHelper {
    
    private OrderDetailHelper() {
        
    }
    
    private static OrderDetailHelper odh;
    private Connection conn = null;
    private PreparedStatement pres = null;
    
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個OrderDetailHelper物件
     *
     * @return the helper 回傳OrderDetailHelper物件
     */
    public static OrderDetailHelper getHelper() {
        /** Singleton檢查是否已經有OrderDetailHelper物件，若無則new一個，若有則直接回傳 */
        if(odh == null) odh = new OrderDetailHelper();
        
        return odh;
    }
    
    public JSONArray createByList(long id_Order, List<OrderDetail> orderdetail) {
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        for(int i=0 ; i < orderdetail.size() ; i++) {
        	OrderDetail od = orderdetail.get(i);
            
            
            /** 取得所需之參數 */
        	//int orderdetail_id = op.getId();
        	//int id_Order = od.getId_Order();
            int id_Product = od.getProduct().getID();
            String specification_Product = od.getSpecification();
            int	quantity_Product = od.getQuantity_Product();
            int price_Product = od.getPrice_Product();
            int id_Cup = od.getId_Cup();
            int quantity_Cup = od.getQuantity_Cup();
            int price_Cup = od.getPrice_Cup();
            int price_Coupon = od.getPrice_Coupon();
            
            try {
                /** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql = "INSERT INTO `missa`.`orderdetails`(`id_Order`, `id_Product`, `specification_Product`, `quantity_Product`, `price_Product`,"
                		+ "	`id_Cup`, `quantity_Cup`, `price_Cup`, `price_Coupon`)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
               
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pres.setLong(1, id_Order);
                pres.setInt(2, id_Product);
                pres.setString(3, specification_Product);
                pres.setInt(4, quantity_Product);
                pres.setInt(5, price_Product);
                pres.setInt(6, id_Cup);
                pres.setInt(7, quantity_Cup);
                pres.setInt(8, price_Cup);
                pres.setInt(9, price_Coupon);
                
                /** 執行新增之SQL指令並記錄影響之行數 */
                pres.executeUpdate();
                
                /** 紀錄真實執行的SQL指令，並印出 **/
                exexcute_sql = pres.toString();
                System.out.println(exexcute_sql);
                
                ResultSet rs = pres.getGeneratedKeys();

                if (rs.next()) {
                    long id = rs.getLong(1);
                    jsa.put(id);
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
        }
        
        return jsa;
    }
    public JSONObject getById(String order_id) {
        JSONObject data = new JSONObject();
        JSONArray jsa = new JSONArray();
        OrderDetail od = null;
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
            String sql = "SELECT * FROM `missa`.`orderdetails` WHERE `id_Order` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, order_id);
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
                int id = rs.getInt("id");
                int id_Order=rs.getInt("id_Order");
                int id_Product=rs.getInt("id_Product");
                String specification_Product=rs.getString("specification_Product");
                int quantity_Product=rs.getInt("quantity_Product");
                int price_Product=rs.getInt("price_Product");
                int id_Cup=rs.getInt("id_Cup");
                int quantity_Cup=rs.getInt("quantity_Cup");
                int price_Cup=rs.getInt("price_Cup");
                int price_Coupon=rs.getInt("price_Coupon");
                
                
                /** 將每一筆商品資料產生一名新Product物件 */
                od = new OrderDetail(id,id_Order,id_Product,specification_Product,quantity_Product,price_Product,id_Cup,quantity_Cup,price_Cup,price_Coupon);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                //data = od.getData();
                jsa.put(od.getData()); 
                
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
    public JSONObject getByMId(String member_id) {
        JSONObject data = new JSONObject();
        JSONArray jsa = new JSONArray();
        Order o = null;
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
            String sql = "SELECT * FROM `missa`.`orderss` WHERE `id_Member` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, member_id);
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
                int id = rs.getInt("id");
                String stateOforder = rs.getString("stateOforder");
                int id_Member = rs.getInt("id_Member");
                String phoneNumber_Member = rs.getString("phoneNumber_Member");
                String name_Member = rs.getString("name_Member");
                String sn_Coupon = rs.getString("sn_Coupon");
                Timestamp createOforder = rs.getTimestamp("createOforder");
                Timestamp modifyOforder = rs.getTimestamp("modifyOforder");
                
                
                /** 將每一筆商品資料產生一名新Product物件 */
                o = new Order(id,  stateOforder, id_Member, phoneNumber_Member, name_Member, sn_Coupon, createOforder, modifyOforder);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                //data = o.getOrderAllInfo();
                //data=o.getOrderData();
                jsa.put(o.getOrderAllInfo());
                
                
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
    
    
    public JSONObject deleteByID(int id) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
       
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            
            /** SQL指令 */
            String sql = "DELETE FROM `missa`.`orderdetails` WHERE `id_Order` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行刪除之SQL指令並記錄影響之行數 */
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
            DBMgr.close(rs, pres, conn);
        }

       
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        //response.put("time", duration);

        return response;
    }
    public JSONObject getAll() {
        OrderDetail od = null;
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
            String sql = "SELECT * FROM `missa`.`orderdetails`";
            
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
                int id = rs.getInt("id");
                int id_Order=rs.getInt("id_Order");
                int id_Product=rs.getInt("id_Product");
                String specification_Product=rs.getString("specification_Product");
                int quantity_Product=rs.getInt("quantity_Product");
                int price_Product=rs.getInt("price_Product");
                int id_Cup=rs.getInt("id_Cup");
                int quantity_Cup=rs.getInt("quantity_Cup");
                int price_Cup=rs.getInt("price_Cup");
                int price_Coupon=rs.getInt("price_Coupon");
                
                od = new OrderDetail(id,id_Order,id_Product,specification_Product,quantity_Product,price_Product,id_Cup,quantity_Cup,price_Cup,price_Coupon);
               
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(od.getData());
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

    
    public ArrayList<OrderDetail> getOrderProductByOrderId(int id_Order) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        ResultSet rs = null;
        OrderDetail od;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`orderdetails` WHERE `id_Order` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id_Order);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                
                /** 將 ResultSet 之資料取出 */
            	int orderdetails_id =rs.getInt("id");
            	//int id_order = rs.getInt("id_Order");
            	int id_Product = rs.getInt("id_Product");
                String specification_Product = rs.getString("specification_Product");
                int	quantity_Product = rs.getInt("quantity_Product");
                int price_Product = rs.getInt("price_Product");
                int id_Cup = rs.getInt("id_Cup");
                int quantity_Cup = rs.getInt("quantity_Cup");
                int price_Cup = rs.getInt("price_Cup");
                int price_Coupon = rs.getInt("price_Coupon");
            	
                /** 將每一筆訂單明細資料產生一名新OrderDetail物件 */
                od = new OrderDetail(orderdetails_id, id_Order, id_Product, specification_Product, quantity_Product,
                		price_Product, id_Cup, quantity_Cup, price_Cup, price_Coupon );
                /** 取出該名訂單明細之資料並封裝至 JSONsonArray 內 */
                result.add(od);
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
        
        return result;
    }
    
    
}
