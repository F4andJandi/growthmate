package com.wanted.growthmate.learning.lecture.controller;

import com.wanted.growthmate.learning.course.service.CourseService;
import com.wanted.growthmate.learning.lecture.domain.dto.*;
import com.wanted.growthmate.learning.lecture.service.LectureService;
import com.wanted.growthmate.learning.section.service.SectionService;
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
    private final SectionService sectionService;
    private final CourseService courseService;

    public LectureController(LectureService lectureService, SectionService sectionService, CourseService courseService) {
        this.lectureService = lectureService;
        this.sectionService = sectionService;
        this.courseService = courseService;
    }

    @GetMapping
    public String list(@PathVariable Long sectionId, Model model) {
        List<LectureSummaryResponse> lectures = lectureService.findBySectionId(sectionId);
        com.wanted.growthmate.learning.section.domain.dto.SectionResponse section = 
                sectionService.findBySectionId(sectionId);
        
        // 강좌 작성자 ID 조회
        com.wanted.growthmate.learning.course.domain.entity.Course course = 
                courseService.getCourseById(section.getCourseId());
        Long courseUserId = course.getUserId();
        
        model.addAttribute("lectures", lectures);
        model.addAttribute("sectionId", sectionId);
        model.addAttribute("section", section);
        model.addAttribute("courseUserId", courseUserId);
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

