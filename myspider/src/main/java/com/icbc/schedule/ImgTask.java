package com.icbc.schedule;

import com.icbc.executor.AsyncTask;
import com.icbc.mapper.ImgInfoMapper;
import com.icbc.model.ImgInfo;
import com.icbc.model.ImgStatusEnum;
import com.icbc.myspider.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Component
@Slf4j
public class ImgTask {

    @Resource
    private ImgInfoMapper imgInfoMapper;
    @Resource
    private AsyncTask downLoadTask;

    @Scheduled(cron = "0 0/1 * * * *")
    public void execute() {
        log.info("下载图片任务开始");
        List<ImgInfo> list = imgInfoMapper.getList(ImgStatusEnum.INIT.getValue());
        if (CollectionUtils.isNotEmpty(list)) {
            for (ImgInfo info : list) {
                String filePathDir = Constants.DEST_DIR_CAOLIU + info.getTitle();
                File desPathFile = new File(filePathDir);
                if (!desPathFile.exists()) {
                    desPathFile.mkdirs();
                }
                String title = info.getTitle();
                String imgUrl = info.getUrl();
                int indexOfSlash = imgUrl.lastIndexOf("/");
                String imgName = imgUrl.substring(indexOfSlash + 1);
                //文件绝对路径
                String fullPath = filePathDir + File.separator + imgName;
                File file = new File(fullPath);
                if (file.exists()) {
                    continue;
                }
                try {
                    downLoadTask.downLoadImg(info);
                } catch (Exception e) {
                    log.info("线程池问题", e);
                }
            }
        }
    }
}


