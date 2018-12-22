package com.icbc.myspider;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
/*
 * 获取抓取url，解析html页面
 */
@Service
@Slf4j
public class CaoLiuPageProcessor implements PageProcessor{
	
	 private Site site = Site.me().setRetryTimes(2).setSleepTime(500).setTimeOut(3 * 60 * 1000)
	            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
	            .setCharset("gb2312");

	@Override
	public void process(Page page) {
		log.info("开始处理page, " + page.getRequest().getUrl());
		//获取当前页面上的帖子地址
		List<String> postUrls = page.getHtml().xpath("//tr[@class='tr3 t_one tac']/td/h3/a/@href").all();
		//将帖子地址加入要抓取的url队列
		page.addTargetRequests(postUrls);
		
		//抓取其他页
		List<String> otherPageUrls = page.getHtml().links().regex(".*thread0806.php\\?fid=8&search=&page=\\d+").all();
		page.addTargetRequests(otherPageUrls);
		
		//进入帖子后，获取帖子标题
		String postName = page.getHtml().xpath("//tr[@class='tr1 do_not_catch']/th/table/tbody/tr/td/h4/text()").toString();
		if(postName != null){
			log.info("解析到一个有效帖子，postName={}, url={}",postName, page.getUrl());
			//进入帖子之后，帖子内容，即要抓取的图片url
			List<String> postContent = page.getHtml().xpath("//div[@class='tpc_content do_not_catch']/input/@src").all();
			page.putField("postContent", postContent);
			page.putField("postName", postName);
		}else{
			page.setSkip(true);
		}
	}

	@Override
	public Site getSite() {
		return site;
	}
}