package com.example.jaasModule1;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BytesLoungeLoginModule implements LoginModule {

	private CallbackHandler callbackHandler;
	private Subject subject;
	Map<String, String> sharedState;
	Map<String, String> options;
	private UserPrincipal userPrincipal;
	private RolePrincipal rolePrincipal;
	private String login;
	private List<String> userGroups;

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
		this.callbackHandler = callbackHandler;
		this.subject = subject;
		this.sharedState = (Map<String, String>) sharedState;
		this.options = (Map<String, String>) options;
	}

	@Override
	public boolean login() throws LoginException {
		String username = null;
		String password = null;

		if ("true".equalsIgnoreCase(options.get("useSharedState")))
		{
			username = (String)sharedState.get("javax.security.auth.login.name");
			password = (String)sharedState.get("javax.security.auth.login.password");
		}
		else
		{
			try {
				Callback[] callbacks = new Callback[2];
				callbacks[0] = new NameCallback("login");
				callbacks[1] = new PasswordCallback("password", true);
				callbackHandler.handle(callbacks);
				username = ((NameCallback) callbacks[0]).getName();
				password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());
				sharedState.put("javax.security.auth.login.name",username);
				sharedState.put("javax.security.auth.login.password",password);
			} catch (IOException e) {
				throw new LoginException(e.getMessage());
			} catch (UnsupportedCallbackException e) {
				throw new LoginException(e.getMessage());
			}
		}

		login = username;
		userGroups = new ArrayList<String>();
		userGroups.add("admin");
		return true;
	}

	@Override
	public boolean commit() throws LoginException {

		userPrincipal = new UserPrincipal(login);
		subject.getPrincipals().add(userPrincipal);

		if (userGroups != null && userGroups.size() > 0) {
			for (String groupName : userGroups) {
				rolePrincipal = new RolePrincipal(groupName);
				subject.getPrincipals().add(rolePrincipal);
			}
		}

		return true;
	}

	@Override
	public boolean abort() throws LoginException {
		return false;
	}

	@Override
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(userPrincipal);
		subject.getPrincipals().remove(rolePrincipal);
		return true;
	}

}
