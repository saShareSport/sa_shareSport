package ncu.im3069.Group17.app;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.*;

public class Video {
	private int id;
	private int video_course_id;
	private String video_link;
	private Timestamp created;
	
	/** vh，VideoHelper之物件與Video相關之資料庫方法（Sigleton） */
    private VideoHelper vh =  VideoHelper.getHelper();


	public Video(int course_id, String video_link) {
		this.video_course_id = course_id;
		this.video_link = video_link;
		this.created = Timestamp.valueOf(LocalDateTime.now());
	}

	// 查詢教程
	public Video (int id,int course_id, String video_link) {
		this.id = id;
		this.video_course_id = course_id;
		this.video_link = video_link;
		this.created = Timestamp.valueOf(LocalDateTime.now());
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getCourseID() {
		return this.video_course_id;
	}
	
	public String getVideo_link() {
		return this.video_link;
	}
	
	public Timestamp getCreated() {
		return this.created;
	}
	

	
	
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("video_course_id", getCourseID());
        jso.put("video_link", getVideo_link());       
        jso.put("created", getCreated());
      
        return jso;
    }
}
