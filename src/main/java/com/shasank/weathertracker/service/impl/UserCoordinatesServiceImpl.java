package com.shasank.weathertracker.service.impl;

import com.shasank.weathertracker.entity.UserCoordinates;
import com.shasank.weathertracker.repository.UserCoordinatesRepository;
import com.shasank.weathertracker.service.UserCoordinatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class UserCoordinatesServiceImpl implements UserCoordinatesService {

    @Autowired
    private UserCoordinatesRepository userCoordinatesRepository;

    @Override
    public ResponseEntity<String> saveUserCoordinates(UserCoordinates userCoordinates) {
        userCoordinatesRepository.save(userCoordinates);
        return ResponseEntity.ok("UserCoordinates saved");
    }
}
