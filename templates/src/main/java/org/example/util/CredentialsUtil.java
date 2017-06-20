package org.example.util;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.example.client.RestTemplateCacheKey;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 *  Utility classes used for object conversion related to credentials handling.
 */
public class CredentialsUtil {
	public static UsernamePasswordCredentials authTokenToCredentials(Authentication authentication){
		if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
			throw new IllegalArgumentException(
				CredentialsUtil.class.getName() + " only supports " + UsernamePasswordAuthenticationToken.class.getName()
					+".  Authentication object is of type "+ authentication.getClass().getName()+".");
		}

		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

		String username = token.getPrincipal().toString();
		String password = token.getCredentials().toString();

		return new UsernamePasswordCredentials(username, password);
	}

	public static RestTemplateCacheKey authTokenToCacheKey(Authentication authentication){
		if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
			throw new IllegalArgumentException(
				CredentialsUtil.class.getName() + " only supports " + UsernamePasswordAuthenticationToken.class.getName()
					+".  Authentication object is of type "+ authentication.getClass().getName()+".");
		}

		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

		String username = token.getPrincipal().toString();
		String password = token.getCredentials().toString();

		return new RestTemplateCacheKey(username, password);
	}

	public static UsernamePasswordAuthenticationToken clone(Authentication authentication){
		if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
			throw new IllegalArgumentException(
				CredentialsUtil.class.getName() + " only supports " + UsernamePasswordAuthenticationToken.class.getName()
					+".  Authentication object is of type "+ authentication.getClass().getName()+".");
		}

		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

		return new UsernamePasswordAuthenticationToken(
			token.getPrincipal(), token.getCredentials(), token.getAuthorities());
	}

	public static UsernamePasswordCredentials cacheKeyToCredentials(RestTemplateCacheKey cacheKey){

		return new UsernamePasswordCredentials(cacheKey.getUserName(), cacheKey.getPassword());
	}
}
