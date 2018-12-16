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

		HttpGet httpGet = new HttpGet(imgUrl);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK){
				InputStream in = null;
				OutputStream out = null;
				try{
					in = response.getEntity().getContent();
					out = new FileOutputStream(file);
					IOUtils.copy(in, out);
				}catch (Exception e){
					logger.info("保存图片异常,imgUrl={}",imgUrl, e);
					return;
				}finally {
					IOUtils.closeQuietly(in);
					IOUtils.closeQuietly(out);
				}
			}
		} catch (IOException e) {
			logger.info("下载图片异常,imgUrl={}",imgUrl, e);
			return;
		}finally {
			httpGet.releaseConnection();
			if(response != null){
				response.close();
			}
		}
	}
}