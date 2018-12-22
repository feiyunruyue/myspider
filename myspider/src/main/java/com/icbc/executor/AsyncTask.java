package com.icbc.executor;

import com.icbc.model.ImgInfo;
import com.icbc.myspider.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.concurrent.Future;

@Component("downLoadTask")
@Slf4j
public class AsyncTask {

    @Resource(name = "myClient")
    private CloseableHttpClient client;

    @Async
    public Future<String> downLoadImg(ImgInfo imgInfo) {
        log.info("Task1 started.");
        long startTime = System.currentTimeMillis();
        String imgUrl = imgInfo.getUrl();
        String title = imgInfo.getTitle();

        int indexOfSlash = imgUrl.lastIndexOf("/");
        String imgName = imgUrl.substring(indexOfSlash + 1);
        String filePathDir = Constants.DEST_DIR_CAOLIU + title;
        String fullPath = filePathDir + File.separator + imgName;
        File file = new File(fullPath);
        if (file.exists()) {
            return new AsyncResult<>("xxx");
        }
        HttpGet httpGet = new HttpGet(imgUrl);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = response.getEntity().getContent();
                    out = new FileOutputStream(file);
                    IOUtils.copy(in, out);
                } catch (Exception e) {
                    log.info("保存图片异常,imgUrl={}", imgUrl, e);
                } finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
            }
        } catch (IOException e) {
            log.info("下载图片异常,imgUrl={}", imgUrl, e);
        } finally {
            httpGet.releaseConnection();
        }

        long endTime = System.currentTimeMillis();
        log.info("下载图片，耗时{}, imgUrl={}", endTime - startTime, imgUrl);
        return new AsyncResult<>("Task1 accomplished!");
    }

}
