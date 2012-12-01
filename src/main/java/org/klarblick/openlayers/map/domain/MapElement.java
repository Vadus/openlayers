package org.klarblick.openlayers.map.domain;

import java.awt.Color;
import java.io.Serializable;
import java.util.logging.Logger;

import org.klarblick.openlayers.map.resource.MapResource;

public class MapElement implements Serializable {

	private static final long serialVersionUID = 8601687261429111378L;

	private static final Logger log = Logger.getLogger(MapElement.class
			.getName());

	private int id;
	private int posX;
	private int posY;
	private int tileX;
	private int tileY;

	private Color color;

	private double xPercent;
	private double yPercent;

	public MapElement(int id, int posX, int posY) {
		this.id = id;
		this.posX = posX;
		this.posY = posY;
		this.tileX = posX;
		this.tileY = posY;

		xPercent = (double) posX / MapResource.MAX_WIDTH * 100;
		yPercent = (double) posY / MapResource.MAX_WIDTH * 100;

		log.fine("Element " + id + " is at " + xPercent + "% width, "
				+ yPercent + "% height");
	}

	public void calcPositions(int zoom, int x, int y) {
		int mapWidth = (int) (Math.pow(2, zoom) * MapResource.TILE_WIDTH);

		if (y >= 0) {
			posX = (int) (mapWidth / (double) 100 * xPercent);
			tileX = posX - (x * MapResource.TILE_WIDTH);

			posY = (int) (mapWidth / (double) 100 * yPercent);
			tileY = posY - (y * MapResource.TILE_WIDTH);
		} else if (zoom == 0) {
			// minimap in ( 180px x 90px ) frame
			posX = (int) (90 / (double) 100 * xPercent);
			tileX = posX;

			posY = (int) (90 / (double) 100 * yPercent);
			tileY = posY + (MapResource.TILE_WIDTH - 90);

		}
	}

	public int getId() {
		return id;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public boolean isInTile(int zoom, int x, int y) {
		if (zoom == 0) {
			// Max Zoom, all Elements are always in minimap
			return true;
		}
		if (y >= 0) {
			int xTile = x * MapResource.TILE_WIDTH;
			int yTile = y * MapResource.TILE_WIDTH;
			return getPosX() >= xTile
					&& getPosX() <= xTile + MapResource.TILE_WIDTH
					&& getPosY() >= yTile
					&& getPosY() <= yTile + MapResource.TILE_WIDTH;
		} else {
			// check if tile is part of the minimap tile
			int xTile = x * MapResource.TILE_WIDTH;
			int yTile = y * MapResource.TILE_WIDTH + 90;
			return getPosX() >= xTile
					&& getPosX() <= xTile + MapResource.TILE_WIDTH
					&& getPosY() >= yTile
					&& getPosY() <= yTile + MapResource.TILE_WIDTH;

		}

	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}