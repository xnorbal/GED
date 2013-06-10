package donnees;

import java.awt.List;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.contacts.ContactsInterface;
import com.aetrion.flickr.people.PeopleInterface;
import com.aetrion.flickr.photosets.PhotosetsInterface;
import com.aetrion.flickr.uploader.UploadMetaData;
import com.aetrion.flickr.uploader.Uploader;

public class FlickrUpload {

		public static void uploadPhoto(String myToken, Document d) throws IOException, NoSuchAlgorithmException{

		String apiKey = "db22c5e66e7e93f7bb7d25ecbadd0c60";
		String token = myToken;
		String secretKey = "e40c2be7229cd101";
		String title=d.getTitle();
		String photo = d.getPath();
		String description=d.getDescription();

		Flickr f;
		ContactsInterface c;
		PeopleInterface p;
		PhotosetsInterface o;
		Uploader up = new Uploader(apiKey,secretKey);
		REST rest;

		RequestContext requestContext;

		AuthInterface authInterface;
		String frob = "";
		//void setup() 

		   InputStream in = new FileInputStream(photo);
	       ByteArrayOutputStream out = new ByteArrayOutputStream();
	       int i;
	       byte[] buffer = new byte[1024];
	       while ((i = in.read(buffer)) != -1) {
	           out.write(buffer, 0, i);
	       }
	       in.close();
	    //   byte[] result = out.toByteArray();

		byte data[] = out.toByteArray();
		//size(500, 500);
		f= new Flickr(apiKey,secretKey,(new Flickr(apiKey)).getTransport());
		up=f.getUploader();
		authInterface=f.getAuthInterface();
		requestContext = RequestContext.getRequestContext();
		requestContext.setSharedSecret(secretKey);

		try {
		frob = authInterface.getFrob();
		URL joep = authInterface.buildAuthenticationUrl(Permission.WRITE, frob);

		} catch (IOException e) {
		e.printStackTrace();
		} catch (SAXException e) {
		e.printStackTrace();
		} catch (FlickrException e) {
		e.printStackTrace();
		}

		try {
		Auth auth = new Auth();
		requestContext.setAuth(auth);
		auth.setToken(token);
		auth.setPermission(Permission.WRITE);
		f.setAuth(auth);
		UploadMetaData uploadMetaData = new UploadMetaData(); 
		uploadMetaData.setTitle(title);
		uploadMetaData.setDescription(description);
		String listTags = d.getTags();
		ArrayList<String> mesTags = new ArrayList<String>();
		if (!listTags.equals("")) {
			String[] tags = listTags.split("\n");
			for (int j = 0; j < tags.length; j++) {
				mesTags.add(tags[j]);
			}
		}
		uploadMetaData.setTags(mesTags);
		up.upload(data,uploadMetaData);

		} catch (IOException e) {
		e.printStackTrace();
		} catch (SAXException e) {
		e.printStackTrace();
		} catch (FlickrException e) {
		e.printStackTrace();
		}

		}

}
