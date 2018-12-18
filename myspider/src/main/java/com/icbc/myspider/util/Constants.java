package com.icbc.myspider.util;

public class Constants {
	public static final String DOMAIN = "";//网站域名, 自己去找
	public static final String START_URL = DOMAIN + "/thread0806.php?fid=8";//要抓取的入口url
	public static final String DIR = "/server/webmagic/";
	public static final String DEST_DIR_CAOLIU = DIR + "caoliu/";//图片所在目录
	public static final String FILE_CACHE_URLS = DIR + "urls/";//记录url,下次重启时可以从之前抓取到的URL继续
}
