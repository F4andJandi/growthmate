package com.wanted.growthmate.learning.section.controller;

import com.wanted.growthmate.learning.lecture.domain.dto.LectureSummaryResponse;
import com.wanted.growthmate.learning.lecture.service.LectureService;
import com.wanted.growthmate.learning.section.domain.dto.*;
import com.wanted.growthmate.learning.section.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;
    private final LectureService lectureService;

    public SectionController(SectionService sectionService, LectureService lectureService) {
        this.sectionService = sectionService;
        this.lectureService = lectureService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long courseId, Model model) {
        if (courseId != null) {
            List<SectionSummaryResponse> sections = sectionService.findByCourseId(courseId);
            model.addAttribute("sections", sections);
            model.addAttribute("courseId", courseId);
        }
        return "section/list";
    }

    @GetMapping("/{sectionId}")
    public String detail(@PathVariable Long sectionId, Model model) {
        SectionResponse section = sectionService.findBySectionId(sectionId);
        List<LectureSummaryResponse> lectures = lectureService.findBySectionId(sectionId);
        model.addAttribute("section", section);
        model.addAttribute("lectures", lectures);
        return "section/detail";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute SectionCreateRequest request, RedirectAttributes redirectAttributes) {
        SectionResponse created = sectionService.save(request);
        redirectAttributes.addFlashAttribute("message", "섹션이 생성되었습니다.");
        return "redirect:/sections?courseId=" + request.getCourseId();
    }

    @PostMapping("/{sectionId}/update")
    public String update(@PathVariable Long sectionId, @Valid @ModelAttribute SectionUpdateRequest request, RedirectAttributes redirectAttributes) {
        sectionService.updateInfo(sectionId, request);
        redirectAttributes.addFlashAttribute("message", "섹션이 수정되었습니다.");
        return "redirect:/sections/" + sectionId;
    }

    @PostMapping("/{sectionId}/order")
    public String updateOrder(@PathVariable Long sectionId, @Valid @ModelAttribute SectionOrderUpdateRequest request, RedirectAttributes redirectAttributes) {
        sectionService.updateOrder(sectionId, request);
        redirectAttributes.addFlashAttribute("message", "순서가 변경되었습니다.");
        return "redirect:/sections?courseId=" + request.getCourseId();
    }

    @PostMapping("/{sectionId}/delete")
    public String delete(@PathVariable Long sectionId, @RequestParam Long courseId, RedirectAttributes redirectAttributes) {
        sectionService.delete(sectionId);
        redirectAttributes.addFlashAttribute("message", "섹션이 삭제되었습니다.");
        return "redirect:/sections?courseId=" + courseId;
    }

    @PostMapping("/{sectionId}/soft-delete")
    public String softDelete(@PathVariable Long sectionId, @RequestParam Long courseId, RedirectAttributes redirectAttributes) {
        sectionService.softDelete(sectionId);
        redirectAttributes.addFlashAttribute("message", "섹션이 비활성화되었습니다.");
        return "redirect:/sections?courseId=" + courseId;
    }
}

