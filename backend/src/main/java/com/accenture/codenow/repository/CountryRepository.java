package com.accenture.codenow.repository;

import com.accenture.codenow.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

}
