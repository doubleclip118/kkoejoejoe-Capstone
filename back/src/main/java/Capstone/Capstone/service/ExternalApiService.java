package Capstone.Capstone.service;

import Capstone.Capstone.service.dto.AWSCloudDriverDTO;
import Capstone.Capstone.service.dto.AWSConfigDTO;
import Capstone.Capstone.service.dto.AWSCredentialDTO;
import Capstone.Capstone.service.dto.AWSRegionDTO;
import Capstone.Capstone.service.dto.AzureCloudDriverDTO;
import Capstone.Capstone.service.dto.AzureConfigDTO;
import Capstone.Capstone.service.dto.AzureCredentialDTO;
import Capstone.Capstone.service.dto.AzureRegionDTO;
import Capstone.Capstone.service.dto.CreateKeyPairRequestDTO;
import Capstone.Capstone.service.dto.CreateKeyPairResponseDTO;
import Capstone.Capstone.service.dto.CreateSecurityGroupRequestDTO;
import Capstone.Capstone.service.dto.CreateSecurityGroupResponseDTO;
import Capstone.Capstone.service.dto.CreateVMRequestDTO;
import Capstone.Capstone.service.dto.CreateVMResponseDTO;
import Capstone.Capstone.service.dto.CreateVPCRequestDTO;
import Capstone.Capstone.service.dto.CreateVPCResponseDTO;
import Capstone.Capstone.service.dto.OpenstackCloudDriverDTO;
import Capstone.Capstone.service.dto.OpenstackConfigDTO;
import Capstone.Capstone.service.dto.OpenstackCredentialDTO;
import Capstone.Capstone.service.dto.OpenstackRegionDTO;
import Capstone.Capstone.utils.error.CbSpiderServerException;
import Capstone.Capstone.utils.error.CloudInfoIncorrectException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ExternalApiService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://43.203.226.196:1024";

    public ExternalApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public AWSCloudDriverDTO postToSpiderAWSDriver(AWSCloudDriverDTO AWSCloudDriverDTO) {
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

    public AWSCredentialDTO postToSpiderAWSCredential(AWSCredentialDTO awsCredentialDTO) {
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



    public AWSRegionDTO postToSpiderAWSRegion(AWSRegionDTO awsRegionDTO) {
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

    public AWSConfigDTO postToSpiderAWSConfig(AWSConfigDTO awsConfigDTO) {
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
    public boolean deleteSpiderDriver(String driverName) {
        String endpoint = "/spider/driver/" + driverName;
        String fullUrl = baseUrl + endpoint;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            log.info("클라우드 드라이버 삭제 요청 전송: {}", fullUrl);

            ResponseEntity<String> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.DELETE,
                entity,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("클라우드 드라이버 삭제 성공: {}", driverName);
                return true;
            } else {
                log.warn("클라우드 드라이버 삭제 실패. 상태 코드: {}", response.getStatusCode());
                return false;
            }

        } catch (HttpClientErrorException e) {
            log.error("클라우드 드라이버 삭제 중 클라이언트 오류 발생: {}", e.getMessage());
            throw new RuntimeException("클라우드 드라이버 삭제 실패: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            log.error("클라우드 드라이버 삭제 중 서버 오류 발생: {}", e.getMessage());
            throw new RuntimeException("클라우드 드라이버 삭제 중 서버 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            log.error("클라우드 드라이버 삭제 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("클라우드 드라이버 삭제 중 예상치 못한 오류 발생", e);
        }
    }
    public boolean deleteSpiderCredential(String credentialName) {
        String endpoint = "/spider/credential/" + credentialName;
        String fullUrl = baseUrl + endpoint;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            log.info("클라우드 Credential 정보 삭제 요청 전송: {}", fullUrl);

            ResponseEntity<String> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.DELETE,
                entity,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("클라우드 Credential 정보 삭제 성공: {}", credentialName);
                return true;
            } else {
                log.warn("클라우드 Credential 정보 삭제 실패. 상태 코드: {}", response.getStatusCode());
                return false;
            }

        } catch (HttpClientErrorException e) {
            log.error("클라우드 Credential 정보 삭제 중 클라이언트 오류 발생: {}", e.getMessage());
            throw new RuntimeException("클라우드 Credential 정보 삭제 실패: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            log.error("클라우드 Credential 정보 삭제 중 서버 오류 발생: {}", e.getMessage());
            throw new RuntimeException("클라우드 Credential 정보 삭제 중 서버 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            log.error("클라우드 Credential 정보 삭제 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("클라우드 Credential 정보 삭제 중 예상치 못한 오류 발생", e);
        }
    }
    public boolean deleteSpiderRegion(String regionName) {
        String endpoint = "/spider/region/" + regionName;
        String fullUrl = baseUrl + endpoint;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            log.info("클라우드 Region/Zone 정보 삭제 요청 전송: {}", fullUrl);

            ResponseEntity<String> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.DELETE,
                entity,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("클라우드 Region/Zone 정보 삭제 성공: {}", regionName);
                return true;
            } else {
                log.warn("클라우드 Region/Zone 정보 삭제 실패. 상태 코드: {}", response.getStatusCode());
                return false;
            }

        } catch (HttpClientErrorException e) {
            log.error("클라우드 Region/Zone 정보 삭제 중 클라이언트 오류 발생: {}", e.getMessage());
            throw new RuntimeException("클라우드 Region/Zone 정보 삭제 실패: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            log.error("클라우드 Region/Zone 정보 삭제 중 서버 오류 발생: {}", e.getMessage());
            throw new RuntimeException("클라우드 Region/Zone 정보 삭제 중 서버 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            log.error("클라우드 Region/Zone 정보 삭제 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("클라우드 Region/Zone 정보 삭제 중 예상치 못한 오류 발생", e);
        }
    }
    public boolean deleteSpiderConnectionConfig(String configName) {
        String endpoint = "/spider/connectionconfig/" + configName;
        String fullUrl = baseUrl + endpoint;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            log.info("클라우드 Connection Configuration 정보 삭제 요청 전송: {}", fullUrl);

            ResponseEntity<String> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.DELETE,
                entity,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("클라우드 Connection Configuration 정보 삭제 성공: {}", configName);
                return true;
            } else {
                log.warn("클라우드 Connection Configuration 정보 삭제 실패. 상태 코드: {}", response.getStatusCode());
                return false;
            }

        } catch (HttpClientErrorException e) {
            log.error("클라우드 Connection Configuration 정보 삭제 중 클라이언트 오류 발생: {}", e.getMessage());
            throw new RuntimeException("클라우드 Connection Configuration 정보 삭제 실패: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            log.error("클라우드 Connection Configuration 정보 삭제 중 서버 오류 발생: {}", e.getMessage());
            throw new RuntimeException("클라우드 Connection Configuration 정보 삭제 중 서버 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            log.error("클라우드 Connection Configuration 정보 삭제 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("클라우드 Connection Configuration 정보 삭제 중 예상치 못한 오류 발생", e);
        }
    }

    public AzureCloudDriverDTO postToSpiderAZUREDriver(AzureCloudDriverDTO azureCloudDriverDTO) {
        String endpoint = "/spider/driver";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AzureCloudDriverDTO> request = new HttpEntity<>(azureCloudDriverDTO, headers);
        try {
            log.info("cloud azure Diver 응답요청 ");
            AzureCloudDriverDTO azureCloudDriverDTO1 = restTemplate.postForObject(fullUrl, request,
                AzureCloudDriverDTO.class);
            System.out.println(
                azureCloudDriverDTO1.getDriverName() +"\n"+ azureCloudDriverDTO1.getDriverLibFileName() +"\n" + azureCloudDriverDTO1.getProviderName());
            return azureCloudDriverDTO1;
        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            System.out.println(e);
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }

    public AzureCredentialDTO postToSpiderAzureCredential(AzureCredentialDTO azureCredentialDTO) {
        String endpoint = "/spider/credential";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AzureCredentialDTO> request = new HttpEntity<>(azureCredentialDTO, headers);
        try {
            log.info("cloud azure credential 응답요청 ");
            AzureCredentialDTO azureCredentialDTO1 = restTemplate.postForObject(fullUrl, request,
                AzureCredentialDTO.class);
            System.out.println(azureCredentialDTO1.toString());
            System.out.println(azureCredentialDTO1);
            return azureCredentialDTO1;

        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            System.out.println(e);
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }

    public AzureRegionDTO postToSpiderAzureRegion(AzureRegionDTO azureRegionDTO) {
        String endpoint = "/spider/region";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AzureRegionDTO> request = new HttpEntity<>(azureRegionDTO, headers);
        try {
            log.info("cloud azure region 응답요청 ");
            AzureRegionDTO azureRegionDTO1 = restTemplate.postForObject(fullUrl, request,
                AzureRegionDTO.class);
            System.out.println(azureRegionDTO1);
            return azureRegionDTO1;

        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            System.out.println(e);
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }

    public AzureConfigDTO postToSpiderAzureConfig(AzureConfigDTO azureConfigDTO) {
        String endpoint = "/spider/connectionconfig";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AzureConfigDTO> request = new HttpEntity<>(azureConfigDTO, headers);
        try {
            log.info("cloud aws region 응답요청 ");
            AzureConfigDTO azureConfigDTO1 = restTemplate.postForObject(fullUrl, request,
                AzureConfigDTO.class);
            System.out.println(azureConfigDTO1.toString());
            return azureConfigDTO1;

        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }

    public OpenstackCloudDriverDTO postToSpiderOpenstackDriver(OpenstackCloudDriverDTO openstackCloudDriverDTO) {
        String endpoint = "/spider/driver";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OpenstackCloudDriverDTO> request = new HttpEntity<>(openstackCloudDriverDTO, headers);
        try {
            log.info("cloud aws Diver 응답요청 ");
            OpenstackCloudDriverDTO openstackCloudDriverDTO1 = restTemplate.postForObject(fullUrl, request,
                OpenstackCloudDriverDTO.class);
            System.out.println(
                openstackCloudDriverDTO1.getDriverName() +"\n"+ openstackCloudDriverDTO1.getDriverLibFileName() +"\n" + openstackCloudDriverDTO1.getProviderName());
            return openstackCloudDriverDTO1;
        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            System.out.println(e);
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }
    public OpenstackCredentialDTO postToSpiderOpenStackCredential(OpenstackCredentialDTO openStackCredentialDTO) {
        String endpoint = "/spider/credential";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OpenstackCredentialDTO> request = new HttpEntity<>(openStackCredentialDTO, headers);
        try {
            log.info("cloud openstack credential 응답요청 ");
            OpenstackCredentialDTO openStackCredentialDTO1 = restTemplate.postForObject(fullUrl, request,
                OpenstackCredentialDTO.class);
            System.out.println(openStackCredentialDTO1.toString());
            return openStackCredentialDTO1;

        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            System.out.println(e);
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }
    public OpenstackRegionDTO postToSpiderOpenStackRegion(OpenstackRegionDTO openStackRegionDTO) {
        String endpoint = "/spider/region";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OpenstackRegionDTO> request = new HttpEntity<>(openStackRegionDTO, headers);
        try {
            log.info("cloud openstack region 응답요청 ");
            OpenstackRegionDTO openStackRegionDTO1 = restTemplate.postForObject(fullUrl, request,
                OpenstackRegionDTO.class);
            System.out.println(openStackRegionDTO1.toString());
            return openStackRegionDTO1;

        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            System.out.println(e);
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }
    public OpenstackConfigDTO postToSpiderOpenStackConfig(OpenstackConfigDTO openStackConfigDTO) {
        String endpoint = "/spider/connectionconfig";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OpenstackConfigDTO> request = new HttpEntity<>(openStackConfigDTO, headers);
        try {
            log.info("cloud openstack config 응답요청 ");
            OpenstackConfigDTO openStackConfigDTO1 = restTemplate.postForObject(fullUrl, request,
                OpenstackConfigDTO.class);
            System.out.println(openStackConfigDTO1.toString());
            return openStackConfigDTO1;

        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            System.out.println(e);
            // 5xx 서버 오류
            throw new CbSpiderServerException("cbspider not working");
        }
    }


    public CreateVPCResponseDTO createVPC(CreateVPCRequestDTO createVPCRequestDTO) {
        String endpoint = "/spider/vpc";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateVPCRequestDTO> request = new HttpEntity<>(createVPCRequestDTO, headers);
        try {
            log.info("VPC 생성 요청 전송: {}", fullUrl);
            return restTemplate.postForObject(fullUrl, request, CreateVPCResponseDTO.class);
        } catch (HttpClientErrorException e) {
            log.error("VPC 생성 중 클라이언트 오류 발생: {}", e.getMessage());
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            log.error("VPC 생성 중 서버 오류 발생: {}", e.getMessage());
            throw new CbSpiderServerException("cbspider not working");
        } catch (RestClientException e) {
            log.error("VPC 생성 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("VPC 생성 중 예상치 못한 오류 발생", e);
        }
    }

    public CreateSecurityGroupResponseDTO createSecurityGroup(
        CreateSecurityGroupRequestDTO createSecurityGroupRequestDTO) {
        String endpoint = "/spider/securitygroup";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateSecurityGroupRequestDTO> request = new HttpEntity<>(createSecurityGroupRequestDTO, headers);
        try {
            log.info("Security Group 생성 요청 전송: {}", fullUrl);
            return restTemplate.postForObject(fullUrl, request, CreateSecurityGroupResponseDTO.class);
        } catch (HttpClientErrorException e) {
            log.error("Security Group 생성 중 클라이언트 오류 발생: {}", e.getMessage());
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            log.error("Security Group 생성 중 서버 오류 발생: {}", e.getMessage());
            throw new CbSpiderServerException("cbspider not working");
        } catch (RestClientException e) {
            log.error("Security Group 생성 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("Security Group 생성 중 예상치 못한 오류 발생", e);
        }
    }
    public CreateKeyPairResponseDTO createKeypair(CreateKeyPairRequestDTO createKeyPairRequestDTO) {
        String endpoint = "/spider/keypair";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateKeyPairRequestDTO> request = new HttpEntity<>(createKeyPairRequestDTO, headers);
        try {
            log.info("Key Pair 생성 요청 전송: {}", fullUrl);
            return restTemplate.postForObject(fullUrl, request, CreateKeyPairResponseDTO.class);
        } catch (HttpClientErrorException e) {
            log.error("Key Pair 생성 중 클라이언트 오류 발생: {}", e.getMessage());
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            log.error("Key Pair 생성 중 서버 오류 발생: {}", e.getMessage());
            throw new CbSpiderServerException("cbspider not working");
        } catch (RestClientException e) {
            log.error("Key Pair 생성 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("Key Pair 생성 중 예상치 못한 오류 발생", e);
        }
    }
    public CreateVMResponseDTO createVM(CreateVMRequestDTO createVMRequestDTO) {
        String endpoint = "/spider/vm";
        String fullUrl = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateVMRequestDTO> request = new HttpEntity<>(createVMRequestDTO, headers);
        try {
            log.info("VM 생성 요청 전송: {}", fullUrl);
            return restTemplate.postForObject(fullUrl, request, CreateVMResponseDTO.class);
        } catch (HttpClientErrorException e) {
            log.error("VM 생성 중 클라이언트 오류 발생: {}", e.getMessage());
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            log.error("VM 생성 중 서버 오류 발생: {}", e.getMessage());
            throw new CbSpiderServerException("cbspider not working");
        } catch (RestClientException e) {
            log.error("VM 생성 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("VM 생성 중 예상치 못한 오류 발생", e);
        }
    }
    public String deleteVm(String vmName, String connectionName) {
        String endpoint = "/spider/vm/" + vmName;
        String fullUrl = baseUrl + endpoint;

        log.info("VM 삭제 시작: {}", vmName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ConnectionName을 포함한 요청 본문 생성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("ConnectionName", connectionName);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            log.info("VM 삭제 요청 전송: {}", fullUrl);
            ResponseEntity<String> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.DELETE,
                request,
                String.class
            );
            log.info("VM 삭제 응답 수신: {}", response.getStatusCode());

            return response.getBody();

        } catch (HttpClientErrorException e) {
            log.error("VM 삭제 중 클라이언트 오류 발생: {}", e.getMessage());
            throw new CloudInfoIncorrectException("InfoIncorrect");
        } catch (HttpServerErrorException e) {
            log.error("VM 삭제 중 서버 오류 발생: {}", e.getMessage());
            throw new CbSpiderServerException("cbspider not working");
        } catch (RestClientException e) {
            log.error("VM 삭제 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("VM 삭제 중 예상치 못한 오류 발생", e);
        }
    }


}
