package com.icbc.myspider.util;

import com.icbc.mapper.ImgInfoMapper;
import com.icbc.model.ImgInfo;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class FileUtil {
	private CloseableHttpClient client = MyHttpClient.generateClient() ;
	private static ExecutorService executorService = Executors.newFixedThreadPool(3);

	@Resource
	private ImgInfoMapper imgInfoMapper;

	public void downloadFile(String imgUrl, String postName, String fileName) throws IOException {
		// 目标目录
		String filePathDir = Constants.DEST_DIR_CAOLIU + postName;
		File desPathFile = new File(filePathDir);
		if (!desPathFile.exists()) {
			desPathFile.mkdirs();
		}
		//文件绝对路径
		String fullPath = filePathDir + File.separator + fileName;
		File file = new File(fullPath);
		if(file.exists()){
			return ;
		}

		ImgInfo imgInfo = new ImgInfo();
		imgInfo.setTitle(postName);
		imgInfo.setUrl(imgUrl);
		imgInfoMapper.insert(imgInfo);

		// 线程池 TODO 改成Callable 判断是否下载成功，或者下载成功后插数据库？
		executorService.execute(new FileDownloadThread(client, imgUrl, file));
	}
}