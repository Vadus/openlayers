package org.klarblick.openlayers.map.service;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Named;

import org.klarblick.openlayers.map.domain.Map;
import org.klarblick.openlayers.map.domain.MapElement;
import org.klarblick.openlayers.map.resource.MapResource;

@Named
@Stateless
public class MapService implements Serializable {

	private static final long serialVersionUID = -7898496548257332809L;

	private static final Logger log = Logger.getLogger(MapService.class
			.getName());

	private static Color[] starColors = new Color[] { Color.RED, Color.YELLOW,
			Color.WHITE, Color.ORANGE, Color.CYAN };

	// TODO: nicht mehr stateless!
	private static java.util.Map<Integer, Map> mapStore = new HashMap<Integer, Map>();

	public Map createMap(int numberOfElements) {

		MapElement[] elements = new MapElement[numberOfElements];

		int leastDistance = 20;

		// set MapElements randomly
		for (int i = 0; i < numberOfElements; i++) {

			int rX = 0;
			int rY = 0;
			int minDistanceToOthers = 0;
			int maxApproaches = 10;
			int approach = 1;
			do {

				double randomPercentageX = Math.random() * 100;
				double randomPercentageY = Math.random() * 100;

				rX = (int) (MapResource.MAX_WIDTH / (double) 100 * randomPercentageX);
				// get at least $leastDistance away from tile border
				if (rX % MapResource.TILE_WIDTH < leastDistance) {
					if (rX + leastDistance < MapResource.MAX_WIDTH) {
						rX += leastDistance;
					} else {
						rX -= leastDistance;
					}
				}
				rY = (int) (MapResource.MAX_WIDTH / (double) 100 * randomPercentageY);
				// get at least $leastDistance away from tile border
				if (rY % MapResource.TILE_WIDTH < leastDistance) {
					if (rY + leastDistance < MapResource.MAX_WIDTH) {
						rY += leastDistance;
					} else {
						rY -= leastDistance;
					}
				}

				for (int e = 0; e < i; e++) {
					MapElement previousElement = elements[e];
					int deltaX = previousElement.getPosX() - rX;
					if (deltaX < 0)
						deltaX = rX - previousElement.getPosX();

					int deltaY = previousElement.getPosY() - rY;
					if (deltaY < 0)
						deltaY = rY - previousElement.getPosY();

					int distance = (int) Math.sqrt(deltaX * deltaX + deltaY
							* deltaY);
					log.fine("Try to put a new element at " + rX + ", " + rY
							+ " with distance to others: " + distance);
					if (minDistanceToOthers == 0
							|| minDistanceToOthers > distance)
						minDistanceToOthers = distance;
				}

				if (i == 0) {
					minDistanceToOthers = leastDistance;
				}

				approach++;

			} while (minDistanceToOthers < leastDistance
					&& approach < maxApproaches);

			int color = (((int) (Math.random() * 100) + 1) / (100 / starColors.length));
			if (color >= starColors.length) {
				color = starColors.length - 1;
			}

			MapElement mapElement = new MapElement(i + 1, rX, rY);
			mapElement.setColor(starColors[color]);
			elements[i] = mapElement;
		}

		for (MapElement mapElement : elements) {
			log.fine("Created Element " + mapElement.getId() + " at "
					+ mapElement.getPosX() + "," + mapElement.getPosY());
		}

		Map map = new Map();
		map.setElements(elements);

		return map;

	}

	public Map getMap(Integer id) {
		return mapStore.get(id);
	}

	public Integer getMapCount() {
		return mapStore.size();
	}

	public Integer storeMap(Map map) {
		mapStore.clear();
		Integer id = mapStore.size() + 1;
		map.setId(id);
		mapStore.put(map.getId(), map);

		return id;
	}
}
