package Capstone.Capstone.repository;

import Capstone.Capstone.domain.AWSCloudInfo;
import Capstone.Capstone.domain.OpenstackCloudInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenstackCloudInfoRepository extends JpaRepository<OpenstackCloudInfo,Long> {

}
