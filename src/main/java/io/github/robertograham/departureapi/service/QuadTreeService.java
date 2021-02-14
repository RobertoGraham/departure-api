package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.dto.Type;
import io.github.robertograham.departureapi.model.SimpleQuadTreeData;
import io.github.robertograham.departureapi.response.BusStop;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.sis.geometry.DirectPosition2D;
import org.apache.sis.index.tree.QuadTree;
import org.apache.sis.referencing.CRS;
import org.apache.sis.referencing.CommonCRS;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuadTreeService {

    private static final CoordinateOperation COORDINATE_OPERATION;

    static {
        try {
            COORDINATE_OPERATION = CRS.findOperation(CommonCRS.WGS84.normalizedGeographic(), CommonCRS.SPHERE.geographic(), null);
        } catch (final FactoryException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private final TransportApiClient transportApiClient;

    private final QuadTree quadTree = new QuadTree(5, Integer.MAX_VALUE);

    private static Optional<SimpleQuadTreeData> createSimpleQuadTreeData(final BusStop busStop) {
        try {
            final var directPosition = COORDINATE_OPERATION.getMathTransform().transform(new DirectPosition2D(busStop.getLongitude().doubleValue(), busStop.getLatitude().doubleValue()), null);
            return Optional.of(SimpleQuadTreeData.newBuilder()
                .longitude(directPosition.getOrdinate(1))
                .latitude(directPosition.getOrdinate(0))
                .build());
        } catch (final TransformException exception) {
            return Optional.empty();
        }
    }

    @SneakyThrows
    public List<BusStop> getNearbyBusStops(final BigDecimal longitude, final BigDecimal latitude) {
        final var busStops = transportApiClient.places(latitude, longitude, null, null, null, null, null, Type.BUS_STOP)
            .getMembers().stream()
            .filter(Objects::nonNull)
            .filter((final var member) -> Type.BUS_STOP == member.getType())
            .map(BusStopHelper::createBusStop)
            .collect(Collectors.toList());
        busStops.stream()
            .map(QuadTreeService::createSimpleQuadTreeData)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(quadTree::insert);
        final var directPosition = COORDINATE_OPERATION.getMathTransform().transform(new DirectPosition2D(longitude.doubleValue(), latitude.doubleValue()), null);
        final var quadTreeData = quadTree.queryByPointRadius(new DirectPosition2D(directPosition.getOrdinate(1), directPosition.getOrdinate(0)), 400);
        System.out.println(quadTreeData.size());
        return busStops;
    }
}
