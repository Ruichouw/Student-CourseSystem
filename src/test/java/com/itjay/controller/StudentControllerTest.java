package com.itjay.controller;

import com.itjay.pojo.Result;
import com.itjay.pojo.Student;
import com.itjay.service.StudentService;
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
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setStudentId("2021001");
        testStudent.setName("张三");
        testStudent.setPassword("123456");
    }

    @Test
    void getById_Success() {
        when(studentService.getById("2021001")).thenReturn(testStudent);

        Result result = studentController.getById("2021001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testStudent, result.getData());
        verify(studentService, times(1)).getById("2021001");
    }

    @Test
    void getById_NotFound() {
        when(studentService.getById("2021001")).thenReturn(null);

        Result result = studentController.getById("2021001");

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("学生不存在", result.getMsg());
        verify(studentService, times(1)).getById("2021001");
    }

    @Test
    void getAll() {
        List<Student> studentList = Arrays.asList(testStudent);
        when(studentService.getAll()).thenReturn(studentList);

        Result result = studentController.getAll();

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(studentList, result.getData());
        verify(studentService, times(1)).getAll();
    }

    @Test
    void search() {
        Student searchCriteria = new Student();
        searchCriteria.setName("张三");

        List<Student> studentList = Arrays.asList(testStudent);
        when(studentService.getByCondition(any(Student.class))).thenReturn(studentList);

        Result result = studentController.search(searchCriteria);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(studentList, result.getData());
        verify(studentService, times(1)).getByCondition(any(Student.class));
    }

    @Test
    void add_Success() {
        when(studentService.getById("2021001")).thenReturn(null);
        when(studentService.add(any(Student.class))).thenReturn(true);

        Result result = studentController.add(testStudent);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(studentService, times(1)).getById("2021001");
        verify(studentService, times(1)).add(any(Student.class));
    }

    @Test
    void add_DuplicateId() {
        when(studentService.getById("2021001")).thenReturn(testStudent);

        Result result = studentController.add(testStudent);

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("学号已存在", result.getMsg());
        verify(studentService, times(1)).getById("2021001");
        verify(studentService, never()).add(any(Student.class));
    }

    @Test
    void update() {
        when(studentService.update(any(Student.class))).thenReturn(true);

        Result result = studentController.update(testStudent);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(studentService, times(1)).update(any(Student.class));
    }

    @Test
    void delete() {
        when(studentService.delete("2021001")).thenReturn(true);

        Result result = studentController.delete("2021001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(studentService, times(1)).delete("2021001");
    }

    @Test
    void login_Success() {
        Student loginStudent = new Student();
        loginStudent.setStudentId("2021001");
        loginStudent.setPassword("123456");

        when(studentService.login("2021001", "123456")).thenReturn(testStudent);

        Result result = studentController.login(loginStudent);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testStudent, result.getData());
        verify(studentService, times(1)).login("2021001", "123456");
    }

    @Test
    void login_Failure() {
        Student loginStudent = new Student();
        loginStudent.setStudentId("2021001");
        loginStudent.setPassword("wrongPassword");

        when(studentService.login("2021001", "wrongPassword")).thenReturn(null);

        Result result = studentController.login(loginStudent);

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("学号或密码错误", result.getMsg());
        verify(studentService, times(1)).login("2021001", "wrongPassword");
    }
}