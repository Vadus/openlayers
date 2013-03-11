package org.klarblick.openlayers.feature.service;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Named;

import org.klarblick.openlayers.feature.domain.Feature;

@Named
@Stateless
public class FeatureService implements Serializable {

	private static final long serialVersionUID = -7898496548257332809L;

	private static final Logger log = Logger.getLogger(FeatureService.class
			.getName());

	private static Color[] starColors = new Color[] { Color.RED, Color.YELLOW,
			Color.WHITE, Color.ORANGE, Color.CYAN };

	/**
	 * Map Projection = EPSG:900913
	 */
	private static final int WIDTH = 20000000;
	private static final long MAX_WIDTH = WIDTH * 2;

	/**
	 * Map Projection = EPSG:4326
	 */
	// private static final int WIDTH = 180;
	// private static final int MAX_WIDTH = WIDTH * 2;

	// TODO: nicht mehr stateless!
	private static List<Feature> featureStore = new ArrayList<Feature>();

	public Feature[] createFeatures(int numberOfElements) {

		Feature[] elements = new Feature[numberOfElements];

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

				rX = (int) (MAX_WIDTH / (double) 100 * randomPercentageX);
				// get at least $leastDistance away from tile border
				if (rX % MAX_WIDTH < leastDistance) {
					if (rX + leastDistance < MAX_WIDTH) {
						rX += leastDistance;
					} else {
						rX -= leastDistance;
					}
				}
				rY = (int) (MAX_WIDTH / (double) 100 * randomPercentageY);
				// get at least $leastDistance away from tile border
				if (rY % MAX_WIDTH < leastDistance) {
					if (rY + leastDistance < MAX_WIDTH) {
						rY += leastDistance;
					} else {
						rY -= leastDistance;
					}
				}

				for (int e = 0; e < i; e++) {
					Feature previousElement = elements[e];
					long deltaX = previousElement.getPosX() - rX;
					if (deltaX < 0)
						deltaX = rX - previousElement.getPosX();

					long deltaY = previousElement.getPosY() - rY;
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

			// shift position to bottom left
			rX = rX - WIDTH;
			rY = rY - WIDTH;

			Feature mapElement = new Feature(i + 1, rX, rY);
			// mapElement.setColor(starColors[color]);
			elements[i] = mapElement;
		}

		for (Feature mapElement : elements) {
			log.fine("Created Feature " + mapElement.getId() + " at "
					+ mapElement.getPosX() + "," + mapElement.getPosY());
		}

		return elements;

	}

	public List<Feature> getFeatures() {
		return featureStore;
	}

	public void storeFeatures(Feature[] featureList) {
		featureStore.clear();
		featureStore.addAll(Arrays.asList(featureList));
	}
}
