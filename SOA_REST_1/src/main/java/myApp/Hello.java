package myApp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// Sets the path to base URL + /hello
@Path("/hello")
public class Hello {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String textHello() {
		return "Hello world in REST";
	}
	
	@GET
	@Produces(MediaType.TEXT_XML)
	public String xmlHello() {
		return "<?xml version=\"1.0\"?> <hello> Hello World in REST </hello>";
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String htmlHello() {
		return "<html>" +
				"<head><title>Hello World Test </title></head>" + 
					"<body> <h1> Hello World in REST</h1> </body>" +
					"</html>";
	}
	
}
