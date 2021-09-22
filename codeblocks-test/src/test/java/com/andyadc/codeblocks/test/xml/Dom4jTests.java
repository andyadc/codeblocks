package com.andyadc.codeblocks.test.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Dom4jTests {

	/**
	 * <pre>
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <document>
	 *   <request id="request">
	 *     <head>
	 *       <Version>1.0.0</Version>
	 *       <Function>ff</Function>
	 *       <ReqTime>Wed Sep 22 18:55:10 CST 2021</ReqTime>
	 *       <ReqTimeZone>UTC+8</ReqTimeZone>
	 *       <ReqMsgId>8f6adca0-f9bd-4735-a61a-07f9ca78dd97</ReqMsgId>
	 *       <InputCharset>UTF-8</InputCharset>
	 *     </head>
	 *     <body>
	 *       <IsvOrgId>202210000000000001508</IsvOrgId>
	 *       <PrincType>MERCHANT</PrincType>
	 *       <Status>1</Status>
	 *       <Ccy>156</Ccy>
	 *       <StmtAcctType>OTHER_BK_BALANCE</StmtAcctType>
	 *     </body>
	 *   </request>
	 * </document>
	 * </pre>
	 */
	@Test
	public void testParseFile() {
		Map<String, String> params = buildParams();

		String content = getContent("head");
		Element headElement = buildElement("head", content, params);

		content = getContent("body");
		Element bodyElement = buildElement("body", content, params);

		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("document");
		Element requestElement = rootElement.addElement("request").addAttribute("id", "request");

		requestElement.add(headElement);
		requestElement.add(bodyElement);

		System.out.println(document.asXML());
	}

	private Map<String, String> buildParams() {
		Map<String, String> params = new HashMap<>();
		params.put("Function", "ff");
		params.put("ReqTime", new Date().toString());
		params.put("ReqMsgId", UUID.randomUUID().toString());
		params.put("Status", "1");
		return params;
	}

	private Element buildElement(String elementName, String content, Map<String, String> params) {
		try {
			Document document = DocumentHelper.parseText(content);
			Element rootElement = document.getRootElement();
			List<Element> elementList = rootElement.elements();

			Element element = new DOMElement(elementName);
			for (Element ele : elementList) {
				String tagName = ele.attributeValue("tagName");
				String value = params.get(tagName);
				if (value == null) {
					String defaultValue = ele.attributeValue("defaultValue");
					if (defaultValue != null) {
						value = defaultValue;
					}
				}

				if (value != null) {
					element.addElement(tagName).setText(value);
				}
			}
			return element;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getContent(String filename) {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("xml/" + filename + ".xml");

		StringBuilder builder = new StringBuilder();
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
			String s;
			while ((s = bufferedReader.readLine()) != null) {
				builder.append(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
