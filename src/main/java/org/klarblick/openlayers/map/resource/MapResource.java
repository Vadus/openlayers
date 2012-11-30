package org.klarblick.openlayers.map.resource;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.ManagedBean;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.klarblick.openlayers.map.domain.Map;
import org.klarblick.openlayers.map.domain.MapElement;
import org.klarblick.openlayers.map.service.MapService;

@ManagedBean
@Path("/map/")
public class MapResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(MapResource.class
			.getName());

	public static final int TILE_WIDTH = 256;
	public static final int MAX_ZOOM = 3;
	public static final int MAX_WIDTH = (int) (Math.pow(2, MAX_ZOOM) * TILE_WIDTH);

	@Inject
	private MapService mapService;

	@GET
	@Produces("image/png")
	@Path("{i}/{z}/{x}/{y}")
	public byte[] getMap(@PathParam("i") int mapId, @PathParam("z") int z,
			@PathParam("x") int x, @PathParam("y") int y) {

		if (z > MAX_ZOOM) {
			z = MAX_ZOOM;
		}

		log.info("--> getMap(" + mapId + "), zoom=" + z + ", tile=" + x + ","
				+ y);

		BufferedImage bi = new BufferedImage(TILE_WIDTH, TILE_WIDTH,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D) bi.getGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, TILE_WIDTH, TILE_WIDTH);

		// Map map = mapService.createMap(50);
		Map map = mapService.getMap(mapId);
		// if (map == null) {
		// map = mapService.createMap(50);
		// mapId = mapService.storeMap(map);
		// }

		if (map != null) {

			if (map.getDebug()) {

				g.setColor(Color.WHITE);
				g.drawRect(0, 0, TILE_WIDTH, TILE_WIDTH);

				g.drawString("Tile " + x + "," + y + " at " + (x * TILE_WIDTH)
						+ "," + (y * TILE_WIDTH), 5, 12);

			}

			for (MapElement mapElement : map.getElements()) {
				mapElement.calcPositions(z, x, y);
				if (mapElement.isInTile(x, y)) {
					log.info("Element " + mapElement.getId() + " is at "
							+ mapElement.getPosX() + ", "
							+ mapElement.getPosY() + "("
							+ mapElement.getTileX() + ","
							+ mapElement.getTileY() + ")");

					int diameter = (int) Math.pow(2, z) + 1;

					g.setColor(mapElement.getColor());

					if (map.getDebug()) {
						g.drawString("" + mapElement.getId(),
								mapElement.getTileX(), mapElement.getTileY());
					} else {
						g.fillOval(mapElement.getTileX(),
								mapElement.getTileY(), diameter, diameter);
					}
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
		} else {
			log.info("Requested Map " + mapId + " does not exist");
		}

		return new byte[] {};
	}

	@GET
	@Produces("text/plain")
	@Path("count")
	public String getMapCount() {

		return mapService.getMapCount().toString();
	}

	@GET
	@Produces("text/plain")
	@Path("create/{c}")
	public String create(@PathParam("c") Integer elements) {

		Map map = mapService.createMap(elements);
		mapService.storeMap(map);

		return map.getId().toString();
	}
}
