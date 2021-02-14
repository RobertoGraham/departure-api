package io.github.robertograham.departureapi.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.apache.sis.geometry.DirectPosition2D;
import org.apache.sis.index.tree.QuadTreeData;

@Value
public class SimpleQuadTreeData implements QuadTreeData {

    @Getter(AccessLevel.NONE)
    DirectPosition2D directPosition2D;

    @lombok.Builder(builderClassName = "Builder", builderMethodName = "newBuilder")
    public SimpleQuadTreeData(final double longitude, final double latitude) {
        directPosition2D = new DirectPosition2D(longitude, latitude);
    }

    @Override
    public double getX() {
        return directPosition2D.getX();
    }

    @Override
    public double getY() {
        return directPosition2D.getY();
    }

    @Override
    public DirectPosition2D getLatLon() {
        return directPosition2D;
    }

    @Override
    public String getFileName() {
        return null;
    }
}
