package org.example.util;

import com.marklogic.spring.http.RestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

/**
 * Utility class for URI related actions.
 */
@Component
public class URIUtil {

	private static final boolean decodeQuerystring = true;
	private static final String encoding = "UTF-8";

	@Autowired
	private RestConfig restConfig;

	public URI buildUri(String path, String queryString) {
		try {
			/**
			 * Gotta decode this, the URI constructor will then encode it. The QS will often have encoded text on it
			 * already, such as for a structured query for a /v1/search request. The text is then decoded here, and then
			 * encoded by the URI class, which ensures that we don't double-encode the QS.
			 */
			if (isDecodeQuerystring()) {
				queryString = decode(queryString);
			}
			return new URI(restConfig.getScheme(), null, restConfig.getHost(), restConfig.getRestPort(), path,
				queryString, null);
		} catch (Exception ex) {
			throw new RuntimeException("Unable to build URI, cause: " + ex.getMessage(), ex);
		}
	}

	protected String decode(String queryString) {
		try {
			return queryString != null ? URLDecoder.decode(queryString, "UTF-8") : null;
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException("Unable to decode queryString, cause: " + ex.getMessage(), ex);
		}
	}

	public static boolean isDecodeQuerystring() {
		return decodeQuerystring;
	}

	public static String getEncoding() {
		return encoding;
	}

}
