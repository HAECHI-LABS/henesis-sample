package io.haechi.henesis.assignment.config.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.haechi.henesis.assignment.domain.util.Converter;

import java.io.IOException;
import java.math.BigInteger;

public class HexStringToBigInteger extends JsonDeserializer<BigInteger> {
    @Override
    public BigInteger deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException{
        return Converter.hexStringToBigInteger(jsonParser.getText());
    }
}
