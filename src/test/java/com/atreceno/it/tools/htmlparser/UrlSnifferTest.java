package com.atreceno.it.tools.htmlparser;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.http.client.ClientProtocolException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test for UrlSniffer.
 */
@RunWith(JUnit4.class)
public class UrlSnifferTest {

	/**
	 * Rigorous Test :-)
	 */
	@Test
	public void testApp() {
		assertTrue(true);
	}

	/**
	 * It should create the filename specified in properties file:
	 * sniffer.links.transform.filename
	 */
	@Test
	@Ignore
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
	@Test
	@Ignore
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
	@Test
	@Ignore
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
