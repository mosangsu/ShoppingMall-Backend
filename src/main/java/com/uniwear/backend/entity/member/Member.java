package com.uniwear.backend.entity.member;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.uniwear.backend.entity.DeliveryAddress;
import com.uniwear.backend.entity.MemberCoupon;
import com.uniwear.backend.entity.Membership;
import com.uniwear.backend.enums.MemberType;
import com.uniwear.backend.enums.Role;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "member")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true)
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String realname;
    private String nickname;

    @Convert(converter=MemberType.MemberTypeConverter.class)
    @Column(insertable=false, updatable=false)
    private MemberType type;
    private String email;
    private String phone;
    
    @Column(columnDefinition = "BIT")
    private int receiveEmailYn;
    @Column(columnDefinition = "BIT")
    private int receiveSmsYn;
    @Column(columnDefinition = "BIT")
    private int deniedYn;
    @Column(columnDefinition = "BIT")
    private int emailCertYn;
    
    @Column(columnDefinition = "TINYINT")
    private int level;
    private int point;
    private int totalPurchaseAmount;
    
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_NOT_PERMITTED;
    
    private Date lastLoggedinAt;
    private Date createdAt;

    // 배송지는 조회할 때 기본 배송지 1개만 조회함
    // Member와 함께 일괄적으로 추가할 경우가 없기 때문에 new 선언하지 않음
    @OneToMany(mappedBy = "member")
    @Where(clause = "default_yn = true")
    private List<DeliveryAddress> deliveryAddress;

    @OneToMany(mappedBy = "member")
    private List<MemberCoupon> memberCoupon;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="membership_id")
    private Membership membership;

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
    }
}