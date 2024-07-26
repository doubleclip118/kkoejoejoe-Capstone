package Capstone.Capstone.service;

import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.AWSCloudInfoRepository;
import Capstone.Capstone.repository.AzureCloudInfoRepository;
import Capstone.Capstone.repository.UserRepository;
import Capstone.Capstone.service.dto.AWSCloudDriverDTO;
import Capstone.Capstone.service.dto.AWSConfigDTO;
import Capstone.Capstone.service.dto.AWSCredentialDTO;
import Capstone.Capstone.service.dto.KeyValueInfo;
import Capstone.Capstone.service.dto.AWSRegionDTO;
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
    private final ExternalApiService externalApiService;

    public CbspiderConService(UserRepository userRepository,
        AWSCloudInfoRepository awsCloudInfoRepository,
        AzureCloudInfoRepository azureCloudInfoRepository, ExternalApiService externalApiService) {
        this.userRepository = userRepository;
        this.awsCloudInfoRepository = awsCloudInfoRepository;
        this.azureCloudInfoRepository = azureCloudInfoRepository;
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
        externalApiService.postToSpiderDriver(
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
        externalApiService.postToSpiderCredential(awsCredentialDTO);

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

        externalApiService.postToSpiderRegion(awsRegionDTO);

        //커넥션 생성
        AWSConfigDTO awsConfigDTO = new AWSConfigDTO(user.getAwsCloudInfo().getConfigName(),
            user.getAwsCloudInfo().getProviderName(),
            user.getAwsCloudInfo().getDriverName(), user.getAwsCloudInfo().getCredentialName(),
            user.getAwsCloudInfo().getRegionName());
        externalApiService.postToSpiderConfig(awsConfigDTO);


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
        externalApiService.deleteSpiderRegion(user.getAwsCloudInfo().getConfigName());

        return "삭제 완료";
    }




}
