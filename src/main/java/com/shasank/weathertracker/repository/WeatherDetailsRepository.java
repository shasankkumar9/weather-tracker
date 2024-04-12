package com.shasank.weathertracker.repository;

import com.shasank.weathertracker.entity.WeatherDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherDetailsRepository extends JpaRepository<WeatherDetails, Long> {

    List<WeatherDetails> findByUserCoordinatesIdOrderByCreatedOnDesc(Long userCoordinatesId);

}
