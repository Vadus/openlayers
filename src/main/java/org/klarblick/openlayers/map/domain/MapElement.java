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
		posX = (int) (mapWidth / (double) 100 * xPercent);
		posY = (int) (mapWidth / (double) 100 * yPercent);

		tileX = posX - (x * MapResource.TILE_WIDTH);
		tileY = posY - (y * MapResource.TILE_WIDTH);
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

	public boolean isInTile(int x, int y) {
		int xTile = x * MapResource.TILE_WIDTH;
		int yTile = y * MapResource.TILE_WIDTH;
		return getPosX() >= xTile
				&& getPosX() <= xTile + MapResource.TILE_WIDTH
				&& getPosY() >= yTile
				&& getPosY() <= yTile + MapResource.TILE_WIDTH;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}