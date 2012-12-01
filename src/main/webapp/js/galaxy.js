function Galaxy (){

	/*****************************
	* VARIABLES
	*****************************/

	/*****************************
	* OBJECTS
	*****************************/
	var map;
	var imageLayer;
	
	/*****************************
	* PUBLIC FUNCTIONS
	*****************************/
};

Galaxy.prototype.init = function(mapDiv, mapId, maxZoom) {
	
	imageLayer = new OpenLayers.Layer.XYZ(
            "StarMap",
            [
				  "jaxrs/map/"+mapId+"/${z}/${x}/${y}"
            ],
            {
                transitionEffect: "resize"
            }
        );
	
	
	
	map = new OpenLayers.Map({
	    div: mapDiv,
	    projection: "EPSG:900913",
	    layers: [ imageLayer ],
	    controls: [ new OpenLayers.Control.LayerSwitcher(),
	                new OpenLayers.Control.Navigation(),
	                new OpenLayers.Control.MousePosition(),
	                new OpenLayers.Control.Zoom(),
	                new OpenLayers.Control.OverviewMap() 
	    		  ],
	    center: [0, 0],
	    zoom: 1,
	    numZoomLevels: maxZoom//,
	    //minExtent: new OpenLayers.Bounds(-1, -1, 1, 1)
	});
	
	var overview = new OpenLayers.Control.OverviewMap({
	    mapOptions: {
	        numZoomLevels: 1
	    }
	});
	map.addControl(overview);
};

Galaxy.prototype.update = function(){
	imageLayer.redraw(true);
};