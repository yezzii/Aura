package com.aura.main.member;

import com.aura.main.model.MemberDTO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    //카카오 로그인 주소
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;
    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    private final static  String KAKAO_AUTH_URL = "https://kauth.kakao.com";
    private final static String KAKAO_API_URL = "http://kapi.kakao.com";


    // 아이디와 비밀번호로 회원 정보를 조회하여 일치하는 회원이 있는지 확인하는 메서드
    public MemberDTO loginCheck(String memberId, String memberPwd) {
        return memberRepository.findByMemberIdAndMemberPwd(memberId, memberPwd);
    }



        //카카오 로그인 주소
        public String KakaoLogin() {
            return KAKAO_AUTH_URL + "/oauth/authorize"
                    + "?client_id=" + KAKAO_CLIENT_ID
                    + "&redirect_uri=" + KAKAO_REDIRECT_URL
                    + "&response_type=code";
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
            sb.append("&client_id="+KAKAO_CLIENT_ID); //rest값
            sb.append("&redirect_uri="+KAKAO_REDIRECT_URL); //redurect_uri 값
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
    //카카오 사용자 정보 받아오기
    public Map<String,Object> getUserInfo(String access_token)throws IOException{
        String host = "https://kapi.kakao.com/v2/user/me";
        //요청하는 클라이언트마다 가진 정보가 다를 수 있기때문에 HaspMap타입으로 선언
        Map<String,Object> result = new HashMap<>();
        // access_token을 이용하여 사용자 정보 조회

        try {
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 요청에 필요한 Header에 포함될 내용
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
            urlConnection.setRequestMethod("GET");

            // 결과 코드 200 성공,,
            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);


            //요청을 통해 얻은 JSON타입의 RESPONSE 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while((line=br.readLine())!=null)
            {
                res+=line;
            }

            System.out.println("res = " + res);

            //Gson 라이브러리로 json 파싱
            JsonParser parser = new JsonParser();
            JsonObject obj  = (JsonObject) parser.parse(res);
            JsonObject kakao_account = (JsonObject)obj.get("kakao_account");
            JsonObject properties = (JsonObject)obj.get("properties");


            String id = obj.get("id").toString(); //고유 코드(ID) 추출
            String nickname = properties.get("nickname").toString();
            String profile = properties.get("profile_image").toString();
            String age_range = kakao_account.get("age_range").toString();
            String email = kakao_account.get("email").toString();
            String birthday = kakao_account.get("birthday").toString();
            String gender = kakao_account.get("gender").toString();

            result.put("id", id); // result에 정보추가
            result.put("nickname", nickname);
            result.put("profile", profile);
            result.put("age_range", age_range);
            result.put("email", email);
            result.put("birthday", birthday);
            result.put("gender", gender);


        br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }

    // KAKAO 로그인 DB에 카카오 유저ID가 있는지 체크하는 메서드
    public MemberDTO kakaologinCheck(String kakao_id) {
        return memberRepository.findByKakaoId(kakao_id);
    }



}

