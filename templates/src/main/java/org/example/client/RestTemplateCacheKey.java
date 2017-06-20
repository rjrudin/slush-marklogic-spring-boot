package org.example.client;

import org.apache.http.util.LangUtils;

/**
 * Key Object for the RestTemplateCache {@link RestTemplateCache} that considers the password in the equals() implementation.
 *
 */
public class RestTemplateCacheKey {
	private String userName;
	private String password;

	public RestTemplateCacheKey(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	@Override
	public int hashCode() {
		return (userName==null?0:userName.hashCode())+(password==null?0:password.hashCode());
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
			if (   LangUtils.equals(this.userName, that.userName)
			    && LangUtils.equals(this.password, that.password)) {
				return true;
			}
		}
		return false;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
