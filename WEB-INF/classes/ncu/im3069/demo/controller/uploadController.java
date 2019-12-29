package ncu.im3069.demo.controller;

import java.io.*;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Coach;
import ncu.im3069.demo.app.Student;
import ncu.im3069.tools.JsonReader;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class StudentController<br>
 * StudentController類別（class）主要用於處理Student相關之Http請求（Request），繼承HttpServlet
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */
@WebServlet("/api/upload.do")
public class uploadController extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	
	// 上傳檔案儲存目錄
	private static final String UPLOAD_DIRECTORY = "upload";

//	private static final String UPLOAD_DIRECTORY = "statics\\img";
	private String filePath = null;
	private String fileName = null;
	private String savePath = null;
	// 上傳配置
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

	/**
	 * 上傳資料及儲存檔案
	 */
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
//		JsonReader jsr = new JsonReader(request);
		// 檢測是否為多媒體上傳
		if (!ServletFileUpload.isMultipartContent(request)) {
			// 如果不是則停止
			PrintWriter writer = response.getWriter();
			writer.println("Error: 表單必須包含 enctype=multipart/form-data");
			writer.flush();
			return;
		}
		
		// 配置上傳引數
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 設定記憶體臨界值 - 超過後將產生臨時檔案並存儲於臨時目錄中
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		// 設定臨時儲存目錄
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 設定最大檔案上傳值
		upload.setFileSizeMax(MAX_FILE_SIZE);
		// 設定最大請求值 (包含檔案和表單資料)
		upload.setSizeMax(MAX_REQUEST_SIZE);
		// 構造臨時路徑來儲存上傳的檔案
		// 這個路徑相對當前應用的目錄
		String uploadPath = getServletContext().getRealPath("./")+ File.separator + UPLOAD_DIRECTORY;
				
		System.out.println(uploadPath);
		// 如果目錄不存在則建立
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		
		try {
			// 解析請求的內容提取檔案資料
			@SuppressWarnings("unchecked")			
			List<FileItem> formItems = upload.parseRequest(request);
			if (formItems != null && formItems.size() > 0) {
				// 迭代表單資料
				for (FileItem item : formItems) {
					// 處理不在表單中的欄位
					if (!item.isFormField()) {					
						fileName = new File(item.getName()).getName();
						filePath = uploadPath + File.separator+ fileName;
						//儲存在資料庫的路徑
						savePath = ".\\upload\\"+fileName;

						File storeFile = new File(filePath);
						// 在控制檯輸出檔案的上傳路徑
						System.out.println(filePath);
						// 儲存檔案到硬碟
						item.write(storeFile);
						request.setAttribute("message", "照片上傳成功!");						
					}
				}
				
			}
			
			
		} catch (Exception ex) {
			request.setAttribute("message", "錯誤資訊: " + ex.getMessage());
		}
		
	}
    
	 /**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        String role = jso.getString("role");
        JSONObject data = null;
        if(role.equals("coach")) {
             /** 透過傳入之參數，新建一個以這些參數之教練物件 */
             Coach c = new Coach(id,savePath);  		
             /** 透過Coach物件的saveImg()方法至資料庫更新該名教練資料，回傳之資料為JSONObject物件 */
             data = c.saveImg();
        }else if(role.equals("student")) {
        	 Student s = new Student(id,savePath);
             data = s.saveImg();
        }
       
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新照片");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }
}