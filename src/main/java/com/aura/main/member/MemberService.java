package com.aura.main.member;

import com.aura.main.model.MemberDTO;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.tomcat.util.json.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    // 아이디와 비밀번호로 회원 정보를 조회하여 일치하는 회원이 있는지 확인하는 메서드
    public MemberDTO loginCheck(String memberId, String memberPwd) {
        return memberRepository.findByMemberIdAndMemberPwd(memberId, memberPwd);
    }

    // 카카오 로그인
    public String getToken(String code) throws IOException{
        //인가코드로 토큰 받기
        String host="https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        String token = "";
        try{
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=2cba3134e81cfb884bccc490cd964f4a"); //rest값
            sb.append("&redirect_uri=http://localhost:8989/kakao/login"); //redurect_uri 값
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while((line = br.readLine()) != null){
                result += line;
            }
            System.out.println("result ="+ result);

            //Json parsing
            JsonParser parser = new JsonParser();
            JsonObject elem = (JsonObject)parser.parse(result);

            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();
            System.out.println("access_token =" + access_token);
            System.out.println("refresh_token =" + refresh_token);

            token = access_token;

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

}

