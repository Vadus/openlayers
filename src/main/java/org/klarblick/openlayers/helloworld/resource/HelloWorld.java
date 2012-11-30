package org.klarblick.openlayers.helloworld.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/helloworld")
public class HelloWorld {
	@GET
	@Produces("text/plain")
	public String sayHello() {
		return "Hello World\n";
	}

	@GET
	@Produces("text/html")
	public String sayHelloInHtml() {
		return "<html><title>Hello, world</title><body><h2>Hi!</h2></body></html>";
	}
}
