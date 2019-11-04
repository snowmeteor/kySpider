package org.ky.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener;

/**
 * 爬虫助手工具类
 * 
 * @author snowmeteor
 *
 */
public class CrawlHelper {

	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private static final String FTP = "ftp://";

	/**
	 * 根据参照URL将目标URL补充成完整的以http或https开头的URL路径
	 * 
	 * @param referUrl  用于参考包含域名的URL，如：http://iranshao.com/bundled_races?page=1
	 * @param targetUrl 待转换的路径，如：/bundled_races?date=2018&amp;page=13
	 * @return 补充完的完整路径，如：http://iranshao.com/bundled_races?date=2018&amp;page=13
	 */
	public static String buildAbsoluteUrl(String referUrl, String targetUrl) {
		if (StringUtils.startsWith(targetUrl, "//")) {
			if (StringUtils.startsWith(referUrl, HTTP)) {
				return "http:" + targetUrl;
			}
			if (StringUtils.startsWith(referUrl, HTTPS)) {
				return "https:" + targetUrl;
			}
		}
		if (StringUtils.startsWith(targetUrl, HTTP) || StringUtils.startsWith(targetUrl, HTTPS)
				|| StringUtils.startsWith(targetUrl, FTP)) {
			return targetUrl;
		}
		if (!StringUtils.startsWith(referUrl, HTTP) && !StringUtils.startsWith(referUrl, HTTPS)) {
			return targetUrl;
		}
		if (StringUtils.isEmpty(targetUrl)) {
			return referUrl;
		}

		targetUrl = removeEscapeString(targetUrl);
		String domainRegexp = "http[s]?://[a-zA-Z\\.0-9]+/";
		String domainName = fetchTextByRegex(referUrl, domainRegexp);
		if (StringUtils.isBlank(domainName)) {
			domainRegexp = referUrl;
		}

		String relativeRegexp = "[^\\?]*/";
		String relativePath = fetchTextByRegex(referUrl, relativeRegexp);

		String slash = "/";
		if (relativePath.endsWith(".html")) {
			relativePath = relativePath.substring(0, relativePath.lastIndexOf(slash));
		}
		if (StringUtils.startsWith(targetUrl, slash)) {
			return domainName + targetUrl.substring(1);
		}
		return StringUtils.endsWith(relativePath, slash) ? relativePath + targetUrl : relativePath + slash + targetUrl;
	}

	/**
	 * 去除HTML转义符
	 * 
	 * @param text
	 * @return
	 */
	public static String removeEscapeString(String text) {
		return text.replace("&amp;", "&");
	}

	/**
	 * 根据参照URL，从网站相对路径构建网站绝对路径
	 * 
	 * @param referUrl      包含完整网站访问路径的参照URL
	 * @param targetUrlList 相对路径列表
	 * @return
	 */
	public static List<String> buildAbsoluteUrlList(String referUrl, List<String> targetUrlList) {
		if (CollectionUtils.isEmpty(targetUrlList)) {
			return targetUrlList;
		}
		List<String> urlList = new ArrayList<>();
		for (String targetUrl : targetUrlList) {
			urlList.add(buildAbsoluteUrl(referUrl, targetUrl));
		}
		return urlList;
	}

	/**
	 * 根据xpath从html中获取指定的文本串
	 * 
	 * @param html
	 * @param xpath
	 * @return
	 */
	public static List<String> fetchTextList(String html, String xpath) {
		List<String> valueList = new ArrayList<>();
		if (StringUtils.isBlank(xpath) || StringUtils.isBlank(html)) {
			return valueList;
		}

		try {
			HtmlCleaner hCleaner = new HtmlCleaner();
			TagNode tNode = hCleaner.clean(html);
			Document dom = new DomSerializer(new CleanerProperties()).createDOM(tNode);

			XPath xPath = XPathFactory.newInstance().newXPath();
			Object result = xPath.evaluate(xpath, dom, XPathConstants.NODESET);
			if (result instanceof NodeList) {
				NodeList nodeList = (NodeList) result;
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					String value = node.getNodeValue();
					value = value == null ? node.getTextContent() : value;
					if ("#".equals(value)) {
						continue;
					}
					valueList.add(value);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return valueList;
	}

	/**
	 * 采用HTML cleaner提取单个文本
	 * 
	 * @param html
	 * @param xpath
	 * @return
	 */
	public static String fetchText(String html, String xpath) {
		List<String> valueList = fetchTextList(html, xpath);
		return CollectionUtils.isEmpty(valueList) ? "" : valueList.get(0);
	}

	/**
	 * 通过正则表达式提取第1个匹配的字符串
	 *
	 * @param text
	 * @param regexExp
	 * @return
	 */
	public static String fetchTextByRegex(String text, String regexExp) {
		List<String> valueList = fetchListByRegex(text, regexExp);
		return CollectionUtils.isEmpty(valueList) ? "" : valueList.get(0);
	}

	/**
	 * 通过正则表达式提取所有匹配的字符串
	 *
	 * @param text
	 * @param regexExp
	 * @return 匹配的字符串列表
	 */
	public static List<String> fetchListByRegex(String text, String regexExp) {
		List<String> valueList = new ArrayList<>();
		if (StringUtils.isBlank(regexExp)) {
			valueList.add(text);
			return valueList;
		}
		try {
			Pattern regex = Pattern.compile(regexExp, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Matcher matcher = regex.matcher(text);
			while (matcher.find()) {
				valueList.add(matcher.group(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valueList;
	}

	/**
	 * 获取JS动态渲染的网页详情html（目前不支持滚动加载页面，仅能获取第一页）
	 * 
	 * @param url
	 * @return
	 */
	public static String getDynamicPageHtml(String url) {
		double t1 = System.currentTimeMillis();

		String html = "";
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");

		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setCssEnabled(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setTimeout(40 * 1000);
			webClient.setCssErrorHandler(new SilentCssErrorHandler());
			webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());

			HtmlPage page = webClient.getPage(url);

			webClient.waitForBackgroundJavaScript(20 * 1000);

			html = page.asXml();
		} catch (Exception e) {
			e.printStackTrace();
		}

		double t2 = System.currentTimeMillis();
		System.out.println(url + " 动态获取网页时间：" + (t2 - t1) / 1000.0 + "s");
		return html;
	}

	/**
	 * 获取静态网页详情html
	 * 
	 * @param url
	 * @return
	 */
	public static String getStaticPageHtml(String url) {
		double t1 = System.currentTimeMillis();
		String html = "";
		try {
			html = Jsoup.connect(url).timeout(10 * 1000).get().html();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double t2 = System.currentTimeMillis();
		System.out.println(url + " 静态获取网页时间：" + (t2 - t1) / 1000.0 + "s");
		return html;
	}

	/**
	 * 获取网页标题
	 * 
	 * @param html
	 * @return
	 */
	public static String getHtmlTitle(String html) {
		return Jsoup.parse(html).select("title").text();
	}
}