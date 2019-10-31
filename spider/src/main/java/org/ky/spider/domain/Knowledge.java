package org.ky.spider.domain;

import java.util.ArrayList;
import java.util.List;

public class Knowledge {

	private String title;
	private String url;

	private List<KnowledgeAttribute> attributes;

	public Knowledge() {
		attributes = new ArrayList<>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<KnowledgeAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<KnowledgeAttribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "Knowledge [title=" + title + ", url=" + url + ", attributes=" + attributes + "]";
	}

}
