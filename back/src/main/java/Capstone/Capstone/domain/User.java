package Capstone.Capstone.domain;

import Capstone.Capstone.controller.dto.UserResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_tb")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private AWSCloudInfo awsCloudInfo;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AWSVmInfo> awsVmInfos = new ArrayList<>();

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "azure_cloud_info_id")
    private AzureCloudInfo azureCloudInfo;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AzureVmInfo> azureVmInfos = new ArrayList<>();

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "openstack_cloud_info_id")
    private OpenstackCloudInfo openstackCloudInfo;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpenStackVmInfo> openStackVmInfos = new ArrayList<>();

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlockChainNetwork> blockChainNetworks = new ArrayList<>();


    public UserResponse UserconvertToDTO(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setPassword(user.getPassword());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }

    public void removeAwsCloudInfo(){
        this.setAwsCloudInfo(null);
    }

    public void removeAzureCloudInfo(){
        this.setAzureCloudInfo(null);
    }

    public void removeOpenStackCloudInfo(){
        this.setOpenstackCloudInfo(null);
    }

}
