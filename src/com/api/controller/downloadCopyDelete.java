package com.api.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model.FileInfo;
import com.outputFormat.OutputFormat;




@RestController
@CrossOrigin(origins = "*")
public class downloadCopyDelete {

	//Logger Implementation
	static Logger logger = Logger.getLogger(downloadCopyDelete.class);

	//API for downloading any type of file
	@RequestMapping(value = "downloadFile", method = RequestMethod.POST)
	public OutputFormat downloadFile(@RequestBody FileInfo fileInfo, HttpServletResponse response) throws IOException, JSONException {
		OutputFormat output = new OutputFormat();
		Dictionary<Object, Object> data = new Hashtable<Object, Object>();
		Dictionary<Object, Object> errors = new Hashtable<Object, Object>();
		List<Dictionary<Object, Object>> dataList = new ArrayList<Dictionary<Object, Object>>();
		logger.info("Entering into Downloading functionality");
		String fileUrl = fileInfo.getOriginalFileLoc();
		String mimeType = null;
		File f = null;
		try {	
			f = new File(fileUrl);
			if(!f.exists()){
				logger.info("File not exist");
				output.setStatus(false);
				errors.put("Message", "File not exist");
				dataList.add(errors);
				output.setError(dataList);
				return output;
            }
			mimeType = getMimeType(f.getCanonicalPath());
		} catch (Exception e) {
			logger.info("Error in downloading file");
			output.setStatus(false);
			errors.put("Message", "Error in downloading file");
			dataList.add(errors);
			output.setError(dataList);
			return output;
		}
		if (mimeType == null) {
			mimeType = "application/excel";
		}
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", "attachment;filename=\"" + f.getName() + "\"");
		response.setContentLength((int) f.length());
		InputStream is = null;
		try {
			is = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ServletOutputStream outStream = null;
		try {
			outStream = response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			org.apache.commons.io.IOUtils.copy(is, outStream);
			logger.info("File Downloaded Successfully...");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.setStatus(true);
		data.put("Message", "File Downloaded Successfully");
		dataList.add(data);
		output.setData(dataList);
		return output;
	}

	
	//Method for finding mimeType of file
	public static String getMimeType(String fName) {

		fName = fName.toLowerCase();
		if (fName.endsWith(".jpg") || fName.endsWith(".jpeg") || fName.endsWith(".jpe"))
			return "image/jpeg";
		else if (fName.endsWith(".gif"))
			return "image/gif";
		else if (fName.endsWith(".pdf"))
			return "application/pdf";
		else if (fName.endsWith(".htm") || fName.endsWith(".html") || fName.endsWith(".htmls")
				|| fName.endsWith(".shtml"))
			return "text/html";
		else if (fName.endsWith(".avi"))
			return "video/x-msvideo";
		else if (fName.endsWith(".mov") || fName.endsWith(".qt"))
			return "video/quicktime";
		else if (fName.endsWith(".mpg") || fName.endsWith(".mpeg") || fName.endsWith(".mpe"))
			return "video/mpeg";
		else if (fName.endsWith(".zip"))
			return "application/zip";
		else if (fName.endsWith(".tiff") || fName.endsWith(".tif"))
			return "image/tiff";
		else if (fName.endsWith(".rtf"))
			return "application/rtf";
		else if (fName.endsWith(".mid") || fName.endsWith(".midi"))
			return "audio/x-midi";
		else if (fName.endsWith(".xl") || fName.endsWith(".xls") || fName.endsWith(".xlsx") || fName.endsWith(".xlv")
				|| fName.endsWith(".xla") || fName.endsWith(".xlb") || fName.endsWith(".xlt") || fName.endsWith(".xlm")
				|| fName.endsWith(".xlk"))
			return "application/excel";
		else if (fName.endsWith(".doc") || fName.endsWith(".docx") || fName.endsWith(".dot"))
			return "application/msword";
		else if (fName.endsWith(".png"))
			return "image/png";
		else if (fName.endsWith(".ppt") || fName.endsWith(".pptx"))
			return "application/mspowerpoint";
		else if (fName.endsWith(".js"))
			return "application/javascript";
		else if (fName.endsWith(".xml"))
			return "text/xml";
		else if (fName.endsWith(".svg"))
			return "image/svg+xml";
		else if (fName.endsWith(".mp3"))
			return "audio/mp3";
		else if (fName.endsWith(".ogg"))
			return "audio/ogg";
		else if (fName.endsWith(".mp4"))
			return "video/mp4";
		else
			return "text/plain";
	}

	
	//API for copy file 
	@RequestMapping(value = "copyFile", method = RequestMethod.POST)
	public OutputFormat copyFile(@RequestBody FileInfo fileInfo) throws IOException, JSONException {
		
		OutputFormat output = new OutputFormat();
		Dictionary<Object, Object> data = new Hashtable<Object, Object>();
		Dictionary<Object, Object> errors = new Hashtable<Object, Object>();
		List<Dictionary<Object, Object>> dataList = new ArrayList<Dictionary<Object, Object>>();
		logger.info("Entering into copying functionality");
		String originalFileName=fileInfo.getOriginalFileLoc();
		File original = new File(originalFileName);
		if(!original.exists()){
			logger.info("File not exist");
			output.setStatus(false);
			errors.put("Message", "File not exist");
			dataList.add(errors);
			output.setError(dataList);
			return output;
        }
		String fileName=originalFileName.substring((originalFileName.lastIndexOf(File.separator)+1),originalFileName.length());
		Random rand = new Random();
		String random_value = String.valueOf(100 + rand.nextInt(900));
		String copiedFileName=fileInfo.getCopiedFileLoc()+"\\"+fileName.split("\\.")[0]+random_value+"."+fileName.split("\\.")[1];
		File copied = new File(copiedFileName);
		if(copied.exists()){
			random_value = String.valueOf(100 + rand.nextInt(900));
			String newfileName=copiedFileName.substring((copiedFileName.lastIndexOf(File.separator)+1),copiedFileName.length());
			copiedFileName=fileInfo.getCopiedFileLoc()+"\\"+newfileName.split("\\.")[0]+random_value+"."+newfileName.split("\\.")[1];
			copied = new File(copiedFileName);
		}
		try {
        FileUtils.copyFile(original, copied);
        logger.info("Copying File Successfully...");
        output.setStatus(true);
		data.put("Message", "Filed Copied Successfully");
		dataList.add(data);
		output.setData(dataList);
		return output;
		}
		catch(Exception e) {
			logger.info("Error in copying file");
			output.setStatus(false);
			errors.put("Message", "Error in copying file");
			dataList.add(errors);
			output.setError(dataList);
			return output;
		}
	}
	
	
	//API for delete the file
	@RequestMapping(value = "deleteFile", method = RequestMethod.DELETE)
	public OutputFormat deleteFile(@RequestBody FileInfo fileInfo) throws IOException {
		OutputFormat output = new OutputFormat();
		Dictionary<Object, Object> data = new Hashtable<Object, Object>();
		Dictionary<Object, Object> errors = new Hashtable<Object, Object>();
		List<Dictionary<Object, Object>> dataList = new ArrayList<Dictionary<Object, Object>>();
		logger.info("Entering into delete functionality");
		File deleteFileUrl=new File(fileInfo.getOriginalFileLoc());
        if(deleteFileUrl.exists()){
            if(deleteFileUrl.delete()){
            	logger.info("File Deleted successfully");
            	output.setStatus(true);
        		data.put("Message", "File Deleted Successfully");
        		dataList.add(data);
        		output.setData(dataList);
        		return output;
            }else{
            	logger.info("Fail to delete file");
            	output.setStatus(false);
    			errors.put("Message", "Fail to delete file");
    			dataList.add(errors);
    			output.setError(dataList);
    			return output;
            }
        }
        else {
        	logger.info("File not exist");
        	output.setStatus(false);
			errors.put("Message", "File not exist");
			dataList.add(errors);
			output.setError(dataList);
			return output;
        }
        
    } 
	
}
