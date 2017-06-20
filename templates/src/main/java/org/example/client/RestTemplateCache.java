package org.example.client;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *  A cache for RestTemplate objects with time-based expiration.
 *  Intended to be used by an AuthenticationManager (i.e DigestAuthenticationManager) to temporarily store RestTemplate
 *    objects wired by the given CacheLoader(i.e DigestRestTemplateLoader) with specific AuthCache and appropriate
 *    Scheme(i.e DigestScheme).
 *  This cache is needed since the reuse is required of the RestTemplate created during authentication on succeeding
 *    requests to MarkLogic.  Necessary since there's no access to the user session inside the
 *    DigestAuthenticationManager.authenticate() method.
 *  Values are expected to be moved to the user's http session on the next request right after login.
 *    see: DigestRestClient.getUpstreamRestTemplate()
 */
public class RestTemplateCache<K> {
	private LoadingCache<K, RestTemplate> cache;

	public RestTemplateCache(long duration, TimeUnit timeUnit, CacheLoader<K, RestTemplate> cacheLoader){
		cache = CacheBuilder.newBuilder()
				.expireAfterWrite(duration, timeUnit)
				.build(cacheLoader);
	}

	public RestTemplate fetchOrCreate(K key) throws ExecutionException {
		return cache.getUnchecked(key);
	}

	public void remove(K key){
		cache.invalidate(key);
	}

	public void remove(String username){
		for (Map.Entry<K, RestTemplate> item: cache.asMap().entrySet()){
			if (  item.getKey() instanceof RestTemplateCacheKey
					&& ((RestTemplateCacheKey)item.getKey()).getUserName().equals(username)){
				cache.invalidate(item.getKey());
			}
		}
	}

}
