var OBJECT_TYPE = {
	STAR : 0,
	PLANET : 1
};

function Star() {
	this.x = 0;
	this.y = 0;
	this.z = 0;
	this.size = 5;
	//default
	this.planets = new Array();
	this.group = undefined;
	this.starSphere = undefined;
	this.starLight = undefined;
}

Star.prototype.getObjectType = function(){
	return OBJECT_TYPE.STAR;
};

Star.prototype.getStar = function(){
	return this;
};

Star.prototype.getDistanceTo = function(position) {

	var x1 = this.x;
	var y1 = this.y;
	var z1 = this.z;

	var x2 = position.x;
	var y2 = position.y;
	var z2 = position.z;

	//sqrt((x1-x2)² + (y1-y2)² + (z1-z2)²)
	return dist = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2) + Math.pow((z1 - z2), 2));
};

Star.prototype.getMesh = function() {

	return this.starSphere;
};

Star.prototype.getSize = function() {

	return this.size;
};

Star.prototype.getAttractionDistance = function() {

	return this.planets.length * 150;
};

Star.prototype.onClick = function() {

	console.log("clicked at star");
};

Star.prototype.isActive = function() {
};

Star.prototype.activate = function() {
};

Star.prototype.deactivate = function() {
};

Star.prototype.setVisible = function(visible) {
};

var PLANET_TYPE = {
	J : 5, //Mercury
	C : 4, //Venus
	M : 8, //Earth
	K : 6, //Mars
	A : 2, //Jupter
	B : 3, //Saturn
	W : 7 // Water World
};

function Planet(star, type, size, radius) {
	this.star = star;
	this.size = size;
	// 1 - 5
	this.radius = radius;
	this.type = type;
	this.x = 0;
	this.y = 0;
	this.z = 0;
	this.angle = Math.random() * 360;
	this.group = undefined;
	this.planetSphere = undefined;
	this.cloudSphere = undefined;
	this.planetGlow = undefined;
	this.ellipse = undefined;
}

Planet.prototype.getStar = function() {

	return this.star;
};

Planet.prototype.getObjectType = function(){
	return OBJECT_TYPE.PLANET;
};

Planet.prototype.updatePos = function(angle) {
	this.angle = angle;
	this.x = parseInt(this.star.x) + this.radius * Math.cos(this.angle);
	this.y = parseInt(this.star.y) + (this.radius - (this.radius * 0.2)) * Math.sin(this.angle);
};

Planet.prototype.isVisible = function() {
	return true;
};

Planet.prototype.getMesh = function() {

	return this.planetSphere;
};

Planet.prototype.getSize = function() {

	return this.size;
};

Planet.prototype.getAttractionDistance = function() {

	return this.size * 100 / 3;
};

Planet.prototype.onClick = function() {

	console.log("clicked at planet");
};

Planet.prototype.isActive = function() {
	return this.planetGlow.visible == true;
};

Planet.prototype.activate = function() {
	this.planetGlow.visible = true;
};

Planet.prototype.deactivate = function() {
	this.planetGlow.visible = false;
};

Planet.prototype.setVisible = function(visible) {

	//save visibility of planetGlow
	var glowVisible = visible == true ? this.planetGlow.visible : false;

	this.group.traverse(function(child) {
		child.visible = visible;
	});

	//restore visibility of planetGlow
	this.planetGlow.visible = glowVisible;
};

