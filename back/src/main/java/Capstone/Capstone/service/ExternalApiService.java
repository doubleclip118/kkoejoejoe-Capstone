package Capstone.Capstone.service;

import Capstone.Capstone.service.dto.AWSCloudDriverDTO;
import Capstone.Capstone.service.dto.AWSConfigDTO;
import Capstone.Capstone.service.dto.AWSCredentialDTO;
import Capstone.Capstone.service.dto.AWSRegionDTO;
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
    private final String baseUrl = "http://54.180.246.122:1024";

    public ExternalApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public AWSCloudDriverDTO postToSpiderDriver(AWSCloudDriverDTO AWSCloudDriverDTO) {
        String endpoint = "/spider/driver";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AWSCloudDriverDTO> request = new HttpEntity<>(AWSCloudDriverDTO, headers);
        try {
            log.info("cloud aws Diver 응답요청 ");
            AWSCloudDriverDTO AWSCloudDriverDTO1 = restTemplate.postForObject(fullUrl, request,
                AWSCloudDriverDTO.class);
            System.out.println(
                AWSCloudDriverDTO1.getDriverName() +"\n"+ AWSCloudDriverDTO1.getDriverLibFileName() +"\n" + AWSCloudDriverDTO1.getProviderName());
            return AWSCloudDriverDTO1;
        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            System.out.println(e);
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }

    public AWSCredentialDTO postToSpiderCredential(AWSCredentialDTO awsCredentialDTO) {
        String endpoint = "/spider/credential";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AWSCredentialDTO> request = new HttpEntity<>(awsCredentialDTO, headers);
        try {
            log.info("cloud aws credential 응답요청 ");
            AWSCredentialDTO awsCredentialDTO1 = restTemplate.postForObject(fullUrl, request,
                AWSCredentialDTO.class);
            System.out.println(awsCredentialDTO1.toString());
            System.out.println(awsCredentialDTO1);
            return awsCredentialDTO1;

        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            System.out.println(e);
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }

    public AWSRegionDTO postToSpiderRegion(AWSRegionDTO awsRegionDTO) {
        String endpoint = "/spider/region";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AWSRegionDTO> request = new HttpEntity<>(awsRegionDTO, headers);
        try {
            log.info("cloud aws region 응답요청 ");
            AWSRegionDTO awsRegionDTO1 = restTemplate.postForObject(fullUrl, request,
                AWSRegionDTO.class);
            System.out.println(awsRegionDTO1);
            return awsRegionDTO1;

        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            System.out.println(e);
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }

    public AWSConfigDTO postToSpiderConfig(AWSConfigDTO awsConfigDTO) {
        String endpoint = "/spider/connectionconfig";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AWSConfigDTO> request = new HttpEntity<>(awsConfigDTO, headers);
        try {
            log.info("cloud aws region 응답요청 ");
            AWSConfigDTO awsConfigDTO1 = restTemplate.postForObject(fullUrl, request,
                AWSConfigDTO.class);
            System.out.println(awsConfigDTO1.toString());
            return awsConfigDTO1;

        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }


}
