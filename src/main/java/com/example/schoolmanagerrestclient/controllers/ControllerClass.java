package com.example.schoolmanagerrestclient.controllers;

import com.example.schoolmanagerrestclient.dtos.SchoolUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
//@RequestMapping("/aB")
public class ControllerClass {

    @Autowired
    OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/displayUserInfo")
    public SchoolUserDTO getUserInfo(@AuthenticationPrincipal OidcUser user){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId() , oAuth2AuthenticationToken.getName());
        String JWTAccesstoken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization" , "Bearer " + JWTAccesstoken);

        HttpEntity entity = new HttpEntity<>(headers);

        if(user.getAttribute("database-id") == null){
            restTemplate.exchange("http://localhost:8082/registerUser" , HttpMethod.POST , entity , (Class<Object>) null);
        }
        
        ResponseEntity<SchoolUserDTO> responseEntity = restTemplate.exchange("http://localhost:8082/getUserInfo", HttpMethod.GET, entity, new ParameterizedTypeReference<SchoolUserDTO>() {});
        return responseEntity.getBody();
    }

    @PutMapping("/addInfoToUser")
    public String addInfoToUser(@AuthenticationPrincipal OidcUser user , @RequestBody Map<String , Object> body){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId() , oAuth2AuthenticationToken.getName());
        String JWTAccesstoken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization" , "Bearer " + JWTAccesstoken);

        HttpEntity entity = new HttpEntity<>(headers);

        if(user.getAttribute("database-id") == null){
            restTemplate.exchange("http://localhost:8082/registerUser" , HttpMethod.POST , entity , (Class<Object>) null);
        }

        Integer ageValue = null;
        String genderValue = null;
        String mobileNumberValue = null;

        if(body.containsKey("age")){
            ageValue = (Integer)body.get("age");
        }

        if(body.containsKey("mobileNumber")){
            mobileNumberValue = (String)body.get("mobileNumber");
        }

        if(body.containsKey("gender")){
            genderValue = (String)body.get("gender");
        }

        if(ageValue == null && genderValue == null && mobileNumberValue == null){
            return "fail";
        }

        HttpEntity entity2 = new HttpEntity<>(body , headers);

        restTemplate.postForEntity("http://localhost:8082/addInfoToUser" , entity2 , (Class<Object>) null);

        return "success";
    }

}
