package com.ludo.Snake.and.Ladder.model

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import groovy.util.logging.Slf4j
import jakarta.persistence.AttributeConverter
import com.ludo.Snake.and.Ladder.Util.Utilities


@Slf4j
class GenericMapConverter<K, V> implements AttributeConverter<Map<K, V>, String> {

    @Override
    String convertToDatabaseColumn(Map<K, V> object) {
        try {
            return Utilities.objectMapper().writeValueAsString(object)
        } catch (JsonProcessingException ex) {
            log.error("Exception occurred while converting to DBColumn. Exception: [{}]", ex.getLocalizedMessage())
            throw ex
        }
    }

    @Override
    Map<K, V> convertToEntityAttribute(String dbData) {
        if (dbData == "{}" || dbData == null) {
            return null
        }
        try {
            return Utilities.objectMapper().readValue(dbData, new TypeReference<Map<K, V>>() {})
        } catch (Exception ex) {
            String errorMessage = "Exception occurred while parsing GenericMap DBColumn. Exception: ${ex.getLocalizedMessage()}"
            log.error(errorMessage, ex)
            throw ex
        }
    }
}
