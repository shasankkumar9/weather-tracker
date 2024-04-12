package com.shasank.weathertracker.service;

import com.shasank.weathertracker.entity.WeatherDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WeatherDetailsService {

    ResponseEntity<WeatherDetails> getWeatherDetailsByLatitudeAndLongitude(Double latitude, Double longitude);

    ResponseEntity<List<WeatherDetails>> getPreviousWeatherDetailsByUserCoordinatesId(Long id);
}
