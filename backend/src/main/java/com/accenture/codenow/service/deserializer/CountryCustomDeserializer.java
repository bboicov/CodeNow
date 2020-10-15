package com.accenture.codenow.service.deserializer;

import com.accenture.codenow.domain.Country;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * CountryCustomDeserializer required to construct Country entity
 *
 * @author  Boyko Boykov
 * @version 0.1
 * @since   2020-10-09
 */
public class CountryCustomDeserializer extends StdDeserializer<Country> {
    private static final long serialVersionUID = 1883547683050039861L;

    public CountryCustomDeserializer() {
        this(null);
    }

    public CountryCustomDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Country deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final JsonNode node = jp.getCodec().readTree(jp);

        Country country = new Country();
        country.setCode(node.asText());

        return country;
    }
}
