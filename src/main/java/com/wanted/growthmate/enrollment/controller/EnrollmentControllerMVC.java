package com.wanted.growthmate.enrollment.controller;


import com.wanted.growthmate.enrollment.entity.Enrollment;
import com.wanted.growthmate.enrollment.entity.Status;
import com.wanted.growthmate.enrollment.service.EnrollmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EnrollmentControllerMVC {

    private EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentControllerMVC(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/myc")
    public String myCourses(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUserId");
        if (userId == null) {
            // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }
        
        Status status = Status.ACTIVE;
        List<Enrollment> enrollments = enrollmentService.findEnrollmentByUserId(userId, status);

        model.addAttribute("userId", userId);
        model.addAttribute("enrollments", enrollments);
        return "enrollment/my";
    }

    @GetMapping("/edit")
    public String editEnrollment(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Status status = Status.ACTIVE;
        List<Enrollment> enrollments = enrollmentService.findEnrollmentByUserId(userId, status);
        model.addAttribute("enrollments", enrollments);
        return "enrollment/editt";
    }

    @PostMapping("/change-order")
    public String changeOrder(@RequestParam Long enrollmentId, @RequestParam Long newOrderNum) {
        enrollmentService.changeOrder(enrollmentId, newOrderNum);
        return "redirect:/edit";
    }

    @PostMapping("/hide")
    public String hideEnrollment(@RequestParam Long enrollmentId) {
        enrollmentService.updateEnrollmentStatus(enrollmentId, Status.HIDDEN);
        return "redirect:/edit";
    }

    @GetMapping("/hidden")
    public String showHiddenEnrollment(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUserId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        Status status = Status.HIDDEN;
        List<Enrollment> hiddenEnrollments = enrollmentService.findEnrollmentByUserId(userId, status);
        model.addAttribute("enrollments", hiddenEnrollments);
        model.addAttribute("userId", userId);
        return "enrollment/hid";
    }

    @PostMapping("/restore")
    public String restoreEnrollment(@RequestParam Long enrollmentId) {
        enrollmentService.updateEnrollmentStatus(enrollmentId, Status.ACTIVE);
        return "redirect:/edit";
    }

    @PostMapping("/refund")
    public String refundEnrollment(@RequestParam Long enrollmentId) {
        enrollmentService.updateEnrollmentStatus(enrollmentId, Status.REFUNDED);
        return "redirect:/edit";
    }
}
