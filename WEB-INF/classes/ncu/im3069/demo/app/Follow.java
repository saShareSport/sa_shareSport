package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.json.*;

public class Follow{
	private int id;
	private int follstuId;
	private int follcoaId;
	private JSONObject studentName;
	private JSONObject coachName;

	private StudentHelper stuh =  StudentHelper.getHelper();
	private CoachHelper coah =  CoachHelper.getHelper();
	
	/*建構子*/
    public Follow(int id, int follstuId, int follcoaId) {
    	this.id = id; 
    	this.follstuId = follstuId;
    	this.follcoaId = follcoaId;
    }
    public Follow(int follstuId, int follcoaId) {
    	this.follstuId = follstuId;
    	this.follcoaId = follcoaId;
    }
    
    private int getId() {
		return this.id;
	}
    public int getFollowsStudentId() {
        return this.follstuId;
    }
    public int getFollowsCoachId() {
        return this.follcoaId;
    }

    public JSONObject getStudentName(int stuId) {
    	String id = String.valueOf(stuId);
        this.studentName = stuh.getNameByID(id);
        return studentName;
    }
    public JSONObject getCoachName(int coaId) {
    	String id = String.valueOf(coaId);
        this.coachName = coah.getNameByID(id);
        return coachName;
    }
    /**
     * 取得該追蹤紀錄的所有資料
     *
     * @return the data 取得該追蹤紀錄的所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名教練所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("foll_stuId", getFollowsStudentId());
        jso.put("student_name", getStudentName(getFollowsStudentId()));
        jso.put("foll_coaId", getFollowsCoachId());
        jso.put("coach_name", getCoachName(getFollowsCoachId()));
        return jso;
    }
//    //get coach's follower's id and name
//    public JSONObject getCoachFollowData() {
//        JSONObject jso = new JSONObject();
//        jso.put("id", getId());
//        jso.put("foll_stuId", getFollowsStudentId());
//        jso.put("student_name", getStudentName(getFollowsStudentId()));
//        return jso;
//    }

//	//get student's follow list of coaches'id and name
//    public JSONObject getStudentFollowData() {
//        JSONObject jso = new JSONObject();
//        jso.put("id", getId());
//        jso.put("foll_coaId", getFollowsCoachId());
//        jso.put("coach_name", getCoachName(getFollowsCoachId()));
//        return jso;
//    }
}