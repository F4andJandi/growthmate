package com.wanted.growthmate.learning.lecture.service;

import com.wanted.growthmate.learning.lecture.domain.dto.LectureCreateRequest;
import com.wanted.growthmate.learning.lecture.domain.dto.LectureResponse;
import com.wanted.growthmate.learning.lecture.domain.dto.LectureUpdateRequest;
import com.wanted.growthmate.learning.lecture.domain.dto.LectureOrderUpdateRequest;
import com.wanted.growthmate.learning.lecture.domain.dto.LectureSummaryResponse;
import com.wanted.growthmate.learning.lecture.domain.entity.Lecture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LectureServiceImplTest {

    @Autowired
    LectureServiceImpl lectureService;

    @Test
    void save() {
        LectureResponse result = lectureService.save(
                LectureCreateRequest.builder()
                        .sectionId(1L)
                        .title("테스트 강의")
                        .mediaId(1L)
                        .courseId(1L)
                        .order(1)
                        .duration(10L)
                        .build()
        );
        Assertions.assertTrue(result.isVisible());
    }

    @Test
    void findBySectionId() {
        LectureResponse result = lectureService.save(LectureCreateRequest.builder().sectionId(1L).title("제목1").mediaId(1l).courseId(1l).order(1).duration(1l).build());

        Assertions.assertTrue(result.isVisible());

        List<LectureSummaryResponse> result1 = lectureService.findBySectionId(1L);
        Assertions.assertTrue(result1.size() == 0, "section 번허로 된게 없음");

    }

    @Test
    void updateInfo() {
        // given: 하나 생성
        LectureResponse created = lectureService.save(LectureCreateRequest.builder()
                .sectionId(10L)
                .title("원본 제목")
                .mediaId(10L)
                .courseId(10L)
                .order(1)
                .duration(100L)
                .build());

        // when: 정보 수정
        LectureUpdateRequest request = LectureUpdateRequest.builder()
                .sectionId(20L)
                .title("수정된 제목")
                .duration(200L)
                .mediaId(20L)
                .isVisible(false)
                .build();
        LectureResponse updated = lectureService.updateInfo(created.getLectureId(), request);

        // then
        Assertions.assertEquals(20L, updated.getSectionId());
        Assertions.assertEquals("수정된 제목", updated.getTitle());
        Assertions.assertEquals(200L, updated.getDuration());
        Assertions.assertEquals(20L, updated.getMediaId());
        Assertions.assertFalse(updated.isVisible());
    }

    @Test
    void updateOrder() {
        // given: 같은 섹션에 5개 생성 (order 1..5)
        long sectionId = 99L;
        LectureResponse l1 = lectureService.save(LectureCreateRequest.builder().sectionId(sectionId).title("L1").mediaId(1L).courseId(1L).order(1).duration(1L).build());
        LectureResponse l2 = lectureService.save(LectureCreateRequest.builder().sectionId(sectionId).title("L2").mediaId(2L).courseId(1L).order(2).duration(1L).build());
        LectureResponse l3 = lectureService.save(LectureCreateRequest.builder().sectionId(sectionId).title("L3").mediaId(3L).courseId(1L).order(3).duration(1L).build());
        LectureResponse l4 = lectureService.save(LectureCreateRequest.builder().sectionId(sectionId).title("L4").mediaId(4L).courseId(1L).order(4).duration(1L).build());
        LectureResponse l5 = lectureService.save(LectureCreateRequest.builder().sectionId(sectionId).title("L5").mediaId(5L).courseId(1L).order(5).duration(1L).build());

        // when: 2번째( order=2 )와 5번째( order=5 ) 순서 교환
        LectureOrderUpdateRequest swapWith5 = LectureOrderUpdateRequest.builder()
                .sectionId(sectionId)
                .newOrder(5) // 교환 대상의 displayOrder
                .build();
        lectureService.updateOrder(l2.getLectureId(), swapWith5);

        // then: 각 항목의 displayOrder가 기대대로 바뀌었는지 확인
        List<LectureSummaryResponse> summaries = lectureService.findBySectionId(sectionId);
        Integer orderL1 = summaries.stream().filter(s -> s.getTitle().equals("L1")).findFirst().get().getOrder();
        Integer orderL2 = summaries.stream().filter(s -> s.getTitle().equals("L2")).findFirst().get().getOrder();
        Integer orderL3 = summaries.stream().filter(s -> s.getTitle().equals("L3")).findFirst().get().getOrder();
        Integer orderL4 = summaries.stream().filter(s -> s.getTitle().equals("L4")).findFirst().get().getOrder();
        Integer orderL5 = summaries.stream().filter(s -> s.getTitle().equals("L5")).findFirst().get().getOrder();

        Assertions.assertEquals(1, orderL1);
        Assertions.assertEquals(5, orderL2);
        Assertions.assertEquals(3, orderL3);
        Assertions.assertEquals(4, orderL4);
        Assertions.assertEquals(2, orderL5);
    }
}