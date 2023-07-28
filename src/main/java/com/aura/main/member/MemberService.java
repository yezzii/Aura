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
}
