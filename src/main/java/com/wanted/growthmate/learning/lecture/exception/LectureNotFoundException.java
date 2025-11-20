package com.wanted.growthmate.learning.lecture.exception;

public class LectureNotFoundException extends RuntimeException  {
    public LectureNotFoundException(Long lectureId) {
        super(lectureId + " ID의 강의를 찾을 수 없습니다.");
    }
}
