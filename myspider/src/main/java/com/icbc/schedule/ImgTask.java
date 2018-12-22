package com.icbc.schedule;

import com.icbc.executor.AsyncTask;
import com.icbc.mapper.ImgInfoMapper;
import com.icbc.model.FutureImgResult;
import com.icbc.model.ImgInfo;
import com.icbc.model.ImgStatusEnum;
import com.icbc.myspider.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
        while (CollectionUtils.isNotEmpty(list)) {
            List<Future<FutureImgResult>> futureList = new ArrayList<>();
            for (ImgInfo info : list) {
                String filePathDir = Constants.DEST_DIR_CAOLIU + info.getTitle();
                File desPathFile = new File(filePathDir);
                if (!desPathFile.exists()) {
                    desPathFile.mkdirs();
                }
                try {
                    Future<FutureImgResult> result = downLoadTask.downLoadImg(info);
                    futureList.add(result);
                } catch (Exception e) {
                    log.info("线程池问题", e);
                    return;
                }
            }

            List<Integer> successIdList = new ArrayList<>();
            List<Integer> failIdList = new ArrayList<>();
            for(Future<FutureImgResult> future : futureList){
                try {
                    FutureImgResult result = future.get();
                    if(result.getSuccess()){
                        successIdList.add(result.getId());
                    }else{
                        failIdList.add(result.getId());
                    }
                } catch (Exception e) {
                    log.info("获取多线程结果异常，future={}",future, e );
                }
            }
            log.info("failIdList={}", failIdList);
            if(CollectionUtils.isNotEmpty(successIdList)){
                int successResult = imgInfoMapper.updateStatusByIdList(ImgStatusEnum.DOWN_SUCCESS.getValue(), successIdList);
                log.info("更新了{}条记录为成功", successResult);
            }
            if(CollectionUtils.isNotEmpty(failIdList)){
                int failResult = imgInfoMapper.updateStatusByIdList(ImgStatusEnum.DOWN_FAIL.getValue(), failIdList);
                log.info("更新了{}条记录为失败", failResult);
            }
            list = imgInfoMapper.getList(ImgStatusEnum.INIT.getValue());
        }
        log.info("任务结束，已处理完成");
    }
}


