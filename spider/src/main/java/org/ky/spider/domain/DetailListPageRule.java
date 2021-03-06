package org.ky.spider.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * 网页列表-详情页类型爬取规则
 * 
 * @author snowmeteor
 *
 */
public class DetailListPageRule {

	private String listPageUrl;// 列表页起始url
	private String detailPageXpath;// 详情页对应的xpath规则，必须有
	private String detailPageRegexExp;// 详情页对应的正则，可以没有

	private String nextPageXpath;// 下一页对应的xpath规则，必须有
	private String nextPageRegexExp;// 下一页对应的正则，可以没有

	public String getListPageUrl() {
		return listPageUrl;
	}

	public void setListPageUrl(String listPageUrl) {
		this.listPageUrl = StringUtils.trim(listPageUrl);
	}

	public String getDetailPageXpath() {
		return detailPageXpath;
	}

	public void setDetailPageXpath(String detailPageXpath) {
		this.detailPageXpath = StringUtils.trim(detailPageXpath);
	}

	public String getDetailPageRegexExp() {
		return detailPageRegexExp;
	}

	public void setDetailPageRegexExp(String detailPageRegexExp) {
		this.detailPageRegexExp = StringUtils.trim(detailPageRegexExp);
	}

	public String getNextPageXpath() {
		return nextPageXpath;
	}

	public void setNextPageXpath(String nextPageXpath) {
		this.nextPageXpath = StringUtils.trim(nextPageXpath);
	}

	public String getNextPageRegexExp() {
		return nextPageRegexExp;
	}

	public void setNextPageRegexExp(String nextPageRegexExp) {
		this.nextPageRegexExp = StringUtils.trim(nextPageRegexExp);
	}

	@Override
	public String toString() {
		return "DetailListPageRule [listPageUrl=" + listPageUrl + ", detailPageXpath=" + detailPageXpath
				+ ", detailPageRegexExp=" + detailPageRegexExp + ", nextPageXpath=" + nextPageXpath
				+ ", nextPageRegexExp=" + nextPageRegexExp + "]";
	}

}