Planet.prototype.isVisible = function() {
	return this.planetSphere.visible == true;
};
function Three_Galaxy(container) {

	this.container = container;

	this.mouse = {
		x : 0,
		y : 0
	};

	///////////
	// SCENE //
	///////////
	this.scene = new THREE.Scene();

	////////////
	// CAMERA //
	////////////

	// set the view size in pixels (custom or according to window size)
	// var SCREEN_WIDTH = 400, SCREEN_HEIGHT = 300;
	var SCREEN_WIDTH = window.innerWidth, SCREEN_HEIGHT = window.innerHeight;
	// camera attributes
	var VIEW_ANGLE = 45, ASPECT = SCREEN_WIDTH / SCREEN_HEIGHT, NEAR = 0.1, FAR = 20000;
	// set up camera
	this.camera = new THREE.PerspectiveCamera(VIEW_ANGLE, ASPECT, NEAR, FAR);
	// add the camera to the scene
	this.scene.add(this.camera);
	// the camera defaults to position (0,0,0)
	// 	so pull it back (z = 400) and up (y = 100) and set the angle towards the scene origin
	
	this.cameraDistance = 3000;
	
	this.camera.position.set(0, 0 - this.cameraDistance, 0 + this.cameraDistance);
	this.camera.lookAt(this.scene.position);
	//save initial camera location
	this.cameraOrigin = this.camera.position.clone();

	//////////////
	// RENDERER //
	//////////////

	// create and start the renderer; choose antialias setting.
	this.renderer = new THREE.WebGLRenderer();

	this.renderer.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

	// attach renderer to the container div
	this.container.appendChild(this.renderer.domElement);

	////////////
	// EVENTS //
	////////////

	// automatically resize renderer
	THREEx.WindowResize(this.renderer, this.camera);
	// toggle full-screen on given key press
	//THREEx.FullScreen.bindKey({ charCode : 'm'.charCodeAt(0) });

	this.light = new THREE.HemisphereLight(0xFFFFFF, 0x000000, 0.5);
	this.scene.add(this.light);

	// create a set of coordinate axes to help orient user
	//    specify length in pixels in each direction
	//this.scene.add(new THREE.AxisHelper(100));

	// start the renderer
	this.renderer.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

	// attach the render-supplied DOM element
	this.container.appendChild(this.renderer.domElement);

	this.clickable = [];
	this.activeItem = undefined;
	this.cameraAttention = undefined;
	this.cameraAttentionPosition = undefined;

	this.stars = new Array();
	this.setupStars();

	//////////////
	// CONTROLS //
	//////////////

	// move mouse and: left   click to rotate,
	//                 middle click to zoom,
	//                 right  click to pan
	this.controls = new THREE.OrbitControls(this);

}

Three_Galaxy.prototype.setupStars = function() {

	var star_1 = new Star();
	star_1.planets[0] = new Planet(star_1, PLANET_TYPE.J, 1, 100);
	star_1.planets[1] = new Planet(star_1, PLANET_TYPE.C, 1, 200);
	star_1.planets[2] = new Planet(star_1, PLANET_TYPE.M, 1, 300);
	star_1.planets[3] = new Planet(star_1, PLANET_TYPE.K, 1, 400);

	this.stars[0] = star_1;

	for (var i in this.stars) {
		var star = this.stars[i];

		this.setupStar(star);

		for (var i in star.planets) {
			var planet = star.planets[i];

			this.setupPlanet(planet);
		}
	}
	
	//this.setupShip(star_1.x, star_1.y, star_1.z + 200);
};

Three_Galaxy.prototype.setupStar = function(star) {

	//Star Body
	var sphereGeo = new THREE.SphereGeometry(star.size * 10, 32, 16);

	var colors = THREE.ImageUtils.loadTexture("img/sun-surface.jpg");
	//var bumpy = THREE.ImageUtils.loadTexture("img/sun_surface.jpg");
	var shiny = THREE.ImageUtils.loadTexture("img/sun-specular.png");

	var starMaterial = new THREE.MeshPhongMaterial({
		color : 0xffffff,
		map : colors,
		//bumpMap : bumpy,
		//bumpScale : 4,
		specular : 0xffff00,
		specularMap : shiny,
		emissive : 0xE3E771
	});

	var starSphere = new THREE.Mesh(sphereGeo, starMaterial);
	starSphere.position.set(star.x, star.y, star.z);

	starSphere.rotation.x = Math.PI / 2;

	// create a point light
	var starLight = new THREE.PointLight(0xFFFFFF);

	starLight.position.set(star.x, star.y, star.z);

	var group = new THREE.Object3D();
	group.add(starSphere);
	group.add(starLight);

	this.scene.add(group);

	star.group = group;
	star.starSphere = starSphere;
	star.starLight = starLight;

	this.clickable.push(star);
};

