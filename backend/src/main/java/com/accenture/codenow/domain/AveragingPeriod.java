package com.accenture.codenow.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * AveragingPeriod entity
 *
 * @author  Boyko Boykov
 * @version 0.1
 * @since   2020-10-09
 */
@Entity
@Table(name = "averaging_period")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AveragingPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Float value;

    @NotNull
    @Column(nullable = false)
    private String unit;

    public Float getValue() {
        return value;
    }

    public AveragingPeriod setValue(Float value) {
        this.value = value;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public AveragingPeriod setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public String toString() {
        return "AveragingPeriod{" +
            "id=" + id +
            ", value=" + value +
            ", unit='" + unit + '\'' +
            '}';
    }
}
