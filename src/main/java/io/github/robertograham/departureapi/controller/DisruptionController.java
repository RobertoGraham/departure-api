package io.github.robertograham.departureapi.controller;

import io.github.robertograham.departureapi.service.DisruptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.org.siri.siri20.Siri;

@RestController
@RequestMapping("/disruptions")
@RequiredArgsConstructor
final class DisruptionController {

    private final DisruptionService disruptionService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Siri getDisruptions() {
        return disruptionService.getDisruptions();
    }
}
