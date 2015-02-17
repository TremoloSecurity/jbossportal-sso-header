# jbossportal-sso-header
Read a header for the username for JBoss Portal 6.3 on JBoss EAP.  This is useful when using an external authentication system that isn't directly supported by JBoss Portal

# Install

Installing this module assumes that you have some way to inject the user's id into a specific header.

1. Build using "$ mvn package"
2. Create a directory called com/tremolosecurity/sso/jpp/header/main
3. Copy target/jbossportal-sso-trustheader-0.0.1-SNAPSHOT.jar to com/tremolosecurity/sso/jpp/header/main
4. Copy src/main/xml/module.xml to com/tremolosecurity/sso/jpp/header/main
5. Copy com (and all subdirectories) into the modules directory for JBoss
6. Add the module as a global module
7. Add the module to the gatein-domain security domain AFTER the header is set but BEFORE the JBoss7ASLogin module

...
<login-module code="com.tremolosecurity.sso.jbossportal.HeaderLoginModule" flag="required">
  <module-option name="headerName" value="uid" />
</login-module>
...
