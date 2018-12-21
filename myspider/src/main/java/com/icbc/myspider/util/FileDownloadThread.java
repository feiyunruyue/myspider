package com.icbc.myspider.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class FileDownloadThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private String imgUrl;
    private CloseableHttpClient client;
    private File file;

    public FileDownloadThread(CloseableHttpClient client,String imgUrl, File file){
        this.client = client;
        this.imgUrl = imgUrl;
        this.file = file;
    }

    @Override
    public void run(){
        long startTime = System.currentTimeMillis();
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
                }finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
            }
        } catch (IOException e) {
            logger.info("下载图片异常,imgUrl={}",imgUrl, e);
        }finally {
            httpGet.releaseConnection();
        }

        long endTime = System.currentTimeMillis();
        logger.info("下载耗时{}ms，imgUrl={}", endTime - startTime, imgUrl);
    }
}
