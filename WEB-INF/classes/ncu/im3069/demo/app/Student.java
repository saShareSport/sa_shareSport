package ncu.im3069.demo.app;

import org.json.*;

import java.util.*;
import java.sql.*;
import java.time.LocalDateTime;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class Student
 * Member類別（class）具有學生所需要之屬性與方法，並且儲存與學生相關之商業判斷邏輯<br>
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class Student {
    
    /** id，學生編號 */
    private int id;
    
    /** email，學生電子郵件信箱 */
    private String email;
    
    /** name，學生姓名 */
    private String name;
    
    /** password，學生密碼 */
    private String password;
    
    /** sex，學生性別*/
    private String sex;
    
    /** modified，學生修改時間 */
    private Timestamp modified;
    
    /** status，學生創建時間 */
    private Timestamp created;
    
    /** status，學生之狀態 */
    private int status;
    
    /** image，學生之圖片 */
    private String image=null;
    
    /** sh，StudentHelper之物件與Student相關之資料庫方法（Sigleton） */
    private StudentHelper sh =  StudentHelper.getHelper();
    
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立學生資料時，產生一名新的學生
     *
     * @param email 學生電子信箱
     * @param password 學生密碼
     * @param name 學生姓名
     */
    public Student(String email, String password, String name, String sex) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.created = Timestamp.valueOf(LocalDateTime.now());
        this.modified = Timestamp.valueOf(LocalDateTime.now());
        update();
    }

    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新學生資料時，產生一名學生同時需要去資料庫檢索原有更新時間分鐘數與學生組別
     * 
     * @param id 學生編號
     * @param email 學生電子信箱
     * @param password 學生密碼
     * @param name 學生姓名
     */
    public Student(int id, String email, String password, String name, String sex,String image) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.image = image;
        this.modified = Timestamp.valueOf(LocalDateTime.now());
     
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢學生資料時，將每一筆資料新增為一個學生物件
     *
     * @param id 學生編號
     * @param email 學生電子信箱
     * @param password 學生密碼
     * @param name 學生姓名
     * @param login_times 更新時間的分鐘數
     * @param status the 學生之組別
     */
    public Student(int id, String email, String password, String name, String sex,String image,int status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.image = image;
        this.sex = sex;
        this.status = status;
    }
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於subscribe&follow查詢學生資料時，將每一筆資料新增為一個學生物件
     *
     * @param name 學生姓名
     */
    public Student(String name) {
        this.name = name;
    }
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢學生資料&儲存照片時，將每一筆資料新增為一個學生物件
     *
     * @param id 學生編號
     * @param name 學生姓名
     */
    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }
    /**
     * 取得學生之編號
     *
     * @return the id 回傳學生編號
     */
    public int getID() {
        return this.id;
    }
    
    /**
     * 取得學生之電子郵件信箱
     *
     * @return the email 回傳學生電子郵件信箱
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * 取得學生之姓名
     *
     * @return the name 回傳學生姓名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 取得學生之密碼
     *
     * @return the password 回傳學生密碼
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * 取得創建會員之時間
     */
    public Timestamp getCreated() {
        return this.created;
    }
    /**
     * 取得修改會員之時間
     */
    public Timestamp getModified() {
        return this.modified;
    }
    
    /**
     * 取得學生之狀態
     *
     * @return the status 回傳學生狀態
     */
    public int getStatus() {
        return this.status;
    }
    /**
     * 取得學生之性別
     *
     * @return the sex 回傳學生性別
     */
    public String getSex() {
        return this.sex;
    }
    
    /**
     * 取得學生之照片
     *
     * @return the status 回傳學生狀態
     */
    public String getImg() {
    	return this.image;
    }
    /**
     * 更新學生資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名學生是否已經在資料庫 */
        if(this.id != 0) {       
            /** 透過MemberHelper物件，更新目前之學生資料置資料庫中 */
            data = sh.update(this);
        }
        
        return data;
    }
    

    /**
     * 更新學生照片
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject saveImg() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();

        /** 檢查該名學生是否已經在資料庫 */
             
            /** 透過MemberHelper物件，更新目前之學生資料置資料庫中 */
        data = sh.saveImage(this);
        
        
        return data;
    }
    
    /**
     * 取得該名學生所有資料
     *
     * @return the data 取得該名學生之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名學生所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("email", getEmail());
        jso.put("password", getPassword());
        jso.put("sex", getSex());
        jso.put("image", getImg());
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
   
          return jso;
      }
 
}