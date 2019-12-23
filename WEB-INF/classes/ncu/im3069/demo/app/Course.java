package ncu.im3069.demo.app;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.*;

public class Course {
	private int id;
	private int coach_id;
	private String name;
	private String image;
	private String information;
	private int upper_limb; //0為false，1為true
	private int lower_limb;
	private int core;
	private int status;
	private Timestamp modified;
	private Timestamp created;
	
	/** courh，CourseHelper之物件與Course相關之資料庫方法（Sigleton） */
    private CourseHelper courh =  CourseHelper.getHelper();

	// 建立教程
	public Course(int coach_id, String name, String information, int upper, int lower, int core, String image) {
		this.coach_id = coach_id;
		this.name = name;
		this.information = information;
		this.upper_limb = upper;
		this.lower_limb = lower;
		this.core = core;
		this.image = image;
		this.modified = Timestamp.valueOf(LocalDateTime.now());
		this.created = Timestamp.valueOf(LocalDateTime.now());

	}
	// 更新教程
	public Course(int id, String name, String information, String image, int status) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.information = information;
		this.status = status;
		this.modified = Timestamp.valueOf(LocalDateTime.now());
		
	}
	// 刪除教程 
	public Course (int id, int status) {
		this.id = id;
		this.status = status; //刪除為1
	}
	
	// 查詢教程
	public Course (int id,int coach_id, String name, String information, int upper, int lower, int core, String image, int status, Timestamp created, Timestamp modified) {
		this.id = id;
		this.coach_id = coach_id;
		this.name = name;
		this.image = image;
		this.information = information;
		this.upper_limb = upper;
		this.lower_limb = lower;
		this.core = core;
		this.status = status;
		this.modified = modified;
		this.created = created;
	
	}
	// 為了取得訂閱中的教程資訊 
	public Course (String name, String information) {
		this.name = name;
		this.information = information;
	}
		
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getCoachID() {
		return this.coach_id;
	}
	
	public String getImage() {
		return this.image;
	}
	
	public String getInfo() {
		return this.information;
	}
	
	public int getUpper() {
		return this.upper_limb;
	}
	
	public int getLower() {
		return this.lower_limb;
	}
	
	public int getCore() {
		return this.core;
	}
	
	public Timestamp getModified() {
		return this.modified;
	}
	
	public Timestamp getCreated() {
		return this.created;
	}
	
	public int getStatus() {
		return this.status;
	}
	

	public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該教程是否已經在資料庫 */
        if(this.id != 0) {       
            /** 透過MemberHelper物件，更新目前之教練資料置資料庫中 */
            data = courh.update(this);
        }
        
        return data;
    }
    
	 /**
     * 更新課程照片
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject saveImg() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        /** 檢查該名課程是否已經在資料庫 */
             
            /** 透過MemberHelper物件，更新目前之課程資料置資料庫中 */
        data = courh.saveImage(this);
        
        
        return data;
    }
	
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("coach_id", getCoachID());
        jso.put("image", getImage());
        jso.put("information", getInfo());
        jso.put("lower_limb", getLower());
        jso.put("core", getCore());
        jso.put("upper_limb", getUpper());
        jso.put("modified", getModified());
        jso.put("created", getCreated());
        jso.put("status", getStatus());

        
        return jso;
    }
	//for student getting subscribe
	public JSONObject getSubData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject(); 
        jso.put("name", getName());
        jso.put("information", getInfo());
 
        return jso;
    }
}
