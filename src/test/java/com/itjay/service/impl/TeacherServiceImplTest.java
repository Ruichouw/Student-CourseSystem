package com.itjay.service.impl;

import com.itjay.mapper.TeacherMapper;
import com.itjay.mapper.CourseMapper;
import com.itjay.mapper.CourseSelectionMapper;
import com.itjay.pojo.Teacher;
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
class TeacherServiceImplTest {

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseSelectionMapper courseSelectionMapper;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    private Teacher teacher;
    private Course course;
    private CourseSelection courseSelection;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setTeacherId("tea001");
        teacher.setName("李老师");
        teacher.setGender("男");
        teacher.setDepartment("计算机学院");
        teacher.setTitle("教授");
        teacher.setPassword("password123");
        teacher.setEmail("li@example.com");
        teacher.setPhone("13900139000");

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
    }

    @Test
    void login_Success() {
        when(teacherMapper.login("tea001", "password123")).thenReturn(teacher);

        Teacher result = teacherService.login("tea001", "password123");

        assertNotNull(result);
        assertEquals("tea001", result.getTeacherId());
        assertEquals("李老师", result.getName());
        verify(teacherMapper).login("tea001", "password123");
    }

    @Test
    void login_Failure() {
        when(teacherMapper.login("tea001", "wrongpassword")).thenReturn(null);

        Teacher result = teacherService.login("tea001", "wrongpassword");

        assertNull(result);
        verify(teacherMapper).login("tea001", "wrongpassword");
    }

    @Test
    void getById_Success() {
        when(teacherMapper.getById("tea001")).thenReturn(teacher);

        Teacher result = teacherService.getById("tea001");

        assertNotNull(result);
        assertEquals("tea001", result.getTeacherId());
        assertEquals("李老师", result.getName());
        verify(teacherMapper).getById("tea001");
    }

    @Test
    void getAll_Success() {
        List<Teacher> teachers = Arrays.asList(teacher);
        when(teacherMapper.getAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("tea001", result.get(0).getTeacherId());
        verify(teacherMapper).getAll();
    }

    @Test
    void add_Success() {
        when(teacherMapper.insert(teacher)).thenReturn(1);

        boolean result = teacherService.add(teacher);

        assertTrue(result);
        verify(teacherMapper).insert(teacher);
    }

    @Test
    void update_Success() {
        when(teacherMapper.update(teacher)).thenReturn(1);

        boolean result = teacherService.update(teacher);

        assertTrue(result);
        verify(teacherMapper).update(teacher);
    }

    @Test
    void delete_Success() {
        when(teacherMapper.delete("tea001")).thenReturn(1);

        boolean result = teacherService.delete("tea001");

        assertTrue(result);
        verify(teacherMapper).delete("tea001");
    }

    @Test
    void getTeacherCourses_Success() {
        List<Course> courses = Arrays.asList(course);
        when(courseMapper.getByTeacherId("tea001")).thenReturn(courses);

        List<Course> result = teacherService.getTeacherCourses("tea001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("tea001", result.get(0).getTeacherId());
        verify(courseMapper).getByTeacherId("tea001");
    }

    @Test
    void getCourseStudents_Success() {
        List<CourseSelection> students = Arrays.asList(courseSelection);
        when(courseSelectionMapper.getStudentsByCourseId("course001")).thenReturn(students);

        List<CourseSelection> result = teacherService.getCourseStudents("course001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("course001", result.get(0).getCourseId());
        verify(courseSelectionMapper).getStudentsByCourseId("course001");
    }

    @Test
    void updateStudentGrade_Success() {
        when(courseSelectionMapper.updateScore("stu001", "course001", 90.0f)).thenReturn(1);

        boolean result = teacherService.updateStudentGrade("stu001", "course001", 90.0f);

        assertTrue(result);
        verify(courseSelectionMapper).updateScore("stu001", "course001", 90.0f);
    }

    @Test
    void updateStudentGrade_Failure() {
        when(courseSelectionMapper.updateScore("stu001", "course001", 90.0f)).thenReturn(0);

        boolean result = teacherService.updateStudentGrade("stu001", "course001", 90.0f);

        assertFalse(result);
        verify(courseSelectionMapper).updateScore("stu001", "course001", 90.0f);
    }

    @Test
    void submitCoursePlan_Success() {
        Course newCourse = new Course();
        newCourse.setCourseId("course002");
        newCourse.setName("Python编程");
        newCourse.setStatus(null); // 初始状态为空

        when(courseMapper.update(newCourse)).thenReturn(1);

        boolean result = teacherService.submitCoursePlan(newCourse);

        assertTrue(result);
        assertEquals("PENDING_APPROVAL", newCourse.getStatus());
        verify(courseMapper).update(newCourse);
    }

    @Test
    void updateCourseInfo_Success() {
        when(courseMapper.update(course)).thenReturn(1);

        boolean result = teacherService.updateCourseInfo(course);

        assertTrue(result);
        verify(courseMapper).update(course);
    }

    @Test
    void updateCourseInfo_Failure() {
        when(courseMapper.update(course)).thenReturn(0);

        boolean result = teacherService.updateCourseInfo(course);

        assertFalse(result);
        verify(courseMapper).update(course);
    }
}