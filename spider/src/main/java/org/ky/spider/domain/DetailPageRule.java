package org.ky.spider.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * 详情页爬取规则
 * 
 * @author snowmeteor
 *
 */
public class DetailPageRule {

	/**
	 * 常量表达式前缀，当xpath字符串以常量表达式开头时，爬取结果为去除常量表达式剩余的部分
	 */
	public static final String CONSTANT_EXP_PREFIX = "###";

	private String id;
	private String xpath;//必须有
	private String regexExp;//可以没有
	
	public boolean isConstantExp() {
		if (StringUtils.isNoneBlank(xpath) && xpath.startsWith(CONSTANT_EXP_PREFIX)) {
			return true;
		}
		return false;
	}

	public String getConstantValue() {
		if (StringUtils.isBlank(xpath)) {
			return null;
		}
		return StringUtils.removeStart(xpath, CONSTANT_EXP_PREFIX);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getRegexExp() {
		return regexExp;
	}

	public void setRegexExp(String regexExp) {
		this.regexExp = regexExp;
	}

	@Override
	public String toString() {
		return "DetailPageRule [id=" + id + ", xpath=" + xpath + ", regexExp=" + regexExp + "]";
	}

}
