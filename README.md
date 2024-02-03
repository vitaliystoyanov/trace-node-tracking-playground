# GPS trace node tracking playground application ![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)

This is to demonstrate modern Android architecture, decomposition of UI, domain, data layers, and rendering GeoJSON source at runtime.

<img src="/docs/demo.gif" width="420">
<img src="/docs/demo1.gif" width="420">

## Tech stack

Nothing special :)

* Jetpack Compose
* Kotlin Flows API
* [MapBox SDK for Android](https://docs.mapbox.com/android/maps/guides/)
* [Mapbox Maps Compose Extension](https://github.com/mapbox/mapbox-maps-android/tree/extension-compose-v0.1.0/extension-compose)
* [Scarlet: A Retrofit inspired WebSocket client](https://github.com/Tinder/Scarlet)
  * [Scarlet coroutines stream adapter](https://github.com/Tinder/Scarlet/tree/main/scarlet-stream-adapter-coroutines)
* [Room database](https://developer.android.com/training/data-storage/room)
* [OkHttp](http://square.github.io/okhttp/)
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-jetpack)

## Functionality
* Opens WebSocket connection on app launch
* Stores trace nodes to Room database
* Reactive rendering GeoJSON source and a real-time trace node. A GeoJSON source is a collection of one or more geographic features, which may be points, lines and so on.
* Adds style layers to MapBox at runtime
* Supports retrieving traces in background
* Supports simple in-memory cache for traces 

## Design layered architecture
* The single source of truth principle: its database layer*
* Kotlin Coroutines and channels, flows as communication between arch layers: **websocket/database data sources** <-> **repository layer** <-> **data layer** <-> **reactive UI layer**
* The data and business layer expose suspend functions and Flows
* A model per layer: ViewModels include data layer models, repositories map DAO models to simpler data classes, a remote data source maps the model that it receives through the network to a simpler class
* ViewModels at screen level
* A single-activity application
* Follows Unidirectional Data Flow (UDF) principles
* The data layer exposes application data using a repository

## Streaming WebSocket GPS trace nodes with Glitch

Simple Node.js back-end produces GPS trace nodes from a raw file every 250-1000 milliseconds (randomly). On web socket connection starts to send the raw file line-by-line.


Here is a sample trace node:
```
{
   "ept":0.001,
   "eps":0.1,
   "epv":1,
   "lon":4.864389408103556,
   "time":"2015-09-24T11:22:18.147Z",
   "epd":0.1,
   "epx":1,
   "speed":9.367439184887354,
   "alt":0,
   "epy":1,
   "track":332.49117875064536,
   "class":"fbb17080-bfc0-11ee-bdfe-a58c0c9e65ee",
   "lat":52.16027760433122,
   "mode":3
}
```

'class' field is UIID per trace node


[Code project is publicly available on Glitch.com](https://glitch.com/edit/#!/websockets-diver)

Ready to use WebSocket endpoint:
```
wss://websockets-diver.glitch.me
```

## Improvements

* Paging for Room
* MemCache for traces
