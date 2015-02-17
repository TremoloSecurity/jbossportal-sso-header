/*
Copyright 2015 Tremolo Security, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.tremolosecurity.sso.jbossportal;

import javax.security.auth.login.LoginException;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.Authenticator;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.UsernameCredential;
import org.exoplatform.services.security.jaas.AbstractLoginModule;


public class HeaderLoginModule extends AbstractLoginModule {

	static Log logger = ExoLogger.getExoLogger(HeaderLoginModule.class.getName());
	
	@Override
	public boolean login() throws LoginException {
		HttpServletRequest request;
		try {
			request = (HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest");
		} catch (PolicyContextException e1) {
			logger.error("Could not retrieve http request",e1);
			throw new LoginException("Could not retrieve http request");
		}
		
		String headerName = (String) options.get("headerName");
		if (logger.isDebugEnabled()) {
			logger.debug("Header Name : '" + headerName + "'");
		}
		
		String headerVal = request.getHeader(headerName);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Header Value : '" + headerVal + "'");
		}
		
		if (headerVal != null) {
			Authenticator authenticator;
			try {
				authenticator = (Authenticator)getContainer().getComponentInstanceOfType(Authenticator.class);
			} catch (Exception e) {
				logger.error("Could not get authenticator",e);
				throw new LoginException("could not get authenticator");
			}
	
			Identity identity;
			try {
				identity = authenticator.createIdentity(headerVal);
			} catch (Exception e) {
				logger.error("Could not created identity",e);
				throw new LoginException("Could not created identity");
			}
	
	        sharedState.put("exo.security.identity", identity);
	        sharedState.put("javax.security.auth.login.name", headerVal);
	        
	        
	        subject.getPublicCredentials().add(new UsernameCredential(headerVal));
			
			return true;
		} else {
			logger.debug("No header to trust");
			return false;
		}
	}

	@Override
	public boolean commit() throws LoginException {
		return true;
	}

	@Override
	public boolean abort() throws LoginException {
		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		return true;
	}

	@Override
	protected Log getLogger() {
		return logger;
	}

}
