package org.klarblick.mooo.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Planet {

	private Long id;
	private int angle;

	public Planet() {
	}

	public Planet(Long id) {
		this.id = id;
		this.angle = (int) (Math.random() * 360) + 1;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
}
