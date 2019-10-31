package org.ky.spider.domain;

/**
 * 网页列表-详情页类型爬取规则
 * 
 * @author snowmeteor
 *
 */
public class DetailListPageRule {

	private String listPageUrl;// 列表页起始url
	private String detailPageXpath;// 详情页对应的xpath规则
	private String detailPageRegexExp;// 详情页对应的正则

	private String nextPageXpath;// 下一页对应的xpath规则
	private String nextPageRegexExp;// 下一页对应的正则

	public String getListPageUrl() {
		return listPageUrl;
	}

	public void setListPageUrl(String listPageUrl) {
		this.listPageUrl = listPageUrl;
	}

	public String getDetailPageXpath() {
		return detailPageXpath;
	}

	public void setDetailPageXpath(String detailPageXpath) {
		this.detailPageXpath = detailPageXpath;
	}

	public String getDetailPageRegexExp() {
		return detailPageRegexExp;
	}

	public void setDetailPageRegexExp(String detailPageRegexExp) {
		this.detailPageRegexExp = detailPageRegexExp;
	}

	public String getNextPageXpath() {
		return nextPageXpath;
	}

	public void setNextPageXpath(String nextPageXpath) {
		this.nextPageXpath = nextPageXpath;
	}

	public String getNextPageRegexExp() {
		return nextPageRegexExp;
	}

	public void setNextPageRegexExp(String nextPageRegexExp) {
		this.nextPageRegexExp = nextPageRegexExp;
	}

	@Override
	public String toString() {
		return "DetailListPageRule [listPageUrl=" + listPageUrl + ", detailPageXpath=" + detailPageXpath
				+ ", detailPageRegexExp=" + detailPageRegexExp + ", nextPageXpath=" + nextPageXpath
				+ ", nextPageRegexExp=" + nextPageRegexExp + "]";
	}

}
