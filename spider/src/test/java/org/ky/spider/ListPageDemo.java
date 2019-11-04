package org.ky.spider;

import java.util.List;

import org.ky.spider.domain.DetailListPageRule;

/**
 * 列表页爬取获得详情页URL
 * 
 * @author snowmeteor
 *
 */
public class ListPageDemo {

	public static void main(String[] args) throws Exception {

		// 列表页URL
		String url = "http://www.mingyihui.net/0_19_doctors.html"; 

		// 设置列表页爬取规则
		DetailListPageRule dlRule = new DetailListPageRule();
		dlRule.setListPageUrl(url);
		dlRule.setDetailPageXpath("//div[@class='H-phone']//li/a/@href");
		dlRule.setNextPageXpath("//div[@class='H_tra w900']//a/@href");
		dlRule.setNextPageRegexExp("0_19_0_doctors_\\d+\\.html");

		// 爬取详情页URL
		List<String> detailUrls = DetailPageListSpider.getDetailPageUrl(dlRule, false);
		for (String detailUrl : detailUrls) {
			System.out.println(detailUrl);
		}
		
//		//动态网页列表爬取示例
//		String url = "https://www.toutiao.com/c/user/1857275809/#mid=3459271237";
//		// 设置列表页爬取规则
//		DetailListPageRule dlRule = new DetailListPageRule();
//		dlRule.setListPageUrl(url);
//		dlRule.setDetailPageXpath("//div[@class='rbox-inner']/div[@class='title-box']/a/@href");
//		
//
//		// 爬取详情页URL
//		List<String> detailUrls = DetailPageListSpider.getDetailPageUrl(dlRule, true);
//		for (String detailUrl : detailUrls) {
//			System.out.println(detailUrl);
//		}
	}

}
