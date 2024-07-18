package Capstone.Capstone.service;

import Capstone.Capstone.domain.User;
import Capstone.Capstone.repository.AWSCloudInfoRepository;
import Capstone.Capstone.repository.AzureCloudInfoRepository;
import Capstone.Capstone.repository.UserRepository;
import Capstone.Capstone.service.dto.CloudDriver;
import Capstone.Capstone.utils.error.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
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

    public String conAWS(Long id){
        User user = userRepository.findByUserIdWithAWSCloudInfo(id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        CloudDriver cloudDriver = new CloudDriver(user.getAwsCloudInfo().getDriverName(),
            user.getAwsCloudInfo().getProviderName(),
            user.getAwsCloudInfo().getDriverLibFileName());
        CloudDriver cloudDriverResponse = externalApiService.postToSpiderDriver(cloudDriver);

        return "생성 완료";
    }


}
