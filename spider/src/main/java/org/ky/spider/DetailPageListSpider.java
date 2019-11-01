package org.ky.spider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ky.spider.domain.DetailListPageRule;

/**
 * 爬取列表页，获取对应的详情页URL
 * 
 * @author snowmeteor
 *
 */
public class DetailPageListSpider {

	/**
	 * 分页爬取列表页，获取所有列表页对应的详情页URL
	 * 
	 * @param dlRule
	 * @param isDynamicPage
	 * @return
	 */
	public static List<String> getDetailPageUrl(DetailListPageRule dlRule, boolean isDynamicPage) {
		String firstUrl = dlRule.getListPageUrl();

		List<String> allListPageUrls = new ArrayList<>();
		List<String> allDetailUrls = new ArrayList<>();
		getListPageUrl(allListPageUrls, firstUrl, dlRule, isDynamicPage, allDetailUrls);

		return allDetailUrls;
	}

	private static void getListPageUrl(List<String> allListPageUrls, String url, DetailListPageRule dlRule,
			boolean isDynamicPage, List<String> allDetailUrls) {

		String html = isDynamicPage ? CrawlHelper.getDynamicPageHtml(url) : CrawlHelper.getStaticPageHtml(url);
		if (StringUtils.isBlank(html)) {
			return;
		}

		// 获取详情页url
		List<String> detailUrls = getAbsoluteUrlList(url, html, dlRule.getDetailPageXpath(),
				dlRule.getDetailPageRegexExp());

		if (CollectionUtils.isNotEmpty(detailUrls)) {
			for (String dUrl : detailUrls) {
				if (!allDetailUrls.contains(dUrl)) {
					allDetailUrls.add(dUrl);
				}
			}
		}

		// 获取列表页url
		List<String> listPageUrls = getAbsoluteUrlList(url, html, dlRule.getNextPageXpath(),
				dlRule.getNextPageRegexExp());
		if (CollectionUtils.isNotEmpty(listPageUrls)) {
			for (String lpUrl : listPageUrls) {
				if (allListPageUrls.contains(lpUrl)) {
					continue;
				}
				allListPageUrls.add(lpUrl);
				getListPageUrl(allListPageUrls, lpUrl, dlRule, isDynamicPage, allDetailUrls);
			}
		}

	}

	private static List<String> getAbsoluteUrlList(String refUrl, String html, String xpath, String regexExp) {
		List<String> detailUrls = CrawlHelper.fetchTextList(html, xpath);
		if (CollectionUtils.isEmpty(detailUrls)) {
			return null;
		}

		// 去重
		Set<String> urlSets = new HashSet<>();
		urlSets.addAll(detailUrls);

		detailUrls.clear();
		detailUrls.addAll(urlSets);

		if (StringUtils.isNoneBlank(regexExp)) {
			int size = detailUrls.size();
			for (int i = 0; i < size; i++) {
				detailUrls.set(i, CrawlHelper.fetchTextByRegex(detailUrls.get(i), regexExp));
			}
		}

		return CrawlHelper.buildAbsoluteUrlList(refUrl, detailUrls);
	}
}
