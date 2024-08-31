package Capstone.Capstone.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockChainNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String networkName;
    @Column
    private String caCSP;
    @Column
    private String caIP;
    @Column
    private String caSecretKey;
    @Column
    private String orgCSP;
    @Column
    private String orgIP;
    @Column
    private String orgSecretKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userInfo;

    public BlockChainNetwork(String networkName, String caCSP, String caIP, String caSecretKey,
        String orgCSP, String orgIP, String orgSecretKey, User userInfo) {
        this.networkName = networkName;
        this.caCSP = caCSP;
        this.caIP = caIP;
        this.caSecretKey = caSecretKey;
        this.orgCSP = orgCSP;
        this.orgIP = orgIP;
        this.orgSecretKey = orgSecretKey;
        this.userInfo = userInfo;
    }
}
