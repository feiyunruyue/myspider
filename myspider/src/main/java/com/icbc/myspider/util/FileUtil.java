package com.icbc.myspider.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileUtil {
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	private static CloseableHttpClient client = MyHttpClient.generateClient();

	public static void downloadFile(String imgUrl, String filePathDir, String fileName) throws IOException {
		// 目标目录
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

		// TODO 线程池
		new FileDownloadThread(client, imgUrl, file).run();
	}
}