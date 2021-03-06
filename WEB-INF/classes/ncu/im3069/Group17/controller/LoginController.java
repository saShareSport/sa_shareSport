package ncu.im3069.Group17.controller;

import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.json.*;

import ncu.im3069.Group17.app.AdminHelper;
import ncu.im3069.Group17.app.CoachHelper;
import ncu.im3069.Group17.app.StudentHelper;
import ncu.im3069.tools.JsonReader;

@WebServlet("/api/login.do")
public class LoginController extends HttpServlet {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** sh，StudentHelper之物件與Student相關之資料庫方法（Sigleton） */
	private StudentHelper sh = StudentHelper.getHelper();
	private CoachHelper ch = CoachHelper.getHelper();
	private AdminHelper ah = AdminHelper.getHelper();

	private String srole;
	private String crole;
	private String arole;

	private Cookie idCookie;
	private Cookie nameCookie;
	private Cookie roleCookie;
	private Cookie loginCookie;

	@SuppressWarnings("unused")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		srole = (String) request.getParameter("student");
		crole = (String) request.getParameter("coach");
		arole = (String) request.getParameter("admin");
		JSONObject result = null;
		int flag = 0;
		String role = null;

		if (arole != null) {
			flag = ah.checkLogin(email, password);
			System.out.println("flag= " + flag);
			result = ah.getLogin(email, password);
			role = "admin";
		}

		if (srole != null) {
			flag = sh.checkLogin(email, password);
			System.out.println("flag= " + flag);
			result = sh.getLogin(email, password);
			role = "student";
		}

		if (crole != null) {
			flag = ch.checkLogin(email, password);
			System.out.println("flag= " + flag);
			result = ch.getLogin(email, password);
			role = "coach";
		}

//		System.out.println(flag);
//		System.out.println(result.getJSONArray("data").getJSONObject(0).get("name"));
//		System.out.println(result.getJSONArray("data").getJSONObject(0).get("id"));

		// 编码，解决中文乱码
//		String name = URLEncoder.encode((String) result.getJSONArray("data").getJSONObject(0).get("name"),"utf-8");
//		String id = URLEncoder.encode(Integer.toString((int) result.getJSONArray("data").getJSONObject(0).get("id")),"utf-8");

		if (flag == 1) {
			// 编码，解决中文乱码
			String name = URLEncoder.encode((String) result.getJSONArray("data").getJSONObject(0).get("name"), "utf-8");
			String id = URLEncoder
					.encode(Integer.toString((int) result.getJSONArray("data").getJSONObject(0).get("id")), "utf-8");
			String login = "success";

			idCookie = new Cookie("id", id);
			nameCookie = new Cookie("name", name);
			roleCookie = new Cookie("role", role);
			loginCookie = new Cookie("login", login);

			// setting cookie to expiry in 30 mins
			idCookie.setMaxAge(30 * 60);
			nameCookie.setMaxAge(30 * 60);
			roleCookie.setMaxAge(30 * 60);
			loginCookie.setMaxAge(30 * 60);

			// 將cookie傳給client端
			response.addCookie(idCookie);
			response.addCookie(nameCookie);
			response.addCookie(roleCookie);
			response.addCookie(loginCookie);

			response.sendRedirect("../index.html");

		} else {
			String login = "not";
			loginCookie = new Cookie("login", login);
			loginCookie.setMaxAge(30 * 60);
			response.addCookie(loginCookie);
			response.sendRedirect("../login.html");
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userId = null;
		String userRole = null;
		String status = null;
		String login = null;

		// 從client端拿cookie，為陣列型式所以再用for迴圈去找要的cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("id"))
					userId = cookie.getValue();
				if (cookie.getName().equals("role"))
					userRole = cookie.getValue();
				if (cookie.getName().equals("login"))
					login = cookie.getValue();
			}
		}
//			if(userId == null) response.sendRedirect("/sa_shareSport/login_demo.html");

		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject query = null;

		/** 透過xxxHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
		if (arole != null) {
			query = ah.getByID(userId);
		}
		if (srole != null) {
			query = sh.getByID(userId);
		}
		if (crole != null) {
			query = ch.getByID(userId);
		}

		if (userId == null && userRole == null) {
			status = "400";
		} else {
			status = "200";
		}
		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
		JSONObject resp = new JSONObject();
		resp.put("status", status);
		resp.put("message", "cookie資料取得成功");
		resp.put("response", query);
		resp.put("role", userRole);
		resp.put("login", login);

		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		jsr.response(resp, response);
	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// setting cookie to expiry in 0 mins
		idCookie.setMaxAge(0);
		nameCookie.setMaxAge(0);
		roleCookie.setMaxAge(0);
		loginCookie.setMaxAge(0);

		// 將cookie傳給client端
		response.addCookie(idCookie);
		response.addCookie(nameCookie);
		response.addCookie(roleCookie);
		response.addCookie(loginCookie);

		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject query = null;

		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
		JSONObject resp = new JSONObject();
		resp.put("status", "200");
		resp.put("message", "cookie資料刪除成功");

		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		jsr.response(resp, response);
	}
}