package com.atreceno.it.tools.htmlparser;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.http.client.ClientProtocolException;

/**
 * Unit test for UrlSniffer.
 */
public class UrlSnifferTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public UrlSnifferTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(UrlSnifferTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	/**
	 * It should create the filename specified in properties file:
	 * sniffer.links.transform.filename
	 */
	public void testLinks() {
		UrlSniffer s = new UrlSniffer();
		Properties p = s.getProperties();
		try {
			s.links(2);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File f = new File(p.getProperty("sniffer.links.transform.filename"));
		assertTrue(f.exists());
	}

	/**
	 * It should create the filename specified in properties file:
	 * sniffer.targets.truncate.filename
	 */
	public void testParser() {
		UrlSniffer s = new UrlSniffer();
		Properties p = s.getProperties();
		try {
			s.targetsParser();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File f = new File(p.getProperty("sniffer.targets.truncate.filename"));
		assertTrue(f.exists());
	}

	/**
	 * It should create the filename specified in properties file:
	 * sniffer.targets.truncate.filename
	 */
	public void testTransformer() {
		UrlSniffer s = new UrlSniffer();
		Properties p = s.getProperties();
		try {
			s.targetsTransformer();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		File f = new File(p.getProperty("sniffer.targets.transform.filename"));
		assertTrue(f.exists());
	}

}
