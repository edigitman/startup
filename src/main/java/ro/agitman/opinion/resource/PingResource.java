package ro.agitman.opinion.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.agitman.opinion.service.PongService;

import com.google.inject.Inject;

@Path("/ping")
public class PingResource {

	private static Logger LOGGER = LoggerFactory.getLogger(PingResource.class);
	private final PongService pongService;

	@Inject
	public PingResource(PongService ps) {
		this.pongService = ps;
	}

	@RequiresRoles("user")
	@GET
	public Response getMsg() {
		LOGGER.debug("ping called");
		String output = "User say : " + pongService.pong();

		return Response.status(200).entity(output).build();
	}

	@RequiresRoles("admin")
	@GET
	@Path("admin")
	public Response getSecure() {
		String output = "Admin say : " + pongService.pong();
		return Response.status(200).entity(output).build();
	}

}
