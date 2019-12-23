package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.json.*;

public class Subscribe{
	private int id;
	private int substuId;
	private int subcourId;
	private JSONObject studentName;
	private JSONObject courseName;

	private StudentHelper stuh =  StudentHelper.getHelper();
	private CourseHelper courh =  CourseHelper.getHelper();

	/*建構子*/
    public Subscribe(int id, int stuId, int courId) {
    	this.id = id; 
    	this.substuId = stuId;
    	this.subcourId = courId;
    }
    public Subscribe(int stuId, int courId) {
    	this.substuId = stuId;
    	this.subcourId = courId;
    }
    
    private int getId() {
		return this.id;
	}
    public int getSubStudentId() {
        return this.substuId;
    }
    public int getSubCourseId() {
        return this.subcourId;
    }

    public JSONObject getStuName(int stuId) {
    	String id = String.valueOf(stuId);
        this.studentName = stuh.getNameByID(id);
        return studentName;
    }
    public JSONObject getCourName(int courId) {
    	String id = String.valueOf(courId);
        this.courseName = courh.getNameByID(id);
        return courseName;
    }
    /**
     * 取得該訂閱紀錄的所有資料
     *
     * @return the data 取得該訂閱紀錄的所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該訂閱紀錄所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("sub_stuId", getSubStudentId());
        jso.put("student_name", getStuName(getSubStudentId()));
        jso.put("sub_courId", getSubCourseId());
        jso.put("course_name", getCourName(getSubCourseId()));
        return jso;
    }

//	//get student's follow list of courses'id and name
//    public JSONObject getStudentFollowData() {
//        JSONObject jso = new JSONObject();
//        jso.put("id", getId());
//        jso.put("sub_courId", getSubCourseId());
//        jso.put("course_name", getCourName(getSubCourseId()));
//        return jso;
//    }
    
}