package com.accenture.codenow.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Measurement entity
 *
 * @author  Boyko Boykov
 * @version 0.1
 * @since   2020-10-09
 */
@Entity
@Table(name = "measurement")
public class Measurement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Size(min=2, max=8)
    private String parameter;

    @NotNull
    @Column(nullable = false)
    private Float value;

    private Date lastUpdated;

    @NotNull
    @Column(nullable = false)
    private String unit;

    @Column
    private String sourceName;

    @ManyToOne
    private AveragingPeriod averagingPeriod;

    public String getParameter() {
        return parameter;
    }

    public Measurement setParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    public Float getValue() {
        return value;
    }

    public Measurement setValue(Float value) {
        this.value = value;
        return this;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Measurement setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public Measurement setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String getSourceName() {
        return sourceName;
    }

    public Measurement setSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public AveragingPeriod getAveragingPeriod() { return averagingPeriod; }

    public Measurement setAveragingPeriod(AveragingPeriod averagingPeriod) {
        this.averagingPeriod = averagingPeriod;
        return this;
    }

    @Override
    public String toString() {
        return "Measurement{" +
            "id=" + id +
            ", parameter='" + parameter + '\'' +
            ", value=" + value +
            ", lastUpdated=" + lastUpdated +
            ", unit='" + unit + '\'' +
            ", sourceName='" + sourceName + '\'' +
            ", averagingPeriod=" + averagingPeriod +
            '}';
    }
}