Three_Galaxy.prototype.setupPlanet = function(planet) {

	//position in star system
	//planet position at ellipse

	planet.updatePos(planet.angle);

	var sphereGeo = new THREE.SphereGeometry(planet.size * 10, 64, 32);

	var surface = undefined;
	var topografics = undefined;
	var specularics = undefined;

	if (planet.type == PLANET_TYPE.M) {
		surface = "img/earth-day.jpg";
		topografics = "img/earth-topo.jpg";
		specularics = "img/earth-specular.jpg";
	} else if (planet.type == PLANET_TYPE.J) {
		surface = "img/planet_j_surface.jpg";
		topografics = "img/planet_j_bump.jpg";
	} else if (planet.type == PLANET_TYPE.C) {
		surface = "img/planet_c_surface.jpg";
		topografics = "img/planet_c_bump.jpg";
	} else if (planet.type == PLANET_TYPE.K) {
		surface = "img/planet_k_surface.jpg";
		topografics = "img/planet_k_bump.jpg";
	}

	var colors = THREE.ImageUtils.loadTexture(surface);
	var bumpy = THREE.ImageUtils.loadTexture(topografics);
	var shiny = null;
	if (specularics != undefined) {
		shiny = THREE.ImageUtils.loadTexture(specularics);
	}
	//var shiny = THREE.ImageUtils.loadTexture("img/earth-specular.jpg");

	var planetMaterial = new THREE.MeshPhongMaterial({
		color : 0xffffff,
		map : colors,
		bumpMap : bumpy,
		bumpScale : 4,
		specular : 0xffffff,
		specularMap : shiny,
		emissive : 0x888888
	});

	var planetSphere = new THREE.Mesh(sphereGeo, planetMaterial);
	planetSphere.position.set(planet.x, planet.y, planet.z);

	// add a cloud layer

	var cloudMaterial = new THREE.MeshBasicMaterial({
		map : THREE.ImageUtils.loadTexture("img/earth-clouds.png"),
		transparent : true
	});

	var cloudSphere = undefined;
	if (planet.type == PLANET_TYPE.M) {
		cloudSphere = new THREE.Mesh(sphereGeo.clone(), cloudMaterial);
		cloudSphere.scale.x = cloudSphere.scale.y = cloudSphere.scale.z = 1.005;
		cloudSphere.position.set(planet.x, planet.y, planet.z);
	}


	var planetGlowMaterial = new THREE.MeshBasicMaterial( { color: 0xff0000, side: THREE.BackSide } );
	var planetGlow = new THREE.Mesh( sphereGeo, planetGlowMaterial );
	planetGlow.position = planetSphere.position;
	planetGlow.scale.multiplyScalar(1.05);
	planetGlow.visible = false;

	/*

	// create glow/active sphere around planet
	//   that is within specially labeled script tags
	var customMaterial = new THREE.ShaderMaterial({
		uniforms : {
			"c" : {
				type : "f",
				value : 1.0
			},
			"p" : {
				type : "f",
				value : 4.0
			},
			glowColor : {
				type : "c",
				value : new THREE.Color(0xffff00)
			},
			viewVector : {
				type : "v3",
				value : this.camera.position
			}
		},
		vertexShader : document.getElementById('vertexShader_glow').textContent,
		fragmentShader : document.getElementById('fragmentShader_glow').textContent,
		side : THREE.FrontSide,
		blending : THREE.AdditiveBlending,
		transparent : true
	});

	

	var planetGlow = new THREE.Mesh(sphereGeo.clone(), customMaterial.clone());
	planetGlow.position = planetSphere.position;
	planetGlow.scale.multiplyScalar(1.2);
	planetGlow.visible = false;
	
	*/

	// add planets ellipse around star
	// use LineBasicMaterial if no dashes are desired

	var material = new THREE.LineBasicMaterial({
		color : 0xAAAAAA,
		opacity : 1
	});
	var ellipseCurve = new THREE.EllipseCurve(planet.star.x, planet.star.y, planet.radius, planet.radius - (planet.radius * 0.2), 0, 2.0 * Math.PI, false);
	var ellipsePath = new THREE.CurvePath();
	ellipsePath.add(ellipseCurve);
	var ellipseGeometry = ellipsePath.createPointsGeometry(100);
	ellipseGeometry.computeTangents();
	var ellipse = new THREE.Line(ellipseGeometry, material);

	var group = new THREE.Object3D();
	group.add(planetSphere);
	if (cloudSphere != undefined) {
		group.add(cloudSphere);
	}
	group.add(planetGlow);
	group.add(ellipse);

	this.scene.add(group);

	planet.group = group;
	planet.planetSphere = planetSphere;
	planet.cloudSphere = cloudSphere;
	planet.planetGlow = planetGlow;
	planet.ellipse = ellipse;

	planet.planetSphere.rotation.x += 1;
	if (planet.cloudSphere != undefined) {
		planet.cloudSphere.rotation.x += 1;
	}
	planet.planetGlow.rotation.x += 1;

	this.clickable.push(planet);

};

