package com.accenture.codenow.controller;

import com.accenture.codenow.domain.Country;
import com.accenture.codenow.domain.Result;
import com.accenture.codenow.service.CountryService;
import com.accenture.codenow.service.ResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * AirQualityApi controller provides main application endpoints.
 *
 * @author  Boyko Boykov
 * @version 0.1
 * @since   2020-10-09
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(allowedHeaders = "*")  // Dev only
public class AirQualityApi {
    private final Logger log = LoggerFactory.getLogger(AirQualityApi.class);

    private static final String ENTITY_NAME = "result";

    private final CountryService countryService;
    private final ResultService resultService;

    public AirQualityApi(CountryService countryService,
                         ResultService resultService) {
        this.countryService = countryService;
        this.resultService = resultService;
    }

    /**
     * {@code POST  /results} : Create a new result.
     *
     * @param result the result to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new result, or with status {@code 400 (Bad Request)} if the result has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/results")
    public ResponseEntity<Result> createResult(@Valid @RequestBody Result result) throws URISyntaxException {
        log.debug("REST request to save Result : {}", result);
        if (result.getId() != null) {
            log.error("A new result cannot already have an ID");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (countryService != null) {
            Optional<Country> countryOptional = countryService.findOne(result.getCountry().getCode());
            if (countryOptional.isEmpty()) {
                Country country = countryService.save(result.getCountry());
                result.setCountry(country);
            }
        }
        Result resultSaved = resultService.save(result);

        return ResponseEntity
            .created(new URI("/api/results/" + resultSaved.getId()))
            .body(result);
    }

    /**
     * {@code GET  /results} : get all results.
     */
    @GetMapping("/results")
    public List<Result> getAllResults() {
        log.debug("REST request to get all Results");
        return resultService.findAll();
    }

    /**
     * {@code GET  /results/:id} : get the "id" result.
     *
     * @param id the id of the result to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the result, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/results/{id}")
    public ResponseEntity<Result> getResult(@PathVariable Long id) {
        log.debug("REST request to get Result : {}", id);
        Optional<Result> resultOptional = resultService.findOne(id);
        if (!resultOptional.isEmpty()) {
            return new ResponseEntity(resultOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * {@code GET  /results} : get all results - pageable and sorted.
     */
    @GetMapping("/results/pageable")
    public Page<Result> getResultsPageable(@NotNull Pageable pageable) {
        log.debug("REST request to get all Results - pageable and sorted");
        return resultService.findAll(pageable);
    }

    /**
     * {@code GET  /results} : search results - pageable and sorted.
     */
    @GetMapping("/results/search")
    public Page<Result> getResultsPageable(@RequestParam String country,
                                           @RequestParam String city,
                                           Pageable pageable) {
        log.debug("REST request to get all Results - pageable and sorted");
        return resultService.findAllByCountryAndCity(country, city, pageable);
    }
}
