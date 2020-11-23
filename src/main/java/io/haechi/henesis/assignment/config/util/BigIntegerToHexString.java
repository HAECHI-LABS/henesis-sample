package io.haechi.henesis.assignment.config.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.haechi.henesis.assignment.domain.util.Converter;

import java.io.IOException;
import java.math.BigInteger;

public class BigIntegerToHexString extends JsonSerializer<BigInteger> {
    @Override
    public void serialize(BigInteger value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException{
        jsonGenerator.writeString(Converter.bigIntegerToHexString(value));
    }
}