Three_Galaxy.prototype.setupShip = function(x, y, z){
	
	var spaceShipMesh = "obj/spaceship/spacefighter_e.obj";
	var spaceShipSurface = "obj/spaceship/metal.jpg";

	var spaceShipTexture = new THREE.Texture();

	var loader = new THREE.ImageLoader(new THREE.LoadingManager());
	loader.load(spaceShipSurface, function(image) {

		spaceShipTexture.image = image;
		spaceShipTexture.needsUpdate = true;

	});

	// model
	var theScene = this.scene;

	var manager = new THREE.LoadingManager();
	var loader = new THREE.OBJLoader(manager);
	loader.load(spaceShipMesh, function(spaceShip) {

		spaceShip.traverse(function(child) {

			if ( child instanceof THREE.Mesh) {

				child.material.map = spaceShipTexture;

			}

		});

		spaceShip.position.x = x;
		spaceShip.position.y = y;
		spaceShip.position.z = z;
		theScene.add(spaceShip);

	});
};

Three_Galaxy.prototype.onClick = function(event) {

	//check if anything clickable was clicked

	// update the mouse variable
	this.mouse.x = (event.clientX / window.innerWidth ) * 2 - 1;
	this.mouse.y = -(event.clientY / window.innerHeight ) * 2 + 1;

	// find intersections

	// create a Ray with origin at the mouse position
	//   and direction into the scene (camera direction)
	var vector = new THREE.Vector3(this.mouse.x, this.mouse.y, 1);
	projector.unprojectVector(vector, this.camera);
	var ray = new THREE.GalaxyRaycaster(this, vector);

	// create an array containing all objects in the scene with which the ray intersects
	var intersects = ray.intersectGalaxy();

	console.log("Click intersects " + intersects.length + " targets");
	// if there is one (or more) intersections
	if (intersects.length > 0) {
		var galaxyItem = intersects[0].galaxyItem;

		//is this a new active Item?
		if (this.activeItem == undefined || this.activeItem.getMesh().id != galaxyItem.getMesh().id) {
			for (var i = 0; i < this.clickable.length; i++) {
				var item = this.clickable[i];
				item.deactivate();
			}
		}
		this.activeItem = galaxyItem;
		this.activeItem.activate();

		galaxyItem.onClick();
	} else {
		//deactive all clickable items
		for (var i = 0; i < this.clickable.length; i++) {
			var item = this.clickable[i];
			item.deactivate();
		}
		this.activeItem = undefined;
	}
};

