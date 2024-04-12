package com.shasank.weathertracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCoordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitude;
    private Double longitude;
    private String city;
    private String country;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @OneToMany(mappedBy = "userCoordinates")
    @ToString.Exclude
    private Set<WeatherDetails> weatherDetails;

}
