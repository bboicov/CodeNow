package com.accenture.codenow.repository;

import com.accenture.codenow.domain.AveragingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface AveragingPeriodRepository extends JpaRepository<AveragingPeriod, Long> {
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    AveragingPeriod findByValueAndUnit(Float value, String unit);
}