Three_Galaxy.prototype.onDoubleClick = function(event) {

	console.log("doubleClick");
	
	var formerActiveItem = this.activeItem;
	
	this.onClick(event);
	
	//TODO: sepearate doubleClick from click.
	//Check what is currently active
	//if it is a planet, and clicked at nothing, then switch to system view
	//if is is a star, and clicked at nothing, then switch to galactic view

	if (this.activeItem === undefined) {
		
		//double click at nothing, switch to active star, if there was one
		if(formerActiveItem != undefined && formerActiveItem.getObjectType() == OBJECT_TYPE.PLANET){
			this.activeItem = formerActiveItem.getStar();
			this.activeItem.activate();
			this.updateCameraAttentionOn(this.activeItem, true);
		}
		else{
			this.camera.position = this.cameraOrigin.clone();
			this.cameraAttention = this.scene.position;
			this.cameraAttentionPosition = undefined;
		}
	} else {
		this.updateCameraAttentionOn(this.activeItem, true);
	}
};

Three_Galaxy.prototype.updateCameraAttentionOn = function(galacticItem, itemClicked) {

	if (galacticItem === undefined) {
		return;
	}

	this.cameraAttention = galacticItem.getMesh().position;

	//var cameraDistance = this.activeItem.getSize() * 100 / 2;
	if(itemClicked){
		this.cameraDistance = galacticItem.getAttractionDistance();
	}

	this.cameraAttentionPosition = this.camera.position;
	this.cameraAttentionPosition.set(this.cameraAttention.x, this.cameraAttention.y - this.cameraDistance, this.cameraAttention.z + this.cameraDistance);

	this.camera.position = this.cameraAttentionPosition;
};

Three_Galaxy.prototype.render = function() {

	this.renderer.render(this.scene, this.camera);

};

Three_Galaxy.prototype.update = function() {

	for (var i in this.stars) {
		var star = this.stars[i];

		//location of a star
		var x1 = star.x;
		var y1 = star.y;
		var z1 = star.z;

		var x2 = this.camera.position.x;
		var y2 = this.camera.position.y;
		var z2 = this.camera.position.z;

		//sqrt((x1-x2)² + (y1-y2)² + (z1-z2)²)

		//var dist = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2) + Math.pow((z1 - z2), 2));
		var dist = star.getDistanceTo(this.camera.position);

		//TODO: Better: star.position.distanceTo(otherVector3D like this.camera.position)

		for (var i in star.planets) {
			var planet = star.planets[i];

			//position at ellipse
			planet.updatePos(planet.angle + 0.001);

			planet.planetSphere.position.x = planet.x;
			planet.planetSphere.position.y = planet.y;

			planet.planetSphere.rotation.y += 0.0050;
			if (planet.cloudSphere != undefined) {
				planet.cloudSphere.rotation.y += 0.0040;
				planet.cloudSphere.position.x = planet.x;
				planet.cloudSphere.position.y = planet.y;
			}

			if (dist > 2000) {
				planet.setVisible(false);
			} else {
				planet.setVisible(true);
			}

		}
	}

	if (this.cameraAttentionPosition != undefined && this.controls.getState() == -1) {

		this.updateCameraAttentionOn(this.activeItem, false);

		/*
		 var cameraDistance = 100; // default
		 if(this.activeItem != undefined){
		 cameraDistance = this.activeItem.getSize() * 100 / 2;
		 }

		 this.camera.position.set(this.cameraAttention.x, this.cameraAttention.y - cameraDistance, this.cameraAttention.z + cameraDistance);
		 */
	}

	this.controls.update(this.cameraAttention);
};

Three_Galaxy.prototype.getContainer = function() {
	return this.container;
};

Three_Galaxy.prototype.getScene = function() {
	return this.scene;
};

Three_Galaxy.prototype.getRenderer = function() {
	return this.renderer;
};

Three_Galaxy.prototype.getControls = function() {
	return this.controls;
};

