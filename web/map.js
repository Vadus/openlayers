var map = new OpenLayers.Map({
    div: "map",
    projection: "EPSG:900913",
    layers: [
        new OpenLayers.Layer.XYZ(
            "Imagery",
            [
				  "jaxrs/map/${z}/${x}/${y}"
            ],
            {
                transitionEffect: "resize"
            }
        )
    ],
    center: [0, 0],
    zoom: 1,
    numZoomLevels: 4//,
    //minExtent: new OpenLayers.Bounds(-1, -1, 1, 1)
});

map.addControl(new OpenLayers.Control.LayerSwitcher());