package ru.practicum;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;

@Service
public class EndpointHitClient {

    private final RestTemplate rest = new RestTemplateBuilder()
            .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090"))
            .requestFactory(HttpComponentsClientHttpRequestFactory::new)
            .build();

    public ResponseEntity<Object> get(String path, String start, String end, String[] uris, Boolean unique) {
        StringBuilder fullPath = new StringBuilder().append("http://localhost:9090").append(path);
        fullPath.append("?start=").append(start).append("&end=").append(end);
        if (uris != null) {
            for (String uri : uris) {
                fullPath.append("&uris=").append(uri);
            }
        }
        System.out.println(rest.getUriTemplateHandler());
        fullPath.append("&unique=").append(unique);
        return makeAndSendRequest(HttpMethod.GET, fullPath.toString(), null);
    }

    public  <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(HttpMethod.POST, "http://localhost:9090" + path, body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path,
                                                          @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> exploreWithMeServerResponse;
        try {
            exploreWithMeServerResponse = rest.exchange(path, method, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(exploreWithMeServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

}
