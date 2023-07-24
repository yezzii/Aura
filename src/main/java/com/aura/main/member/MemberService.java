package com.aura.main.member;

import com.aura.main.model.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<MemberDTO> memberList() {

        return memberRepository.findAll();
    }
}
