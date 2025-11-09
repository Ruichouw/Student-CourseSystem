package com.itjay.controller;

import com.itjay.pojo.*;
import com.itjay.service.AdminService;
import com.itjay.service.StudentService;
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
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private AdminController adminController;

    private Admin testAdmin;
    private Student testStudent;
    private Teacher testTeacher;
    private Course testCourse;
    private SystemConfig testSystemConfig;

    @BeforeEach
    void setUp() {
        testAdmin = new Admin();
        testAdmin.setAdminId("admin001");
        testAdmin.setName("系统管理员");
        testAdmin.setPassword("admin123");

        testStudent = new Student();
        testStudent.setStudentId("2021001");
        testStudent.setName("张三");

        testTeacher = new Teacher();
        testTeacher.setTeacherId("T001");
        testTeacher.setName("李老师");

        testCourse = new Course();
        testCourse.setCourseId("C001");
        testCourse.setName("Java程序设计");

        testSystemConfig = new SystemConfig();
        testSystemConfig.setId(1);
    }

    @Test
    void login_Success() {
        Admin loginAdmin = new Admin();
        loginAdmin.setAdminId("admin001");
        loginAdmin.setPassword("admin123");

        when(adminService.login("admin001", "admin123")).thenReturn(testAdmin);

        Result result = adminController.login(loginAdmin);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testAdmin, result.getData());
        verify(adminService, times(1)).login("admin001", "admin123");
    }

    @Test
    void login_Failure() {
        Admin loginAdmin = new Admin();
        loginAdmin.setAdminId("admin001");
        loginAdmin.setPassword("wrongPassword");

        when(adminService.login("admin001", "wrongPassword")).thenReturn(null);

        Result result = adminController.login(loginAdmin);

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("管理员账号或密码错误", result.getMsg());
        verify(adminService, times(1)).login("admin001", "wrongPassword");
    }

    @Test
    void getProfile_Success() {
        when(adminService.getById("admin001")).thenReturn(testAdmin);

        Result result = adminController.getProfile("admin001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testAdmin, result.getData());
        verify(adminService, times(1)).getById("admin001");
    }

    @Test
    void updateProfile_Success() {
        when(adminService.update(any(Admin.class))).thenReturn(true);

        Result result = adminController.updateProfile(testAdmin);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(adminService, times(1)).update(any(Admin.class));
    }

    @Test
    void getAllStudents() {
        List<Student> studentList = Arrays.asList(testStudent);
        when(studentService.getAll()).thenReturn(studentList);

        Result result = adminController.getAllStudents();

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(studentList, result.getData());
        verify(studentService, times(1)).getAll();
    }

    @Test
    void getAllTeachers() {
        List<Teacher> teacherList = Arrays.asList(testTeacher);
        when(teacherService.getAll()).thenReturn(teacherList);

        Result result = adminController.getAllTeachers();

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(teacherList, result.getData());
        verify(teacherService, times(1)).getAll();
    }

    @Test
    void getAllCourses() {
        List<Course> courseList = Arrays.asList(testCourse);
        when(courseService.getAll()).thenReturn(courseList);

        Result result = adminController.getAllCourses();

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(courseList, result.getData());
        verify(courseService, times(1)).getAll();
    }

    @Test
    void addStudent_Success() {
        when(adminService.addStudent(any(Student.class))).thenReturn(true);

        Result result = adminController.addStudent(testStudent);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(adminService, times(1)).addStudent(any(Student.class));
    }

    @Test
    void addTeacher_Success() {
        when(adminService.addTeacher(any(Teacher.class))).thenReturn(true);

        Result result = adminController.addTeacher(testTeacher);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(adminService, times(1)).addTeacher(any(Teacher.class));
    }

    @Test
    void updateStudent_Success() {
        when(adminService.updateStudent(any(Student.class))).thenReturn(true);

        Result result = adminController.updateStudent("2021001", testStudent);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testStudent, result.getData());
        verify(adminService, times(1)).updateStudent(any(Student.class));
    }

    @Test
    void updateStudent_Failure() {
        when(adminService.updateStudent(any(Student.class))).thenReturn(false);

        Result result = adminController.updateStudent("2021001", testStudent);

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("更新学生信息失败", result.getMsg());
        verify(adminService, times(1)).updateStudent(any(Student.class));
    }

    @Test
    void deleteStudent_Success() {
        when(adminService.deleteStudent("2021001")).thenReturn(true);

        Result result = adminController.deleteStudent("2021001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals("删除学生成功", result.getData());
        verify(adminService, times(1)).deleteStudent("2021001");
    }

    @Test
    void deleteStudent_Failure() {
        when(adminService.deleteStudent("2021001")).thenReturn(false);

        Result result = adminController.deleteStudent("2021001");

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("删除学生失败", result.getMsg());
        verify(adminService, times(1)).deleteStudent("2021001");
    }

    @Test
    void updateTeacher_Success() {
        when(adminService.updateTeacher(any(Teacher.class))).thenReturn(true);

        Result result = adminController.updateTeacher("T001", testTeacher);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testTeacher, result.getData());
        verify(adminService, times(1)).updateTeacher(any(Teacher.class));
    }

    @Test
    void deleteTeacher_Success() {
        when(adminService.deleteTeacher("T001")).thenReturn(true);

        Result result = adminController.deleteTeacher("T001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals("删除教师成功", result.getData());
        verify(adminService, times(1)).deleteTeacher("T001");
    }

    @Test
    void resetPassword_Success() {
        Map<String, String> data = new HashMap<>();
        data.put("userType", "student");
        data.put("userId", "2021001");
        data.put("newPassword", "newPassword123");

        when(adminService.resetPassword("student", "2021001", "newPassword123")).thenReturn(true);

        Result result = adminController.resetPassword(data);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(adminService, times(1)).resetPassword("student", "2021001", "newPassword123");
    }

    @Test
    void getPendingCourses() {
        List<Course> pendingCourses = Arrays.asList(testCourse);
        when(adminService.getPendingCourses()).thenReturn(pendingCourses);

        Result result = adminController.getPendingCourses();

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(pendingCourses, result.getData());
        verify(adminService, times(1)).getPendingCourses();
    }

    @Test
    void approveCourse_Success() {
        when(adminService.approveCourse("C001")).thenReturn(true);

        Result result = adminController.approveCourse("C001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(adminService, times(1)).approveCourse("C001");
    }

    @Test
    void rejectCourse_Success() {
        Map<String, String> data = new HashMap<>();
        data.put("reason", "课程内容不符合要求");

        when(adminService.rejectCourse("C001", "课程内容不符合要求")).thenReturn(true);

        Result result = adminController.rejectCourse("C001", data);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertTrue((Boolean) result.getData());
        verify(adminService, times(1)).rejectCourse("C001", "课程内容不符合要求");
    }

    @Test
    void getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalStudents", 100);
        statistics.put("totalTeachers", 20);

        when(adminService.getSystemStatistics()).thenReturn(statistics);

        Result result = adminController.getStatistics();

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(statistics, result.getData());
        verify(adminService, times(1)).getSystemStatistics();
    }

    @Test
    void adminAddCourse_Success() {
        when(adminService.adminAddCourse(any(Course.class))).thenReturn(true);

        Result result = adminController.adminAddCourse(testCourse);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testCourse, result.getData());
        verify(adminService, times(1)).adminAddCourse(any(Course.class));
    }

    @Test
    void adminAddCourse_Failure() {
        when(adminService.adminAddCourse(any(Course.class))).thenReturn(false);

        Result result = adminController.adminAddCourse(testCourse);

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("管理员添加课程失败，课程编号可能已存在", result.getMsg());
        verify(adminService, times(1)).adminAddCourse(any(Course.class));
    }

    @Test
    void adminUpdateCourse_Success() {
        when(adminService.adminUpdateCourse(any(Course.class))).thenReturn(true);

        Result result = adminController.adminUpdateCourse("C001", testCourse);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testCourse, result.getData());
        verify(adminService, times(1)).adminUpdateCourse(any(Course.class));
    }

    @Test
    void adminDeleteCourse_Success() {
        when(adminService.adminDeleteCourse("C001")).thenReturn(true);

        Result result = adminController.adminDeleteCourse("C001");

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals("管理员删除课程成功", result.getData());
        verify(adminService, times(1)).adminDeleteCourse("C001");
    }

    @Test
    void getSystemConfig_Success() {
        when(adminService.getSystemConfig()).thenReturn(testSystemConfig);

        Result result = adminController.getSystemConfig();

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testSystemConfig, result.getData());
        verify(adminService, times(1)).getSystemConfig();
    }

    @Test
    void getSystemConfig_NotFound() {
        when(adminService.getSystemConfig()).thenReturn(null);

        Result result = adminController.getSystemConfig();

        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("系统配置未找到", result.getMsg());
        verify(adminService, times(1)).getSystemConfig();
    }

    @Test
    void updateSystemConfig_Success() {
        when(adminService.updateSystemConfig(any(SystemConfig.class))).thenReturn(true);

        Result result = adminController.updateSystemConfig(testSystemConfig);

        assertNotNull(result);
        assertEquals(1, result.getCode());
        assertEquals(testSystemConfig, result.getData());
        verify(adminService, times(1)).updateSystemConfig(any(SystemConfig.class));
    }
}