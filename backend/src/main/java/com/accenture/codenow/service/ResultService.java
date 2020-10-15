package com.accenture.codenow.service;

import com.accenture.codenow.domain.Result;
import com.accenture.codenow.repository.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Result}.
 *
 * @author  Boyko Boykov
 * @version 0.1
 * @since   2020-10-09
 */
@Service
@Transactional
public class ResultService {

    private final Logger log = LoggerFactory.getLogger(ResultService.class);

    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    /**
     * Save a result.
     *
     * @param result the entity to save.
     * @return the persisted entity.
     */
    public Result save(Result result) {
        log.debug("Request to save Result : {}", result);
        Result resultSaved = resultRepository.save(result);

        return resultSaved;
    }

    /**
     * Get all the results.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Result> findAll() {

        return resultRepository.findAll();
    }

    /**
     * Get all the results with pagination.
     *
     * @return the list of entities.
     */
    public Page<Result> findAll(Pageable pageable) {

        return resultRepository.findAll(pageable);
    }

    /**
     * Search the results with pagination.
     *
     * @return the list of entities.
     */
    public Page<Result> findAllByCountryAndCity(String country, String city, Pageable pageable) {

        return resultRepository.findAllByCountryAndCity(country, city, pageable);
    }

    /**
     * Get one result by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Result> findOne(Long id) {
        log.debug("Request to get Result : {}", id);
        return resultRepository.findById(id);
    }

    /**
     * Delete the result by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Result : {}", id);
        resultRepository.deleteById(id);
    }

}
