package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import org.json.*;

import ncu.im3069.demo.util.DBMgr;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class CoachHelper<br>
 * CoachHelper類別（class）主要管理所有與Member相關與資料庫之方法（method）
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class CourseHelper {
    
    /**
     * 實例化（Instantiates）一個新的（new）CoachHelper物件<br>
     * 採用Singleton不需要透過new
     */
    private CourseHelper() {
        
    }
    
    /** 靜態變數，儲存CourseHelper物件 */
    private static CourseHelper courh;
    
    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;
    
    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個CourseHelper物件
     *
     * @return the helper 回傳CourseHelper物件
     */
    public static CourseHelper getHelper() {
        /** Singleton檢查是否已經有CoachHelper物件，若無則new一個，若有則直接回傳 */
        if(courh == null) courh = new CourseHelper();
        
        return courh;
    }
    
    /**
     * 透過教程編號（ID）刪除教程
     *
     * @param id 教程編號
     * @return the JSONObject 回傳SQL執行結果
     */
    public JSONObject deleteByID(int id) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            
            /** SQL指令 */
            String sql = "UPDATE `sa_sharesport`.`courses` SET `status` = '1'  WHERE `id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行刪除之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);

        return response;
    }
    
   
    
    /**
     * 取回所有教程資料
     *
     * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
     */
    public JSONObject getAll() {
        /** 新建一個 Course 物件之 c 變數，用於紀錄每一位查詢回之教程資料 */
        Course c = null;
        /** 用於儲存所有檢索回之教程，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `sa_sharesport`.`courses`";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int course_id = rs.getInt("id");
                int coach_id = rs.getInt("course_coach_id");
                String name = rs.getString("name");
                String image = rs.getString("image");
                String information = rs.getString("information");
                int upper_limb = rs.getInt("upper_limb");
                int core = rs.getInt("core");
                int lower_limb = rs.getInt("lower_limb");
                Timestamp modified = rs.getTimestamp("modified");
                Timestamp created = rs.getTimestamp("created");
                int status = rs.getInt("status");
                
                /** 將每一筆教練資料產生一名新Coach物件 */
                c = new Course(course_id,coach_id,name,information, upper_limb, lower_limb, core, image, status, created, modified);
                /** 取出該名教練之資料並封裝至 JSONsonArray 內 */
                jsa.put(c.getData());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有教練資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    /**
     * 透過教程編號（ID）取得教程資料
     *
     * @param id 教練編號
     * @return the JSON object 回傳SQL執行結果與該教練編號之教練資料
     */
    public JSONObject getByID(String id) {
        /** 新建一個 Course 物件之 c 變數，用於紀錄每一位查詢回之教程資料 */
    	Course c = null;
        /** 用於儲存所有檢索回之教練，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `sa_sharesport`.`courses` WHERE `id` = ? ";
         
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);   
            pres.setString(1, id);
   
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該教練編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int course_id = rs.getInt("id");
                int coach_id = rs.getInt("course_coach_id");
                String name = rs.getString("name");
                String image = rs.getString("image");
                String information = rs.getString("information");
                int upper_limb = rs.getInt("upper_limb");
                int core = rs.getInt("core");
                int lower_limb = rs.getInt("lower_limb");
                Timestamp modified = rs.getTimestamp("modified");
                Timestamp created = rs.getTimestamp("created");
                int status = rs.getInt("status");
                
                /** 將每一筆教練資料產生一名新Coach物件 */
                c = new Course(course_id,coach_id,name,information, upper_limb, lower_limb, core, image, status, created, modified);
                /** 取出該名教練之資料並封裝至 JSONsonArray 內 */
                jsa.put(c.getData());
            }
           
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有教練資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    } 
    
    public JSONObject getByCoachID(String coach_id) {
        /** 新建一個 Course 物件之 c 變數，用於紀錄每一位查詢回之教程資料 */
    	Course c = null;
        /** 用於儲存所有檢索回之教練，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `sa_sharesport`.`courses` WHERE `course_coach_id` = ? ";
         
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);   
            pres.setString(1, coach_id);
   
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該教練編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int course_id = rs.getInt("id");
                int course_coach_id = rs.getInt("course_coach_id");
                String name = rs.getString("name");
                String image = rs.getString("image");
                String information = rs.getString("information");
                int upper_limb = rs.getInt("upper_limb");
                int core = rs.getInt("core");
                int lower_limb = rs.getInt("lower_limb");
                Timestamp modified = rs.getTimestamp("modified");
                Timestamp created = rs.getTimestamp("created");
                int status = rs.getInt("status");
                
                /** 將每一筆教練資料產生一名新Coach物件 */
                c = new Course(course_id,course_coach_id,name,information, upper_limb, lower_limb, core, image, status, created, modified);
                /** 取出該名教練之資料並封裝至 JSONsonArray 內 */
                jsa.put(c.getData());
            }
           
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有教練資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    } 
    
    /**
     * 透過教程編號（ID）取得教程姓名，為了subscribe的資訊
     *
     * @param id 教程編號
     * @return the JSON object 回傳SQL執行結果與該教程編號之教程資料
     */
    public JSONObject getNameByID(String id) {
        /** 新建一個 Course 物件之 c 變數，用於紀錄每一位查詢回之教程資料 */
    	Course c = null;
        /** 用於儲存所有檢索回之教程，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;

        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令，?為變數的意思 */
            String sql = "SELECT `name`,`information`FROM `sa_sharesport`.`courses` WHERE `id` = ? LIMIT 1";

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該教練編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;

                /** 將 ResultSet 之資料取出 */
                String name = rs.getString("name");
                String information = rs.getString("information");
                /** 將每一筆教練資料產生一名新Course物件 */
                c = new Course(name, information);
                /** 取出該名教練之資料並封裝至 JSONsonArray 內 */
                jsa.put(c.getSubData());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);

        /** 將SQL指令、花費時間、影響行數與所有教練資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        //response.put("sql", exexcute_sql);
        //response.put("row", row);
        //response.put("time", duration);
        response.put("data", jsa);

        return response;
    } 
    
    /**
     * 建立該教程至資料庫
     *
     * @param c 教程之course物件
     * @return the JSON object 回傳SQL指令執行之結果
     */
    public JSONObject create(Course c) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `sa_sharesport`.`courses`(`course_coach_id`, `name`, `information`, `image`, `upper_limb`, `lower_limb`, `core`,`status`, `modified`, `created`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            /** 取得所需之參數 */
            int id = c.getID();
            int coach_id = c.getCoachID();
            String name = c.getName();
            String information = c.getInfo();
            String image = c.getImage();
            int upper_limb = c.getUpper();
            int lower_limb = c.getLower();
            int core = c.getCore();
            int status = c.getStatus();
            Timestamp modified = c.getModified();
            Timestamp created = c.getCreated();
     
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, coach_id);
            pres.setString(2, name);
            pres.setString(3, information);
            pres.setString(4, image);
            pres.setInt(5, upper_limb);
            pres.setInt(6, lower_limb);
            pres.setInt(7, core);
            pres.setInt(8, status);
            pres.setTimestamp(9,modified);
            pres.setTimestamp(10, created);
          
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("time", duration);
        response.put("row", row);

        return response;
    }
    
    /**
     * 更新教程之資料
     *
     * @param c 教程之Course物件
     * @return the JSONObject 回傳SQL指令執行結果與執行之資料
     */
    public JSONObject update(Course c) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `sa_sharesport`.`courses` SET `name` = ? ,`information` = ? ,`image` = ?, `upper_limb` = ?,`lower_limb` = ?,`core` = ?, `status` =? ,`modified` =? WHERE `id` = ?";
            /** 取得所需之參數 */
            String name = c.getName();
            String information = c.getInfo();
            String image = c.getImage();
            int upper_limb = c.getUpper();
            int lower_limb = c.getLower();
            int core = c.getCore();
            int status = c.getStatus();
            Timestamp modified = c.getModified();
            int id = c.getID();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, name);
            pres.setString(2, information);
            pres.setString(3, image);
            pres.setInt(4, upper_limb);
            pres.setInt(5, lower_limb);
            pres.setInt(6, core);
            pres.setInt(7, status);
            pres.setTimestamp(8, modified);
            pres.setInt(9, id);
    
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    /**
     * 上傳教程之照片
     *
     * @param c 一名教程之Coach物件
     * @return the JSONObject 回傳SQL指令執行結果與執行之資料
     */
    public JSONObject saveImage(Course c) {

        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `sa_sharesport`.`courses` SET `image` = ? WHERE `id` = ?";
            /** 取得所需之參數 */
            
            String image = c.getName();  //因為用同一個建構子取得 filePath 變成getName方法     
            int id = c.getID();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, image);
            pres.setInt(2, id);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }

}
