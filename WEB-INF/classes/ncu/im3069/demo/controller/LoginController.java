package ncu.im3069.demo.controller;

import java.io.*;


import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.tools.JsonReader;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class LoginController<br>
 * LoginController類別（class）主要用於處理Login相關之Http請求（Request），繼承HttpServlet
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */
@WebServlet("/api/login.do")
public class LoginController extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
	private MemberHelper mh = MemberHelper.getHelper();

	/**
	 * 處理Http Method請求POST方法（新增資料）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		/** 取出經解析到JSONObject之Request參數 */
		String phoneNumber = jso.getString("phoneNumber");
		String password = jso.getString("password");
		/** 建立一個新的會員物件 */
		/** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
		if (phoneNumber.isEmpty() || password.isEmpty()) {
			/** 以字串組出JSON格式之資料 */
			String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
			/** 透過JsonReader物件回傳到前端（以字串方式） */
			jsr.response(resp, response);
		}
		/** 透過MemberHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
		else { 
			/** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
			JSONObject data = mh.getByphoneNumber(phoneNumber);
			JSONObject resp = new JSONObject();			
				resp.put("status", "200");
				resp.put("message", "成功! 登入會員資料");
				resp.put("response", data);
				/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
				jsr.response(resp, response);			
		}
}
}	