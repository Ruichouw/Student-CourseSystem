package com.itjay.service;

import com.itjay.pojo.Admin;
import com.itjay.pojo.Student;
import com.itjay.pojo.Teacher;
import com.itjay.pojo.Course;
import com.itjay.pojo.SystemConfig;

import java.util.List;
import java.util.Map;

public interface AdminService {
    Admin login(String adminId, String password);
    Admin getById(String adminId);
    boolean update(Admin admin);
    boolean addStudent(Student student);
    boolean addTeacher(Teacher teacher);
    boolean resetPassword(String userType, String userId, String newPassword);
    List<Course> getPendingCourses();
    boolean approveCourse(String courseId);
    boolean rejectCourse(String courseId, String reason);
    Map<String, Object> getSystemStatistics();

    // Student management
    boolean deleteStudent(String studentId);
    boolean updateStudent(Student student);

    // Teacher management
    boolean deleteTeacher(String teacherId);
    boolean updateTeacher(Teacher teacher);

    // Admin direct course management
    boolean adminAddCourse(Course course);
    boolean adminUpdateCourse(Course course);
    boolean adminDeleteCourse(String courseId);

    // System Configuration
    SystemConfig getSystemConfig();
    boolean updateSystemConfig(SystemConfig config);
}