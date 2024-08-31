package Capstone.Capstone.repository;

import Capstone.Capstone.domain.AWSVmInfo;
import Capstone.Capstone.domain.OpenStackVmInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenStackVmInfoRepository extends JpaRepository<OpenStackVmInfo,Long> {
    public void deleteByIp(String ip);
    public Optional<OpenStackVmInfo> findByIpIs(String ip);
}
