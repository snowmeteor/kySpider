package org.ky.spider;

public class Demo {

	public static void main(String[] args) {
		// 测试头条新闻兼容性
		String url = "https://www.toutiao.com/i6754183669176336909/";

		System.out.println(CrawlHelper.getDynamicPageHtml(url));
	}

}
