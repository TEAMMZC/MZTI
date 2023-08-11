package com.example.mzti_server.controller;

import com.example.mzti_server.dto.Member.LoginRequestDTO;
import com.example.mzti_server.dto.Member.LoginResponseDTO;
import com.example.mzti_server.dto.Member.SignupRequestDTO;
import com.example.mzti_server.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String userInfo() {
        return null;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return memberService.login(loginRequestDTO.getLoginId(), loginRequestDTO.getPassword());
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        return memberService.signup(signupRequestDTO.getLoginId(), signupRequestDTO.getPassword(), signupRequestDTO.getUsername(), signupRequestDTO.getMbti());
    }

//    @PostMapping("/signup")
//    public String signup(String id, String password, String nickname, String mbti, MultipartFile profileImage){
//        return memberService.signup(id, password, nickname, mbti, profileImage);
//    }


}