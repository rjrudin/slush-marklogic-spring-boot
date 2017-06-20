package org.example.client;

import org.apache.http.auth.Credentials;
import org.apache.http.util.LangUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;

/**
 * Designed to be used as key for RestTemplateCache {@link RestTemplateCache} to consider both username and password in
 *   the equality comparison
 * Also intended to be a replacement for UsernamePasswordAuthenticationToken {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}
 *   and UsernamePasswordCredentials{@link org.apache.http.auth.UsernamePasswordCredentials}.
 * Needed since UsernamePasswordCredentials does not consider the password value in equality comparison
 *   and UsernamePasswordAuthenticationToken has an extra comparison on isAuthenticated value.
 */
public class RestTemplateCacheKey extends UsernamePasswordAuthenticationToken implements Credentials  {
	final private String userName;
	final private String password;
	final private Principal principal;

	public RestTemplateCacheKey(final Object principal, Object credentials,
								Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		this.userName = (principal==null?"":(String)principal);
		this.password = (credentials==null?"":(String)credentials);
		this.principal = new Principal() {
			@Override
			public String getName() {
				return userName;
			}
		};
	}

	public RestTemplateCacheKey(Object principal, Object credentials) {
		this(principal, credentials, null);
	}

	public RestTemplateCacheKey(final String userName, final String password) {
		this(userName, password, null);
	}

	@Override
	public int hashCode() {
		return (userName+password).hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o==null){
			return false;
		}
		if (o instanceof RestTemplateCacheKey) {
			final RestTemplateCacheKey that = (RestTemplateCacheKey) o;
			return LangUtils.equals(this.userName, that.userName)
					&& LangUtils.equals(this.password, that.password);
		}
		return false;
	}

	public String getUserName() {
		return userName;
	}


	@Override
	public Principal getUserPrincipal() {
		return this.principal;
	}

	@Override
	public String getPassword() {
		return password;
	}

}
