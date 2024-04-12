package com.shasank.weathertracker.controller;

import com.shasank.weathertracker.entity.WeatherDetails;
import com.shasank.weathertracker.service.WeatherDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather_details")
@CrossOrigin("*")
public class WeatherDetailsController {

    @Autowired
    private WeatherDetailsService weatherDetailsService;

    @GetMapping
    public ResponseEntity<WeatherDetails> getWeatherDetailsByLatitudeAndLongitude(@RequestParam("latitude") Double latitude,
                                                                                  @RequestParam("longitude") Double longitude) {
        return weatherDetailsService.getWeatherDetailsByLatitudeAndLongitude(latitude, longitude);
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<WeatherDetails>> getAllPreviousWeatherDetails(@PathVariable(value = "id") Long userCoordinatesId) {
        return weatherDetailsService.getPreviousWeatherDetailsByUserCoordinatesId(userCoordinatesId);
    }

}
