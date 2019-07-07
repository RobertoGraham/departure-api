package io.github.robertograham.busapi.factory;

import io.github.robertograham.busapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.busapi.client.dto.PlacesResponse;
import io.github.robertograham.busapi.client.dto.Type;
import io.github.robertograham.busapi.dto.BusStop;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class BusStopFactory {

    public List<BusStop> createBusStopList(final PlacesResponse placesResponse) {
        return createBusStopList(placesResponse.getMember());
    }

    public BusStop createBusStop(final BusStopDeparturesResponse busStopDeparturesResponse) {
        final StringBuilder nameBuilder = new StringBuilder(busStopDeparturesResponse.getStopName());
        if (!StringUtils.isEmpty(busStopDeparturesResponse.getBearing()))
            nameBuilder.append(" - ")
                    .append(busStopDeparturesResponse.getBearing())
                    .append("-bound");
        final BusStop.Builder busStopBuilder = BusStop.newBuilder()
                .id(busStopDeparturesResponse.getAtcoCode())
                .name(nameBuilder.toString())
                .locality(busStopDeparturesResponse.getLocality());
        final List<BigDecimal> coordinates = busStopDeparturesResponse.getLocation().getCoordinates();
        if (coordinates.size() == 2)
            return busStopBuilder.longitude(coordinates.get(0))
                    .latitude(coordinates.get(1))
                    .build();
        return busStopBuilder.build();
    }

    private List<BusStop> createBusStopList(final List<PlacesResponse.Member> members) {
        return members.stream()
                .filter((final PlacesResponse.Member member) -> Type.BUS_STOP.getValue().equals(member.getType()))
                .map(this::createBusStop)
                .collect(Collectors.toList());
    }

    private BusStop createBusStop(final PlacesResponse.Member member) {
        return BusStop.newBuilder()
                .id(member.getAtcoCode())
                .name(member.getName())
                .locality(member.getDescription())
                .latitude(member.getLatitude())
                .longitude(member.getLongitude())
                .build();
    }
}
