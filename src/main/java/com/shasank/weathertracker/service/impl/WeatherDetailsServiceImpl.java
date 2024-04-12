package com.shasank.weathertracker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shasank.weathertracker.classes.WeatherResponse;
import com.shasank.weathertracker.entity.UserCoordinates;
import com.shasank.weathertracker.entity.WeatherDetails;
import com.shasank.weathertracker.repository.UserCoordinatesRepository;
import com.shasank.weathertracker.repository.WeatherDetailsRepository;
import com.shasank.weathertracker.service.WeatherDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeatherDetailsServiceImpl implements WeatherDetailsService {

    @Value("${weather.api.base-url}")
    private String baseUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private WeatherDetailsRepository weatherDetailsRepository;

    @Autowired
    private UserCoordinatesRepository userCoordinatesRepository;

    @Override
    public ResponseEntity<WeatherDetails> getWeatherDetailsByLatitudeAndLongitude(Double latitude, Double longitude) {

        WeatherResponse weatherResponse = null;

        String url =
                baseUrl + "?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=metric";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        log.info("Response: {}", response);

        // Parse JSON data
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            weatherResponse = objectMapper.readValue(response, WeatherResponse.class);
        } catch (Exception e) {
            log.error("Error reading JSON data: {}", e.getMessage());
        }

        if (weatherResponse == null) return ResponseEntity.internalServerError().build();

        final String city = weatherResponse.getName();
        final String country = weatherResponse.getSys().getCountry();

        // Save UserCoordinates if not already present
        UserCoordinates userCoordinates = userCoordinatesRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElseGet(() -> {
                    UserCoordinates newUserCoordinates =
                            UserCoordinates.builder()
                                    .latitude(latitude)
                                    .longitude(longitude)
                                    .city(city)
                                    .country(country)
                                    .build();
                    return userCoordinatesRepository.save(newUserCoordinates);
                });

        Double rain = ObjectUtils.isNotEmpty(weatherResponse.getRain()) ? weatherResponse.getRain().get1h() : null;
        // Create and save weather detail
        WeatherDetails weatherDetails =
                WeatherDetails.builder()
                        .icon(weatherResponse.getWeather().get(0).getIcon())
                        .description(weatherResponse.getWeather().get(0).getDescription())
                        .temperature(weatherResponse.getMain().getTemp())
                        .rainfall(rain)
                        .humidity(weatherResponse.getMain().getHumidity())
                        .windSpeed(weatherResponse.getWind().getSpeed())
                        .windDirection(weatherResponse.getWind().getDeg())
                        .cloudCoverage(weatherResponse.getClouds().getAll())
                        .pressure(weatherResponse.getMain().getPressure())
                        .userCoordinates(userCoordinates)
                        .build();

        WeatherDetails savedWeatherDetails = weatherDetailsRepository.save(weatherDetails);

        return ResponseEntity.ok(savedWeatherDetails);
    }

    @Override
    public ResponseEntity<List<WeatherDetails>> getPreviousWeatherDetailsByUserCoordinatesId(Long id) {

        final int TIME_GAP_IN_MINUTES = 15;

        List<WeatherDetails> weatherDetails = weatherDetailsRepository.findByUserCoordinatesIdOrderByCreatedOnDesc(id);
        List<WeatherDetails> finalDetails = weatherDetails.stream()
                .collect(Collectors.groupingBy(detail -> detail.getCreatedOn().truncatedTo(ChronoUnit.MINUTES).minusMinutes(detail.getCreatedOn().getMinute() % TIME_GAP_IN_MINUTES))) // Group by x-minute intervals
                .values().stream()
                .map(group -> group.stream().min((d1, d2) -> d1.getCreatedOn().compareTo(d2.getCreatedOn()))) // Find the earliest record in each group
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        log.info("finalDetails: {}", finalDetails);
        return ResponseEntity.ok(finalDetails);
    }

}
