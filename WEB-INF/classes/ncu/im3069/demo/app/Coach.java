package ncu.im3069.demo.app;

import org.json.*;

import java.util.*;
import java.sql.*;
import java.time.LocalDateTime;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class Coach
 * Member類別（class）具有教練所需要之屬性與方法，並且儲存與教練相關之商業判斷邏輯<br>
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class Coach {
    
    /** id，教練編號 */
    private int id;

    /** email，教練電子郵件信箱 */
    private String email;
    
    /** name，教練姓名 */
    private String name;
    
    /** password，教練密碼 */
    private String password;
    
    /** sex，教練性別*/
    private String sex;
    
    /** modified，教練修改時間 */
    private Timestamp modified;
    
    /** created，教練創建時間 */
    private Timestamp created;
    
    /** status，教練之狀態 */
    private int status;
    
    /** image，教練之圖片 */
    private String image=null;
    
    /** information，教練之簡介 */
    private String information;
    
    /** followers_count，教練之被追蹤人數 */
    private int followers_count;
    
    
    
    /** ch，CoachHelper之物件與Coach相關之資料庫方法（Sigleton） */
    private CoachHelper ch =  CoachHelper.getHelper();
    
    
    /**
     * 實例化（Instantiates）一個新的（new）Coach物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立教練資料時，產生一名新的教練
     *
     * @param email 教練電子信箱
     * @param password 教練密碼
     * @param name 教練姓名
     */
    public Coach(String email, String password, String name, String sex) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.created = Timestamp.valueOf(LocalDateTime.now());
        this.modified = Timestamp.valueOf(LocalDateTime.now());
        update();
    }

    /**
     * 實例化（Instantiates）一個新的（new）Coach物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新教練資料時，產生一名教練同時需要去資料庫檢索原有更新時間分鐘數與教練組別
     * 
     * @param id 教練編號
     * @param email 教練電子信箱
     * @param password 教練密碼
     * @param name 教練姓名
     */
    public Coach(int id, String email, String password, String name, String sex,String image,String information, int status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.image = image;
        this.information = information;
        this.modified = Timestamp.valueOf(LocalDateTime.now());
        this.status = status;
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢教練資料時，將每一筆資料新增為一個教練物件
     *
     * @param id 教練編號
     * @param email 教練電子信箱
     * @param password 教練密碼
     * @param name 教練姓名
     * @param login_times 更新時間的分鐘數
     * @param status the 教練之組別
     */
    public Coach(int id, String email, String password, String name, String sex,String image,String information,int followers_count,int status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.image = image;
        this.sex = sex;
        this.information = information;
        this.followers_count = followers_count;
        this.status = status;
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Coach物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢教練&儲存照片資料時，將每一筆資料新增為一個教練物件
     *
     * @param id 教練編號
     * @param name 教練姓名
     */
    public Coach(int id, String name) {
        this.id = id;
        this.name = name;
    }
    /**
     * 實例化（Instantiates）一個新的（new）Coach物件<br>
     * 採用多載（overload）方法進行，此建構子用於follow查詢教練時，將每一筆資料新增為一個教練物件
     *
     * @param id 教練編號
     * @param name 教練姓名
     */
    public Coach(String name, String information) {
        this.name = name;
        this.information = information;
    }
    /**
     * 取得教練之編號
     *
     * @return the id 回傳教練編號
     */
    public int getID() {
        return this.id;
    }

    /**
     * 取得教練之電子郵件信箱
     *
     * @return the email 回傳教練電子郵件信箱
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * 取得教練之姓名
     *
     * @return the name 回傳教練姓名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 取得教練之密碼
     *
     * @return the password 回傳教練密碼
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
     * 取得教練之狀態
     *
     * @return the status 回傳教練狀態
     */
    public int getStatus() {
        return this.status;
    }
    /**
     * 取得教練之性別
     *
     * @return the sex 回傳教練性別
     */
    public String getSex() {
        return this.sex;
    }
    
    /**
     * 取得教練之照片
     *
     * @return the image 回傳教練狀態
     */
    public String getImg() {
    	return this.image;
    }
    /**
     * 取得教練之被追蹤人數
     *
     * @return the followersCount 回傳教練狀態
     */
    public int getFollowers() {
    	return this.followers_count;
    }
    /**
     * 取得教練之簡介
     *
     * @return the status 回傳教練狀態
     */
    public String getInformation() {
    	return this.information;
    }
    /**
     * 更新教練資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名教練是否已經在資料庫 */
        if(this.id != 0) {       
            /** 透過MemberHelper物件，更新目前之教練資料置資料庫中 */
            data = ch.update(this);
        }
        
        return data;
    }
    
    /**
     * 更新教練照片
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject saveImg() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        System.out.print("this is saveImg");
        /** 檢查該名教練是否已經在資料庫 */
             
            /** 透過MemberHelper物件，更新目前之教練資料置資料庫中 */
        data = ch.saveImage(this);
        
        
        return data;
    }
    
    /**
     * 取得該名教練所有資料
     *
     * @return the data 取得該名教練之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名教練所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("email", getEmail());
        jso.put("password", getPassword());
        jso.put("sex", getSex());
        jso.put("image", getImg());
        jso.put("information", getInformation());
        jso.put("followers_count", getFollowers());
        jso.put("modified", getModified());
        jso.put("created", getCreated());
        jso.put("status", getStatus());
        
        return jso;
    }
    
   //for student getting follow's coach
  	public JSONObject getFollData() {
          /** 透過JSONObject將該項產品所需之資料全部進行封裝*/ 
          JSONObject jso = new JSONObject(); 
          jso.put("name", getName());
          jso.put("information", getInformation());
          
          return jso;
      }
 
}