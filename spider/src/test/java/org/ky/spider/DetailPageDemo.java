package org.ky.spider;

import java.util.ArrayList;
import java.util.List;

import org.ky.spider.domain.DetailPageRule;

/**
 * 单网页爬取成结构化知识详情
 * 
 * @author snowmeteor
 *
 */
public class DetailPageDemo {

	public static void main(String[] args) throws Exception {
		// 详情页URL
		String url = "http://www.mingyihui.net/doctor_22786.html";

		// 设置详情页爬取规则
		List<DetailPageRule> rules = new ArrayList<>();
		DetailPageRule rule = new DetailPageRule();
		rule.setId("标签");
		rule.setXpath(DetailPageRule.CONSTANT_EXP_PREFIX + "医生");

		rules.add(rule);
		rule = new DetailPageRule();
		rule.setId("职称");
		rule.setXpath("//div[@class='doctorName']//li[1]/text()");
		rules.add(rule);
		
		rule = new DetailPageRule();
		rule.setId("医院");
		rule.setXpath("//div[@class='hospitalocation']/p/text()");
		rules.add(rule);
		
		rule = new DetailPageRule();
		rule.setId("医生");
		rule.setXpath("//div[@class='doctorName']/h1/text()");
		rules.add(rule);

		// 爬取详情页根据规则转化为结构化知识
		System.out.println(DetailPageSpider.crawl(url, rules, false));

	}

}
