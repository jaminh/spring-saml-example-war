package com.jaminh.ws.example.endpoint;

import org.jdom2.Element;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class HelloEndpoint {
	
	@PayloadRoot(localPart="Hello", namespace="urn:jaminh:example")
	@ResponsePayload
	public Element hello(@RequestPayload Element request) {
		return new Element("Hello", "urn:jaminh:example");
	}

}
