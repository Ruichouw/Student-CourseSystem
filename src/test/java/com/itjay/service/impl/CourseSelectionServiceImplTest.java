package com.itjay.service.impl;

import com.itjay.mapper.CourseMapper;
import com.itjay.mapper.CourseSelectionMapper;
import com.itjay.pojo.Course;
import com.itjay.pojo.CourseSelection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseSelectionServiceImplTest {

    @Mock
    private CourseSelectionMapper courseSelectionMapper;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseSelectionServiceImpl courseSelectionService;

    private CourseSelection courseSelection;
    private Course course;

    @BeforeEach
    void setUp() {
        // 使用setter方法初始化对象，而不是构造函数
        courseSelection = new CourseSelection();
        courseSelection.setId(1);
        courseSelection.setStudentId("stu001");
        courseSelection.setCourseId("course001");
        courseSelection.setSelectionTime(new Date());
        courseSelection.setScore(85.5f);
        courseSelection.setStudentName("张三");
        courseSelection.setCourseName("Java编程");
        courseSelection.setCredit(3.0f);
        courseSelection.setTeacherName("李老师");

        course = new Course();
        course.setCourseId("course001");
        course.setName("Java编程");
        course.setCredit(3.0f);
        course.setHours(48);
        course.setTeacherId("tea001");
        course.setDescription("Java编程基础");
        course.setMaxStudents(50);
        course.setCurrentStudents(30);
        course.setStatus("APPROVED");
        course.setTeacherName("李老师");
    }

    @Test
    void getById_Success() {
        when(courseSelectionMapper.selectById(1)).thenReturn(courseSelection);

        CourseSelection result = courseSelectionService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("stu001", result.getStudentId());
        verify(courseSelectionMapper).selectById(1);
    }

    @Test
    void getByStudentId_Success() {
        List<CourseSelection> selections = Arrays.asList(courseSelection);
        when(courseSelectionMapper.selectByStudentId("stu001")).thenReturn(selections);

        List<CourseSelection> result = courseSelectionService.getByStudentId("stu001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("stu001", result.get(0).getStudentId());
        verify(courseSelectionMapper).selectByStudentId("stu001");
    }

    @Test
    void getByCourseId_Success() {
        List<CourseSelection> selections = Arrays.asList(courseSelection);
        when(courseSelectionMapper.selectByCourseId("course001")).thenReturn(selections);

        List<CourseSelection> result = courseSelectionService.getByCourseId("course001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("course001", result.get(0).getCourseId());
        verify(courseSelectionMapper).selectByCourseId("course001");
    }

    @Test
    void getByStudentAndCourse_Success() {
        when(courseSelectionMapper.selectByStudentAndCourse("stu001", "course001")).thenReturn(courseSelection);

        CourseSelection result = courseSelectionService.getByStudentAndCourse("stu001", "course001");

        assertNotNull(result);
        assertEquals("stu001", result.getStudentId());
        assertEquals("course001", result.getCourseId());
        verify(courseSelectionMapper).selectByStudentAndCourse("stu001", "course001");
    }

    @Test
    void selectCourse_Success() {
        when(courseSelectionMapper.selectByStudentAndCourse("stu001", "course001")).thenReturn(null);
        when(courseMapper.selectById("course001")).thenReturn(course);
        when(courseSelectionMapper.insert(any(CourseSelection.class))).thenReturn(1);
        when(courseMapper.updateCurrentStudents("course001")).thenReturn(1);

        boolean result = courseSelectionService.selectCourse("stu001", "course001");

        assertTrue(result);
        verify(courseSelectionMapper).insert(any(CourseSelection.class));
        verify(courseMapper).updateCurrentStudents("course001");
    }

    @Test
    void selectCourse_AlreadySelected() {
        when(courseSelectionMapper.selectByStudentAndCourse("stu001", "course001")).thenReturn(courseSelection);

        boolean result = courseSelectionService.selectCourse("stu001", "course001");

        assertFalse(result);
        verify(courseSelectionMapper, never()).insert(any(CourseSelection.class));
        verify(courseMapper, never()).updateCurrentStudents(anyString());
    }

    @Test
    void selectCourse_CourseNotFound() {
        when(courseSelectionMapper.selectByStudentAndCourse("stu001", "course001")).thenReturn(null);
        when(courseMapper.selectById("course001")).thenReturn(null);

        boolean result = courseSelectionService.selectCourse("stu001", "course001");

        assertFalse(result);
        verify(courseSelectionMapper, never()).insert(any(CourseSelection.class));
        verify(courseMapper, never()).updateCurrentStudents(anyString());
    }

    @Test
    void selectCourse_CourseFull() {
        Course fullCourse = new Course();
        fullCourse.setCourseId("course001");
        fullCourse.setName("Java编程");
        fullCourse.setMaxStudents(50);
        fullCourse.setCurrentStudents(50);
        fullCourse.setStatus("APPROVED");

        when(courseSelectionMapper.selectByStudentAndCourse("stu001", "course001")).thenReturn(null);
        when(courseMapper.selectById("course001")).thenReturn(fullCourse);

        boolean result = courseSelectionService.selectCourse("stu001", "course001");

        assertFalse(result);
        verify(courseSelectionMapper, never()).insert(any(CourseSelection.class));
        verify(courseMapper, never()).updateCurrentStudents(anyString());
    }

    @Test
    void dropCourse_Success() {
        when(courseSelectionMapper.deleteByStudentAndCourse("stu001", "course001")).thenReturn(1);
        when(courseMapper.updateCurrentStudents("course001")).thenReturn(1);

        boolean result = courseSelectionService.dropCourse("stu001", "course001");

        assertTrue(result);
        verify(courseSelectionMapper).deleteByStudentAndCourse("stu001", "course001");
        verify(courseMapper).updateCurrentStudents("course001");
    }

    @Test
    void dropCourse_Failure() {
        when(courseSelectionMapper.deleteByStudentAndCourse("stu001", "course001")).thenReturn(0);

        boolean result = courseSelectionService.dropCourse("stu001", "course001");

        assertFalse(result);
        verify(courseSelectionMapper).deleteByStudentAndCourse("stu001", "course001");
        verify(courseMapper, never()).updateCurrentStudents(anyString());
    }

    @Test
    void updateScore_Success() {
        when(courseSelectionMapper.update(any(CourseSelection.class))).thenReturn(1);

        boolean result = courseSelectionService.updateScore(1, 90.0f);

        assertTrue(result);
        verify(courseSelectionMapper).update(any(CourseSelection.class));
    }

    @Test
    void updateScore_Failure() {
        when(courseSelectionMapper.update(any(CourseSelection.class))).thenReturn(0);

        boolean result = courseSelectionService.updateScore(1, 90.0f);

        assertFalse(result);
        verify(courseSelectionMapper).update(any(CourseSelection.class));
    }

    @Test
    void getGradesByStudentId_Success() {
        List<CourseSelection> grades = Arrays.asList(courseSelection);
        when(courseSelectionMapper.selectGradesByStudentId("stu001")).thenReturn(grades);

        List<CourseSelection> result = courseSelectionService.getGradesByStudentId("stu001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("stu001", result.get(0).getStudentId());
        verify(courseSelectionMapper).selectGradesByStudentId("stu001");
    }
}