package com.aura.main.member;

import com.aura.main.model.MemberDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public int naverLoginCheck(String email, String name){
        return memberRepository.findByMemberEmailAndMemberName(email, name);
    }

    public String getNaverAccessToken(String authorize_code, HttpSession session) throws Exception {

        String access_token = "";
        String refresh_token = "";
        String reqURL = "https://nid.naver.com/oauth2.0/token";
        String clientId = "8ljUOFqbhL8ClN644LoP";
        String clientSecret = "PLsyV7b8qH";//애플리케이션 클라이언트 시크릿값";
        String state = (String) session.getAttribute("state");
        String redirectURI = "http://localhost:8989/naverLogin.go";

        // 요청 할 url 객체 생성
        URL url = new URL(reqURL);

        // URL 연결에 주소값 담긴 객체 전송
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 메서드 post 요청
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();

        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+clientId); //본인이 발급받은 key
        sb.append("&client_secret="+clientSecret); //애플리케이션 클라이언트 시크릿값";
        sb.append("&redirect_uri="+redirectURI); // 본인이 설정한 주소
        sb.append("&code=" + authorize_code);
        sb.append("&state=" + state);

        bw.write(sb.toString());
        bw.flush();

        int responseCode = conn.getResponseCode();

        System.out.println("responseCode : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);

        access_token = element.getAsJsonObject().get("access_token").getAsString();
        refresh_token = element.getAsJsonObject().get("refresh_token").getAsString();

        br.close();
        bw.close();

        return access_token;


    }

    public HashMap<String, Object> getNaverUserInfo(String access_Token) throws Exception {

        HashMap<String, Object> userInfo = new HashMap<String, Object>();

        String reqURL = "https://openapi.naver.com/v1/nid/me";

        URL url = new URL(reqURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + access_Token);

        int responseCode = conn.getResponseCode();

        System.out.println("responseCode : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        System.out.println("response body : " + result);

        JsonParser parser = new JsonParser();

        JsonElement element = parser.parse(result);

        JsonObject response = element.getAsJsonObject().get("response").getAsJsonObject();

        String id = response.getAsJsonObject().get("id").getAsString();
        String name = response.getAsJsonObject().get("name").getAsString();
        String email = response.getAsJsonObject().get("email").getAsString();
        String phone = response.getAsJsonObject().get("mobile").getAsString();
        String birthyear = response.getAsJsonObject().get("birthyear").getAsString();
        String birthday = response.getAsJsonObject().get("birthday").getAsString();
        String gender = response.getAsJsonObject().get("gender").getAsString();

        userInfo.put("id", id);
        userInfo.put("name", name);
        userInfo.put("email", email);
        userInfo.put("phone", phone);
        userInfo.put("birthyear", birthyear);
        userInfo.put("birthday", birthday);
        userInfo.put("gender", gender);

        return userInfo;
    }
   
    }
