# compose-mapbox-library (based on https://github.com/dellisd/compose-web-mapbox)

A Compose for Web wrapper of Mapbox.

## Usage

To create a map, call `rememberMapboxState()` to obtain a map state object that you can use to
programmatically interact with the map. Then call the `MapboxMap()` composable and pass it your
Mapbox access token and a map style URL at a minimum.

```kotlin
val mapState = rememberMapboxState()

MapboxMap(
    accessToken = MAPBOX_ACCESS_TOKEN,
    style = "mapbox://styles/mapbox/dark-v10",
    state = mapState,
) {
    // Place your sources and layers here
}
```

### Adding Sources

Data sources are added using Composable methods in the `sources` block of the `MapboxMap` function.
Currently, only GeoJSON sources are implemented.

```kotlin
import geojson.GeoJsonObject

MapboxMap(/* ... */) {
    GeoJsonSource(id = "test", data = data.unsafeCast<GeoJsonObject>()) {
        // Layers for this data source go here
    }
}
```

### Adding Layers

Layers are added within the scope of a data source. The layer is automatically set up to pull data
from that source.
Paint and layout properties can be applied to the layer.

```kotlin
MapboxMap(/* ... */) {
    GeoJsonSource(/* ... */) {
        CircleLayer(id = "circles") {
            circleColor("#FF0000")
            circleRadius(10)
        }
    }
}
```

All the source, layer, and paint/layout property functions are composable functions meaning that
their properties
can be updated like any other composable function.

```kotlin
var size by remember { mutableStateOf(1) }

MapboxMap(/* ... */) {
    GeoJsonSource(/* ... */) {
        CircleLayer(id = "circles") {
            circleColor("#FF0000")
            // The circle radius will automatically update on the map
            circleRadius(size)
        }
    }
}
Button(attrs = {
    onClick = { size++ }
}) {
    Text("Increase Size")
}
```