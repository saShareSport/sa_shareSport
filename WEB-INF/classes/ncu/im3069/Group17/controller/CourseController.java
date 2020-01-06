package ncu.im3069.Group17.controller;

import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.Group17.app.Course;
import ncu.im3069.Group17.app.CourseHelper;
import ncu.im3069.tools.JsonReader;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class StudentController<br>
 * StudentController類別（class）主要用於處理Student相關之Http請求（Request），繼承HttpServlet
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */
@WebServlet("/api/course.do")
public class CourseController extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** sh，StudentHelper之物件與Student相關之資料庫方法（Sigleton） */
	private CourseHelper courh = CourseHelper.getHelper();

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
		JSONObject data;
		/** 取出經解析到JSONObject之Request參數 */

		int coach_id = jso.getInt("course_coach_id");
		String name = jso.getString("name");
		// String image = jso.getString("image");
		String information = jso.getString("information");
		int upper_limb = jso.getInt("upper_limb");
		int core = jso.getInt("core");
		int lower_limb = jso.getInt("lower_limb");

		/** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
		if (name.isEmpty() || information.isEmpty()) {
			/** 以字串組出JSON格式之資料 */
			String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
			/** 透過JsonReader物件回傳到前端（以字串方式） */
			jsr.response(resp, response);
		}

		if (jso.has("image")) {
			System.out.println("i get image \n");
			String image = jso.getString("image");
			Course c = new Course(coach_id, name, information, upper_limb, lower_limb, core, image);
			data = courh.create(c);
		} else {
			Course c = new Course(coach_id, name, information, upper_limb, lower_limb, core);
			data = courh.create(c);
		}

		

		// data = courh.create(c);

		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
		JSONObject resp = new JSONObject();
		resp.put("status", "200");
		resp.put("message", "成功新增教程資料");
		resp.put("response", data);

		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		jsr.response(resp, response);

	}

	/**
	 * 處理Http Method請求GET方法（取得資料）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		/** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
		String id = jsr.getParameter("id");
		String coach_id = jsr.getParameter("coach_id");

		/** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
		if (id.isEmpty() && coach_id.isEmpty()) {
			/** 透過MemberHelper物件之getAll()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
			JSONObject query = courh.getAll();

			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "所有教程資料取得成功");
			resp.put("response", query);

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		} else if (!coach_id.isEmpty()) {
			JSONObject query = courh.getByCoachID(coach_id);

			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "所有教程資料取得成功");
			resp.put("response", query);

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		} else {
			/** 透過CourseHelper物件的getByID()方法自資料庫取回該教程之資料，回傳之資料為JSONObject物件 */
			JSONObject query = courh.getByID(id);

			/** 新建一個JSONObject用於將回傳之資料進行封裝 */
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "教程資料取得成功");
			resp.put("response", query);

			/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
			jsr.response(resp, response);
		}
	}

	/**
	 * 處理Http Method請求DELETE方法（刪除）停權是用update，不會刪除會員
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();

		/** 取出經解析到JSONObject之Request參數 */
		int id = jso.getInt("id");

		/** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
		JSONObject query = courh.deleteByID(id);

		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
		JSONObject resp = new JSONObject();
		resp.put("status", "200");
		resp.put("message", "會員移除成功！");
		resp.put("response", query);

		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		jsr.response(resp, response);
	}

	/**
	 * 處理Http Method請求PUT方法（更新）
	 *
	 * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
	 * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		JSONObject data;
		/** 取出經解析到JSONObject之Request參數 */
		int id = jso.getInt("id");
		String name = jso.getString("name");
		String information = jso.getString("information");
		int status = jso.getInt("status");

		if (jso.has("image")) {
			String image = jso.getString("image");
			Course c = new Course(id, name, information, image, status);
			data = c.update();
		} else {
			Course c = new Course(id, name, information, status);
			data = c.updateWithoutImg();
		}

		/** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */

		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
		JSONObject resp = new JSONObject();
		resp.put("status", "200");
		resp.put("message", "成功更新教程資料! ");
		resp.put("response", data);

		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		jsr.response(resp, response);
	}
}