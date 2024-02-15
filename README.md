# PoC of design implementation for processing large GPS node trace data effectively ![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)


The application processes GPS node trace data using the Scarlet websocket client, deserializes it using Gson and writes the data to a local Room database for further data rendering based on Jetpack Compose and MapBox SDK built-in capabilities.
On the other side, the Node.js backend generates sample data using the turf.js library. For each node, the route, direction and speed are generated at runtime.

Some considerations:
* The application processes 10 thousand websocket messages and then renders efficiently. The websocket message format is a string, but it is better to implement a binary message format. These nodes are dynamic moving, which is CPU and GPU-intensive to render and process highly frequently updated node movements. (However, there is a slight stuttering in UI rendering frames due to MapBox rendering on the native C++ side). All buffers have been disabled in the MapBox configuration.
* At each layer of the architecture: db entity, external and network models have mappers for each other. Used object pool design pattern to avoid intensive allocation/deallocation of Java/Kotlin objects.

<img src="/docs/demo3.gif" width="520">


<img src="/docs/demo1.gif" width="520">

## Tech stack

Nothing special :)

* Kotlin Channels & Flows APIs
* (experimental) Kotlin multiplatform / multi-format reflectionless serialization 
  * [Kotlin Serialization ProtoBuf](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/formats.md#protobuf-experimental)
* Jetpack Compose with Material3 design
* [MapBox SDK for Android](https://docs.mapbox.com/android/maps/guides/)
* [Mapbox Maps Compose Extension](https://github.com/mapbox/mapbox-maps-android/tree/extension-compose-v0.1.0/extension-compose)
* [Scarlet: A Retrofit inspired WebSocket client](https://github.com/Tinder/Scarlet)
  * [Scarlet coroutines stream adapter](https://github.com/Tinder/Scarlet/tree/main/scarlet-stream-adapter-coroutines)
* [Room database](https://developer.android.com/training/data-storage/room)
* [OkHttp](http://square.github.io/okhttp/)
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-jetpack)

## Functionality
* Supports websocket connections in background
* Stores trace nodes to in-memory Room database
* Reactive rendering GeoJSON features from in-memory GeoJSON source. A GeoJSON source is a collection of one or more geographic features, which may be points, lines and so on.
* Data-driven map layer styling. Mapbox’s data-driven styling features allow to use attributes in the data to style maps. The app can style map features automatically based on their individual attributes.
* Allocates memory for processing node traces at runtime using Pool Object manager

## Design architecture
<img src="/docs/design.drawio.png" alt="Design architecture diagram" title="Design diagram">

* The single source of truth principle: its database layer*
* Kotlin Coroutines and channels, flows as communication between arch layers: **websocket/database data sources** <-> **repository layer** <-> **data layer** <-> **reactive UI layer**
* The data and business layer expose suspend functions and Flows
* A model per layer: ViewModels include data layer models, repositories map DAO models to simpler data classes, a remote data source maps the model that it receives through the network to a simpler class
* ViewModels at screen level
* A single-activity application
* Follows Unidirectional Data Flow (UDF) principles
* The data layer exposes application data using a repository
* TBR

## Streaming node traces with Ktor

TBR

## Goals

To be a part of ... you know :)

