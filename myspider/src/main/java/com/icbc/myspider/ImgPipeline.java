package com.icbc.myspider;

import com.icbc.myspider.util.Constants;
import com.icbc.myspider.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
/*
 * 处理页面解析后的信息
 */
public class ImgPipeline extends FilePersistentBase implements Pipeline {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public ImgPipeline() {
		setPath(Constants.DEST_DIR_CAOLIU);
	}

	public ImgPipeline(String path) {
		setPath(path);
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
	        Map<String, Object> fields = resultItems.getAll();
            List<String> postContent = (List<String>) fields.get("postContent");
            if(postContent != null){
            	String postName = (String) fields.get("postName");
	            for(String imgUrl: postContent){
	    			int indexOfSlash = imgUrl.lastIndexOf("/");
	    			String title = imgUrl.substring(indexOfSlash + 1);
	    			
	    			try {
	    				title = URLEncoder.encode(title, "UTF-8");
	    			} catch (UnsupportedEncodingException e1) {
	    				e1.printStackTrace();
	    			}
	    			imgUrl = imgUrl.substring(0, indexOfSlash + 1) + title;
	    			postName = postName.replaceAll("[\\/:\"\\*\\?\\|<>]", "");
	    		    String filePathDir = Constants.DEST_DIR_CAOLIU + postName;
	    		    logger.info("开始处理图片，" + imgUrl);
	    			try {
	    				FileUtil.downloadFile(imgUrl, filePathDir, title);
	    			}catch (Exception e) {
	    				logger.info("下载图片异常,imgUrl={}, filePathDir={},title={}",imgUrl, filePathDir, title , e);
	    			}
	            }
           }
	}

}