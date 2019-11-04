package org.ky.spider;

public class Demo {

	public static void main(String[] args) {
		// 测试头条新闻兼容性
		String url = "https://www.toutiao.com/a6755070347285889543/";

		String html = CrawlHelper.getStaticPageHtml(url);
		//System.out.println(html);

		//用正则获取需要的内容
		System.out.println(CrawlHelper.fetchTextByRegex(html, "articleInfo(.*?)commentInfo:"));
	}

}
