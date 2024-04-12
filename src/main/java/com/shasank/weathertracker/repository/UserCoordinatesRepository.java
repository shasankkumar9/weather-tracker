package com.shasank.weathertracker.repository;

import com.shasank.weathertracker.entity.UserCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCoordinatesRepository extends JpaRepository<UserCoordinates, Long> {
    Optional<UserCoordinates> findByLatitudeAndLongitude(Double latitude, Double longitude);
}
