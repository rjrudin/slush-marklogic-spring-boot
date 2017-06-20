package org.example.client;

import com.marklogic.spring.http.proxy.DefaultRequestCallback;
import com.marklogic.spring.http.proxy.DefaultResponseExtractor;
import org.apache.directory.api.util.Strings;
import org.example.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.CredentialsExpiredException;
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
 * Client class to proxy requests to MarkLogic using Digest authentication.
 *
 * Will reuse RestTemplate created by DigestAuthenticationManager {@link DigestAuthenticationManager} for the user.
 *
 * Proxy methods were derived from HttpProxy {@link com.marklogic.spring.http.proxy.HttpProxy} class.
 */
@Component
public class DigestRestClient {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static final String RESTTEMPLATE_SESSION_KEY = "upstream.restemplate";

	@Autowired
	private RestTemplateCache<RestTemplateCacheKey> restTemplateCache;

	@Autowired
	private URIUtil uriUtil;

	public DigestRestClient() {
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

	private HttpMethod determineMethod(HttpServletRequest httpRequest) {
		return HttpMethod.valueOf(httpRequest.getMethod());
	}

	private RestTemplate getUpstreamRestTemplate(HttpServletRequest httpRequest){
		// check first if user already have a template stored in session
		RestTemplate template = (RestTemplate)httpRequest.getSession().getAttribute(RESTTEMPLATE_SESSION_KEY);
		if (template==null){
			//get the auth token from context and use it to check the cache
			RestTemplateCacheKey cacheKey = (RestTemplateCacheKey)SecurityContextHolder.getContext().getAuthentication();
			if (cacheKey!=null){
				try {
					if (!Strings.isEmpty(cacheKey.getPassword())){
						// fetch the RestTemplate stored by DigestAuthenticationManager in the cache
						// create if the template doesn't yet exist
						template = restTemplateCache.fetchOrCreate(cacheKey);
						// move the template from the cache to the session (removing the password in memory)
						httpRequest.getSession().setAttribute(RESTTEMPLATE_SESSION_KEY, template);
						restTemplateCache.remove(cacheKey);
					}else{
						//for further safety
						restTemplateCache.remove(cacheKey.getUserName());
					}
				} catch (ExecutionException e) {
					throw new RuntimeException("Unable to create RestTemplate object.", e);
				}
			}else{
				throw new CredentialsExpiredException("Missing security context.");
			}
		}
		return template;
	}

}
