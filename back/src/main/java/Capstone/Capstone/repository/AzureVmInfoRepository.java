package Capstone.Capstone.repository;

import Capstone.Capstone.domain.AWSVmInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AzureVmInfoRepository extends JpaRepository<AWSVmInfo,Long> {

}
