package com.wanted.growthmate.common.config;

import com.wanted.growthmate.learning.lecture.domain.dto.LectureCreateRequest;
import com.wanted.growthmate.learning.lecture.service.LectureService;
import com.wanted.growthmate.learning.section.domain.dto.SectionCreateRequest;
import com.wanted.growthmate.learning.section.service.SectionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitializer implements CommandLineRunner {

    private final SectionService sectionService;
    private final LectureService lectureService;

    public TestDataInitializer(SectionService sectionService, LectureService lectureService) {
        this.sectionService = sectionService;
        this.lectureService = lectureService;
    }

    @Override
    public void run(String... args) {
        // 테스트 데이터가 이미 있는지 확인 (간단한 체크)
        if (sectionService.findByCourseId(1L).isEmpty()) {
            initializeTestData();
        }
    }

    private void initializeTestData() {
        Long courseId = 1L;

        // 섹션 1: Java 기초
        var section1 = sectionService.save(SectionCreateRequest.builder()
                .courseId(courseId)
                .title("Java 기초")
                .order(1)
                .isVisible(true)
                .build());

        // 섹션 1의 강의들
        lectureService.save(LectureCreateRequest.builder()
                .courseId(courseId)
                .sectionId(section1.getSectionId())
                .title("Java 소개")
                .duration(600L)
                .mediaId(101L)
                .order(1)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
                .courseId(courseId)
                .sectionId(section1.getSectionId())
                .title("변수와 데이터 타입")
                .duration(900L)
                .mediaId(102L)
                .order(2)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
                .courseId(courseId)
                .sectionId(section1.getSectionId())
                .title("연산자")
                .duration(750L)
                .mediaId(103L)
                .order(3)
                .isVisible(true)
                .build());

        // 섹션 2: 객체지향 프로그래밍
        var section2 = sectionService.save(SectionCreateRequest.builder()
                .courseId(courseId)
                .title("객체지향 프로그래밍")
                .order(2)
                .isVisible(true)
                .build());

        // 섹션 2의 강의들
        lectureService.save(LectureCreateRequest.builder()
                .courseId(courseId)
                .sectionId(section2.getSectionId())
                .title("클래스와 객체")
                .duration(1200L)
                .mediaId(201L)
                .order(1)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
                .courseId(courseId)
                .sectionId(section2.getSectionId())
                .title("상속과 다형성")
                .duration(1500L)
                .mediaId(202L)
                .order(2)
                .isVisible(true)
                .build());

        // 섹션 3: 컬렉션 프레임워크
        var section3 = sectionService.save(SectionCreateRequest.builder()
                .courseId(courseId)
                .title("컬렉션 프레임워크")
                .order(3)
                .isVisible(true)
                .build());

        // 섹션 3의 강의들
        lectureService.save(LectureCreateRequest.builder()
                .courseId(courseId)
                .sectionId(section3.getSectionId())
                .title("List와 ArrayList")
                .duration(1000L)
                .mediaId(301L)
                .order(1)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
                .courseId(courseId)
                .sectionId(section3.getSectionId())
                .title("Map과 HashMap")
                .duration(1100L)
                .mediaId(302L)
                .order(2)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
                .courseId(courseId)
                .sectionId(section3.getSectionId())
                .title("Set과 HashSet")
                .duration(950L)
                .mediaId(303L)
                .order(3)
                .isVisible(true)
                .build());

        // 섹션 4: 예외 처리
        var section4 = sectionService.save(SectionCreateRequest.builder()
                .courseId(courseId)
                .title("예외 처리")
                .order(4)
                .isVisible(true)
                .build());

        // 섹션 4의 강의들
        lectureService.save(LectureCreateRequest.builder()
                .courseId(courseId)
                .sectionId(section4.getSectionId())
                .title("try-catch-finally")
                .duration(800L)
                .mediaId(401L)
                .order(1)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
                .courseId(courseId)
                .sectionId(section4.getSectionId())
                .title("사용자 정의 예외")
                .duration(700L)
                .mediaId(402L)
                .order(2)
                .isVisible(true)
                .build());
    }
}

