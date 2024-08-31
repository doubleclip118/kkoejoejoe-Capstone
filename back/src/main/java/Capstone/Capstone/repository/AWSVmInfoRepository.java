package Capstone.Capstone.repository;

import Capstone.Capstone.domain.AWSVmInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AWSVmInfoRepository extends JpaRepository<AWSVmInfo,Long> {
    public void deleteByIp(String ip);

    public Optional<AWSVmInfo> findByIpIs(String ip);
}
