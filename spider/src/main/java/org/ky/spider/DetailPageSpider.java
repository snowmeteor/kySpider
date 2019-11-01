package org.ky.spider;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ky.spider.domain.DetailPageRule;
import org.ky.spider.domain.Knowledge;
import org.ky.spider.domain.KnowledgeAttribute;

/**
  * 网页详情页结构化处理
 * 
 * @author snowmeteor
 *
 */
public class DetailPageSpider {

	/**
	 * 网页详情页结构化知识处理
	 * @param url
	 * @param rules
	 * @param isDynamicPage
	 * @return
	 */
	public static Knowledge crawl(String url, List<DetailPageRule> rules, boolean isDynamicPage) {
		String html = isDynamicPage ? CrawlHelper.getDynamicPageHtml(url) : CrawlHelper.getStaticPageHtml(url);
		if (StringUtils.isBlank(html)) {
			return null;
		}

		Knowledge knowledge = new Knowledge();
		knowledge.setTitle(CrawlHelper.getHtmlTitle(html));
		knowledge.setUrl(url);

		if (CollectionUtils.isEmpty(rules)) {
			return knowledge;
		}

		for (DetailPageRule rule : rules) {
			KnowledgeAttribute attribute = new KnowledgeAttribute();
			attribute.setId(rule.getId());
			if (rule.isConstantExp()) {
				attribute.setValue(rule.getConstantValue());
				knowledge.getAttributes().add(attribute);
				continue;
			}

			if (StringUtils.isNotBlank(rule.getXpath())) {
				String value = CrawlHelper.fetchText(html, rule.getXpath());
				if (StringUtils.isNoneBlank(rule.getRegexExp())) {
					value = CrawlHelper.fetchTextByRegex(value, rule.getRegexExp());
				}
				attribute.setValue(value);
			}

			knowledge.getAttributes().add(attribute);
		}

		return knowledge;
	}
}
