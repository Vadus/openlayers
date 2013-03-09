package org.klarblick.mooo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.klarblick.openlayers.feature.domain.Feature;

@XmlRootElement
public class StarSystem extends Feature {

	public StarSystem() {
		planets = new ArrayList<Planet>();
	}

	public StarSystem(int i, int posX, int posY) {
		super(i, posX, posY);
		planets = new ArrayList<Planet>();
	}

	private Long id;

	private List<Planet> planets;

	public void addPlanet(Planet planet) {
		planets.add(planet);
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	public void setPlanets(List<Planet> planets) {
		this.planets = planets;
	}

}
