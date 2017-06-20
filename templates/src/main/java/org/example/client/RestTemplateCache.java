package org.example.client;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.marklogic.spring.http.RestConfig;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.example.util.CredentialsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *  Uses the Guava library to implement a cache of RestTemplate
 */
@Component
public class RestTemplateCache {
	private LoadingCache<RestTemplateCacheKey, RestTemplate> cache;

	@Autowired
	private RestConfig restConfig;

	public RestTemplateCache(long duration, TimeUnit timeUnit){
		CacheLoader<RestTemplateCacheKey, RestTemplate> loader;
		loader = new CacheLoader<RestTemplateCacheKey, RestTemplate>() {
			@Override
			public RestTemplate load(RestTemplateCacheKey key) {
				CredentialsProvider provider = new BasicCredentialsProvider();
				provider.setCredentials(AuthScope.ANY, CredentialsUtil.cacheKeyToCredentials(key));
				CloseableHttpClient client =
						HttpClientBuilder.create().
							setDefaultCredentialsProvider(provider).useSystemProperties().build();
				HttpHost host = new HttpHost(restConfig.getHost(), restConfig.getRestPort(), restConfig.getScheme());
				// Create AuthCache instance
				AuthCache authCache = new BasicAuthCache();
				// Generate DIGEST scheme object, initialize it and add it to the local digest cache
				DigestScheme digestAuth = new DigestScheme();
				// If we already know the realm name
				digestAuth.overrideParamter("realm", "public");
				authCache.put(host, digestAuth);
				DigestRequestFactory requestFactory = new DigestRequestFactory(client, authCache);
				return new RestTemplate(requestFactory);
			}
		};

		cache = CacheBuilder.newBuilder()
			.expireAfterWrite(duration, timeUnit)
			.build(loader);
	}

	public RestTemplate fetchOrCreate(RestTemplateCacheKey key) throws ExecutionException {
		return cache.getUnchecked(key);
	}

	public void remove(RestTemplateCacheKey key){
		cache.invalidate(key);
	}

}
