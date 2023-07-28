package com.aura.main.member;

import com.aura.main.model.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<MemberDTO> memberList() {   return memberRepository.findAll();    }

    public MemberDTO getMemberProfile(Long no) {    return (MemberDTO) memberRepository.findById(no).get();    }
   // 아이디와 비밀번호로 회원 정보를 조회하여 일치하는 회원이 있는지 확인하는 메서드
    public MemberDTO loginCheck(String memberId,String memberPwd){
        return memberRepository.findByMemberIdAndMemberPwd(memberId,memberPwd);
    }
}
