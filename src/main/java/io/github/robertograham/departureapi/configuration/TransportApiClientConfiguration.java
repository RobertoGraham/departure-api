package io.github.robertograham.departureapi.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.dto.Bearing;
import io.github.robertograham.departureapi.client.dto.BusRouteResponse;
import io.github.robertograham.departureapi.client.dto.BusServiceResponse;
import io.github.robertograham.departureapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.departureapi.client.dto.Group;
import io.github.robertograham.departureapi.client.dto.NextBuses;
import io.github.robertograham.departureapi.client.dto.PlacesResponse;
import io.github.robertograham.departureapi.client.dto.Stops;
import io.github.robertograham.departureapi.client.dto.Type;
import io.github.robertograham.departureapi.client.dto.TypeSetContainer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@RegisterReflectionForBinding({Bearing.class, BusRouteResponse.class, BusRouteResponse.Stop.class,
    BusRouteResponse.Stop.Next.class, BusServiceResponse.class, BusServiceResponse.Operator.class,
    BusServiceResponse.Direction.class, BusServiceResponse.Direction.Destination.class,
    BusServiceResponse.Centroid.class, BusStopDeparturesResponse.class,
    BusStopDeparturesResponse.Location.class, BusStopDeparturesResponse.Departure.class,
    PlacesResponse.class, PlacesResponse.Member.class, Type.class})
class TransportApiClientConfiguration {

    @Bean
    TransportApiClient transportApiClient(@Value("${transportApiClient.url}") final String transportApiClientUrl,
                                          @Value("${transportApiClient.applicationId}") final String applicationId,
                                          @Value("${transportApiClient.applicationKey}") final String applicationKey,
                                          final ObjectMapper objectMapper) {
        final var conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(Group.class, String.class, Group::getValue);
        conversionService.addConverter(NextBuses.class, String.class, NextBuses::getValue);
        conversionService.addConverter(Stops.class, String.class, Stops::getValue);
        conversionService.addConverter(TypeSetContainer.class, String.class, typeSetContainer -> typeSetContainer.types()
            .stream()
            .filter(Objects::nonNull)
            .map(type -> {
                try {
                    return objectMapper.writeValueAsString(type);
                } catch (final JsonProcessingException exception) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .map(typeString -> StringUtils.unwrap(typeString, '"'))
            .collect(Collectors.joining(",")));
        conversionService.addConverter(LocalDate.class, String.class, LocalDate::toString);
        conversionService.addConverter(LocalTime.class, String.class, localTime -> localTime.truncatedTo(ChronoUnit.MINUTES)
            .toString());
        return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(WebClient.builder()
                .uriBuilderFactory(new DefaultUriBuilderFactory(UriComponentsBuilder.fromUriString(transportApiClientUrl)
                    .queryParam("app_id", applicationId)
                    .queryParam("app_key", applicationKey)))
                .build()))
            .conversionService(conversionService)
            .build()
            .createClient(TransportApiClient.class);
    }
}
