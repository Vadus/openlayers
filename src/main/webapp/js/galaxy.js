var galaxy = new function (){

	/*****************************
	* VARIABLES
	*****************************/

	/*****************************
	* OBJECTS
	*****************************/
	var map;
	var mapImages;
	
	/*****************************
	* PUBLIC FUNCTIONS
	*****************************/
	this.init = function(mapId, mapDiv) {
		
		mapImages = new OpenLayers.Layer.XYZ(
	            "Imagery",
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
		    layers: [ mapImages ],
		    center: [0, 0],
		    zoom: 1,
		    numZoomLevels: 4//,
		    //minExtent: new OpenLayers.Bounds(-1, -1, 1, 1)
		});
		
		map.addControl(new OpenLayers.Control.LayerSwitcher());
	};
	
	this.update = function(){
		mapImages.redraw(true);
	};
};