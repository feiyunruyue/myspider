package com.icbc.myspider;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

import com.icbc.myspider.util.Constants;

import javax.annotation.Resource;

/*
 * 功能：爬虫入口，爬取并下载论坛上的图片，仅供学习
 * 论坛域名已经隐去，请在Constants里自行配置
 *
 *  TODO 新增pipeline 记录到数据库
 */
@Service
public class MySpider {
	@Resource
	private DbPipeline dbPipeline;
	@Resource
	private PageProcessor caoLiuPageProcessor;

	public void start() {
		Spider.create(caoLiuPageProcessor)
				.addUrl(Constants.START_URL)
				.addPipeline(dbPipeline)
				.setScheduler(new FileCacheQueueScheduler(Constants.FILE_CACHE_URLS))
				.thread(3)
				.run();
				
	}

}