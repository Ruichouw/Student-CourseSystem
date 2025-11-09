package com.itjay.service.impl;

import com.itjay.mapper.StudentMapper;
import com.itjay.pojo.Student;
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
class StudentServiceImplTest {

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setStudentId("stu001");
        student.setName("张三");
        student.setGender("男");
        student.setAge(20);
        student.setDepartment("计算机学院");
        student.setMajor("软件工程");
        student.setClassName("软工1班");
        student.setPassword("123456");
        student.setEmail("zhangsan@example.com");
        student.setPhone("13800138000");
    }

    @Test
    void getById_Success() {
        when(studentMapper.selectById("stu001")).thenReturn(student);

        Student result = studentService.getById("stu001");

        assertNotNull(result);
        assertEquals("stu001", result.getStudentId());
        assertEquals("张三", result.getName());
        verify(studentMapper).selectById("stu001");
    }

    @Test
    void getAll_Success() {
        List<Student> students = Arrays.asList(student);
        when(studentMapper.selectAll()).thenReturn(students);

        List<Student> result = studentService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("stu001", result.get(0).getStudentId());
        verify(studentMapper).selectAll();
    }

    @Test
    void getByCondition_Success() {
        Student condition = new Student();
        condition.setDepartment("计算机学院");

        List<Student> students = Arrays.asList(student);
        when(studentMapper.selectByCondition(condition)).thenReturn(students);

        List<Student> result = studentService.getByCondition(condition);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("计算机学院", result.get(0).getDepartment());
        verify(studentMapper).selectByCondition(condition);
    }

    @Test
    void add_Success_WithDefaultPassword() {
        Student newStudent = new Student();
        newStudent.setStudentId("stu002");
        newStudent.setName("李四");
        newStudent.setPassword(null); // 密码为空

        when(studentMapper.insert(newStudent)).thenReturn(1);

        boolean result = studentService.add(newStudent);

        assertTrue(result);
        assertEquals("123456", newStudent.getPassword()); // 应该设置默认密码
        verify(studentMapper).insert(newStudent);
    }

    @Test
    void add_Success_WithCustomPassword() {
        Student newStudent = new Student();
        newStudent.setStudentId("stu002");
        newStudent.setName("李四");
        newStudent.setPassword("custom123"); // 自定义密码

        when(studentMapper.insert(newStudent)).thenReturn(1);

        boolean result = studentService.add(newStudent);

        assertTrue(result);
        assertEquals("custom123", newStudent.getPassword()); // 应该保持自定义密码
        verify(studentMapper).insert(newStudent);
    }

    @Test
    void add_Failure() {
        Student newStudent = new Student();
        newStudent.setStudentId("stu002");
        newStudent.setName("李四");

        when(studentMapper.insert(newStudent)).thenReturn(0);

        boolean result = studentService.add(newStudent);

        assertFalse(result);
        verify(studentMapper).insert(newStudent);
    }

    @Test
    void update_Success() {
        when(studentMapper.update(student)).thenReturn(1);

        boolean result = studentService.update(student);

        assertTrue(result);
        verify(studentMapper).update(student);
    }

    @Test
    void update_Failure() {
        when(studentMapper.update(student)).thenReturn(0);

        boolean result = studentService.update(student);

        assertFalse(result);
        verify(studentMapper).update(student);
    }

    @Test
    void delete_Success() {
        when(studentMapper.deleteById("stu001")).thenReturn(1);

        boolean result = studentService.delete("stu001");

        assertTrue(result);
        verify(studentMapper).deleteById("stu001");
    }

    @Test
    void delete_Failure() {
        when(studentMapper.deleteById("stu001")).thenReturn(0);

        boolean result = studentService.delete("stu001");

        assertFalse(result);
        verify(studentMapper).deleteById("stu001");
    }

    @Test
    void login_Success() {
        when(studentMapper.login("stu001", "123456")).thenReturn(student);

        Student result = studentService.login("stu001", "123456");

        assertNotNull(result);
        assertEquals("stu001", result.getStudentId());
        assertEquals("张三", result.getName());
        verify(studentMapper).login("stu001", "123456");
    }

    @Test
    void login_Failure_WrongPassword() {
        when(studentMapper.login("stu001", "wrongpassword")).thenReturn(null);

        Student result = studentService.login("stu001", "wrongpassword");

        assertNull(result);
        verify(studentMapper).login("stu001", "wrongpassword");
    }

    @Test
    void login_Failure_UserNotFound() {
        when(studentMapper.login("nonexistent", "123456")).thenReturn(null);

        Student result = studentService.login("nonexistent", "123456");

        assertNull(result);
        verify(studentMapper).login("nonexistent", "123456");
    }
}