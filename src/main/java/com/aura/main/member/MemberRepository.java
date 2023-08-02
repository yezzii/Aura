package com.aura.main.member;

import com.aura.main.model.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberDTO, Long> {
        // memberdto에 id,password가 존재하는지 조회
        MemberDTO findByMemberIdAndMemberPwd(String memberId,String memberPwd);

        // memberdto id가 있는지 조회
        MemberDTO findByMemberId(String memberId);

        //memberdto password가 존재하는지 조회
        MemberDTO findByMemberPwd(String memberPwd);

        // memberdto에 kakaoid가 존재하는지 조회
        MemberDTO findByKakaoId(String kakaoId);

}
