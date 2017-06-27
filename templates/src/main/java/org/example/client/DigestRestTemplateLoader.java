package org.example.client;

import com.google.common.cache.CacheLoader;
import com.marklogic.spring.http.RestConfig;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 *  Loads RestTemplate {@link org.springframework.web.client.RestTemplate} objects wired for Digest authentication.
 */
@Component
public class DigestRestTemplateLoader extends CacheLoader<DigestRestClientSession, RestTemplate> {

	@Autowired
	private RestConfig restConfig;

	@Override
	public RestTemplate load(DigestRestClientSession clientSession) throws Exception {
		final CredentialsProvider provider = new BasicCredentialsProvider();
		provider.setCredentials(AuthScope.ANY, clientSession);
		final CloseableHttpClient httpClient =
				HttpClientBuilder.create().
						setDefaultCredentialsProvider(provider).useSystemProperties().build();
		final HttpHost host = new HttpHost(restConfig.getHost(), restConfig.getRestPort(), restConfig.getScheme());
		// Create AuthCache instance
		final AuthCache authCache = new BasicAuthCache();
		// Generate DIGEST scheme object, initialize it and add it to the local digest cache
		final DigestScheme digestAuth = new DigestScheme();
		digestAuth.overrideParamter("realm", "public");
		authCache.put(host, digestAuth);

		// create a RestTemplate wired with a custom request factory using the above AuthCache with Digest Scheme
		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient){
			@Override
			protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
				// Add AuthCache to the execution context
				BasicHttpContext localcontext = new BasicHttpContext();
				localcontext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
				return localcontext;
			}
		});

		//link template to the session
		clientSession.setRestTemplate(restTemplate);

		return restTemplate;
	}
}
