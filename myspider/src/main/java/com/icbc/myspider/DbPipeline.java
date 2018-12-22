package com.icbc.myspider;

import com.icbc.mapper.ImgInfoMapper;
import com.icbc.model.ImgInfo;
import com.icbc.myspider.util.Constants;
import com.icbc.myspider.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 * 处理页面解析后的信息
 */
@Service
@Slf4j
public class DbPipeline implements Pipeline {
	@Resource
	private ImgInfoMapper imgInfoMapper;

	@Override
	public void process(ResultItems resultItems, Task task) {
	        Map<String, Object> fields = resultItems.getAll();
            List<String> postContent = (List<String>) fields.get("postContent");
            if(postContent != null){
            	String postName = (String) fields.get("postName");
            	List<ImgInfo> imgList = new ArrayList<>();
				postName = postName.replaceAll("[\\/:\"\\*\\?\\|<>]", "");
				for(String imgUrl: postContent){
					log.info("开始处理图片，postName={}, imgUrl={}" , postName, imgUrl);
					ImgInfo imgInfo = new ImgInfo();
					imgInfo.setTitle(postName);
					imgInfo.setUrl(imgUrl);
					imgList.add(imgInfo);
				}
				if(CollectionUtils.isNotEmpty(imgList)){
					int result = imgInfoMapper.insertBatch(imgList);
					log.info("postName={},共插入{}条数据", postName, result);
				}
           }
	}

}