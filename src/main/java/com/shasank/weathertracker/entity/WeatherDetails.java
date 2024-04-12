package com.shasank.weathertracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String icon;
    private String description;
    private Double temperature;
    private Double rainfall;
    private Integer humidity;
    private Double windSpeed;
    private Integer windDirection;
    private Integer cloudCoverage;
    private Integer pressure;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "user_coordinates_id_fk", nullable = false)
    private UserCoordinates userCoordinates;

}
