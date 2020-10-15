package com.accenture.codenow.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Result entity
 *
 * @author  Boyko Boykov
 * @version 0.1
 * @since   2020-10-09
 */
@Entity
@Table(name = "result")
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private String location;

    @NotNull
    @Column
    private String city;

    @NotNull
    @OneToOne
    @JoinColumn(name = "code_id", referencedColumnName = "code")
    private Country country;

    @Column
    private Float distance;

    @NotNull
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id")
    private Set<Measurement> measurements;

    @OneToOne(cascade = CascadeType.ALL)
    private Coordinates coordinates;

    public Long getId() {
        return id;
    }

    public Result setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Result setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Result setCity(String city) {
        this.city = city;
        return this;
    }

    public Country getCountry() {
        return country;
    }

    public Result setCountry(Country country) {
        this.country = country;
        return this;
    }

    public Float getDistance() {
        return distance;
    }

    public Result setDistance(Float distance) {
        this.distance = distance;
        return this;
    }

    public Set<Measurement> getMeasurements() {
        return measurements;
    }

    public Result setMeasurements(Set<Measurement> measurements) {
        this.measurements = measurements;
        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Result setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(id, result.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Result{" +
            "id=" + id +
            ", location='" + location + '\'' +
            ", city='" + city + '\'' +
            ", country=" + country +
            ", distance=" + distance +
            ", measurements=" + measurements +
            ", coordinates=" + coordinates +
            '}';
    }
}
