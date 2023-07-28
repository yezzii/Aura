package com.aura.main.member;

import com.aura.main.model.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberDTO, Long> {
    
    MemberDTO findByMemberIdAndMemberPwd(String memberId,String memberPwd);
}
