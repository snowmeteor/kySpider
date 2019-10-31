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
	public static String regex(String text, String regexExp) {
		List<String> valueList = regexList(text, regexExp);
		return CollectionUtils.isEmpty(valueList) ? "" : valueList.get(0);
	}

	/**
	 * 通过正则表达式提取所有匹配的字符串
	 *
	 * @param text
	 * @param regexExp
	 * @return 匹配的字符串列表
	 */
	public static List<String> regexList(String text, String regexExp) {
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
			webClient.getOptions().setCssEnabled(false);
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
		System.out.println(url + " 静态获取网页时间：" + (t2 - t1) / 1000.0 + "s");
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
		System.out.println(url + " 动态获取网页时间：" + (t2 - t1) / 1000.0 + "s");
		return html;
	}

}