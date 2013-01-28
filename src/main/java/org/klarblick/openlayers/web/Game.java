package org.klarblick.openlayers.web;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.klarblick.openlayers.feature.domain.Feature;
import org.klarblick.openlayers.feature.service.FeatureService;
import org.klarblick.openlayers.map.domain.Map;
import org.klarblick.openlayers.map.resource.MapResource;
import org.klarblick.openlayers.map.service.MapService;

@Named
@SessionScoped
public class Game implements Serializable {

	private static final long serialVersionUID = 6655522847097337344L;

	private static final Logger log = Logger.getLogger(Game.class.getName());

	@Inject
	private MapService mapService;

	@Inject
	private FeatureService featureService;

	private Integer mapId;
	private Integer numberOfElements = 50;
	private Boolean debug = false;

	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}

	public Integer getMapCount() {
		return mapService.getMapCount();
	}

	public String createMap() {
		log.info("Create Map with " + numberOfElements + " stars");
		Map map = mapService.createMap(numberOfElements);
		map.setDebug(debug);
		mapService.storeMap(map);

		Feature[] features = featureService.createFeatures(numberOfElements);
		featureService.storeFeatures(features);

		mapId = map.getId();

		return mapId.toString();
	}

	public Integer getMapId() {
		if (mapId == null) {
			createMap();
		}
		return mapId;
	}

	public Integer getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(Integer numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public int getMaxZoom() {
		return MapResource.MAX_ZOOM + 1;
	}

}
