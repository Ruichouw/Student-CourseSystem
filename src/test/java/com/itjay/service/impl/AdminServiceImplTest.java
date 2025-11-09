package com.itjay.service.impl;

import com.itjay.mapper.*;
import com.itjay.pojo.*;
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
class AdminServiceImplTest {

    @Mock
    private AdminMapper adminMapper;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseSelectionMapper courseSelectionMapper;

    @Mock
    private CourseService courseService;

    @Mock
    private SystemConfigMapper systemConfigMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin admin;
    private Student student;
    private Teacher teacher;
    private Course course;
    private SystemConfig systemConfig;

    @BeforeEach
    void setUp() {
        admin = new Admin("admin001", "管理员", "password123");
        student = new Student("stu001", "张三", "男", 20, "计算机学院", "软件工程", "软工1班", "123456", "zhangsan@example.com", "13800138000");
        teacher = new Teacher("tea001", "李老师", "男", "计算机学院", "教授", "password123", "li@example.com", "13900139000");
        course = new Course("course001", "Java编程", 3.0f, 48, "tea001", "Java编程基础", 50, 0, "教学大纲", "教材", "周一 1-2节", "PENDING_APPROVAL", null, "李老师");
        systemConfig = new SystemConfig(1, null, null);
    }

    @Test
    void login_Success() {
        when(adminMapper.login("admin001", "password123")).thenReturn(admin);

        Admin result = adminService.login("admin001", "password123");

        assertNotNull(result);
        assertEquals("admin001", result.getAdminId());
        assertEquals("管理员", result.getName());
        verify(adminMapper).login("admin001", "password123");
    }

    @Test
    void login_Failure() {
        when(adminMapper.login("admin001", "wrongpassword")).thenReturn(null);

        Admin result = adminService.login("admin001", "wrongpassword");

        assertNull(result);
        verify(adminMapper).login("admin001", "wrongpassword");
    }

    @Test
    void getById_Success() {
        when(adminMapper.getById("admin001")).thenReturn(admin);

        Admin result = adminService.getById("admin001");

        assertNotNull(result);
        assertEquals("admin001", result.getAdminId());
        verify(adminMapper).getById("admin001");
    }

    @Test
    void update_Success() {
        when(adminMapper.update(admin)).thenReturn(1);

        boolean result = adminService.update(admin);

        assertTrue(result);
        verify(adminMapper).update(admin);
    }

    @Test
    void addStudent_Success() {
        when(studentMapper.insert(student)).thenReturn(1);

        boolean result = adminService.addStudent(student);

        assertTrue(result);
        verify(studentMapper).insert(student);
    }

    @Test
    void addTeacher_Success() {
        when(teacherMapper.insert(teacher)).thenReturn(1);

        boolean result = adminService.addTeacher(teacher);

        assertTrue(result);
        verify(teacherMapper).insert(teacher);
    }

    @Test
    void resetPassword_Student_Success() {
        when(studentMapper.getById("stu001")).thenReturn(student);
        when(studentMapper.update(any(Student.class))).thenReturn(1);

        boolean result = adminService.resetPassword("student", "stu001", "newpassword");

        assertTrue(result);
        assertEquals("newpassword", student.getPassword());
        verify(studentMapper).update(student);
    }

    @Test
    void resetPassword_Teacher_Success() {
        when(teacherMapper.getById("tea001")).thenReturn(teacher);
        when(teacherMapper.update(any(Teacher.class))).thenReturn(1);

        boolean result = adminService.resetPassword("teacher", "tea001", "newpassword");

        assertTrue(result);
        assertEquals("newpassword", teacher.getPassword());
        verify(teacherMapper).update(teacher);
    }

    @Test
    void resetPassword_Admin_Success() {
        when(adminMapper.getById("admin001")).thenReturn(admin);
        when(adminMapper.update(any(Admin.class))).thenReturn(1);

        boolean result = adminService.resetPassword("admin", "admin001", "newpassword");

        assertTrue(result);
        assertEquals("newpassword", admin.getPassword());
        verify(adminMapper).update(admin);
    }

    @Test
    void resetPassword_UserNotFound() {
        when(studentMapper.getById("nonexistent")).thenReturn(null);

        boolean result = adminService.resetPassword("student", "nonexistent", "newpassword");

        assertFalse(result);
        verify(studentMapper, never()).update(any(Student.class));
    }

    @Test
    void getPendingCourses_Success() {
        List<Course> pendingCourses = Arrays.asList(course);
        when(courseMapper.getByStatus("PENDING_APPROVAL")).thenReturn(pendingCourses);

        List<Course> result = adminService.getPendingCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("course001", result.get(0).getCourseId());
        verify(courseMapper).getByStatus("PENDING_APPROVAL");
    }

    @Test
    void approveCourse_Success() {
        when(courseMapper.getById("course001")).thenReturn(course);
        when(courseMapper.update(course)).thenReturn(1);

        boolean result = adminService.approveCourse("course001");

        assertTrue(result);
        assertEquals("APPROVED", course.getStatus());
        assertNull(course.getRejectionReason());
        verify(courseMapper).update(course);
    }

    @Test
    void approveCourse_CourseNotFound() {
        when(courseMapper.getById("nonexistent")).thenReturn(null);

        boolean result = adminService.approveCourse("nonexistent");

        assertFalse(result);
        verify(courseMapper, never()).update(any(Course.class));
    }

