package org.example.client;

import com.marklogic.spring.http.proxy.DefaultRequestCallback;
import com.marklogic.spring.http.proxy.DefaultResponseExtractor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.example.util.CredentialsUtil;
import org.example.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.concurrent.ExecutionException;


/**
 * Simple class to proxy requests to MarkLogic using Digest authentication.  Will reuse RestTemplate created in
 * in DigestAuthenticationManager {@link DigestAuthenticationManager}.
 *
 * Proxy methods derived from HttpProxy {@link com.marklogic.spring.http.proxy.HttpProxy} class.
 */
@Component
public class DigestRestClient {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static final String RESTTEMPLATE_SESSION_KEY = "upstream.restemplate";

	@Autowired
	RestTemplateCache restTemplateCache;

	@Autowired
	private URIUtil uriUtil;

	public DigestRestClient() {
	}

	private CredentialsProvider provider(String userName, String password) {
		CredentialsProvider provider = new BasicCredentialsProvider();
		provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
		return provider;
	}

	/**
	 * Proxy a request without copying any headers.
	 *
	 * @param httpRequest
	 * @param httpResponse
	 */
	public void proxy(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		proxy(httpRequest.getServletPath(), httpRequest, httpResponse);
	}

	/**
	 * Proxy a request and copy the given headers on both the request and the response.
	 *
	 * @param httpRequest
	 * @param httpResponse
	 * @param headerNamesToCopy
	 */
	public void proxy(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String... headerNamesToCopy) {
		proxy(httpRequest.getServletPath(), httpRequest, httpResponse,
			new DefaultRequestCallback(httpRequest, headerNamesToCopy),
			new DefaultResponseExtractor(httpResponse, headerNamesToCopy));
	}

	/**
	 * Proxy a request, using the given path instead of the servlet path in the HttpServletRequest.
	 *
	 * @param path
	 * @param httpRequest
	 * @param httpResponse
	 * @param headerNamesToCopy
	 */
	public void proxy(String path, HttpServletRequest httpRequest, HttpServletResponse httpResponse,
					  String... headerNamesToCopy) {
		proxy(path, httpRequest, httpResponse, new DefaultRequestCallback(httpRequest, headerNamesToCopy),
			new DefaultResponseExtractor(httpResponse, headerNamesToCopy));
	}

	/**
	 * Specify your own request callback and response extractor. This gives you the most flexibility, but does the least
	 * for you.
	 *
	 * @param path
	 * @param httpRequest
	 * @param httpResponse
	 * @param requestCallback
	 * @param responseExtractor
	 * @return
	 */
	public <T> T proxy(String path, HttpServletRequest httpRequest, HttpServletResponse httpResponse,
					   RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) {
		URI uri = uriUtil.buildUri(path, httpRequest.getQueryString());
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Proxying to URI: %s", uri));
		}
		HttpMethod method = determineMethod(httpRequest);
		RestTemplate client = getUpstreamRestTemplate(httpRequest);
		return client.execute(uri, method, requestCallback, responseExtractor);
	}

	protected HttpMethod determineMethod(HttpServletRequest httpRequest) {
		return HttpMethod.valueOf(httpRequest.getMethod());
	}

	protected RestTemplate getUpstreamRestTemplate(HttpServletRequest httpRequest){
		// check first if user already have a template stored in session
		RestTemplate template = (RestTemplate)httpRequest.getSession().getAttribute(RESTTEMPLATE_SESSION_KEY);
		if (template==null){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth!=null){
				try {
					RestTemplateCacheKey cacheKey = CredentialsUtil.authTokenToCacheKey(auth);
					// fetch the RestTemplate stored by DigestAuthenticationManager in the cache
					// create if the template doesn't yet exist
					template = restTemplateCache.fetchOrCreate(cacheKey);
					// move the template from the cache to the session (removing the password in memory)
					httpRequest.getSession().setAttribute(RESTTEMPLATE_SESSION_KEY, template);
					restTemplateCache.remove(cacheKey);
				} catch (ExecutionException e) {
					throw new RuntimeException("Unable to create RestTemplate object.", e);
				}
			}else{
				return null;
			}
		}
		return template;
	}

}
