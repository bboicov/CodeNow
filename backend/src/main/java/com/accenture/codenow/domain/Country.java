package com.accenture.codenow.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Country entity
 *
 * @author  Boyko Boykov
 * @version 0.1
 * @since   2020-10-09
 */
@Entity
@Table(name = "country")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length=2, updatable = false, nullable = false)
    @Size(min=2, max=2)
    private String code;

    @Column
    private Integer count;

    @Column
    private Integer locations;

    @Column
    private Integer cities;

    @Column(length=64)
    private String name;

    public String getCode() {
        return code;
    }

    public Country setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public Country setCount(Integer count) {
        this.count = count;
        return this;
    }

    public Integer getLocations() {
        return locations;
    }

    public Country setLocations(Integer locations) {
        this.locations = locations;
        return this;
    }

    public Integer getCities() {
        return cities;
    }

    public Country setCities(Integer cities) {
        this.cities = cities;
        return this;
    }

    public String getName() {
        return name;
    }

    public Country setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "CountryEntity{" +
            "code='" + code + '\'' +
            ", count=" + count +
            ", locations=" + locations +
            ", cities=" + cities +
            ", name='" + name + '\'' +
            '}';
    }
}
