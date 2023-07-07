package com.exercise.carrotproject.web.member.controller;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.util.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.ouath.KaKaoOauth;
import com.exercise.carrotproject.domain.member.ouath.KakaoServiceImpl;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.web.member.form.SignupSocialForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OauthController {
    private final KaKaoOauth kaKaoOauth;
    private final KakaoServiceImpl kakaoService;
    private final MemberService memberService;

    @GetMapping("/login/getKakaoLoginURL")
    public String toKakaoLoginForm() {
        String kakaoUrl = kaKaoOauth.getKakaoLoginUrl()
                + "/oauth/authorize?client_id=" + kaKaoOauth.getKakaoClientId()
                + "&redirect_uri=" + kaKaoOauth.getKakaoLoginRedirectUri() + "&response_type=code";
        return "redirect:"+kakaoUrl;
    }

    @GetMapping("/login/kakao")
    public String kakaoLogin(@RequestParam String code, Model model,
                             HttpServletRequest request) throws Throwable {
        HttpSession session = request.getSession();
        //해당 페이지에서 새로고침시 : 인증 code가 파기되어 오류페이지가 뜨지 않도록, login페이지로 이동한다
        String revokedCode = (String) session.getAttribute(SessionConst.KAKAO_REVOKED_CODE);
        if (revokedCode!= null && revokedCode.equals(code)) {
            session.removeAttribute(SessionConst.KAKAO_ACCESS_TOKEN);
            return "redirect:/login";
        }

        //새로고침이 아니라 해당 url로 처음 진입하는 경우
        session.setAttribute(SessionConst.KAKAO_REVOKED_CODE, code);
        String accessToken = kakaoService.getAccessToken(code);
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        log.info("userInfo 프로필 이미지 = {} ", userInfo.get("profPath").toString());
        log.info("userInfo 닉네임 = {} ", userInfo.get("nickname").toString());
        log.info("userInfo 이메일 = {} ", userInfo.get("email").toString());
        String email = userInfo.get("email").toString();

        boolean hasKakaoMember = memberService.hasEmailAndRole(email, Role.SOCIAL_KAKAO);
        if(!hasKakaoMember) {
            session.setAttribute(SessionConst.KAKAO_ACCESS_TOKEN, accessToken);
            model.addAttribute("userInfo",userInfo);
            model.addAttribute("platform", "kakao");
            model.addAttribute("isSocial", true);
            return "member/signupForm";
        }
        Member socialMember = memberService.findMemberByEmailAndRole(email, Role.SOCIAL_KAKAO);
        session.setAttribute(SessionConst.LOGIN_MEMBER, MemberEntityDtoMapper.toMemberDto(socialMember));
        session.removeAttribute(SessionConst.KAKAO_REVOKED_CODE);
        return "redirect:/";
    }

    @GetMapping("/unlink/kakao")
    @ResponseBody
    public String unlink(HttpSession session) throws Throwable {
        String accessToken = (String) session.getAttribute(SessionConst.KAKAO_ACCESS_TOKEN);
        kakaoService.kakaoUnlink(accessToken);
        session.removeAttribute(SessionConst.KAKAO_REVOKED_CODE);
        return "성공";
    }

    @PostMapping("/signup/kakao")
    @ResponseBody
    public ResponseEntity kakaoSignup(@Valid @RequestBody SignupSocialForm signupSocialForm,
                                      BindingResult result,
                                      HttpSession session,
                                      Model model) throws Throwable {
        HashMap<String, Object> errorMap = new HashMap<>();
        if(result.hasErrors()) {
            errorMap.put("nickname", result.getFieldError("nickname").getDefaultMessage());
            return ResponseEntity.badRequest().body(errorMap);
        }

        MemberDto memberDto = MemberDto.builder()
                .email(signupSocialForm.getEmail())
                .profPath(signupSocialForm.getProfPath())
                .loc(signupSocialForm.getLoc())
                .nickname(signupSocialForm.getNickname())
                .build();
        Map<String, Object> saveResult= memberService.insertSocialMember(memberDto);

        session.setAttribute(SessionConst.LOGIN_MEMBER, saveResult.get("memberDto"));
        session.removeAttribute(SessionConst.KAKAO_REVOKED_CODE);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout/getKakaoLogoutURL")
    @ResponseBody
    public String toKakaoLogoutForm(HttpSession session) {
        String kakaoUrl = kaKaoOauth.getKakaoLoginUrl()
                + "/oauth/logout?client_id=" + kaKaoOauth.getKakaoClientId()
                + "&logout_redirect_uri=" + kaKaoOauth.getKakaoLogoutRedirectUri();
        return kakaoUrl;
    }
}





