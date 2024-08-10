package Capstone.Capstone.service;

import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.AWSCloudInfoRepository;
import Capstone.Capstone.repository.AzureCloudInfoRepository;
import Capstone.Capstone.repository.OpenstackCloudInfoRepository;
import Capstone.Capstone.repository.UserRepository;
import Capstone.Capstone.service.dto.AWSCloudDriverDTO;
import Capstone.Capstone.service.dto.AWSConfigDTO;
import Capstone.Capstone.service.dto.AWSCredentialDTO;
import Capstone.Capstone.service.dto.AzureCloudDriverDTO;
import Capstone.Capstone.service.dto.AzureConfigDTO;
import Capstone.Capstone.service.dto.AzureCredentialDTO;
import Capstone.Capstone.service.dto.AzureRegionDTO;
import Capstone.Capstone.service.dto.KeyValueInfo;
import Capstone.Capstone.service.dto.AWSRegionDTO;
import Capstone.Capstone.service.dto.OpenstackCloudDriverDTO;
import Capstone.Capstone.service.dto.OpenstackConfigDTO;
import Capstone.Capstone.service.dto.OpenstackCredentialDTO;
import Capstone.Capstone.service.dto.OpenstackRegionDTO;
import Capstone.Capstone.utils.error.UserNotFoundException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CbspiderConService {
    private final UserRepository userRepository;
    private final AWSCloudInfoRepository awsCloudInfoRepository;
    private final AzureCloudInfoRepository azureCloudInfoRepository;
    private final OpenstackCloudInfoRepository openstackCloudInfoRepository;

    private final ExternalApiService externalApiService;

    public CbspiderConService(UserRepository userRepository,
        AWSCloudInfoRepository awsCloudInfoRepository,
        AzureCloudInfoRepository azureCloudInfoRepository,
        OpenstackCloudInfoRepository openstackCloudInfoRepository,
        ExternalApiService externalApiService) {
        this.userRepository = userRepository;
        this.awsCloudInfoRepository = awsCloudInfoRepository;
        this.azureCloudInfoRepository = azureCloudInfoRepository;
        this.openstackCloudInfoRepository = openstackCloudInfoRepository;
        this.externalApiService = externalApiService;
    }
    @Transactional
    public String conAWS(Long id){
        User user = userRepository.findByUserIdWithAWSCloudInfo(id).orElseThrow(
            () -> {
                log.error("User not found for id: {}", id);
                return new UserNotFoundException("User Not Found");
            });
        System.out.println(user.getAwsCloudInfo().getCredentialAccessKey());

        //클라우드 드라이버 등록
        AWSCloudDriverDTO AWSCloudDriverDTO = new AWSCloudDriverDTO(user.getAwsCloudInfo().getDriverName(),
            user.getAwsCloudInfo().getProviderName(),
            user.getAwsCloudInfo().getDriverLibFileName());
        externalApiService.postToSpiderAWSDriver(
            AWSCloudDriverDTO);

        //클라우드 크리덴셜 등록
        KeyValueInfo awsAccess = new KeyValueInfo(
            user.getAwsCloudInfo().getCredentialAccessKey(),
            user.getAwsCloudInfo().getCredentialAccessKeyVal()
        );

        KeyValueInfo awsSecret = new KeyValueInfo(
            user.getAwsCloudInfo().getCredentialSecretKey(),
            user.getAwsCloudInfo().getCredentialSecretKeyVal()
        );

        List<KeyValueInfo> keyValueInfoList = Arrays.asList(awsAccess, awsSecret);

        AWSCredentialDTO awsCredentialDTO = new AWSCredentialDTO(
            user.getAwsCloudInfo().getCredentialName(),
            user.getAwsCloudInfo().getProviderName(),
            keyValueInfoList
        );
        externalApiService.postToSpiderAWSCredential(awsCredentialDTO);

        //리전 등록

        KeyValueInfo awsRegion = new KeyValueInfo(
            user.getAwsCloudInfo().getRegionKey(),
            user.getAwsCloudInfo().getRegionValue()
        );

        KeyValueInfo awsZone = new KeyValueInfo(
            user.getAwsCloudInfo().getZoneKey(),
            user.getAwsCloudInfo().getZoneValue()
        );

        List<KeyValueInfo> RegionkeyValueInfoList = Arrays.asList(awsRegion, awsZone);

        AWSRegionDTO awsRegionDTO = new AWSRegionDTO(user.getAwsCloudInfo().getRegionName(),
            user.getAwsCloudInfo().getProviderName(),
            RegionkeyValueInfoList);

        externalApiService.postToSpiderAWSRegion(awsRegionDTO);

        //커넥션 생성
        AWSConfigDTO awsConfigDTO = new AWSConfigDTO(user.getAwsCloudInfo().getConfigName(),
            user.getAwsCloudInfo().getProviderName(),
            user.getAwsCloudInfo().getDriverName(), user.getAwsCloudInfo().getCredentialName(),
            user.getAwsCloudInfo().getRegionName());
        externalApiService.postToSpiderAWSConfig(awsConfigDTO);


        return "생성 완료";
    }

    @Transactional
    public String deleteconAWS(Long id){
        User user = userRepository.findByUserIdWithAWSCloudInfo(id).orElseThrow(
            () -> {
                log.error("User not found for id: {}", id);
                return new UserNotFoundException("User Not Found");
            });
        // 드라이버 삭제
        externalApiService.deleteSpiderDriver(user.getAwsCloudInfo().getDriverName());
        // 크리덴셜 삭제
        externalApiService.deleteSpiderCredential(user.getAwsCloudInfo().getCredentialName());
        // 리즌 삭제
        externalApiService.deleteSpiderRegion(user.getAwsCloudInfo().getRegionName());
        // 커넥션 삭제
        externalApiService.deleteSpiderConnectionConfig(user.getAwsCloudInfo().getConfigName());

        return "삭제 완료";
    }

    @Transactional
    public String conAZURE(Long id){
        User user = userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        // azure 클라우드 드라이버 생성
        AzureCloudDriverDTO azureCloudDriverDTO = new AzureCloudDriverDTO(
            user.getAzureCloudInfo().getDriverName(), user.getAzureCloudInfo().getProviderName(),
            user.getAwsCloudInfo().getDriverLibFileName());

        externalApiService.postToSpiderAZUREDriver(azureCloudDriverDTO);

        //azure 클라우드 크리덴셜 생성

        KeyValueInfo azureAccess = new KeyValueInfo(
            user.getAzureCloudInfo().getClientIdkey(),
            user.getAzureCloudInfo().getClientIdValue()
        );

        KeyValueInfo azureSecret = new KeyValueInfo(
            user.getAzureCloudInfo().getClientSecretKey(),
            user.getAzureCloudInfo().getClientSecretValue()
        );

        KeyValueInfo azureTenant = new KeyValueInfo(
            user.getAzureCloudInfo().getTenantIdKey(),
            user.getAzureCloudInfo().getTenantIdValue()
        );

        KeyValueInfo azureSubscription = new KeyValueInfo(
            user.getAzureCloudInfo().getSubscriptionIdKey(),
            user.getAzureCloudInfo().getSubscriptionIdValue()
        );

        List<KeyValueInfo> keyValueInfoList = Arrays.asList(azureAccess, azureSecret,azureTenant,azureSubscription);

        AzureCredentialDTO azureCredentialDTO = new AzureCredentialDTO(user.getAzureCloudInfo().getCredentialName(),
            user.getAzureCloudInfo().getProviderName(),keyValueInfoList);

        externalApiService.postToSpiderAzureCredential(azureCredentialDTO);

        //azure 클라우드 리전등록

        KeyValueInfo azureRegion = new KeyValueInfo(
            user.getAzureCloudInfo().getRegionKey(),
            user.getAzureCloudInfo().getRegionValue()
        );

        KeyValueInfo azureZone = new KeyValueInfo(
            user.getAzureCloudInfo().getZoneKey(),
            user.getAzureCloudInfo().getZoneValue()
        );

        List<KeyValueInfo> keyValueRegionInfoList = Arrays.asList(azureRegion, azureZone);

        AzureRegionDTO azureRegionDTO = new AzureRegionDTO(user.getAzureCloudInfo().getRegionName(),
            user.getAzureCloudInfo().getProviderName(),
            keyValueRegionInfoList);

        externalApiService.postToSpiderAzureRegion(azureRegionDTO);

        //azure 커넥트 연결

        AzureConfigDTO azureConfigDTO = new AzureConfigDTO(user.getAzureCloudInfo().getConfigName(),
            user.getAzureCloudInfo().getProviderName(),
            user.getAzureCloudInfo().getDriverName(), user.getAzureCloudInfo().getCredentialName(),
            user.getAzureCloudInfo().getRegionName());

        externalApiService.postToSpiderAzureConfig(azureConfigDTO);

        return "연결 성공";
    }

    @Transactional
    public String deleteconAzure(Long id){
        User user = userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );

        // 드라이버 삭제
        externalApiService.deleteSpiderDriver(user.getAzureCloudInfo().getDriverName());
        // 크리덴셜 삭제
        externalApiService.deleteSpiderCredential(user.getAzureCloudInfo().getCredentialName());
        // 리즌 삭제
        externalApiService.deleteSpiderRegion(user.getAzureCloudInfo().getRegionName());
        // 커넥션 삭제
        externalApiService.deleteSpiderConnectionConfig(user.getAzureCloudInfo().getConfigName());

        return "삭제 성공";
    }

    @Transactional
    public String conOpenStack(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );

        // OpenStack 클라우드 드라이버 생성
        OpenstackCloudDriverDTO openstackCloudDriverDTO = new OpenstackCloudDriverDTO(
            user.getOpenstackCloudInfo().getDriverName(),
            user.getOpenstackCloudInfo().getProviderName(),
            user.getOpenstackCloudInfo().getDriverLibFileName()
        );
        externalApiService.postToSpiderOpenstackDriver(openstackCloudDriverDTO);

        // OpenStack 클라우드 크리덴셜 생성
        List<KeyValueInfo> credentialInfoList = Arrays.asList(
            new KeyValueInfo(user.getOpenstackCloudInfo().getIdentityEndpointKey(), user.getOpenstackCloudInfo().getIdentityEndpointValue()),
            new KeyValueInfo(user.getOpenstackCloudInfo().getUsernameKey(), user.getOpenstackCloudInfo().getUsernameValue()),
            new KeyValueInfo(user.getOpenstackCloudInfo().getPasswordKey(), user.getOpenstackCloudInfo().getPasswordValue()),
            new KeyValueInfo(user.getOpenstackCloudInfo().getDomainNameKey(), user.getOpenstackCloudInfo().getDomainNameValue()),
            new KeyValueInfo(user.getOpenstackCloudInfo().getProjectIDKey(), user.getOpenstackCloudInfo().getProjectIDValue())
        );

        OpenstackCredentialDTO openstackCredentialDTO = new OpenstackCredentialDTO(
            user.getOpenstackCloudInfo().getCredentialName(),
            user.getOpenstackCloudInfo().getProviderName(),
            credentialInfoList
        );
        externalApiService.postToSpiderOpenStackCredential(openstackCredentialDTO);

        // OpenStack 클라우드 리전 등록
        List<KeyValueInfo> regionInfoList = Arrays.asList(
            new KeyValueInfo(user.getOpenstackCloudInfo().getRegionKey(), user.getOpenstackCloudInfo().getRegionValue())
        );

        OpenstackRegionDTO openstackRegionDTO = new OpenstackRegionDTO(
            user.getOpenstackCloudInfo().getRegionName(),
            user.getOpenstackCloudInfo().getProviderName(),
            regionInfoList
        );
        externalApiService.postToSpiderOpenStackRegion(openstackRegionDTO);

        // OpenStack 커넥트 연결
        OpenstackConfigDTO openstackConfigDTO = new OpenstackConfigDTO(
            user.getOpenstackCloudInfo().getConfigName(),
            user.getOpenstackCloudInfo().getProviderName(),
            user.getOpenstackCloudInfo().getDriverName(),
            user.getOpenstackCloudInfo().getCredentialName(),
            user.getOpenstackCloudInfo().getRegionName()
        );
        externalApiService.postToSpiderOpenStackConfig(openstackConfigDTO);

        return "연결 성공";
    }

    @Transactional
    public String deleteconOpenStack(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );

        // 드라이버 삭제
        externalApiService.deleteSpiderDriver(user.getOpenstackCloudInfo().getDriverName());
        // 크리덴셜 삭제
        externalApiService.deleteSpiderCredential(user.getOpenstackCloudInfo().getCredentialName());
        // 리전 삭제
        externalApiService.deleteSpiderRegion(user.getOpenstackCloudInfo().getRegionName());
        // 커넥션 삭제
        externalApiService.deleteSpiderConnectionConfig(user.getOpenstackCloudInfo().getConfigName());

        return "삭제 성공";
    }




}
