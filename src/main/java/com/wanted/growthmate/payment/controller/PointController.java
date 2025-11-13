package com.wanted.growthmate.payment.controller;

import com.wanted.growthmate.payment.domain.Point;
import com.wanted.growthmate.payment.dto.PointChargeRequest;
import com.wanted.growthmate.payment.dto.PointTransactionSummary;
import com.wanted.growthmate.payment.service.PaymentService;
import com.wanted.growthmate.payment.service.PointService;
import com.wanted.growthmate.payment.service.PointTransactionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;
    private final PointTransactionService pointTransactionService;
    private final PaymentService paymentService;

    @GetMapping
    public String getPointTransactionsPage(Model model, HttpSession session){
        Long userId = (Long) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/login";
        }

        // 포인트 잔액 조회 (없으면 생성)
        Point point = pointService.getOrCreatePoint(userId);

        // 포인트 내역 조회
        List<PointTransactionSummary> pointTransactions = pointTransactionService.getTransactionSummaries(point);

        // 모델에 데이터 전달
        model.addAttribute("currentPoints", point.getBalance());
        model.addAttribute("pointTransactions", pointTransactions);

        // 페이지 렌더링
        return "points/transactions";
    }

    @GetMapping("/charge")
    public String chargePage(Model model, HttpSession session, RedirectAttributes redirectAttributes){
        Long userId = (Long) session.getAttribute("loginUserId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }
        model.addAttribute("reqBody", new PointChargeRequest());
        return "points/charge";
    }

    @PostMapping("/charge")
    public String chargePoints(
            @Valid @ModelAttribute("reqBody") PointChargeRequest reqBody,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute("loginUserId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        // 유효성 검사 실패 시 사용자가 입력하던 페이지로 복귀
        if (bindingResult.hasErrors()) {
            return "points/charge";
        }

        try {
            // 포인트 충전
            paymentService.chargePoints(userId, reqBody);
            redirectAttributes.addFlashAttribute("message", "포인트 충전이 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "포인트 충전에 실패했습니다: " + e.getMessage());
            return "redirect:/points/charge";
        }

        // 성공 시 포인트 내역 페이지로 리다이렉트
        return "redirect:/points";
    }
}
