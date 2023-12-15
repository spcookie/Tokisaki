package io.micro.starter.config;

import io.quarkus.resteasy.reactive.kotlin.serialization.runtime.KotlinSerializationMessageBodyReader;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import kotlinx.serialization.json.Json;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;

@Provider
@Priority(100)
public class JsonTextReaderAdapter implements MessageBodyReader<Object> {

    @Inject
    public JsonTextReaderAdapter(Json json) {
        this.json = json;
    }

    private Json json;

    private KotlinSerializationMessageBodyReader entrust = new KotlinSerializationMessageBodyReader(json);


    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Objects.equals(mediaType.getSubtype(), "json");
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        return entrust.readFrom(type, genericType, annotations, mediaType, httpHeaders, entityStream);
    }

}
