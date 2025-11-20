package com.wanted.growthmate.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    /**
     * 시작 페이지 (로그인/회원가입 선택)
     * URL: http://localhost:8080/
     */
    @GetMapping("/")
    public String index() {
        return "index"; // -> templates/index.html 파일을 찾음
    }

    /**
     * 로그인 페이지
     * URL: http://localhost:8080/login
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // -> templates/login.html 파일을 찾음
    }

    /**
     * 회원가입 페이지
     * URL: http://localhost:8080/signup
     */
    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // -> templates/signup.html 파일을 찾음
    }

    /**
     * 메인 페이지 (로그인 성공 후)
     * URL: http://localhost:8080/main
     */
    @GetMapping("/main")
    public String mainPage() {
        return "main"; // -> templates/main.html 파일을 찾음
    }
}