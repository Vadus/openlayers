/*
function addPoint(features, index, posX, posY){
	
	features[index] = new OpenLayers.Feature.Vector(
	        new OpenLayers.Geometry.Point(posX, posY), {
	            //type: 5 + parseInt(5 * Math.random()),
	        	
	        	type: index,
	            foo: 100 * Math.random() | 0,
	            posX: posX,
	            posY: posY
	        }
	    );
}
*/
// Create 50 random features, and give them a "type" attribute that
// will be used for the label text.
//var features = new Array(5);
/*
var posX = 0;
var posY = 0;
//for (var i=0; i<features.length; i++) {
for (var i=0; i<50; i++) {
	posX = (360 * Math.random()) - 180;
	posY = (180 * Math.random()) - 90;
	
	addPoint(features, i, posX, posY);
}
*/

/* for default projection
addPoint(features, 0, 0, 0);
addPoint(features, 1, -180, 270);
addPoint(features, 2, -180, -90);
addPoint(features, 3, 180, 270);
addPoint(features, 4, 180, -90);
*/

/* for projection EPSG:900913*/
/*
addPoint(features, 0, 0, 0);
addPoint(features, 1, -20000000, -20000000);
addPoint(features, 2, -20000000, 20000000);
addPoint(features, 3, 20000000, 20000000);
addPoint(features, 4, 20000000, -20000000);
*/

/**
 * To execute ajax ( to load some more features at a certain zoom level,
 * try maybe this 
 * http://osgeo-org.1560.n6.nabble.com/Change-layer-on-max-zoom-level-td3918789.html
 */

/**
 * Create a style instance that is a collection of rules with symbolizers.
 * Use a default symbolizer to extend symoblizers for all rules.
 */
var style = new OpenLayers.Style({
    fillColor: "#ffcc66",
    strokeColor: "#ff9933",
    strokeWidth: 2,
    label: "${type}",
    fontColor: "#333333",
    fontFamily: "sans-serif",
    fontWeight: "bold"
}, {
    rules: [
        new OpenLayers.Rule({
            minScaleDenominator: 200000000,
            symbolizer: {
                pointRadius: 4,
                fontSize: "6px"
            }
        }),
        new OpenLayers.Rule({
            maxScaleDenominator: 200000000,
            minScaleDenominator: 100000000,
            symbolizer: {
                pointRadius: 8,
                fontSize: "12px"
            }
        }),
        new OpenLayers.Rule({
            maxScaleDenominator: 100000000,
            symbolizer: {
                pointRadius: 12,
                fontSize: "18px"
            }
        }),
        new OpenLayers.Rule({
            // a rule contains an optional filter
            filter: new OpenLayers.Filter.Comparison({
                type: OpenLayers.Filter.Comparison.LESS_THAN,
                property: "foo", // the "foo" feature attribute
                value: 25
            }),
            // if a feature matches the above filter, use this symbolizer
            symbolizer: {
            	fillColor: "white"
            }
        }),
        new OpenLayers.Rule({
            filter: new OpenLayers.Filter.Comparison({
                type: OpenLayers.Filter.Comparison.BETWEEN,
                property: "foo",
                lowerBoundary: 25,
                upperBoundary: 50
            }),
            symbolizer: {
            	fillColor: "green"
            }
        }),
        new OpenLayers.Rule({
            filter: new OpenLayers.Filter.Comparison({
                type: OpenLayers.Filter.Comparison.BETWEEN,
                property: "foo",
                lowerBoundary: 50,
                upperBoundary: 75
            }),
            symbolizer: {
            	fillColor: "blue"
            }
        })
    ]
});

// Create a vector layer and give it your style map.
var points = new OpenLayers.Layer.Vector("Points", {
    styleMap: new OpenLayers.StyleMap({
        "default": style,
        select: {
            fillColor: "red",
            pointRadius: 13,
            strokeColor: "yellow",
            strokeWidth: 3
        }
    }),
    //isBaseLayer: true, --> Points are not drawn with that
    renderers: ["Canvas"]
});
points.addFeatures(features);

var map = new OpenLayers.Map({
    div: "map",
    /*
    layers: [
        new OpenLayers.Layer.WMS(
            "OpenLayers WMS",
            "http://vmap0.tiles.osgeo.org/wms/vmap0",
            {layers: "basic"}
        ),
        points
    ],
    */
    projection: "EPSG:900913",
    layers: [
	new OpenLayers.Layer.XYZ(
	        "StarMap",
	        [
				  "jaxrs/map/1/${z}/${x}/${y}"
	        ],
	        {
	            transitionEffect: "resize"
	        }
	    ),
	    points
    ],
    controls: [ new OpenLayers.Control.LayerSwitcher(),
                new OpenLayers.Control.Navigation(),
                new OpenLayers.Control.MousePosition(),
                new OpenLayers.Control.Zoom()
    		  ],
    //center: new OpenLayers.LonLat(0, 0),
    center: [0, 0],
    zoom: 1
});

var selectControl = new OpenLayers.Control.SelectFeature(points);
map.addControl(selectControl);
selectControl.activate();
points.events.on({
    'featureselected': onFeatureSelect,
    'featureunselected': onFeatureUnselect
});

//Needed only for interaction, not for the display.
function onPopupClose(evt) {
    // 'this' is the popup.
    var feature = this.feature;
    if (feature.layer) { // The feature is not destroyed
    	selectControl.unselect(feature);
    } else { // After "moveend" or "refresh" events on POIs layer all 
             //     features have been destroyed by the Strategy.BBOX
        this.destroy();
    }
}
function onFeatureSelect(evt) {
    feature = evt.feature;
    popup = new OpenLayers.Popup.FramedCloud("featurePopup",
                             feature.geometry.getBounds().getCenterLonLat(),
                             new OpenLayers.Size(100,100),
                             "<h2>"+feature.attributes.foo + "</h2>"
                             + feature.attributes.posX + ","
                             + feature.attributes.posY,
                             null, true, onPopupClose);
    feature.popup = popup;
    popup.feature = feature;
    map.addPopup(popup, true);
}
function onFeatureUnselect(evt) {
    feature = evt.feature;
    if (feature.popup) {
        popup.feature = null;
        map.removePopup(feature.popup);
        feature.popup.destroy();
        feature.popup = null;
    }
}


