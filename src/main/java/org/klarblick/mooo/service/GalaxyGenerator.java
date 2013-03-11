package org.klarblick.mooo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.klarblick.mooo.domain.Galaxy;
import org.klarblick.mooo.domain.Planet;
import org.klarblick.mooo.domain.StarSystem;
import org.klarblick.mooo.resource.GalaxyResource;

public class GalaxyGenerator {

	public static final int GALAXY_WIDTH_HALF = 180;
	public static final int GALAXY_WIDTH = GALAXY_WIDTH_HALF * 2;
	public static final int GALAXY_HEIGH_HALF = 90;
	public static final int GALAXY_HEIGH = GALAXY_HEIGH_HALF * 2;

	private static final Logger log = Logger.getLogger(GalaxyResource.class
			.getName());

	private static Map<Long, Galaxy> galaxyStore = new HashMap<Long, Galaxy>();

	public Galaxy generateGalaxy(int numberOfSystems) {

		StarSystem[] systems = generateRandomStarSystems(numberOfSystems);

		Galaxy galaxy = new Galaxy();
		galaxy.setId(new Long(galaxyStore.size()));

		for (StarSystem starSystem : systems) {
			galaxy.addStarSystem(starSystem);
		}

		// for (Feature mapElement : elements) {
		// log.fine("Created Feature " + mapElement.getId() + " at "
		// + mapElement.getPosX() + "," + mapElement.getPosY());
		// }

		return galaxy;
	}

	public Galaxy getGalaxy(Long galaxyId) {
		Galaxy galaxy = galaxyStore.get(galaxyId);
		if (galaxy == null) {
			galaxy = generateGalaxy(50);
		}
		return galaxy;
	}

	private StarSystem[] generateRandomStarSystems(int numberOfSystems) {
		StarSystem[] systems = new StarSystem[numberOfSystems];

		int leastDistance = 10;

		// set MapElements randomly
		for (int i = 0; i < numberOfSystems; i++) {

			int rX = 0;
			int rY = 0;
			int minDistanceToOthers = 0;
			int maxApproaches = 10;
			int approach = 1;
			do {

				double randomPercentageX = Math.random() * 100;
				double randomPercentageY = Math.random() * 100;

				rX = (int) (GALAXY_WIDTH / (double) 100 * randomPercentageX);
				// get at least $leastDistance away from tile border
				if (rX % GALAXY_WIDTH < leastDistance) {
					if (rX + leastDistance < GALAXY_WIDTH) {
						rX += leastDistance;
					} else {
						rX -= leastDistance;
					}
				}
				rY = (int) (GALAXY_HEIGH / (double) 100 * randomPercentageY);
				// get at least $leastDistance away from tile border
				if (rY % GALAXY_HEIGH < leastDistance) {
					if (rY + leastDistance < GALAXY_HEIGH) {
						rY += leastDistance;
					} else {
						rY -= leastDistance;
					}
				}

				for (int e = 0; e < i; e++) {
					StarSystem previousSystem = systems[e];
					long deltaX = previousSystem.getPosX() - rX;
					if (deltaX < 0)
						deltaX = rX - previousSystem.getPosX();

					long deltaY = previousSystem.getPosY() - rY;
					if (deltaY < 0)
						deltaY = rY - previousSystem.getPosY();

					int distance = (int) Math.sqrt(deltaX * deltaX + deltaY
							* deltaY);
					// log.fine("Try to put a new element at " + rX + ", " + rY
					// + " with distance to others: " + distance);
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

			// shift position to bottom left
			rX = rX - GALAXY_WIDTH_HALF;
			rY = rY - GALAXY_HEIGH_HALF;

			StarSystem system = new StarSystem(i + 1, rX, rY);
			// mapElement.setColor(starColors[color]);

			int numberOfPlanets = (int) (Math.random() * 10);
			for (int n = 0; n < numberOfPlanets; n++) {
				system.addPlanet(new Planet(new Long(
						system.getPlanets().size() + 1)));
			}
			log.info("Created StarSystem " + system.getId() + " with "
					+ system.getPlanets().size() + " planets");

			systems[i] = system;
		}
		return systems;
	}
}
