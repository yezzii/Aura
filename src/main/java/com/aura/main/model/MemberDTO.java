package com.aura.main.model;

import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "member")
public class MemberDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;
    private String memberId;
    private String memberName;
    private String memberPwd;
    private String memberPhone;
    private String memberEmail;
    private String addrPost;
    private String addrDetail;
    private String memberBirth;
    private String memberSex;
    private String memberType;
    private String mailKey;
    private String mailAuth;
    private Integer marketing;
    private Long point;
}
