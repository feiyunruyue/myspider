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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

	private static final int TIME_OUT = 5000; // 超时时间
	private static CloseableHttpClient client = HttpClients.createDefault();
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static void downloadFile(String imgUrl, String filePathDir,
			String fileName) throws Exception {
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

		HttpGet httpGet = new HttpGet(imgUrl);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(TIME_OUT).
				setSocketTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT).build();
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
		httpGet.setConfig(config);
		CloseableHttpResponse response = client.execute(httpGet);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode != 200){
			logger.info("不是200");
			return ;
		}
		InputStream in = null;
		OutputStream out = null;
		try{
			in = response.getEntity().getContent();
			out = new FileOutputStream(file);
			IOUtils.copy(in, out);
		}finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}
	
}