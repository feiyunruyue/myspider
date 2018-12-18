package com.icbc.myspider.util;

import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileUtil {
	private static CloseableHttpClient client = MyHttpClient.generateClient();
	private static ExecutorService executorService = Executors.newFixedThreadPool(3);

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

		// TODO 插入数据库

		// 线程池 TODO 改成Callable 判断是否下载成功，或者下载成功后插数据库？
		executorService.execute(new FileDownloadThread(client, imgUrl, file));
	}
}