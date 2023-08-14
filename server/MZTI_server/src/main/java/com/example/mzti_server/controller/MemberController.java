package com.example.mzti_server.controller;

import com.example.mzti_server.domain.Member;
import com.example.mzti_server.dto.FriendListDTO;
import com.example.mzti_server.dto.Member.LoginIdDTO;
import com.example.mzti_server.dto.Member.LoginRequestDTO;
import com.example.mzti_server.dto.Member.LoginResponseDTO;
import com.example.mzti_server.dto.Member.SignupRequestDTO;
import com.example.mzti_server.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Tag(name = "members", description = "사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "멤버 정보 조회", description = "멤버의 로그인 아이디를 통해 멤버의 상세정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @ApiResponse(responseCode = "400", description = "오류")
    })
    @GetMapping("/info")
    public ResponseEntity<LinkedHashMap<String, Object>> userInfo(@RequestBody LoginIdDTO loginIdDTO) {
        String loginId = loginIdDTO.getLoginId();
        return memberService.findMemberByLoginId(loginId);
    }

    @Operation(summary = "멤버 토큰 조회", description = "토큰을 이용해 멤버정보를 조회합니다.")
    @GetMapping("")
    public ResponseEntity<LinkedHashMap<String, Object>> userInfoByToken(HttpServletRequest request) {
        return memberService.findMemberByToken(request.getHeader("Authorization"));
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 통해 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<LinkedHashMap<String, Object>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return memberService.login(loginRequestDTO.getLoginId(), loginRequestDTO.getPassword());
    }

    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 이름, mbti를 이용하여 회원가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<LinkedHashMap<String, Object>> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        return memberService.signup(signupRequestDTO.getLoginId(), signupRequestDTO.getPassword(), signupRequestDTO.getUsername(), signupRequestDTO.getMbti());
    }

    @Operation(summary = "아이디 중복 체크", description = "아이디 중복 여부를 체크합니다.")
    @GetMapping("/isDuplicate")
    public ResponseEntity<LinkedHashMap<String, Object>> checkId(@RequestBody LoginIdDTO loginIdDTO){
        return memberService.checkId(loginIdDTO.getLoginId());
    }

    @Operation(summary = "친구 관계 확인", description = "친구 관계 목록을 확인합니다.")
    @GetMapping("/friendList")
    public ResponseEntity<LinkedHashMap<String, Object>> friendlist(HttpServletRequest request) {
        return memberService.findFriendListByLoginId(request.getHeader("Authorization"));
    }

    @Operation(summary = "친구 추가", description = "친구 ID를 통해 친구를 추가합니다.")
    @PostMapping("/addFriend")
    public ResponseEntity<LinkedHashMap<String, Object>> addFriend(HttpServletRequest request, @RequestBody LoginIdDTO loginIdDTO) {
        return memberService.addFriend(request.getHeader("Authorization"), loginIdDTO.getLoginId());
    }

//    @PostMapping("/signup")
//    public String signup(String id, String password, String nickname, String mbti, MultipartFile profileImage){
//        return memberService.signup(id, password, nickname, mbti, profileImage);
//    }


}
