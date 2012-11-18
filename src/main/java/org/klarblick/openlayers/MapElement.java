package org.klarblick.openlayers;

import java.util.logging.Logger;

class MapElement {

	private static final Logger log = Logger.getLogger(MapElement.class
			.getName());

	private int id;
	private int posX;
	private int posY;
	private int tileX;
	private int tileY;

	private double xPercent;
	private double yPercent;

	public MapElement(int id, int posX, int posY) {
		this.id = id;
		this.posX = posX;
		this.posY = posY;
		this.tileX = posX;
		this.tileY = posY;

		xPercent = (double) posX / Map.MAX_WIDTH * 100;
		yPercent = (double) posY / Map.MAX_WIDTH * 100;

		log.info("Element " + id + " is at " + xPercent + "% width, "
				+ yPercent + "% height");
	}

	public void calcPositions(int zoom, int x, int y) {
		int mapWidth = (int) (Math.pow(2, zoom) * Map.TILE_WIDTH);
		posX = (int) (mapWidth / (double) 100 * xPercent);
		posY = (int) (mapWidth / (double) 100 * yPercent);

		tileX = posX - (x * Map.TILE_WIDTH);
		tileY = posY - (y * Map.TILE_WIDTH);
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
		int xTile = x * Map.TILE_WIDTH;
		int yTile = y * Map.TILE_WIDTH;
		return getPosX() >= xTile && getPosX() <= xTile + Map.TILE_WIDTH
				&& getPosY() >= yTile && getPosY() <= yTile + Map.TILE_WIDTH;
	}
}