    @Test
    void rejectCourse_Success() {
        when(courseMapper.getById("course001")).thenReturn(course);
        when(courseMapper.update(course)).thenReturn(1);

        boolean result = adminService.rejectCourse("course001", "课程内容不符合要求");

        assertTrue(result);
        assertEquals("REJECTED", course.getStatus());
        assertEquals("课程内容不符合要求", course.getRejectionReason());
        verify(courseMapper).update(course);
    }

    @Test
    void getSystemStatistics_Success() {
        when(studentMapper.count()).thenReturn(100);
        when(teacherMapper.count()).thenReturn(10);
        when(courseMapper.count()).thenReturn(20);
        when(courseSelectionMapper.count()).thenReturn(500);

        List<Course> popularCourses = Arrays.asList(course);
        when(courseMapper.getPopularCourses(5)).thenReturn(popularCourses);

        List<Map<String, Object>> departmentDistribution = Arrays.asList(
                new HashMap<>() {{ put("department", "计算机学院"); put("count", 50); }}
        );
        when(studentMapper.getDepartmentDistribution()).thenReturn(departmentDistribution);

        Map<String, Object> statistics = adminService.getSystemStatistics();

        assertNotNull(statistics);
        assertEquals(100, statistics.get("totalStudents"));
        assertEquals(10, statistics.get("totalTeachers"));
        assertEquals(20, statistics.get("totalCourses"));
        assertEquals(500, statistics.get("totalSelections"));
        assertEquals(popularCourses, statistics.get("popularCourses"));
        assertEquals(departmentDistribution, statistics.get("departmentDistribution"));

        verify(studentMapper).count();
        verify(teacherMapper).count();
        verify(courseMapper).count();
        verify(courseSelectionMapper).count();
        verify(courseMapper).getPopularCourses(5);
        verify(studentMapper).getDepartmentDistribution();
    }

    @Test
    void deleteStudent_Success() {
        when(studentMapper.deleteById("stu001")).thenReturn(1);

        boolean result = adminService.deleteStudent("stu001");

        assertTrue(result);
        verify(studentMapper).deleteById("stu001");
    }

    @Test
    void updateStudent_Success() {
        when(studentMapper.update(student)).thenReturn(1);

        boolean result = adminService.updateStudent(student);

        assertTrue(result);
        verify(studentMapper).update(student);
    }

    @Test
    void deleteTeacher_Success() {
        when(teacherMapper.delete("tea001")).thenReturn(1);

        boolean result = adminService.deleteTeacher("tea001");

        assertTrue(result);
        verify(teacherMapper).delete("tea001");
    }

    @Test
    void updateTeacher_Success() {
        when(teacherMapper.update(teacher)).thenReturn(1);

        boolean result = adminService.updateTeacher(teacher);

        assertTrue(result);
        verify(teacherMapper).update(teacher);
    }

    @Test
    void adminAddCourse_Success() {
        when(courseService.getById("course001")).thenReturn(null);
        when(courseMapper.insert(course)).thenReturn(1);

        boolean result = adminService.adminAddCourse(course);

        assertTrue(result);
        assertEquals("APPROVED", course.getStatus());
        assertEquals(0, course.getCurrentStudents());
        assertNull(course.getRejectionReason());
        verify(courseMapper).insert(course);
    }

    @Test
    void adminAddCourse_CourseExists() {
        when(courseService.getById("course001")).thenReturn(course);

        boolean result = adminService.adminAddCourse(course);

        assertFalse(result);
        verify(courseMapper, never()).insert(any(Course.class));
    }

    @Test
    void adminUpdateCourse_Success() {
        when(courseMapper.getById("course001")).thenReturn(course);
        when(courseMapper.update(course)).thenReturn(1);

        boolean result = adminService.adminUpdateCourse(course);

        assertTrue(result);
        assertEquals("APPROVED", course.getStatus());
        assertNull(course.getRejectionReason());
        verify(courseMapper).update(course);
    }

    @Test
    void adminUpdateCourse_CourseNotFound() {
        when(courseMapper.getById("nonexistent")).thenReturn(null);

        Course nonExistentCourse = new Course();
        nonExistentCourse.setCourseId("nonexistent");
        boolean result = adminService.adminUpdateCourse(nonExistentCourse);

        assertFalse(result);
        verify(courseMapper, never()).update(any(Course.class));
    }

    @Test
    void adminDeleteCourse_Success() {
        when(courseMapper.deleteById("course001")).thenReturn(1);

        boolean result = adminService.adminDeleteCourse("course001");

        assertTrue(result);
        verify(courseMapper).deleteById("course001");
    }

    @Test
    void getSystemConfig_Success() {
        when(systemConfigMapper.getConfig()).thenReturn(systemConfig);

        SystemConfig result = adminService.getSystemConfig();

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(systemConfigMapper).getConfig();
    }

    @Test
    void updateSystemConfig_Success() {
        when(systemConfigMapper.updateConfig(systemConfig)).thenReturn(1);

        boolean result = adminService.updateSystemConfig(systemConfig);

        assertTrue(result);
        verify(systemConfigMapper).updateConfig(systemConfig);
    }

    @Test
    void updateSystemConfig_WithNullId() {
        SystemConfig configWithoutId = new SystemConfig();
        configWithoutId.setId(null);

        when(systemConfigMapper.updateConfig(any(SystemConfig.class))).thenReturn(1);

        boolean result = adminService.updateSystemConfig(configWithoutId);

        assertTrue(result);
        assertEquals(1, configWithoutId.getId());
        verify(systemConfigMapper).updateConfig(configWithoutId);
    }
}