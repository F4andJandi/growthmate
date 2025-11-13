package com.wanted.growthmate.common.config;

import com.wanted.growthmate.category.domain.entity.Category;
import com.wanted.growthmate.category.repository.CategoryRepository;
import com.wanted.growthmate.enrollment.entity.Enrollment;
import com.wanted.growthmate.enrollment.repository.EnrollmentRepository;
import com.wanted.growthmate.learning.course.domain.dto.CourseDetailResponse;
import com.wanted.growthmate.learning.course.domain.entity.Course;
import com.wanted.growthmate.learning.course.repository.CourseRepository;
import com.wanted.growthmate.learning.course.service.CourseService;
import com.wanted.growthmate.learning.lecture.domain.dto.LectureCreateRequest;
import com.wanted.growthmate.learning.lecture.service.LectureService;
import com.wanted.growthmate.learning.section.domain.dto.SectionCreateRequest;
import com.wanted.growthmate.learning.section.service.SectionService;
import com.wanted.growthmate.payment.domain.Point;
import com.wanted.growthmate.payment.repository.PointRepository;
import com.wanted.growthmate.user.entity.User;
import com.wanted.growthmate.user.repository.UserRepository;
import com.wanted.growthmate.user.role.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final SectionService sectionService;
    private final LectureService lectureService;
    private final EnrollmentRepository enrollmentRepository;
    private final PointRepository pointRepository;

    public TestDataInitializer(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            CourseRepository courseRepository,
            CourseService courseService,
            SectionService sectionService,
            LectureService lectureService,
            EnrollmentRepository enrollmentRepository,
            PointRepository pointRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
        this.sectionService = sectionService;
        this.lectureService = lectureService;
        this.enrollmentRepository = enrollmentRepository;
        this.pointRepository = pointRepository;
    }

    @Override
    public void run(String... args) {
        // 테스트 데이터가 이미 있는지 확인 (간단한 체크)
        if (userRepository.count() == 0) {
            initializeTestData();
        }
    }

    private void initializeTestData() {
        // 1. User 생성 (강사 1명, 학생 2명)
        User instructor = new User(
                "instructor1",
                "instructor1@example.com",
                "password123",
                Role.INSTRUCTOR,
                LocalDateTime.now()
        );
        instructor = userRepository.save(instructor);

        User student1 = new User(
                "student1",
                "student1@example.com",
                "password123",
                Role.STUDENT,
                LocalDateTime.now()
        );
        student1 = userRepository.save(student1);

        User student2 = new User(
                "student2",
                "student2@example.com",
                "password123",
                Role.STUDENT,
                LocalDateTime.now()
        );
        student2 = userRepository.save(student2);

        // 1-1. Point 생성 (모든 사용자에게 포인트 지급)
        Point instructorPoint = createPoint(instructor.getId(), 100000); // 강사는 100,000 포인트
        pointRepository.save(instructorPoint);

        Point student1Point = createPoint(student1.getId(), 50000); // 학생1은 50,000 포인트
        pointRepository.save(student1Point);

        Point student2Point = createPoint(student2.getId(), 30000); // 학생2는 30,000 포인트
        pointRepository.save(student2Point);

        // 2. Category 생성 (5개) - 리플렉션을 사용하여 필드 설정
        Category category1 = createCategory("프로그래밍", "프로그래밍 언어 및 개발 관련 강좌", 1);
        category1 = categoryRepository.save(category1);

        Category category2 = createCategory("웹 개발", "웹 개발 프레임워크 및 기술", 2);
        category2 = categoryRepository.save(category2);

        Category category3 = createCategory("데이터베이스", "데이터베이스 설계 및 관리", 3);
        category3 = categoryRepository.save(category3);

        Category category4 = createCategory("알고리즘", "알고리즘 및 자료구조", 4);
        category4 = categoryRepository.save(category4);

        Category category5 = createCategory("데이터 사이언스", "데이터 분석 및 머신러닝", 5);
        category5 = categoryRepository.save(category5);

        // 3. Course 생성 (20개)
        CourseDetailResponse course = courseService.createCourse(
                "PUBLISHED",
                instructor.getId(),
                category1.getId(),
                "Java 프로그래밍 기초",
                "Java 언어의 기초부터 객체지향 프로그래밍까지 학습하는 강좌입니다.",
                "https://example.com/images/java-course.jpg",
                50000L
        );
        Course savedCourse = courseRepository.findById(course.getId()).orElseThrow();
        Long courseId = savedCourse.getId();

        // 추가 강좌 19개 생성
        createCourse(instructor.getId(), category1.getId(), "Python 기초 프로그래밍", 
                "Python 언어의 기초 문법부터 실전 프로젝트까지", 
                "https://example.com/images/python-course.jpg", 45000L);
        
        createCourse(instructor.getId(), category1.getId(), "C++ 고급 프로그래밍", 
                "C++의 고급 기능과 메모리 관리, STL 활용법", 
                "https://example.com/images/cpp-course.jpg", 60000L);
        
        createCourse(instructor.getId(), category2.getId(), "Spring Boot 완전정복", 
                "Spring Boot를 활용한 RESTful API 개발과 실전 프로젝트", 
                "https://example.com/images/springboot-course.jpg", 80000L);
        
        createCourse(instructor.getId(), category2.getId(), "React.js 마스터 클래스", 
                "React.js를 활용한 현대적인 웹 애플리케이션 개발", 
                "https://example.com/images/react-course.jpg", 75000L);
        
        createCourse(instructor.getId(), category2.getId(), "Vue.js 입문부터 실전까지", 
                "Vue.js 프레임워크를 활용한 프론트엔드 개발", 
                "https://example.com/images/vue-course.jpg", 55000L);
        
        createCourse(instructor.getId(), category2.getId(), "Node.js 백엔드 개발", 
                "Node.js와 Express를 활용한 서버 사이드 개발", 
                "https://example.com/images/nodejs-course.jpg", 70000L);
        
        createCourse(instructor.getId(), category2.getId(), "Django 웹 개발", 
                "Python Django 프레임워크로 풀스택 웹 애플리케이션 구축", 
                "https://example.com/images/django-course.jpg", 65000L);
        
        createCourse(instructor.getId(), category3.getId(), "MySQL 데이터베이스 설계", 
                "MySQL을 활용한 데이터베이스 설계와 최적화 기법", 
                "https://example.com/images/mysql-course.jpg", 50000L);
        
        createCourse(instructor.getId(), category3.getId(), "PostgreSQL 실무 활용", 
                "PostgreSQL의 고급 기능과 성능 튜닝", 
                "https://example.com/images/postgresql-course.jpg", 60000L);
        
        createCourse(instructor.getId(), category3.getId(), "MongoDB NoSQL 데이터베이스", 
                "MongoDB를 활용한 NoSQL 데이터베이스 설계와 운영", 
                "https://example.com/images/mongodb-course.jpg", 55000L);
        
        createCourse(instructor.getId(), category4.getId(), "알고리즘 문제 해결 전략", 
                "코딩 테스트를 위한 알고리즘 문제 해결 기법", 
                "https://example.com/images/algorithm-course.jpg", 40000L);
        
        createCourse(instructor.getId(), category4.getId(), "자료구조 완벽 가이드", 
                "배열, 리스트, 트리, 그래프 등 핵심 자료구조 학습", 
                "https://example.com/images/datastructure-course.jpg", 45000L);
        
        createCourse(instructor.getId(), category4.getId(), "동적 프로그래밍 마스터", 
                "DP 알고리즘의 원리와 실전 문제 해결", 
                "https://example.com/images/dp-course.jpg", 50000L);
        
        createCourse(instructor.getId(), category5.getId(), "파이썬 데이터 분석", 
                "Pandas, NumPy를 활용한 데이터 분석과 시각화", 
                "https://example.com/images/data-analysis-course.jpg", 70000L);
        
        createCourse(instructor.getId(), category5.getId(), "머신러닝 입문", 
                "Scikit-learn을 활용한 머신러닝 모델 구축", 
                "https://example.com/images/ml-course.jpg", 90000L);
        
        createCourse(instructor.getId(), category5.getId(), "딥러닝 기초", 
                "TensorFlow와 Keras를 활용한 딥러닝 모델 개발", 
                "https://example.com/images/deeplearning-course.jpg", 100000L);
        
        createCourse(instructor.getId(), category1.getId(), "JavaScript 완전정복", 
                "ES6+ 문법부터 비동기 프로그래밍까지", 
                "https://example.com/images/javascript-course.jpg", 50000L);
        
        createCourse(instructor.getId(), category2.getId(), "TypeScript 실전 개발", 
                "TypeScript를 활용한 타입 안전한 웹 개발", 
                "https://example.com/images/typescript-course.jpg", 60000L);
        
        createCourse(instructor.getId(), category2.getId(), "Next.js 풀스택 개발", 
                "Next.js를 활용한 서버 사이드 렌더링과 API 개발", 
                "https://example.com/images/nextjs-course.jpg", 85000L);
        
        createCourse(instructor.getId(), category3.getId(), "Redis 캐싱 전략", 
                "Redis를 활용한 캐싱과 세션 관리", 
                "https://example.com/images/redis-course.jpg", 55000L);

        // 섹션 1: Java 기초
        var section1 = sectionService.save(SectionCreateRequest.builder()
                .courseId(courseId)
                .title("Java 기초")
                .order(1)
                .isVisible(true)
                .build());

        // 섹션 1의 강의들
        lectureService.save(LectureCreateRequest.builder()
                .sectionId(section1.getSectionId())
                .title("Java 소개")
                .duration(600L)
                .mediaId(101L)
                .order(1)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
                .sectionId(section1.getSectionId())
                .title("변수와 데이터 타입")
                .duration(900L)
                .mediaId(102L)
                .order(2)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
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
                .sectionId(section2.getSectionId())
                .title("클래스와 객체")
                .duration(1200L)
                .mediaId(201L)
                .order(1)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
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
                .sectionId(section3.getSectionId())
                .title("List와 ArrayList")
                .duration(1000L)
                .mediaId(301L)
                .order(1)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
                .sectionId(section3.getSectionId())
                .title("Map과 HashMap")
                .duration(1100L)
                .mediaId(302L)
                .order(2)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
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
                .sectionId(section4.getSectionId())
                .title("try-catch-finally")
                .duration(800L)
                .mediaId(401L)
                .order(1)
                .isVisible(true)
                .build());

        lectureService.save(LectureCreateRequest.builder()
                .sectionId(section4.getSectionId())
                .title("사용자 정의 예외")
                .duration(700L)
                .mediaId(402L)
                .order(2)
                .isVisible(true)
                .build());

        // 5. Enrollment 생성 (학생 2명이 강좌 수강)
        // User와 Course 엔티티를 조회하여 Enrollment 생성
        User savedStudent1 = userRepository.findById(student1.getId()).orElseThrow();
        User savedStudent2 = userRepository.findById(student2.getId()).orElseThrow();
        // savedCourse는 이미 위에서 선언되어 있으므로 재사용
        
        Enrollment enrollment1 = new Enrollment(savedStudent1, savedCourse);
        enrollment1.setOrderNum(1L);
        enrollmentRepository.save(enrollment1);

        Enrollment enrollment2 = new Enrollment(savedStudent2, savedCourse);
        enrollment2.setOrderNum(1L);
        enrollmentRepository.save(enrollment2);
    }

    // 강좌 생성 헬퍼 메서드
    private void createCourse(Long instructorId, Long categoryId, String title, 
                              String description, String imageUrl, Long pointAmount) {
        courseService.createCourse(
                "PUBLISHED",
                instructorId,
                categoryId,
                title,
                description,
                imageUrl,
                pointAmount
        );
    }

    // Category 생성 헬퍼 메서드 (리플렉션 사용)
    private Category createCategory(String name, String description, int order) {
        try {
            Category category = new Category();
            java.lang.reflect.Field nameField = Category.class.getDeclaredField("categoryName");
            nameField.setAccessible(true);
            nameField.set(category, name);

            java.lang.reflect.Field descField = Category.class.getDeclaredField("categoryDescription");
            descField.setAccessible(true);
            descField.set(category, description);

            java.lang.reflect.Field orderField = Category.class.getDeclaredField("categoryOrder");
            orderField.setAccessible(true);
            orderField.setInt(category, order);

            return category;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Category", e);
        }
    }

    // Point 생성 헬퍼 메서드 (리플렉션 사용 - protected 생성자 호출)
    private Point createPoint(Long userId, Integer balance) {
        try {
            // User 엔티티 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            
            // Point 생성자 호출 (Point(User user, Integer balance))
            java.lang.reflect.Constructor<Point> constructor = Point.class.getDeclaredConstructor(
                    User.class, Integer.class
            );
            constructor.setAccessible(true);
            Point point = constructor.newInstance(user, balance);

            return point;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Point", e);
        }
    }
}

