package org.example.client;


import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;
/**
 * A HTTP Request Factory that wires the existing Authentication Cache assumed that was assigned for the user.
 *
 */
public class DigestRequestFactory extends HttpComponentsClientHttpRequestFactory {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private AuthCache authCache;

	public DigestRequestFactory(HttpClient httpClient, AuthCache authCache) {
		super(httpClient);
		this.authCache = authCache;
	}

	@Override
	protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
		return createHttpContext();
	}

	private HttpContext createHttpContext() {
		// Add AuthCache to the execution context
		BasicHttpContext localcontext = new BasicHttpContext();
		localcontext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
		return localcontext;
	}
}
