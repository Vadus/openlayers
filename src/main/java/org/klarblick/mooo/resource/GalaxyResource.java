package org.klarblick.mooo.resource;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.klarblick.mooo.domain.Galaxy;
import org.klarblick.mooo.domain.StarSystem;
import org.klarblick.mooo.service.GalaxyGenerator;

@ManagedBean
@Path("/galaxy/")
public class GalaxyResource {

	private static final Logger log = Logger.getLogger(GalaxyResource.class
			.getName());

	@Inject
	private GalaxyGenerator galaxyGenerator;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{i}/systems")
	public List<StarSystem> getSystems(@PathParam("i") long galaxyId) {

		Galaxy galaxy = galaxyGenerator.getGalaxy(galaxyId);

		return galaxy.getSystems();
	}
}
