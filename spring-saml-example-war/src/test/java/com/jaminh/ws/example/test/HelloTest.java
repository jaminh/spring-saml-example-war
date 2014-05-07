package com.jaminh.ws.example.test;

import org.apache.ws.security.common.SAML2CallbackHandler;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.saml.SAMLIssuerImpl;
import org.jdom2.Element;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.JDOMSource;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j.support.CryptoFactoryBean;

public class HelloTest {
	
	@Test
	public void test() throws Exception {
		
		WebServiceTemplate template = new WebServiceTemplate();
		
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.setSoapVersion(SoapVersion.SOAP_12);
		messageFactory.afterPropertiesSet();
		template.setMessageFactory(messageFactory);
		
		CryptoFactoryBean cryptoFactory = new CryptoFactoryBean();
		cryptoFactory.setKeyStoreLocation(new ClassPathResource("keystore.jks"));
		cryptoFactory.setKeyStorePassword("password");
		cryptoFactory.afterPropertiesSet();
		Crypto crypto = cryptoFactory.getObject();
		
		SAML2CallbackHandler samlCallbackHandler = new SAML2CallbackHandler(crypto, "selfsigned");
		
		SAMLIssuerImpl issuer = new SAMLIssuerImpl();
		issuer.setIssuerCrypto(crypto);
		issuer.setIssuerKeyName("selfsigned");
		issuer.setIssuerKeyPassword("password");
		issuer.setIssuerName("selfsigned");
		issuer.setSendKeyValue(false);
		issuer.setSignAssertion(true);
		issuer.setCallbackHandler(samlCallbackHandler);
		
		Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
		securityInterceptor.setSecurementActions("Timestamp SAMLTokenSigned");
		securityInterceptor.setSecurementSignatureCrypto(crypto);
		securityInterceptor.setSecurementUsername("selfsigned");
		securityInterceptor.setSecurementPassword("password");
		securityInterceptor.setSamlIssuer(issuer);
		securityInterceptor.afterPropertiesSet();
		
		template.setInterceptors(new ClientInterceptor[] {securityInterceptor});
		template.afterPropertiesSet();
		
		Element hello = new Element("Hello", "urn:jaminh:example");
		template.sendSourceAndReceiveToResult("http://localhost:8080/spring-saml-example-war", new JDOMSource(hello), new JDOMResult());
		template.sendSourceAndReceiveToResult("http://localhost:8080/spring-saml-example-war", new JDOMSource(hello), new JDOMResult());
		template.sendSourceAndReceiveToResult("http://localhost:8080/spring-saml-example-war", new JDOMSource(hello), new JDOMResult());
		
	}

}
