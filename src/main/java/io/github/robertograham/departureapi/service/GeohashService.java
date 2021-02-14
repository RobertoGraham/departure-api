package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.model.SimpleQuadTreeData;

public interface GeohashService {

    String resolveGeohash(SimpleQuadTreeData simpleQuadTreeData);
}
