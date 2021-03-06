package com.accenture.codenow.service;

import com.accenture.codenow.domain.Country;
import com.accenture.codenow.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Country}.
 */
@Service
@Transactional
public class CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryService.class);

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /**
     * Save a country.
     *
     * @param country the entity to save.
     * @return the persisted entity.
     */
    public Country save(Country country) {
        log.debug("Request to save Country : {}", country);
        Country result = countryRepository.save(country);

        return result;
    }

    /**
     * Get all the countries.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Country> findAll() {

        return countryRepository.findAll();
    }

    /**
     * Get all the countries with pagination.
     *
     * @return the list of entities.
     */
    public Page<Country> findAll(Pageable pageable) {

        return countryRepository.findAll(pageable);
    }

    /**
     * Get one country by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Country> findOne(String id) {
        log.debug("Request to get Country : {}", id);
        return countryRepository.findById(id);
    }

    /**
     * Delete the country by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Country : {}", id);
        countryRepository.deleteById(id);
    }

}
