package org.klarblick.openlayers.feature.resource;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.klarblick.openlayers.feature.domain.Feature;
import org.klarblick.openlayers.feature.service.FeatureService;

@ManagedBean
@Path("/feature/")
public class FeatureResource {

	private static final Logger log = Logger.getLogger(FeatureResource.class
			.getName());

	@Inject
	private FeatureService featureService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("list/{i}")
	public List<Feature> getFeatures(@PathParam("i") int mapId) {

		List<Feature> features = featureService.getFeatures();

		// for (Feature f : features) {
		// features.add(new Feature(el.getId(), el.getPosX(), el.getPosY(), ""
		// + el.getId()));
		// log.info("Feature " + el.getId() + " at " + el.getPosX() + ","
		// + el.getPosY() + " added");
		// }

		// features.add(new Feature(17, 10000000, 10000000, "17"));

		return features;
	}
}
