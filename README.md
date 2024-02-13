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

* Jetpack Compose
* Kotlin Channels & Flows APIs
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

## Design layered architecture
* The single source of truth principle: its database layer*
* Kotlin Coroutines and channels, flows as communication between arch layers: **websocket/database data sources** <-> **repository layer** <-> **data layer** <-> **reactive UI layer**
* The data and business layer expose suspend functions and Flows
* A model per layer: ViewModels include data layer models, repositories map DAO models to simpler data classes, a remote data source maps the model that it receives through the network to a simpler class
* ViewModels at screen level
* A single-activity application
* Follows Unidirectional Data Flow (UDF) principles
* The data layer exposes application data using a repository
* TBR

## Streaming WebSocket GPS trace nodes with Glitch

Simple Node.js back-end produces GPS trace node movements.


[Code project is publicly available on Glitch.com](https://glitch.com/edit/#!/websockets-diver)

Ready to use WebSocket endpoint:
```
wss://websockets-diver.glitch.me
```

```javascript
const { v1: uuidv1 } = require("uuid");
var turf = require("@turf/turf");
var turfRandom = require("@turf/random");

var WebSocket = require("ws");

var express = require("express");
var app = express();
var expressWs = require("express-ws")(app);

const NODE_SAMPLE_NUM = 1000;
const DEFAULT_SLEEP = 1000;

const BLACK_SEA_BBOX = [29.007088, 44.084696, 37.88404, 42.11154];
const SPEED_RANDOM_MIN = 10;
const SPEED_RANDOM_MAX = 50;

function randomIntFromInterval(min, max) {
  return Math.floor(Math.random() * (max - min + 1) + min);
}

function sleep(millis) {
  return new Promise((resolve) => setTimeout(resolve, millis));
}

var sampleNodes = [];
var activeNodeIds = [];

async function sendTraces(ws) {
  var lineStrings = turf.randomLineString(NODE_SAMPLE_NUM, {
    bbox: BLACK_SEA_BBOX, // Black Sea bbox
    max_length: 1.0,
    num_vertices: 2, // just to generate points beteween with turg.among() function later
  });

  // Prepare sample nodes
  for (let i = 0; i < lineStrings.features.length; i++) {
    var geometry = lineStrings.features[i].geometry;
    var speed = randomIntFromInterval(SPEED_RANDOM_MIN, SPEED_RANDOM_MAX);
    var bearing = turf.bearing(
      turf.point(geometry.coordinates[0]),
      turf.point(geometry.coordinates[1])
    );
    // Converts bearing angle from the north line direction (positive clockwise)
    // and returns an angle between 0-360 degrees (positive clockwise), 0 being the north line
    var angle = turf.bearingToAzimuth(bearing);

    sampleNodes[i] = {
      feature: lineStrings.features[i],
      from: geometry.coordinates[0],

      to: geometry.coordinates[1],
      nodeId: uuidv1().split("-")[0],
      speed: speed, // m/s
      steps: Math.round(
        Math.round(turf.length(geometry, { units: "kilometers" }) * 1000) /
          speed
      ),
      countStep: 0,
      // the angle measured in degrees from the north line (0 degrees)
      azimuth: angle, // initial bearing
      mode: 1, // 1 - moving, 0 not moving
    };
    activeNodeIds.push(sampleNodes[i].nodeId);
  }

  // Calculate all steps
  var allSteps = 0;
  sampleNodes.forEach((sample) => (allSteps = allSteps + sample.steps));
  console.log(`Prepared steps: ${allSteps}`);

  while (allSteps != 0) {
    if (ws.readyState === WebSocket.CLOSED) {
      console.log("Stop sending nodes...");
      break;
    }

    // Doing 1 step for all samples untill allSteps aren't reached
    await sampleNodes.forEach(async (sample) => {
      // Complete for indevidual samples
      if (sample.countStep != sample.steps) {
        var newCords = await moveNode(sample);
        sample.countStep += 1;
        sample.mode = 1;
        sample.time = Date.now();
        send(ws, sample, newCords);
      } else {
        sample.mode = 0; // change mode. node is not moving
        sample.time = Date.now();
        var newCords = await moveNode(sample);
        send(ws, sample, newCords);
      }

      allSteps -= 1;
    });
    await sleep(DEFAULT_SLEEP);
  }

  // Remove nodeIds from actives
  sampleNodes.forEach((sample) => {
    const index = activeNodeIds.indexOf(sample.nodeId);
    if (index > -1) {
      // only splice array when item is found
      activeNodeIds.splice(index, 1); // 2nd parameter means remove one item only
    }
  });

  ws.close();
}

async function moveNode(node) {
  var feature = await turf.along(
    node.feature,
    node.countStep * 0.001 * node.speed,
    {
      units: "kilometers",
    }
  );
  return feature.geometry.coordinates;
}

function send(ws, sample, coordinate, time) {
  ws.send(
    JSON.stringify({
      type: "trace",
      l: coordinate[0],
      lt: coordinate[1],
      s: sample.speed + randomIntFromInterval(1, 2), // noise speed
      az: sample.azimuth + randomIntFromInterval(1, 2), // noise,
      a: 0.1,
      t: sample.time, // sendAt millis, it's important
      n: sample.nodeId,
      m: sample.mode,
    })
  );
}

async function sendRoute(ws) {
  sampleNodes.forEach((sample) => {
    ws.send(
      JSON.stringify({
        type: "route",
        r: [sample.from, sample.to],
        n: sample.nodeId,
      })
    );
    sleep(1);
  });
}

app.ws("/nodes/ids", function (ws, req) {
  ws.send(JSON.stringify(activeNodeIds));
  ws.close();
});

app.ws("/nodes/routes", function (ws, req) {
  sendRoute(ws).then();
});

app.ws("/nodes/traces", function (ws, req) {
  ws.on("message", function (message) {
    var messageJson = JSON.parse(message);
    var payload = JSON.stringify({
      type: "rtt",
      clientSentTime: messageJson.time,
      serverTime: new Date().getTime(),
    });

    ws.send(payload);
  });

  sendTraces(ws).then();
});

app.listen(8080);
```
## Goals

To be a part of ... you know :)

