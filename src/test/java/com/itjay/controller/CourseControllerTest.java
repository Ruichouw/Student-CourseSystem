package com.itjay.controller;

import com.itjay.pojo.Course;
import com.itjay.pojo.Result;
import com.itjay.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId("C001");
        testCourse.setName("Java程序设计");
    }

    @Test
    void getById_Success() {
        when(courseService.getById("C001")).thenReturn(testCourse);

        Result result = courseController.getById("C001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testCourse, result.getData());
        verify(courseService, times(1)).getById("C001");
    }

    @Test
    void getById_NotFound() {
        when(courseService.getById("C001")).thenReturn(null);

        Result result = courseController.getById("C001");

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("课程不存在", result.getMsg());
        verify(courseService, times(1)).getById("C001");
    }

    @Test
    void getAll() {
        List<Course> courseList = Arrays.asList(testCourse);
        when(courseService.getAllWithTeacher()).thenReturn(courseList);

        Result result = courseController.getAll();

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(courseList, result.getData());
        verify(courseService, times(1)).getAllWithTeacher();
    }

    @Test
    void search() {
        Course searchCriteria = new Course();
        searchCriteria.setName("Java");

        List<Course> courseList = Arrays.asList(testCourse);
        when(courseService.getByCondition(any(Course.class))).thenReturn(courseList);

        Result result = courseController.search(searchCriteria);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(courseList, result.getData());
        verify(courseService, times(1)).getByCondition(any(Course.class));
    }

    @Test
    void add_Success() {
        when(courseService.getById("C001")).thenReturn(null);
        when(courseService.add(any(Course.class))).thenReturn(true);

        Result result = courseController.add(testCourse);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(courseService, times(1)).getById("C001");
        verify(courseService, times(1)).add(any(Course.class));
    }

    @Test
    void add_DuplicateId() {
        when(courseService.getById("C001")).thenReturn(testCourse);

        Result result = courseController.add(testCourse);

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("课程编号已存在", result.getMsg());
        verify(courseService, times(1)).getById("C001");
        verify(courseService, never()).add(any(Course.class));
    }

    @Test
    void update() {
        when(courseService.update(any(Course.class))).thenReturn(true);

        Result result = courseController.update(testCourse);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(courseService, times(1)).update(any(Course.class));
    }

    @Test
    void delete() {
        when(courseService.delete("C001")).thenReturn(true);

        Result result = courseController.delete("C001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(courseService, times(1)).delete("C001");
    }
}