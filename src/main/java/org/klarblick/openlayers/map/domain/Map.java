package org.klarblick.openlayers.map.domain;

import java.io.Serializable;

public class Map implements Serializable {

	private static final long serialVersionUID = 1141585306537487023L;

	private Integer id;
	private MapElement[] elements;

	private boolean debug;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MapElement[] getElements() {
		return elements;
	}

	public void setElements(MapElement[] elements) {
		this.elements = elements;
	}

	public boolean getDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
