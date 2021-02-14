package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.model.SimpleQuadTreeData;
import lombok.SneakyThrows;
import org.apache.sis.referencing.CommonCRS;
import org.apache.sis.referencing.gazetteer.GazetteerException;
import org.apache.sis.referencing.gazetteer.GeohashReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Service;

@Service
final class GeohashServiceImpl implements GeohashService {

    @Override
    @SneakyThrows(TransformException.class)
    public String resolveGeohash(SimpleQuadTreeData simpleQuadTreeData) {
        return GeohashReferenceSystemContainer.INSTANCE.newCoder().encode(simpleQuadTreeData.getLatLon());
    }

    private enum GeohashReferenceSystemContainer {

        INSTANCE;

        private final GeohashReferenceSystem geohashReferenceSystem;

        GeohashReferenceSystemContainer() {
            try {
                geohashReferenceSystem = new GeohashReferenceSystem(GeohashReferenceSystem.Format.BASE32, CommonCRS.defaultGeographic());
            } catch (final GazetteerException exception) {
                throw new IllegalStateException(exception);
            }
        }

        public GeohashReferenceSystem.Coder newCoder() {
            return geohashReferenceSystem.createCoder();
        }
    }
}
