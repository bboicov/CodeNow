package com.accenture.codenow.controller;

import com.accenture.codenow.ChallengeApplication;
import com.accenture.codenow.domain.AveragingPeriod;
import com.accenture.codenow.domain.Country;
import com.accenture.codenow.domain.Measurement;
import com.accenture.codenow.domain.Result;
import com.accenture.codenow.repository.CountryRepository;
import com.accenture.codenow.repository.ResultRepository;
import com.accenture.codenow.service.CountryService;
import com.accenture.codenow.service.DataFetcherService;
import com.accenture.codenow.service.ResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link AirQualityApi} REST controller.
 */
@SpringBootTest(classes = ChallengeApplication.class)
@AutoConfigureMockMvc
public class AirQualityApiTests {

    private static final String DEFAULT_PARAMETER = "pm25";
    private static final String UPDATED_PARAMETER = "pm25_updated";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AA";
    private static final String UPDATED_CODE = "BB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ResultService resultService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResultMockMvc;

    private Country country;

    private Result result;

    @MockBean
    DataFetcherService dataFetcherService;

    /**
     * Create an entities for this test.
     */
    public static AveragingPeriod createAveragingPeriodEntity(EntityManager em) {
        AveragingPeriod averagingPeriod = new AveragingPeriod()
            .setUnit(DEFAULT_UNIT)
            .setValue(1.0F);
        return averagingPeriod;
    }

    public static Measurement createMeasurementEntity(EntityManager em, AveragingPeriod averagingPeriod) {
        Measurement measurement = new Measurement()
            .setParameter(DEFAULT_PARAMETER)
            .setUnit(DEFAULT_UNIT)
            .setValue(1.0F)
            .setAveragingPeriod(averagingPeriod);

        return measurement;
    }

    public static Country createCountryEntity(EntityManager em) {
        Country country = new Country()
            .setCode(DEFAULT_CODE)
            .setName(DEFAULT_COUNTRY);
        return country;
    }

    public static Result createEntity(EntityManager em,
                                      Country country,
                                      Measurement measurement) {
        Set<Measurement> measurements = new HashSet<>();
        measurements.add(measurement);

        Result result = new Result()
            .setCity(DEFAULT_CITY)
            .setLocation(DEFAULT_LOCATION)
            .setCountry(country)
            .setMeasurements(measurements);

        return result;
    }

    @BeforeEach
    public void initTest() {
        AveragingPeriod averagingPeriod = createAveragingPeriodEntity(em);
        Measurement measurement = createMeasurementEntity(em, null);
        country = createCountryEntity(em);
        result = createEntity(em, country, measurement);
    }

    @Test
    @Transactional
    public void createResult() throws Exception {
        int databaseSizeBeforeCreate = resultRepository.findAll().size();

        // Create the Result
        restResultMockMvc.perform(post("/api/results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(result)))
            .andExpect(status().isCreated());

        // Validate the Result in the database
        List<Result> resultList = resultRepository.findAll();
        assertThat(resultList).hasSize(databaseSizeBeforeCreate + 1);
        Result testResult = resultList.get(resultList.size() - 1);
        assertThat(testResult.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testResult.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testResult.getCountry().getName()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    public void createResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resultRepository.findAll().size();

        // Create the Result with an existing ID
        result.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultMockMvc.perform(post("/api/results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(result)))
            .andExpect(status().isBadRequest());

        // Validate the Result in the database
        List<Result> resultList = resultRepository.findAll();
        assertThat(resultList).hasSize(databaseSizeBeforeCreate);

    }


    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = resultRepository.findAll().size();
        // set the field null
        result.setLocation(null);

        // Create the Result, which fails.

        restResultMockMvc.perform(post("/api/results")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(result)))
            .andExpect(status().isBadRequest());

        // Validate the Result in the database
        List<Result> resultList = resultRepository.findAll();
        assertThat(resultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResults() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        resultRepository.saveAndFlush(result);

        // Get all the myTestEntityList
        restResultMockMvc.perform(get("/api/results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(result.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)));
    }



    @Test
    @Transactional
    public void getResult() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        resultRepository.saveAndFlush(result);

        // Get the company
        restResultMockMvc.perform(get("/api/results/{id}", result.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(result.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY));
    }

    @Test
    @Transactional
    public void getNonExistingResult() throws Exception {
        // Get the result
        restResultMockMvc.perform(get("/api/results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
