package Capstone.Capstone.repository;

import Capstone.Capstone.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String name);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.awsCloudInfo WHERE u.id = :id")
    Optional<User> findByUserIdWithAWSCloudInfo(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.azureCloudInfo WHERE u.id = :id")
    Optional<User> findByUserIdWithAzureCloudInfo(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.openstackCloudInfo WHERE u.id = :id")
    Optional<User> findByUserIdWithOpenstackCloudInfo(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.awsVmInfos WHERE u.id = :id")
    Optional<User> findByUserIdWithVAndAwsVmInfos(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.azureVmInfos WHERE u.id = :id")
    Optional<User> findByUserIdWithVAndAzureVmInfos(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.openStackVmInfos WHERE u.id = :id")
    Optional<User> findByUserIdWithVAndOpenstackVmInfos(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.blockChainNetworks WHERE u.id = :id")
    Optional<User> findByUserIdWithVAndbAndBlockChainNetworks(@Param("id") Long id);
}
