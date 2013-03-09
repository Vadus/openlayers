package org.klarblick.mooo.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Planet {

	private Long id;

	public Planet() {
	}

	public Planet(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
