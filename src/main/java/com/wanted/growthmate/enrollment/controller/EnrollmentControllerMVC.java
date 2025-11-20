package com.wanted.growthmate.enrollment.controller;

import com.wanted.growthmate.enrollment.dto.EnrollmentCreateRequest;
import com.wanted.growthmate.enrollment.dto.EnrollmentResponse;
import com.wanted.growthmate.enrollment.entity.Enrollment;
import com.wanted.growthmate.enrollment.entity.Status;
import com.wanted.growthmate.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentControllerMVC {

    private EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentControllerMVC(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/myList")
    public String myEnrollments(Model model) {
        Long userId = 1L;   // userId를 어디서 받음?
        Status status = Status.ACTIVE;
        List<Enrollment> enrollments = enrollmentService.findEnrollmentByUserId(userId, status);

        model.addAttribute("userId", userId);
        model.addAttribute("enrollments",enrollments);
        return "enrollment/enrollments";
    }

    @GetMapping("/edit")
    public String editEnrollment(Model model) {
        Long userId = 1L;   // userId를 어디서 받음?
        Status status = Status.ACTIVE;
        List<Enrollment> enrollments = enrollmentService.findEnrollmentByUserId(userId,status);
        model.addAttribute("enrollments",enrollments);
        return "enrollment/edit";
    }

    @GetMapping("/hiddenList")
    public String showHiddenEnrollment(Model model) {
        Long userId = 1L;
        Status status = Status.HIDDEN;
        List<Enrollment> hiddenEnrollments = enrollmentService.findEnrollmentByUserId(userId,status);
        model.addAttribute("enrollments",hiddenEnrollments);
        model.addAttribute("userId", userId);
        return "enrollment/hide";
    }

    // 테스트 용
    @ResponseBody
    @PostMapping("/addEnrollment")
    public EnrollmentResponse addEnrollment(@RequestBody @Valid EnrollmentCreateRequest request) {
        Enrollment enrollment = enrollmentService.createEnrollment(request);
        EnrollmentResponse enrollmentResponse = new EnrollmentResponse(enrollment);
        return enrollmentResponse;
    }

    @PostMapping("/change-order")
    public String changeOrder(@RequestParam Long enrollmentId, @RequestParam Long newOrderNum) {
        if (enrollmentId == null || newOrderNum == null) {
            throw new IllegalArgumentException("순서 변경에 문제가 발생");
        }
        enrollmentService.changeOrder(enrollmentId, newOrderNum);
        return "redirect:/enrollment/edit";
    }

    @PostMapping("/hide")
    public String hideEnrollment(@RequestParam Long enrollmentId) {
        if (enrollmentId == null) {
            throw new IllegalArgumentException("강좌를 숨기는데 문제가 발생했습니다.");
        }
        enrollmentService.updateEnrollmentStatus(enrollmentId, Status.HIDDEN);
        return "redirect:/enrollment/edit";
    }

    @PostMapping("/restore")
    public String restoreEnrollment(@RequestParam Long enrollmentId) {
        if (enrollmentId == null) {
            throw new IllegalArgumentException("복구에 문제 발생");
        }
        enrollmentService.updateEnrollmentStatus(enrollmentId, Status.ACTIVE);
        return "redirect:/enrollment/hiddenList";
    }

    @PostMapping("/refund")
    public String refundEnrollment(@RequestParam Long enrollmentId) {
        if (enrollmentId == null) {
            throw new IllegalArgumentException("환불에 문제 발생");
        }
        enrollmentService.updateEnrollmentStatus(enrollmentId, Status.REFUNDED);
        return "redirect:/enrollment/edit";
    }
}
