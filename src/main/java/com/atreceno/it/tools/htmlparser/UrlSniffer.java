package com.atreceno.it.tools.htmlparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AthleteParser
 * 
 */
public class UrlSniffer {

	final Logger logger = LoggerFactory.getLogger(UrlSniffer.class);

	protected Properties getProperties() {
		Properties p = new Properties();
		InputStream is = ClassLoader
				.getSystemResourceAsStream("app.properties");
		if (is == null) {
			logger.warn("Properties file not found");
		}
		try {
			p.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	protected void links(int pageLimit)
			throws TransformerFactoryConfigurationError, TransformerException,
			ClientProtocolException, IOException {

		logger.info("Getting links...");
		Properties p = getProperties();

		String prefix = p.getProperty("sniffer.links.url.prefix");
		String suffix = p.getProperty("sniffer.links.url.suffix");
		String from = p.getProperty("sniffer.links.truncate.from");
		String to = p.getProperty("sniffer.links.truncate.to");
		String regex = p.getProperty("sniffer.links.truncate.regex");
		String replace = p.getProperty("sniffer.links.truncate.replace");
		String xslt = p.getProperty("sniffer.links.transform.xslt");
		String outputfile = p.getProperty("sniffer.links.transform.filename");

		String html = linksParser(prefix, suffix, from, to, regex, replace,
				pageLimit);
		linksTransformer(html, xslt, outputfile);
	}

	private String linksParser(String prefix, String suffix, String from,
			String to, String regex, String replace, int pageLimit)
			throws ClientProtocolException, IOException {

		logger.info("Setting page limit to " + pageLimit);
		HttpClient httpClient = new DefaultHttpClient();
		int m = 0, n = 0, page = 1;
		StringBuffer html = new StringBuffer().append("<div id=\"sniffer\">");
		while (m != -1 || n != -1) {

			if (page > pageLimit)
				break;

			String url = prefix + page + suffix;
			logger.info("Scanning: " + url);
			HttpGet httpget = new HttpGet(url);

			String body = httpClient.execute(httpget,
					new BasicResponseHandler());

			m = body.indexOf(from);
			n = body.indexOf(to, m);
			if (m == -1 || n == -1) {
				logger.warn("String " + from + " or " + to + " not found in: "
						+ url);
				break;
			}

			// Truncate and replace
			body = body.substring(m + from.length(), n);
			if (!regex.isEmpty()) {
				body = body.replaceAll(regex, replace);
			}

			logger.debug(body);
			html.append(body);
			page++;

		}
		return html.append("</div>").toString();

	}

	private void linksTransformer(String html, String xslt, String outputfile)
			throws TransformerFactoryConfigurationError, TransformerException {

		logger.debug(html);
		StreamResult sr = new StreamResult(new File(outputfile));
		Transformer t = TransformerFactory.newInstance(
				"net.sf.saxon.TransformerFactoryImpl", null).newTransformer(
				new StreamSource(ClassLoader.getSystemResourceAsStream(xslt)));
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		t.transform(
				new StreamSource(new ByteArrayInputStream(html.getBytes())), sr);

	}

	protected void targetsParser() throws IOException {

		logger.info("Parsing targets...");
		Properties p = getProperties();
		String parserin = p.getProperty("sniffer.links.transform.filename");
		String parserout = p.getProperty("sniffer.targets.truncate.filename");
		String from = p.getProperty("sniffer.targets.truncate.from");
		String to = p.getProperty("sniffer.targets.truncate.to");
		String regex = p.getProperty("sniffer.targets.truncate.regex");
		String replace = p.getProperty("sniffer.targets.truncate.replace");

		BufferedReader br = new BufferedReader(new FileReader(parserin));
		BufferedWriter bw = new BufferedWriter(new FileWriter(parserout));
		String line = null;
		HttpClient hc = new DefaultHttpClient();
		bw.write("<div id=\"sniffer\">\n");
		while ((line = br.readLine()) != null) {
			logger.info("Scanning line: " + line);
			String[] a = line.split("\t");
			HttpGet hg = new HttpGet(a[2]);
			String body = hc.execute(hg, new BasicResponseHandler());
			logger.debug(body);
			int m = body.indexOf(from);
			int n = body.indexOf(to, m);
			if (m == -1 || n == -1) {
				logger.warn("String " + from + " or " + to + " not found in: "
						+ a[2]);
				break;
			}

			// Truncate and replace
			body = body.substring(m + from.length(), n);
			if (!regex.isEmpty()) {
				body = body.replaceAll(regex, replace);
			}

			bw.append("<div>").append(body).append("</div>\n");
		}
		bw.append("</div>");
		br.close();
		bw.close();

	}

	protected void targetsTransformer()
			throws TransformerFactoryConfigurationError, TransformerException {

		logger.info("Transforming targets...");
		Properties p = getProperties();
		String parserout = p.getProperty("sniffer.targets.truncate.filename");
		String xslt = p.getProperty("sniffer.targets.transform.xslt");
		String outputfile = p.getProperty("sniffer.targets.transform.filename");

		StreamSource ss = new StreamSource(new File(parserout));
		StreamResult sr = new StreamResult(new File(outputfile));
		Transformer t = TransformerFactory.newInstance(
				"net.sf.saxon.TransformerFactoryImpl", null).newTransformer(
				new StreamSource(ClassLoader.getSystemResourceAsStream(xslt)));
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		t.transform(ss, sr);

	}

	private void printUsageAndExit() {
		System.err.println("Usage: java " + UrlSniffer.class.getSimpleName()
				+ " [links | parse | transform]");
		System.exit(-1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		UrlSniffer sniffer = new UrlSniffer();

		if (args.length < 1 || args.length > 2) {
			sniffer.printUsageAndExit();
		}

		if (args[0].equals("links")) {
			try {
				int pageLimit = 0;
				if (args.length == 2) {
					pageLimit = new Integer(args[1]);
				} else {
					pageLimit = Integer.MAX_VALUE;
				}
				sniffer.links(pageLimit);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("parse")) {
			try {
				sniffer.targetsParser();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("transform")) {
			try {
				sniffer.targetsTransformer();
			} catch (TransformerFactoryConfigurationError e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
		} else {
			sniffer.printUsageAndExit();
		}

	}
}
