package donnees;

/**
 * Flickr Web Service API Example: Authenticate Desktop Application User
 *
 * @author Daniel Jones www.danieljones.org
 * Copyright 2007
 * 
 * This example shows how to perform the steps necessary to authenticate a 
 * Yahoo! Flickr user. Once the user has been authenticated, you have full
 * access to the Flickr API.
 *  
 * The Flickr Authentication API Desktop Applications How-To can be found 
 * here: http://www.flickr.com/services/api/auth.howto.desktop.html
 *
 * Before you can use the Flickr API, you must obtain an API key at the following 
 * URL: http://www.flickr.com/services/api/keys/apply/
 * 
 * For the purposes of this example, be sure to choose the Desktop Application
 * radio button for the Authentication Type. We do not want the user
 * automatically redirected to a callback URL with the frob passed in the URL
 * as a GET argument because our desktop application's token request will fail.
 * You should choose Web Application for the Authentication Type if you want
 * the frob argument automatically passed back to the auth handler callback URL
 * for a subsequent call to flickr.auth.getToken.
 * 
 * Once you are comfortable with user authentication, continue with the FlickrRotate
 * example to see a live example of the Flickr API.
 * 
 */


import java.math.BigInteger;
import java.security.MessageDigest;
import java.io.*;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.*;

public class FlickrAuth {
	
	private String frob;
	private HttpClient client;
	private String lien;
	
	public FlickrAuth() throws Exception {
		/**
		 * Use the key and secret provided when you registered your application
		 * http://www.flickr.com/services/api/keys/apply/
		 */
		String key = "db22c5e66e7e93f7bb7d25ecbadd0c60";
		String secret = "e40c2be7229cd101";
			
		/**
		 * Request a frob to identify the login session. This call requires 
		 * a signature. The signature starts with your shared secret and
		 * is followed by your API key and the method name. The API key and
		 * method name are prepended by the words "api_key" and "method" as
		 * shown in the following line.
		**/
		String methodGetFrob = "flickr.auth.getFrob";
		String sig = secret + "api_key" + key + "method" + methodGetFrob;
		
		/**
		 * The API signature must be MD5 encoded and appended to the request
		 */
		String signature = MD5(sig); 
			
		String request = "http://api.flickr.com/services/rest/?method=" + methodGetFrob + "&api_key=" + key + "&api_sig=" + signature;
		
		client = new HttpClient();
		GetMethod method = new GetMethod(request);
		
		// Send GET request
		int statusCode = client.executeMethod(method);
		
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + method.getStatusLine());
		}
		InputStream rstream = null;
		
		// Get the response body
		rstream = method.getResponseBodyAsStream();
		
		/**
		 * Retrieve the XML response to the frob request and get the frob value.
		 */
		org.w3c.dom.Document response = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rstream);
		
		frob = null;
		
		// Check if frob is in the response
		NodeList frobResponse = response.getElementsByTagName("frob");
		Node frobNode = frobResponse.item(0);
		if (frobNode != null) {
			frob = frobNode.getTextContent();
		} else {
			// Get Flickr error code and msg
			NodeList error = response.getElementsByTagName("err");
			String code = error.item(0).getAttributes().item(0).getTextContent();
			String msg = error.item(0).getAttributes().item(1).getTextContent();
		}
		
		/**
		 * Create a Flickr login link
		 * http://www.flickr.com/services/auth/?api_key=[api_key]&perms=[perms]&frob=[frob]&api_sig=[api_sig]
		 * We are using "write" for the perms value because we will be rotating an image. 
		 */
		sig = secret + "api_key" + key + "frob" + frob + "permswrite";
		signature = MD5(sig);
		request = "http://www.flickr.com/services/auth/?api_key=" + key + "&perms=write&frob=" + frob + "&api_sig=" + signature;
		this.lien = request;
	}
	
	public String getToken() throws Exception {
		/**
		 * Use the key and secret provided when you registered your application
		 * http://www.flickr.com/services/api/keys/apply/
		 */
		String key = "db22c5e66e7e93f7bb7d25ecbadd0c60";
		String secret = "e40c2be7229cd101";

		  
		/**
		 * Get auth token using frob. Once again, a signature is required
		 * for authenticated calls to the Flickr API.  
		 */
		String methodGetToken = "flickr.auth.getToken";
		String sig = secret + "api_key" + key + "frob" + frob + "method" + methodGetToken;
		String signature = MD5(sig);
		String request = "http://api.flickr.com/services/rest/?method=" + methodGetToken + "&api_key=" + key + "&frob=" + frob + "&api_sig=" + signature;
			
		GetMethod method = new GetMethod(request);
			
		// Send GET request
		int statusCode = client.executeMethod(method);
		
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + method.getStatusLine());
		}
		
		InputStream rstream = null;
		
		// Get the response body
		rstream = method.getResponseBodyAsStream();
		/**
		 * Retrieve the XML response to the token request and get the token value
		 */
		org.w3c.dom.Document response = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rstream);
		
		String token = null;
		
		// Check if token is in the response
		NodeList tokenResponse = response.getElementsByTagName("token");
		Node tokenNode = tokenResponse.item(0);
		if (tokenNode != null) {
			token = tokenNode.getTextContent();
			return token;
		} else {
			NodeList error = response.getElementsByTagName("err");
			// Get Flickr error code and msg
			String code = error.item(0).getAttributes().item(0).getTextContent();
			String msg = error.item(0).getAttributes().item(1).getTextContent();
			return "Flickr request failed with error code " + code + ", " + msg;
		}
	}
	
	public String getLien() {
		return lien;
	}

	/**
	 * Get the MD5 hash of a text string
	 */
	public static String MD5(String text)
	{
		String md5Text = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			md5Text = new BigInteger(1, digest.digest((text).getBytes())).toString(16);
		} catch (Exception e) {
			System.out.println("Error in call to MD5");
		}
		
        if (md5Text.length() == 31) {
            md5Text = "0" + md5Text;
        }
		return md5Text;
	}
}