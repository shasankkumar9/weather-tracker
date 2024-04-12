package com.shasank.weathertracker.service;

import com.shasank.weathertracker.entity.UserCoordinates;
import org.springframework.http.ResponseEntity;

public interface UserCoordinatesService {

    ResponseEntity<String> saveUserCoordinates(UserCoordinates userCoordinates);
}
