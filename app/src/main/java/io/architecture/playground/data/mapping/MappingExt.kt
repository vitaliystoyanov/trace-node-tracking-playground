package io.architecture.playground.data.mapping

import io.architecture.playground.data.local.LocalDiverTrace
import io.architecture.playground.data.remote.NetworkDiverTrace
import io.architecture.playground.model.DiverTrace

fun DiverTrace.toLocal() = LocalDiverTrace(
    id = id,
    ept = ept,
    eps = eps,
    epv = epv,
    lon = lon,
    time = time,
    epd = epd,
    epx = epx,
    speed = speed,
    alt = alt,
    epy = epy,
    track = track,
    lat = lat,
    mode = mode
)

fun LocalDiverTrace.toExternal() = DiverTrace(
    id = id,
    ept = ept,
    eps = eps,
    epv = epv,
    lon = lon,
    time = time,
    epd = epd,
    epx = epx,
    speed = speed,
    alt = alt,
    epy = epy,
    track = track,
    lat = lat,
    mode = mode
)

fun NetworkDiverTrace.toLocal() = LocalDiverTrace(
    id = id,
    ept = ept,
    eps = eps,
    epv = epv,
    lon = lon,
    time = time,
    epd = epd,
    epx = epx,
    speed = speed,
    alt = alt,
    epy = epy,
    track = track,
    lat = lat,
    mode = mode
)



fun LocalDiverTrace.toNetwork() = NetworkDiverTrace(
    id = id,
    ept = ept,
    eps = eps,
    epv = epv,
    lon = lon,
    time = time,
    epd = epd,
    epx = epx,
    speed = speed,
    alt = alt,
    epy = epy,
    track = track,
    lat = lat,
    mode = mode
)

fun DiverTrace.toNetwork() = toLocal().toNetwork()

fun NetworkDiverTrace.toExternal() = toLocal().toExternal()

fun List<LocalDiverTrace>.toNetwork() = map(LocalDiverTrace::toNetwork)

fun List<DiverTrace>.toLocal() = map(DiverTrace::toLocal)

@JvmName("localToExternal")
fun List<LocalDiverTrace>.toExternal() = map(LocalDiverTrace::toExternal)

@JvmName("networkToLocal")
fun List<NetworkDiverTrace>.toLocal() = map(NetworkDiverTrace::toLocal)

@JvmName("externalToNetwork")
fun List<DiverTrace>.toNetwork() = map(DiverTrace::toNetwork)

@JvmName("networkToExternal")
fun List<NetworkDiverTrace>.toExternal() = map(NetworkDiverTrace::toExternal)