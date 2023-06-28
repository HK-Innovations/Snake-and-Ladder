package com.ludo.Snake.and.Ladder.Util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.ludo.Snake.and.Ladder.Constants

class Utilities {
    private static ObjectMapper mapper = new ObjectMapper()
    .tap {
        registerModule(new JavaTimeModule())
    }
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    .setTimeZone(TimeZone.getTimeZone(Constants.IND_ZONE_ID))

    static ObjectMapper objectMapper() {
        return mapper
    }


    private Utilities() {}

}
