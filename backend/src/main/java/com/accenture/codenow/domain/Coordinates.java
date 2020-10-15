package com.accenture.codenow.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Coordinates entity
 *
 * @author  Boyko Boykov
 * @version 0.1
 * @since   2020-10-09
 */
@Entity
@Table(name = "coordinates")
public class Coordinates implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Float latitude;

    @NotNull
    @Column(nullable = false)
    private Float longitude;

    public Float getLatitude() {
        return latitude;
    }

    public Coordinates setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Coordinates setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
            "id=" + id +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            '}';
    }
}
