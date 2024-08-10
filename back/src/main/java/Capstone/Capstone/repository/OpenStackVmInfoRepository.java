package Capstone.Capstone.repository;

import Capstone.Capstone.domain.AWSVmInfo;
import Capstone.Capstone.domain.OpenStackVmInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenStackVmInfoRepository extends JpaRepository<OpenStackVmInfo,Long> {

}
