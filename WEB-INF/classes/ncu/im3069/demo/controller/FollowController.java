package ncu.im3069.demo.controller;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Follow;
import ncu.im3069.demo.app.FollowHelper;
import ncu.im3069.tools.JsonReader;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class FollowController<br>
 * FollowController類別（class）主要用於處理Follow相關之Http請求（Request），繼承HttpServlet
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */
@WebServlet("/api/follow.do")
public class FollowController extends HttpServlet {
	/** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** fh，FollowHelper之物件與Follow相關之資料庫方法（Sigleton） */
    private FollowHelper fh =  FollowHelper.getHelper();
    
    /**
     * 處理Http Method請求POST方法（新增資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int stuId = jso.getInt("foll_stuId");
        int coaId = jso.getInt("foll_coaId ");
        
        /** 建立一個新的追蹤物件 */
        Follow f = new Follow(stuId, coaId);
        
        /** 透過FollowHelper物件的checkDuplicate()檢查該追蹤紀錄是否有重複 */
        if (!fh.checkDuplicate(f)) {
            /** 透過FollowHelper物件的create()方法新建一筆追蹤紀錄至資料庫 */
            JSONObject data = fh.create(f);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 註冊教練資料...");
            resp.put("response", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        else {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'追蹤失敗，此追蹤資料重複！\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
    }
    /**
     * 處理Http Method請求GET方法（取得資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String stuId = jsr.getParameter("foll_stuId");
        String coaId = jsr.getParameter("id");
        JSONObject query = null;
        int count = 0;
        
        if(stuId != null) {
        	/** 透過FollowHelper物件之getByID()方法取回該學生所有訂閱之資料，回傳之資料為JSONObject物件 */
            query = fh.getByID(stuId);
        }
        if(coaId != null){
        	count = fh.countFoll(coaId);
        }
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "所有追蹤資料取得成功");
        resp.put("response", query);
        resp.put("count", count);
        
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
    }
    /**
     * 處理Http Method請求DELETE方法（刪除）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int stuId = jso.getInt("foll_stuId");
        int coaId = jso.getInt("foll_coaId ");
        
        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名教練，回傳之資料為JSONObject物件 */
        JSONObject query = fh.deleteByID(stuId, coaId);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "追蹤紀錄移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }
}