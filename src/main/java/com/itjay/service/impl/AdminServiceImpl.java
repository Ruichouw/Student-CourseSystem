package com.itjay.service.impl;

import com.itjay.mapper.AdminMapper;
import com.itjay.mapper.StudentMapper;
import com.itjay.mapper.TeacherMapper;
import com.itjay.mapper.CourseMapper;
import com.itjay.mapper.CourseSelectionMapper;
import com.itjay.mapper.SystemConfigMapper;
import com.itjay.pojo.Admin;
import com.itjay.pojo.Student;
import com.itjay.pojo.Teacher;
import com.itjay.pojo.Course;
import com.itjay.pojo.SystemConfig;
import com.itjay.service.AdminService;
import com.itjay.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private TeacherMapper teacherMapper;
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private SystemConfigMapper systemConfigMapper;
    
    @Override
    public Admin login(String adminId, String password) {
        return adminMapper.login(adminId, password);
    }
    
    @Override
    public Admin getById(String adminId) {
        return adminMapper.getById(adminId);
    }
    
    @Override
    public boolean update(Admin admin) {
        return adminMapper.update(admin) > 0;
    }
    
    @Override
    public boolean addStudent(Student student) {
        return studentMapper.insert(student) > 0;
    }
    
    @Override
    public boolean addTeacher(Teacher teacher) {
        return teacherMapper.insert(teacher) > 0;
    }
    
    @Override
    public boolean resetPassword(String userType, String userId, String newPassword) {
        boolean result = false;
        switch (userType) {
            case "student":
                Student student = studentMapper.getById(userId);
                if (student != null) {
                    student.setPassword(newPassword);
                    result = studentMapper.update(student) > 0;
                }
                break;
            case "teacher":
                Teacher teacher = teacherMapper.getById(userId);
                if (teacher != null) {
                    teacher.setPassword(newPassword);
                    result = teacherMapper.update(teacher) > 0;
                }
                break;
            case "admin":
                Admin admin = adminMapper.getById(userId);
                if (admin != null) {
                    admin.setPassword(newPassword);
                    result = adminMapper.update(admin) > 0;
                }
                break;
        }
        return result;
    }
    
    @Override
    public List<Course> getPendingCourses() {
        return courseMapper.getByStatus("PENDING_APPROVAL");
    }
    
    @Override
    public boolean approveCourse(String courseId) {
        Course course = courseMapper.getById(courseId);
        if (course != null) {
            course.setStatus("APPROVED");
            course.setRejectionReason(null);
            return courseMapper.update(course) > 0;
        }
        return false;
    }
    
    @Override
    public boolean rejectCourse(String courseId, String reason) {
        Course course = courseMapper.getById(courseId);
        if (course != null) {
            course.setStatus("REJECTED");
            course.setRejectionReason(reason);
            return courseMapper.update(course) > 0;
        }
        return false;
    }
    
    @Override
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 学生总数
        statistics.put("totalStudents", studentMapper.count());
        
        // 教师总数
        statistics.put("totalTeachers", teacherMapper.count());
        
        // 课程总数
        statistics.put("totalCourses", courseMapper.count());
        
        // 选课总数
        statistics.put("totalSelections", courseSelectionMapper.count());
        
        // 热门课程（选课人数最多的前5门）
        statistics.put("popularCourses", courseMapper.getPopularCourses(5));
        
        // 各院系学生分布
        statistics.put("departmentDistribution", studentMapper.getDepartmentDistribution());
        
        return statistics;
    }

    @Override
    public boolean deleteStudent(String studentId) {
        // Add any business logic if needed, e.g., check for course selections before deleting
        return studentMapper.deleteById(studentId) > 0;
    }

    @Override
    public boolean updateStudent(Student student) {
        return studentMapper.update(student) > 0;
    }

    @Override
    public boolean deleteTeacher(String teacherId) {
        // Add any business logic if needed, e.g., check if teacher has assigned courses
        return teacherMapper.delete(teacherId) > 0;
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        return teacherMapper.update(teacher) > 0;
    }

    @Override
    public boolean adminAddCourse(Course course) {
        if (courseService.getById(course.getCourseId()) != null) {
            // Or throw an exception / return a specific error object
            System.err.println("Admin trying to add course with existing ID: " + course.getCourseId());
            return false; 
        }
        course.setStatus("APPROVED");
        if (course.getCurrentStudents() == null) {
            course.setCurrentStudents(0);
        }
        course.setRejectionReason(null);
        return courseMapper.insert(course) > 0;
    }

    @Override
    public boolean adminUpdateCourse(Course course) {
        Course existingCourse = courseMapper.getById(course.getCourseId());
        if (existingCourse == null) {
            System.err.println("Admin trying to update non-existent course: " + course.getCourseId());
            return false;
        }
        // Admin can update any course, ensure status is set/kept as APPROVED
        course.setStatus("APPROVED"); 
        course.setRejectionReason(null);
        return courseMapper.update(course) > 0;
    }

    @Override
    public boolean adminDeleteCourse(String courseId) {
        // Consider business logic: what if students are enrolled?
        // For now, direct delete.
        return courseMapper.deleteById(courseId) > 0;
    }

    @Override
    public SystemConfig getSystemConfig() {
        return systemConfigMapper.getConfig();
    }

    @Override
    public boolean updateSystemConfig(SystemConfig config) {
        // Ensure the config ID is 1 if we're always updating the same row
        if (config.getId() == null) {
            config.setId(1);
        }
        return systemConfigMapper.updateConfig(config) > 0;
    }
}