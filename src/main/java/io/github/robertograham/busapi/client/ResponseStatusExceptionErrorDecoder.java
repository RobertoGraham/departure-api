package io.github.robertograham.busapi.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

final class ResponseStatusExceptionErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(final String methodKey, final Response response) {
        final String responseBodyAsString = getResponseBodyAsStringOptional(response).orElse(null);
        return Optional.ofNullable(HttpStatus.resolve(response.status()))
                .map(httpStatus -> new ResponseStatusException(httpStatus, responseBodyAsString))
                .map((final ResponseStatusException exception) -> (Exception) exception)
                .orElseGet(() -> new ErrorDecoder.Default().decode(methodKey, response));
    }

    private Optional<String> getResponseBodyAsStringOptional(final Response response) {
        return Optional.ofNullable(response.body())
                .map((final Response.Body responseBody) -> {
                    try (final InputStream inputStream = responseBody.asInputStream()) {
                        return inputStream != null ?
                                IOUtils.toString(inputStream, getCharsetNameAsStringOptional(response).orElse(null))
                                : null;
                    } catch (final IOException exception) {
                        return null;
                    }
                });
    }

    private Optional<String> getCharsetNameAsStringOptional(final Response response) {
        final HttpHeaders httpHeaders = createHttpHeadersFromResponse(response);
        return Optional.ofNullable(httpHeaders.getContentType())
                .map(MediaType::getCharset)
                .map(Charset::name);
    }

    private HttpHeaders createHttpHeadersFromResponse(final Response response) {
        return response.headers().entrySet().stream()
                .reduce(new HttpHeaders(), this::reduceHttpHeadersAndStringToStringCollectionEntryToHttpHeaders, this::combineTwoHttpHeaders);
    }

    private HttpHeaders reduceHttpHeadersAndStringToStringCollectionEntryToHttpHeaders(final HttpHeaders httpHeaders, final Entry<String, Collection<String>> stringToStringCollectionEntry) {
        httpHeaders.addAll(stringToStringCollectionEntry.getKey(), new ArrayList<>(stringToStringCollectionEntry.getValue()));
        return httpHeaders;
    }

    private HttpHeaders combineTwoHttpHeaders(final HttpHeaders httpHeaders1, final HttpHeaders httpHeaders2) {
        httpHeaders1.addAll(httpHeaders2);
        return httpHeaders1;
    }
}
