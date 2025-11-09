package com.itjay.controller;

import com.itjay.pojo.Course;
import com.itjay.pojo.CourseSelection;
import com.itjay.pojo.Result;
import com.itjay.service.CourseSelectionService;
import com.itjay.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseSelectionControllerTest {

    @Mock
    private CourseSelectionService courseSelectionService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseSelectionController courseSelectionController;

    private CourseSelection testSelection;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testSelection = new CourseSelection();
        testSelection.setId(1);
        testSelection.setStudentId("2021001");
        testSelection.setCourseId("C001");
        testSelection.setScore(85f); // 使用 f 表示 float

        testCourse = new Course();
        testCourse.setCourseId("C001");
        testCourse.setName("Java程序设计");
        testCourse.setMaxStudents(50);
        testCourse.setCurrentStudents(30);
    }

    @Test
    void getByStudentId() {
        List<CourseSelection> selectionList = Arrays.asList(testSelection);
        when(courseSelectionService.getByStudentId("2021001")).thenReturn(selectionList);

        Result result = courseSelectionController.getByStudentId("2021001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(selectionList, result.getData());
        verify(courseSelectionService, times(1)).getByStudentId("2021001");
    }

    @Test
    void getByCourseId() {
        List<CourseSelection> selectionList = Arrays.asList(testSelection);
        when(courseSelectionService.getByCourseId("C001")).thenReturn(selectionList);

        Result result = courseSelectionController.getByCourseId("C001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(selectionList, result.getData());
        verify(courseSelectionService, times(1)).getByCourseId("C001");
    }

    @Test
    void selectCourse_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("studentId", "2021001");
        params.put("courseId", "C001");

        when(courseSelectionService.getByStudentAndCourse("2021001", "C001")).thenReturn(null);
        when(courseService.getById("C001")).thenReturn(testCourse);
        when(courseSelectionService.selectCourse("2021001", "C001")).thenReturn(true);

        Result result = courseSelectionController.selectCourse(params);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(courseSelectionService, times(1)).getByStudentAndCourse("2021001", "C001");
        verify(courseService, times(1)).getById("C001");
        verify(courseSelectionService, times(1)).selectCourse("2021001", "C001");
    }

    @Test
    void selectCourse_AlreadySelected() {
        Map<String, String> params = new HashMap<>();
        params.put("studentId", "2021001");
        params.put("courseId", "C001");

        when(courseSelectionService.getByStudentAndCourse("2021001", "C001")).thenReturn(testSelection);

        Result result = courseSelectionController.selectCourse(params);

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("您已经选过这门课程", result.getMsg());
        verify(courseSelectionService, times(1)).getByStudentAndCourse("2021001", "C001");
        verify(courseService, never()).getById(anyString());
        verify(courseSelectionService, never()).selectCourse(anyString(), anyString());
    }

    @Test
    void selectCourse_CourseFull() {
        Map<String, String> params = new HashMap<>();
        params.put("studentId", "2021001");
        params.put("courseId", "C001");

        testCourse.setCurrentStudents(50); // 课程已满
        when(courseSelectionService.getByStudentAndCourse("2021001", "C001")).thenReturn(null);
        when(courseService.getById("C001")).thenReturn(testCourse);

        Result result = courseSelectionController.selectCourse(params);

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("课程已满", result.getMsg());
        verify(courseSelectionService, times(1)).getByStudentAndCourse("2021001", "C001");
        verify(courseService, times(1)).getById("C001");
        verify(courseSelectionService, never()).selectCourse(anyString(), anyString());
    }

    @Test
    void dropCourse_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("studentId", "2021001");
        params.put("courseId", "C001");

        when(courseSelectionService.getByStudentAndCourse("2021001", "C001")).thenReturn(testSelection);
        when(courseSelectionService.dropCourse("2021001", "C001")).thenReturn(true);

        Result result = courseSelectionController.dropCourse(params);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(courseSelectionService, times(1)).getByStudentAndCourse("2021001", "C001");
        verify(courseSelectionService, times(1)).dropCourse("2021001", "C001");
    }

    @Test
    void dropCourse_NotSelected() {
        Map<String, String> params = new HashMap<>();
        params.put("studentId", "2021001");
        params.put("courseId", "C001");

        when(courseSelectionService.getByStudentAndCourse("2021001", "C001")).thenReturn(null);

        Result result = courseSelectionController.dropCourse(params);

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("您没有选过这门课程", result.getMsg());
        verify(courseSelectionService, times(1)).getByStudentAndCourse("2021001", "C001");
        verify(courseSelectionService, never()).dropCourse(anyString(), anyString());
    }

    @Test
    void updateScore_Success() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("score", 90); // 这里可以是 int，因为控制器会转换

        when(courseSelectionService.updateScore(1, 90f)).thenReturn(true);

        Result result = courseSelectionController.updateScore(params);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(courseSelectionService, times(1)).updateScore(1, 90f);
    }

    @Test
    void updateScore_InvalidScore() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("score", 150); // 无效分数

        Result result = courseSelectionController.updateScore(params);

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("分数范围应在0-100之间", result.getMsg());
        verify(courseSelectionService, never()).updateScore(anyInt(), anyFloat());
    }

    @Test
    void getGradesByStudentId() {
        List<CourseSelection> gradeList = Arrays.asList(testSelection);
        when(courseSelectionService.getGradesByStudentId("2021001")).thenReturn(gradeList);

        Result result = courseSelectionController.getGradesByStudentId("2021001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(gradeList, result.getData());
        verify(courseSelectionService, times(1)).getGradesByStudentId("2021001");
    }
}