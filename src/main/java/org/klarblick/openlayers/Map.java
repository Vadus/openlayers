package org.klarblick.openlayers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/map/{z}/{x}/{y}")
public class Map {

	private static final Logger log = Logger.getLogger(Map.class.getName());

	static final int TILE_WIDTH = 256;
	static final int MAX_ZOOM = 3;
	static final int MAX_WIDTH = (int) (Math.pow(2, MAX_ZOOM) * TILE_WIDTH);

	private static final int numberOfElements = 50;

	private static final MapElement[] elements = new MapElement[numberOfElements];

	static {

		// set MapElements randomly
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
			log.info("Created Element " + mapElement.getId() + " at "
					+ mapElement.getPosX() + "," + mapElement.getPosY());
		}
	}

	@GET
	@Produces("image/png")
	public byte[] getMap(@PathParam("z") int z, @PathParam("x") int x,
			@PathParam("y") int y) {

		if (z > MAX_ZOOM) {
			z = MAX_ZOOM;
		}

		log.info("--> getMap(), zoom=" + z + ", tile=" + x + "," + y);

		BufferedImage bi = new BufferedImage(TILE_WIDTH, TILE_WIDTH,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D) bi.getGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, TILE_WIDTH, TILE_WIDTH);

		// g.setColor(Color.WHITE);
		// g.drawRect(0, 0, TILE_WIDTH, TILE_WIDTH);
		//
		// g.drawString("Tile " + x + "," + y + " at " + (x * TILE_WIDTH) + ","
		// + (y * TILE_WIDTH), 5, 12);

		g.setColor(Color.RED);

		for (MapElement mapElement : elements) {
			mapElement.calcPositions(z, x, y);
			if (mapElement.isInTile(x, y)) {
				log.info("Element " + mapElement.getId() + " is at "
						+ mapElement.getPosX() + ", " + mapElement.getPosY()
						+ "(" + mapElement.getTileX() + ","
						+ mapElement.getTileY() + ")");

				int diameter = (int) Math.pow(2, z) + 1;
				g.fillOval(mapElement.getTileX(), mapElement.getTileY(),
						diameter, diameter);
				// g.drawString("" + mapElement.getId(), mapElement.getTileX(),
				// mapElement.getTileY());
			}
		}

		g.dispose();

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "png", baos);
			byte[] bytesOut = baos.toByteArray();

			return bytesOut;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new byte[] {};
	}
}
