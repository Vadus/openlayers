package org.klarblick.openlayers;

import java.util.logging.Logger;

import org.junit.Test;
import org.klarblick.openlayers.map.domain.MapElement;

public class MapTest {

	private static final Logger log = Logger.getLogger(MapTest.class.getName());

	private static final int TILE_WIDTH = 256;
	private static final int MAX_ZOOM = 3;
	private static final int MAX_WIDTH = 2 ^ MAX_ZOOM * TILE_WIDTH;

	private static final MapElement[] elements = new MapElement[100];

	@Test
	public void testRandomMapCreation() {

		int numberOfElements = 100;
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
				rY = (int) (MAX_WIDTH / (double) 100 * randomPercentageY);

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
					log.info("Try to put a new element at " + rX + ", " + rY
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

			MapElement mapElement = new MapElement(i + 1, rX, rY);
			elements[i] = mapElement;
		}

		for (MapElement mapElement : elements) {
			log.info("MapElement at " + mapElement.getPosX() + ","
					+ mapElement.getPosY());
		}
	}
}
