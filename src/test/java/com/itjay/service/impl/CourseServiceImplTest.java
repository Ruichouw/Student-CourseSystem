package com.itjay.service.impl;

import com.itjay.mapper.CourseMapper;
import com.itjay.pojo.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseId("course001");
        course.setName("Java编程");
        course.setCredit(3.0f);
        course.setHours(48);
        course.setTeacherId("tea001");
        course.setDescription("Java编程基础");
        course.setMaxStudents(50);
        course.setCurrentStudents(30);
        course.setSyllabus("教学大纲");
        course.setTextbook("教材");
        course.setScheduleInfo("周一 1-2节");
        course.setStatus("APPROVED");
        course.setTeacherName("李老师");
    }

    @Test
    void getById_Success() {
        when(courseMapper.selectById("course001")).thenReturn(course);

        Course result = courseService.getById("course001");

        assertNotNull(result);
        assertEquals("course001", result.getCourseId());
        assertEquals("Java编程", result.getName());
        verify(courseMapper).selectById("course001");
    }

    @Test
    void getAll_Success() {
        List<Course> courses = Arrays.asList(course);
        when(courseMapper.selectAll()).thenReturn(courses);

        List<Course> result = courseService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("course001", result.get(0).getCourseId());
        verify(courseMapper).selectAll();
    }

    @Test
    void getAllWithTeacher_Success() {
        List<Course> courses = Arrays.asList(course);
        when(courseMapper.selectAllWithTeacher()).thenReturn(courses);

        List<Course> result = courseService.getAllWithTeacher();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("course001", result.get(0).getCourseId());
        verify(courseMapper).selectAllWithTeacher();
    }

    @Test
    void getByCondition_Success() {
        Course condition = new Course();
        condition.setName("Java");

        List<Course> courses = Arrays.asList(course);
        when(courseMapper.selectByCondition(condition)).thenReturn(courses);

        List<Course> result = courseService.getByCondition(condition);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("course001", result.get(0).getCourseId());
        verify(courseMapper).selectByCondition(condition);
    }

    @Test
    void add_Success() {
        Course newCourse = new Course();
        newCourse.setCourseId("course002");
        newCourse.setName("Python编程");

        when(courseMapper.insert(newCourse)).thenReturn(1);

        boolean result = courseService.add(newCourse);

        assertTrue(result);
        assertEquals(0, newCourse.getCurrentStudents());
        verify(courseMapper).insert(newCourse);
    }

    @Test
    void add_WithExistingCurrentStudents() {
        Course existingCourse = new Course();
        existingCourse.setCourseId("course002");
        existingCourse.setName("Python编程");
        existingCourse.setCurrentStudents(10);

        when(courseMapper.insert(existingCourse)).thenReturn(1);

        boolean result = courseService.add(existingCourse);

        assertTrue(result);
        assertEquals(10, existingCourse.getCurrentStudents());
        verify(courseMapper).insert(existingCourse);
    }

    @Test
    void update_Success() {
        when(courseMapper.update(course)).thenReturn(1);

        boolean result = courseService.update(course);

        assertTrue(result);
        verify(courseMapper).update(course);
    }

    @Test
    void delete_Success() {
        when(courseMapper.deleteById("course001")).thenReturn(1);

        boolean result = courseService.delete("course001");

        assertTrue(result);
        verify(courseMapper).deleteById("course001");
    }

    @Test
    void updateCurrentStudents_Success() {
        when(courseMapper.updateCurrentStudents("course001")).thenReturn(1);

        boolean result = courseService.updateCurrentStudents("course001");

        assertTrue(result);
        verify(courseMapper).updateCurrentStudents("course001");
    }

    @Test
    void getByStatus_Success() {
        List<Course> pendingCourses = Arrays.asList(course);
        when(courseMapper.getByStatus("PENDING_APPROVAL")).thenReturn(pendingCourses);

        List<Course> result = courseService.getByStatus("PENDING_APPROVAL");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("course001", result.get(0).getCourseId());
        verify(courseMapper).getByStatus("PENDING_APPROVAL");
    }

    @Test
    void getByTeacherId_Success() {
        List<Course> teacherCourses = Arrays.asList(course);
        when(courseMapper.getByTeacherId("tea001")).thenReturn(teacherCourses);

        List<Course> result = courseService.getByTeacherId("tea001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("tea001", result.get(0).getTeacherId());
        verify(courseMapper).getByTeacherId("tea001");
    }

    @Test
    void count_Success() {
        when(courseMapper.count()).thenReturn(10);

        int result = courseService.count();

        assertEquals(10, result);
        verify(courseMapper).count();
    }

    @Test
    void getPopularCourses_Success() {
        List<Course> popularCourses = Arrays.asList(course);
        when(courseMapper.getPopularCourses(5)).thenReturn(popularCourses);

        List<Course> result = courseService.getPopularCourses(5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("course001", result.get(0).getCourseId());
        verify(courseMapper).getPopularCourses(5);
    }
}