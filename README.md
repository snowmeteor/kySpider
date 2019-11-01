# kySpider
网页结构化知识爬取

## 列表页爬取示例
```
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
```

## 详情页爬取示例
```
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
```
