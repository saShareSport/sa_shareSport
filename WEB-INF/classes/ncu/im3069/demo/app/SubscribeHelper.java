package ncu.im3069.demo.app;

import java.sql.*;
import org.json.*;

import ncu.im3069.demo.util.DBMgr;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class SubscribeHelper<br>
 * SubscribeHelper類別（class）主要管理所有與Subscribe相關與資料庫之方法（method）
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class SubscribeHelper {

	/**
     * 實例化（Instantiates）一個新的（new）SubscribeHelper物件<br>
     * 採用Singleton不需要透過new
     */
    private SubscribeHelper() {

    }

    /** 靜態變數，儲存SubscribeHelper物件 */
    private static SubscribeHelper subh;

    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;

    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;

    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個SubscribeHelper物件
     *
     * @return the helper 回傳SubscribeHelper物件
     */
    public static SubscribeHelper getHelper() {
        /** Singleton檢查是否已經有FollowHelper物件，若無則new一個，若有則直接回傳 */
        if(subh == null) subh = new SubscribeHelper();

        return subh;
    }
    /**
     * 透過追蹤編號（ID）刪除訂閱
     *
     * @param id 訂閱編號
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
            String sql = "DELETE FROM `sa_sharesport`.`subscribes` WHERE `id` = ? LIMIT 1";

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
     * 取回所有訂閱資料
     *
     * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
     */
    public JSONObject getAll() {
        /** 新建一個 Subscribe 物件之 s 變數，用於紀錄每一筆查詢回之訂閱資料 */
    	Subscribe s = null;
        /** 用於儲存所有檢索回之訂閱，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `sa_sharesport`.`subscribes`";

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
                int sub_id = rs.getInt("id");
                int sub_stuId = rs.getInt("sub_stuId");
                int sub_courId = rs.getInt("sub_courId");

                /** 將每一筆訂閱資料產生一名新Subscribe物件 */
                s = new Subscribe(sub_id, sub_stuId, sub_courId);
                /** 取出該訂閱之資料並封裝至 JSONsonArray 內 */
                jsa.put(s.getData());
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

        /** 將SQL指令、花費時間、影響行數與所有訂閱資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    /**
     * 透過學生編號（ID）取得該學生的所有訂閱資料
     *
     * @param stuid 學生編號
     * @return the JSON object 回傳SQL執行結果與該學生編號之所有該學生的訂閱資料
     */
    public JSONObject getByID(String stuId) {
        /** 新建一個Subscribe 物件之 s 變數，用於紀錄每一筆查詢回之訂閱資料 */
    	Subscribe s = null;
        /** 用於儲存所有檢索回之該學生訂閱資料，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `sa_sharesport`.`subscribes` WHERE `subscribe_student_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, stuId);
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
                int subscribe_id = rs.getInt("id");
                int sub_stuId = rs.getInt("subscribe_student_id");
                int sub_courId = rs.getInt("subscribe_course_id");
//                System.out.println(sub_courId);
                /** 將每一筆訂閱資料產生一名新SubscribeCoach物件 */
                s = new Subscribe(subscribe_id, sub_stuId, sub_courId);
                /** 取出該名訂閱之資料並封裝至 JSONsonArray 內 */
                jsa.put(s.getData());
//              jsa.put(s.getStudentFollowData());
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
        
        /** 將SQL指令、花費時間、影響行數與所有訂閱資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        //response.put("sql", exexcute_sql);
        //response.put("row", row);
        //response.put("time", duration);
        response.put("data", jsa);

        return response;
    } 
    /**
     * 檢查該名學生是否已經訂閱過該教程
     *
     * @param s 一筆訂閱之Subscribe物件
     * @return boolean 若重複追蹤回傳False，若該追蹤紀錄不存在則回傳True
     */
    public boolean checkDuplicate(Subscribe s){
        /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        int row = -1;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT count(*) FROM `sa_sharesport`.`subscribes` WHERE `subscribe_student_id` = ? AND `subscribe_course_id` = ?";
  
            /** 取得所需之參數 */
            int stuId = s.getSubStudentId();
            int courId = s.getSubCourseId();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, stuId);
            pres.setInt(2, courId);
            
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
            rs.next();
            row = rs.getInt("count(*)");
            System.out.print(row);

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
        
        /** 
         * 判斷是否已經有一筆該學生訂閱該教程之資料
         * 若無一筆則回傳False，否則回傳True 
         */
        return (row == 0) ? false : true;
    }
    /**
     * 檢查該該教程被訂閱幾次
     *
     * @param courId String
     * @return int 回傳次數
     */
    public int countSub(String courId){
        /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        int row = -1;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT count(*) FROM `sa_sharesport`.`subscribes` WHERE `subscribe_course_id` = ?";
  
            /** 取得所需之參數 */
//            int courId = s.getSubCourseId();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, courId);
            
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
            rs.next();
            row = rs.getInt("count(*)");
            System.out.print(row);

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
        
        /** 
         	*回傳該教程之次數
         */
        return row;
    }
    /**
     * 建立該訂閱紀錄至資料庫
     *
     * @param s 一個訂閱之Subscribe物件
     * @return the JSON object 回傳SQL指令執行之結果
     */
    public JSONObject create(Subscribe s) {
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
            String sql = "INSERT INTO `sa_sharesport`.`subscribes`(`subscribe_student_id`, `subscribe_course_id`)"
                    + " VALUES(?, ?)";
            
            /** 取得所需之參數 */ 
            int stuId = s.getSubStudentId(); 
            int courId = s.getSubCourseId();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, stuId);
            pres.setInt(2, courId);
            
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
    
}