package ncu.im3069.demo.app;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import ncu.im3069.demo.util.Arith;

public class Coupon {


	private int id;
	
	private String sn;
	
	private int price;
	
	private int id_OrderGet;
	
	private int id_Member;
	
	private int id_OrderUsed;
	
	 private ArrayList<Coupon> list = new ArrayList<Coupon>();
	
	private CouponHelper ch =  CouponHelper.getHelper();
	
	public Coupon(int id, String sn, int price, int id_OrderGet, int id_Member) {
		this.id = id;
		this.sn =sn;
		this.price = price;
		this.id_OrderGet = id_OrderGet;
		this.id_Member = id_Member;
		
	}
	public Coupon(String sn, int price, int id_OrderGet, int id_Member, int id_OrderUsed) {
		
		this.sn =sn;
		this.price = price;
		this.id_OrderGet = id_OrderGet;
		this.id_Member = id_Member;
		this.id_OrderUsed = id_OrderUsed;
		
	}
	public Coupon (String sn, int price, int id_OrderGet, int id_Member) {
	
		this.sn =sn;
		this.price = price;
		this.id_OrderGet = id_OrderGet;
		this.id_Member = id_Member;
		
	}
	
	public Coupon(int id, int id_OrderUsed) {
		this.id =id;
		this.id_OrderUsed = id_OrderUsed;
	}
	
	public Coupon(int id, String sn, int price, int id_OrderGet, int id_Member, int id_OrderUsed) {
		this.id = id;
		this.sn =sn;
		this.price = price;
		this.id_OrderGet = id_OrderGet;
		this.id_Member = id_Member;
		this.id_OrderUsed = id_OrderUsed;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getSN() {
		return this.sn;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public int getId_OrderGet() {
		return this.id_OrderGet;
	}
	
	public int getId_Member() {
		return this.id_Member;
	}
	
	public int getId_OrderUsed() {
		return this.id_OrderUsed;
	}
	
	/**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public ArrayList<Coupon> getMemberCoupon() {
        return this.list;
    }

    
	
	 public JSONObject getCouponData() {
	        JSONObject data = new JSONObject();
	        data.put("id", getId());
	        data.put("sn", getSN());
	        data.put("price", getPrice());
	        data.put("id_OrderGet", getId_OrderGet());
	        data.put("id_Member", getId_Member());
	        data.put("id_OrderUsed", getId_OrderUsed());
	      
	        return data;
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
	

}
