package Capstone.Capstone.service;

import Capstone.Capstone.service.dto.CloudDriver;
import Capstone.Capstone.utils.error.CbSpiderServerException;
import Capstone.Capstone.utils.error.CloudInfoIncorrectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ExternalApiService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://3.34.135.215:1024";

    public ExternalApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public CloudDriver postToSpiderDriver(CloudDriver cloudDriver) {
        String endpoint = "/spider/driver";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CloudDriver> request = new HttpEntity<>(cloudDriver, headers);
        try {
            log.info("cloud aws Diver 응답요청 ");
            CloudDriver cloudDriver1 = restTemplate.postForObject(fullUrl, request,
                CloudDriver.class);
            System.out.println(cloudDriver1.getDriverName() + cloudDriver1.getDriverLibFileName() + cloudDriver1.getProviderName());
            return cloudDriver1;
        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }
}
