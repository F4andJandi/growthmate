package com.wanted.growthmate.learning.section.service;

import com.wanted.growthmate.learning.section.domain.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SectionServiceImplTest {

    @Autowired
    SectionServiceImpl sectionService;

    @Test
    void save() {
        SectionResponse result = sectionService.save(
                SectionCreateRequest.builder()
                        .courseId(1L)
                        .title("테스트 섹션")
                        .order(1)
                        .build()
        );
        Assertions.assertTrue(result.isVisible());
    }

    @Test
    void findByCourseId() {
        SectionResponse result = sectionService.save(SectionCreateRequest.builder()
                .courseId(1L)
                .title("제목1")
                .order(1)
                .build());

        Assertions.assertTrue(result.isVisible());

        List<SectionSummaryResponse> result1 = sectionService.findByCourseId(1L);
        Assertions.assertTrue(result1.size() > 0, "courseId로 조회된 섹션이 있어야 함");
    }

    @Test
    void findBySectionId() {
        SectionResponse created = sectionService.save(SectionCreateRequest.builder()
                .courseId(10L)
                .title("테스트 섹션")
                .order(1)
                .build());

        SectionResponse found = sectionService.findBySectionId(created.getSectionId());

        Assertions.assertEquals(created.getSectionId(), found.getSectionId());
        Assertions.assertEquals("테스트 섹션", found.getTitle());
        Assertions.assertEquals(10L, found.getCourseId());
    }

    @Test
    void updateInfo() {
        // given: 하나 생성
        SectionResponse created = sectionService.save(SectionCreateRequest.builder()
                .courseId(10L)
                .title("원본 제목")
                .order(1)
                .build());

        // when: 정보 수정
        SectionUpdateRequest request = SectionUpdateRequest.builder()
                .title("수정된 제목")
                .isVisible(false)
                .build();
        SectionResponse updated = sectionService.updateInfo(created.getSectionId(), request);

        // then
        Assertions.assertEquals("수정된 제목", updated.getTitle());
        Assertions.assertFalse(updated.isVisible());
    }

    @Test
    void updateOrder() {
        // given: 같은 강좌에 5개 생성 (order 1..5)
        long courseId = 99L;
        SectionResponse s1 = sectionService.save(SectionCreateRequest.builder().courseId(courseId).title("S1").order(1).build());
        SectionResponse s2 = sectionService.save(SectionCreateRequest.builder().courseId(courseId).title("S2").order(2).build());
        SectionResponse s3 = sectionService.save(SectionCreateRequest.builder().courseId(courseId).title("S3").order(3).build());
        SectionResponse s4 = sectionService.save(SectionCreateRequest.builder().courseId(courseId).title("S4").order(4).build());
        SectionResponse s5 = sectionService.save(SectionCreateRequest.builder().courseId(courseId).title("S5").order(5).build());

        // when: 2번째( order=2 )와 5번째( order=5 ) 순서 교환
        SectionOrderUpdateRequest swapWith5 = SectionOrderUpdateRequest.builder()
                .courseId(courseId)
                .newOrder(5) // 교환 대상의 displayOrder
                .build();
        sectionService.updateOrder(s2.getSectionId(), swapWith5);

        // then: 각 항목의 displayOrder가 기대대로 바뀌었는지 확인
        List<SectionSummaryResponse> summaries = sectionService.findByCourseId(courseId);
        Integer orderS1 = summaries.stream().filter(s -> s.getTitle().equals("S1")).findFirst().get().getOrder();
        Integer orderS2 = summaries.stream().filter(s -> s.getTitle().equals("S2")).findFirst().get().getOrder();
        Integer orderS3 = summaries.stream().filter(s -> s.getTitle().equals("S3")).findFirst().get().getOrder();
        Integer orderS4 = summaries.stream().filter(s -> s.getTitle().equals("S4")).findFirst().get().getOrder();
        Integer orderS5 = summaries.stream().filter(s -> s.getTitle().equals("S5")).findFirst().get().getOrder();

        Assertions.assertEquals(1, orderS1);
        Assertions.assertEquals(5, orderS2);
        Assertions.assertEquals(3, orderS3);
        Assertions.assertEquals(4, orderS4);
        Assertions.assertEquals(2, orderS5);
    }
}

