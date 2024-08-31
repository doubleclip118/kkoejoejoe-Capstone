package Capstone.Capstone.repository;

import Capstone.Capstone.domain.AWSVmInfo;
import Capstone.Capstone.domain.AzureVmInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AzureVmInfoRepository extends JpaRepository<AzureVmInfo,Long> {
    public void deleteByIp(String ip);
    public Optional<AzureVmInfo> findByIpIs(String ip);
}
