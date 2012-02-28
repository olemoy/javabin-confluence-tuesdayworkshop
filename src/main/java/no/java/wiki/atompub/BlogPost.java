package no.java.wiki.atompub;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Category;
import org.apache.abdera.model.Entry;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogPost {

	private Properties properties = new Properties();
	private Abdera abdera = new Abdera();
	static final Logger LOG = LoggerFactory.getLogger(BlogPost.class);
	static final String CONFLUENCE_CATEGORY_SCHEME = "urn:confluence:category";
	static final String CONFLUENCE_LABEL_SCHEME = "urn:confluence:label";
	static final String NEWS_TERM = "news";
	static final String LABEL_TERM = "tirsdagsmøte";

	public Entry createEntry(LocalDate localDate) {

		loadPostProperties();

		Factory factory = abdera.getFactory();

		Entry entry = factory.newEntry();

		entry.setTitle(formatTitle(localDate));

		entry.addAuthor(properties.getProperty("entry.author"));

		// Category News
		Category category = factory.newCategory();
		category.setScheme(CONFLUENCE_CATEGORY_SCHEME);
		category.setTerm(NEWS_TERM);
		entry.addCategory(category);

		// Category-Label tirsdagsmøte
		Category label = factory.newCategory();
		label.setScheme(CONFLUENCE_LABEL_SCHEME);
		label.setTerm(LABEL_TERM);
		entry.addCategory(label);

		entry.setContent(properties.getProperty("entry.content"));

		return entry;
	}

	public void publishBlogPost(Entry entry) {
		AbderaClient ac = new AbderaClient(abdera);
		Credentials creds = new UsernamePasswordCredentials(
				properties.getProperty("atompub.user"),
				properties.getProperty("atompub.pass"));

		try {
			ac.addCredentials("wiki.java.no", AuthScope.ANY_REALM,
					AuthPolicy.BASIC, creds);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ClientResponse cr = ac.post(properties.getProperty("atompub.url")
				+ "?os_authType=basic", entry);
		LOG.info("Statuscode: " + cr.getStatus(),cr);
	}

	private String formatTitle(LocalDate localDate) {

		String month = Integer.toString(localDate.getMonthOfYear());
		if (localDate.getMonthOfYear() < 10) {
			month = "0" + localDate.getMonthOfYear();
		}

		String day = Integer.toString(localDate.getDayOfMonth());
		if (localDate.getDayOfMonth() < 10) {
			day = "0" + localDate.getDayOfMonth();
		}

		return String.format(properties.getProperty("entry.title"),
				localDate.getYear(), month, day);
	}

	private void loadPostProperties() {
		// Read properties file.
		URL in = getClass().getClassLoader().getResource("post.properties");
		try {
			properties.load(new InputStreamReader(in.openStream(), "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
