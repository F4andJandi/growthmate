package com.wanted.growthmate.learning.lecture.controller;

import com.wanted.growthmate.learning.lecture.domain.dto.*;
import com.wanted.growthmate.learning.lecture.service.LectureService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sections/{sectionId}/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public String list(@PathVariable Long sectionId, Model model) {
        List<LectureSummaryResponse> lectures = lectureService.findBySectionId(sectionId);
        model.addAttribute("lectures", lectures);
        model.addAttribute("sectionId", sectionId);
        return "lecture/list";
    }

    @GetMapping("/{lectureId}")
    public String detail(@PathVariable Long sectionId, @PathVariable Long lectureId, Model model) {
        LectureResponse lecture = lectureService.findByLectureId(lectureId);
        model.addAttribute("lecture", lecture);
        model.addAttribute("sectionId", sectionId);
        return "lecture/detail";
    }

    @PostMapping
    public String create(@PathVariable Long sectionId, @Valid @ModelAttribute LectureCreateRequest request, RedirectAttributes redirectAttributes) {
        request = LectureCreateRequest.builder()
                .courseId(request.getCourseId())
                .sectionId(sectionId)
                .title(request.getTitle())
                .duration(request.getDuration())
                .mediaId(request.getMediaId())
                .order(request.getOrder())
                .isVisible(request.isVisible())
                .build();
        lectureService.save(request);
        redirectAttributes.addFlashAttribute("message", "강의가 생성되었습니다.");
        return "redirect:/sections/" + sectionId + "/lectures";
    }

    @PostMapping("/{lectureId}/update")
    public String update(@PathVariable Long sectionId, @PathVariable Long lectureId, 
                        @Valid @ModelAttribute LectureUpdateRequest request, RedirectAttributes redirectAttributes) {
        lectureService.updateInfo(lectureId, request);
        redirectAttributes.addFlashAttribute("message", "강의가 수정되었습니다.");
        return "redirect:/sections/" + sectionId + "/lectures";
    }

    @PostMapping("/{lectureId}/order")
    public String updateOrder(@PathVariable Long sectionId, @PathVariable Long lectureId, 
                             @Valid @ModelAttribute LectureOrderUpdateRequest request, RedirectAttributes redirectAttributes) {
        lectureService.updateOrder(lectureId, request);
        redirectAttributes.addFlashAttribute("message", "순서가 변경되었습니다.");
        return "redirect:/sections/" + sectionId + "/lectures";
    }

    @PostMapping("/{lectureId}/delete")
    public String delete(@PathVariable Long sectionId, @PathVariable Long lectureId, RedirectAttributes redirectAttributes) {
        lectureService.delete(lectureId);
        redirectAttributes.addFlashAttribute("message", "강의가 삭제되었습니다.");
        return "redirect:/sections/" + sectionId + "/lectures";
    }

    @PostMapping("/{lectureId}/soft-delete")
    public String softDelete(@PathVariable Long sectionId, @PathVariable Long lectureId, RedirectAttributes redirectAttributes) {
        lectureService.softDelete(lectureId);
        redirectAttributes.addFlashAttribute("message", "강의가 비활성화되었습니다.");
        return "redirect:/sections/" + sectionId + "/lectures";
    }
}

