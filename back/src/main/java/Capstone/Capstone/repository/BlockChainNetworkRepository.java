package Capstone.Capstone.repository;

import Capstone.Capstone.domain.BlockChainNetwork;
import Capstone.Capstone.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlockChainNetworkRepository extends JpaRepository<BlockChainNetwork, Long> {

}
