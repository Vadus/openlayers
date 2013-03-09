package org.klarblick.mooo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Galaxy {

	private Long id;

	private List<StarSystem> systems;

	public Galaxy() {
		systems = new ArrayList<StarSystem>();
	}

	public void addStarSystem(StarSystem system) {
		systems.add(system);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<StarSystem> getSystems() {
		return systems;
	}

	public void setSystems(List<StarSystem> systems) {
		this.systems = systems;
	}

}
