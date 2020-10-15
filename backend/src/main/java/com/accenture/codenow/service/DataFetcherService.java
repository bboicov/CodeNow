package com.accenture.codenow.service;

import com.accenture.codenow.domain.AveragingPeriod;
import com.accenture.codenow.domain.Country;
import com.accenture.codenow.domain.Measurement;
import com.accenture.codenow.domain.Result;
import com.accenture.codenow.repository.AveragingPeriodRepository;
import com.accenture.codenow.repository.CountryRepository;
import com.accenture.codenow.repository.ResultRepository;
import com.accenture.codenow.service.deserializer.CountryCustomDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service Implementation for downloading the data from OpenAQ API.
 *
 * @author  Boyko Boykov
 * @version 0.1
 * @since   2020-10-09
 */
@Component
@Transactional
@EnableScheduling
@Profile("!test")
public class DataFetcherService {

    private static final Logger logger = LoggerFactory.getLogger(DataFetcherService.class);

    private static final String BASE_URL = "https://api.openaq.org/v1";

    private final CountryRepository countryRepository;
    private final ResultRepository resultRepository;
    private final AveragingPeriodRepository averagingPeriodRepository;

    private final WebClient webClient;

    public DataFetcherService(CountryRepository countryRepository,
                              ResultRepository resultRepository,
                              AveragingPeriodRepository averagingPeriodRepository,
                              WebClient.Builder webClientBuilder) {
        this.countryRepository = countryRepository;
        this.resultRepository = resultRepository;
        this.averagingPeriodRepository = averagingPeriodRepository;
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    private Mono<String> fetchCountries() {
        return webClient.get().uri("/countries?limit=256").retrieve().bodyToMono(String.class);
    }

    private Mono<String> fetchResults(int limit, int page) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("limit", limit)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    private void updateCountries() {
        final ObjectMapper objectMapper = new ObjectMapper();

        logger.info("Fetching Countries information");
        String json = fetchCountries().block();

        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode results = rootNode.path("results");
            List<Country> resultsList = objectMapper.readValue(results.toString(),
                    new TypeReference<List<Country>>() {
                    });
            countryRepository.saveAll(resultsList);
            logger.info("Fetching Countries information completed successfully.");
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

    private void updateResults() {
        final ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Country.class, new CountryCustomDeserializer());
        objectMapper.registerModule(module);

        logger.info("Fetching Results information");
        int limit = 20;
        int fetched = 0;
        int totalResults = limit;
        int page = 1;

        try {
            do {
                String json = fetchResults(limit, page).block();

                JsonNode rootNode = objectMapper.readTree(json);
                JsonNode meta = rootNode.path("meta");
                totalResults = meta.path("found").asInt();
                JsonNode results = rootNode.path("results");
                List<Result> resultsList = objectMapper.readValue(results.toString(),
                        new TypeReference<List<Result>>() {
                        });
                for (Result result : resultsList) {
                    // populate country from repository
                    Country country = countryRepository.findById(result.getCountry().getCode()).get();
                    result.setCountry(country);
                    // consolidate averaging period (TODO: use hibernate configuration)
                    for (Measurement measurement : result.getMeasurements()) {
                        if (measurement.getAveragingPeriod() != null) {
                            AveragingPeriod averagingPeriod = averagingPeriodRepository
                                    .findByValueAndUnit(measurement.getAveragingPeriod().getValue(),
                                            measurement.getAveragingPeriod().getUnit());
                            if (averagingPeriod == null) {
                                AveragingPeriod averagingPeriodSaved = averagingPeriodRepository.save(measurement.getAveragingPeriod());
                                measurement.setAveragingPeriod(averagingPeriodSaved);
                            } else {
                                measurement.setAveragingPeriod(averagingPeriod);
                            }
                        }
                    }
                }
                resultRepository.saveAll(resultsList);
                fetched += limit;
                page++;
            } while (fetched < totalResults);
            logger.info("Fetching Results information completed successfully.");
        } catch (JsonProcessingException | NoSuchElementException e) {
            logger.error(e.getMessage());
        }
    }

    // Update Countries and Results every 8h
    @Scheduled(fixedRate = 28800000)
    void refreshData() {
        updateCountries();
        updateResults();
    }

}
