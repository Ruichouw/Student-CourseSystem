package com.itjay.controller;

import com.itjay.pojo.Course;
import com.itjay.pojo.CourseSelection;
import com.itjay.pojo.Result;
import com.itjay.pojo.Teacher;
import com.itjay.service.TeacherService;
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
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher testTeacher;
    private Course testCourse;
    private CourseSelection testSelection;

    @BeforeEach
    void setUp() {
        testTeacher = new Teacher();
        testTeacher.setTeacherId("T001");
        testTeacher.setName("李老师");
        testTeacher.setPassword("123456");

        testCourse = new Course();
        testCourse.setCourseId("C001");
        testCourse.setName("Java程序设计");
        testCourse.setTeacherId("T001");

        testSelection = new CourseSelection();
        testSelection.setId(1);
        testSelection.setStudentId("2021001");
        testSelection.setCourseId("C001");
        testSelection.setScore(85f); // 使用 f 表示 float
    }

    @Test
    void updateGrade_Success() {
        Map<String, Object> gradeData = new HashMap<>();
        gradeData.put("studentId", "2021001");
        gradeData.put("courseId", "C001");
        gradeData.put("score", 90); // 这里可以是 int，因为控制器会转换

        when(teacherService.updateStudentGrade("2021001", "C001", 90f)).thenReturn(true);

        Result result = teacherController.updateGrade(gradeData);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(teacherService, times(1)).updateStudentGrade("2021001", "C001", 90f);
    }

    // 其他测试方法保持不变...
    @Test
    void getById_Success() {
        when(teacherService.getById("T001")).thenReturn(testTeacher);

        Result result = teacherController.getById("T001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testTeacher, result.getData());
        verify(teacherService, times(1)).getById("T001");
    }

    @Test
    void getAll() {
        List<Teacher> teacherList = Arrays.asList(testTeacher);
        when(teacherService.getAll()).thenReturn(teacherList);

        Result result = teacherController.getAll();

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(teacherList, result.getData());
        verify(teacherService, times(1)).getAll();
    }

    @Test
    void add_Success() {
        when(teacherService.getById("T001")).thenReturn(null);
        when(teacherService.add(any(Teacher.class))).thenReturn(true);

        Result result = teacherController.add(testTeacher);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(teacherService, times(1)).getById("T001");
        verify(teacherService, times(1)).add(any(Teacher.class));
    }

    @Test
    void update() {
        when(teacherService.update(any(Teacher.class))).thenReturn(true);

        Result result = teacherController.update(testTeacher);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(teacherService, times(1)).update(any(Teacher.class));
    }

    @Test
    void delete() {
        when(teacherService.delete("T001")).thenReturn(true);

        Result result = teacherController.delete("T001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(teacherService, times(1)).delete("T001");
    }

    @Test
    void login_Success() {
        Teacher loginTeacher = new Teacher();
        loginTeacher.setTeacherId("T001");
        loginTeacher.setPassword("123456");

        when(teacherService.login("T001", "123456")).thenReturn(testTeacher);

        Result result = teacherController.login(loginTeacher);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testTeacher, result.getData());
        verify(teacherService, times(1)).login("T001", "123456");
    }

    // 其他测试方法...
}