package org.example.client;

import org.apache.http.auth.Credentials;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Collection;

/**
 * Downstream authentication token designed to also be a holder for the Digest authenticated upstream RestTemplate.
 *
 */
public class DigestRestClientSession extends UsernamePasswordAuthenticationToken implements Credentials  {
	final private String userName;
	final private String password;
	final private Principal principal;
	private RestTemplate restTemplate;

	public DigestRestClientSession(final Object principal, Object credentials,
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

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}


	@Override
	public Principal getUserPrincipal() {
		return this.principal;
	}

	@Override
	public String getPassword() {
		//needed to be kept to compute authentication headers
		return this.password;
	}

}
