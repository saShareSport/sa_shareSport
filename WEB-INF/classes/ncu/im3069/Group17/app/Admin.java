package ncu.im3069.Group17.app;

import org.json.*;

import java.util.*;
import java.sql.*;
import java.time.LocalDateTime;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class Coach
 * Member類別（class）具有管理員所需要之屬性與方法，並且儲存與管理員相關之商業判斷邏輯<br>
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class Admin {
    
    /** id，管理員編號 */
    private int id;
    
    /** email，管理員電子郵件信箱 */
    private String email;
    
    /** name，管理員姓名 */
    private String name;
    
    /** password，管理員密碼 */
    private String password;
    
    /** modified，管理員修改時間 */
    private Timestamp modified;
    
    /** created，管理員創建時間 */
    private Timestamp created;
    
    /** status，管理員之狀態 */
    private int status;
    
    
    /** ah，AdminHelper之物件與Admin相關之資料庫方法（Sigleton） */
    private AdminHelper ah =  AdminHelper.getHelper();
    
    
    /**
     * 實例化（Instantiates）一個新的（new）Admin物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立管理員資料時，產生一名新的管理員
     *
     * @param email 管理員電子信箱
     * @param password 管理員密碼
     * @param name 管理員姓名
     */
    public Admin(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.created = Timestamp.valueOf(LocalDateTime.now());
        this.modified = Timestamp.valueOf(LocalDateTime.now());
        update();
    }

    /**
     * 實例化（Instantiates）一個新的（new）Admin物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新管理員資料時，產生一名管理員同時需要去資料庫檢索原有更新時間分鐘數與管理員組別
     * 
     * @param id 管理員編號
     * @param email 管理員電子信箱
     * @param password 管理員密碼
     * @param name 管理員姓名
     */
    public Admin(int id, String email, String password, String name,int status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.status = status;
        this.modified = Timestamp.valueOf(LocalDateTime.now());
     
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢管理員資料時，將每一筆資料新增為一個管理員物件
     *
     * @param id 管理員編號
     * @param email 管理員電子信箱
     * @param password 管理員密碼
     * @param name 管理員姓名
     * @param status 管理員之狀態
     */
    public Admin(int id, String email, String password, String name,Timestamp modified,Timestamp created,int status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.modified= modified;
        this.created =created;
        this.status = status;
    }
    
    /**
     * 取得管理員之編號
     *
     * @return the id 回傳管理員編號
     */
    public int getID() {
        return this.id;
    }

    /**
     * 取得管理員之電子郵件信箱
     *
     * @return the email 回傳管理員電子郵件信箱
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * 取得管理員之姓名
     *
     * @return the name 回傳管理員姓名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 取得管理員之密碼
     *
     * @return the password 回傳管理員密碼
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * 取得創建管理員之時間
     */
    public Timestamp getCreated() {
        return this.created;
    }
    /**
     * 取得管理員修改之時間
     */
    public Timestamp getModified() {
        return this.modified;
    }
    
    /**
     * 取得管理員之狀態
     *
     * @return the status 回傳管理員狀態
     */
    public int getStatus() {
        return this.status;
    }
    
    /**
     * 更新管理員資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名管理員是否已經在資料庫 */
        if(this.id != 0) {       
            /** 透過MemberHelper物件，更新目前之管理員資料置資料庫中 */
            data = ah.update(this);
        }
        
        return data;
    }
    
    /**
     * 取得該名管理員所有資料
     *
     * @return the data 取得該名管理員之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名管理員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("email", getEmail());
        jso.put("password", getPassword());
        jso.put("modified", getModified());
        jso.put("created", getCreated());
        jso.put("status", getStatus());
        
        return jso;
    }
    
 
}