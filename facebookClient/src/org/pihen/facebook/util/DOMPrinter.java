package org.pihen.facebook.util;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class DOMPrinter {

	public static String convertToString(Document document) {
		try { 
		DOMSource domSource = new DOMSource(document);
		StringWriter sw = new StringWriter();
		Result result = new StreamResult(sw);
		// create an instance of TransformerFactory
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer trans;
		
			trans = transFact.newTransformer();
		
		trans.setOutputProperty(OutputKeys.INDENT, "yes");
		trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		trans.transform(domSource, result);
		return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
}
}
