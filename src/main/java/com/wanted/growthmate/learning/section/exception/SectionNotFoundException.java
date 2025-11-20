package com.wanted.growthmate.learning.section.exception;

public class SectionNotFoundException extends RuntimeException {
    public SectionNotFoundException(Long sectionId) {
        super("섹션을 찾을 수 없습니다. ID: " + sectionId);
    }
}

