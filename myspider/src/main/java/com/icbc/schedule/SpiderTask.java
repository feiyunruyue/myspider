package com.icbc.schedule;

import com.icbc.executor.AsyncTask;
import com.icbc.mapper.ImgInfoMapper;
import com.icbc.model.FutureImgResult;
import com.icbc.model.ImgInfo;
import com.icbc.model.ImgStatusEnum;
import com.icbc.myspider.DbPipeline;
import com.icbc.myspider.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Component
@Slf4j
public class SpiderTask {

    @Resource
    private DbPipeline dbPipeline;
    @Resource
    private PageProcessor caoLiuPageProcessor;

    @Scheduled(cron = "0 0/5 * * * *")
    public void execute() {
        log.info("爬虫任务开始");
        Spider.create(caoLiuPageProcessor)
                .addUrl(Constants.START_URL)
                .addPipeline(dbPipeline)
                .setScheduler(new FileCacheQueueScheduler(Constants.FILE_CACHE_URLS))
                .thread(3)
                .run();
        log.info("爬虫任务结束，已处理完成");
    }
}


