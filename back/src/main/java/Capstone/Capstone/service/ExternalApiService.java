package Capstone.Capstone.service;

import Capstone.Capstone.service.dto.CloudDriver;
import Capstone.Capstone.utils.error.CbSpiderServerException;
import Capstone.Capstone.utils.error.CloudInfoIncorrectException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://3.34.135.215:1024";

    public ExternalApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public CloudDriver postToSpiderDriver(Object requestBody) {
        String endpoint = "/spider/driver";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);
        try {
            return restTemplate.postForObject(fullUrl, request, CloudDriver.class);
        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }
}
