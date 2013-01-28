package org.klarblick.openlayers.feature.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Feature {

	private long id;

	private long posX;
	private long posY;

	private String title;

	public Feature() {
	}

	public Feature(long id, long posX, long posY) {
		this.id = id;
		this.posX = posX;
		this.posY = posY;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public long getPosX() {
		return posX;
	}

	public long getPosY() {
		return posY;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPosX(long posX) {
		this.posX = posX;
	}

	public void setPosY(long posY) {
		this.posY = posY;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
