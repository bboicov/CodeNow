package com.accenture.codenow.repository;

import com.accenture.codenow.domain.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Page<Result> findAllByCountryAndCity(String country, String city, Pageable pageable);
}
