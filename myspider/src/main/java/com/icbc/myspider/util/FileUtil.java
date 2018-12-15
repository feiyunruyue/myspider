package com.icbc.myspider.util;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtil {
	
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
		URL url = new URL(imgUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
		con.setConnectTimeout(5*1000);
		int statusCode = con.getResponseCode();
		if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY){
			String location = con.getHeaderField("location");
			downloadFile(location, filePathDir, fileName);
			return ;
		}

		if(statusCode != 200){
			return ;
		}
		InputStream in = con.getInputStream();

		if(in != null){
		try {
			FileOutputStream fout = new FileOutputStream(file);
			int len = -1;
			byte[] tmp = new byte[1024 * 1024];
			while ((len = in.read(tmp)) != -1) {
				fout.write(tmp, 0, len);
			}
			fout.flush();
			fout.close();
		} finally {
			in.close();
		}
	}
	}
	
}