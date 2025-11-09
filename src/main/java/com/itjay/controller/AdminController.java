package com.itjay.controller;

import com.itjay.pojo.Result;
import com.itjay.pojo.Admin;
import com.itjay.pojo.Student;
import com.itjay.pojo.Teacher;
import com.itjay.pojo.Course;
import com.itjay.pojo.SystemConfig;
import com.itjay.service.AdminService;
import com.itjay.service.StudentService;
import com.itjay.service.TeacherService;
import com.itjay.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private CourseService courseService;

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        Admin result = adminService.login(admin.getAdminId(), admin.getPassword());
        if (result != null) {
            return Result.success(result);
        } else {
            return Result.error("管理员账号或密码错误");
        }
    }

    @GetMapping("/profile/{id}")
    public Result getProfile(@PathVariable("id") String adminId) {
        return Result.success(adminService.getById(adminId));
    }

    @PutMapping("/profile")
    public Result updateProfile(@RequestBody Admin admin) {
        return Result.success(adminService.update(admin));
    }

    // 获取所有学生列表
    @GetMapping("/users/students")
    public Result getAllStudents() {
        List<Student> students = studentService.getAll();
        return Result.success(students);
    }
    
    // 获取所有教师列表
    @GetMapping("/users/teachers")
    public Result getAllTeachers() {
        List<Teacher> teachers = teacherService.getAll();
        return Result.success(teachers);
    }
    
    // 获取所有课程列表
    @GetMapping("/courses/all")
    public Result getAllCourses() {
        List<Course> courses = courseService.getAll();
        return Result.success(courses);
    }

    // 用户管理相关接口
    @PostMapping("/users/students")
    public Result addStudent(@RequestBody Student student) {
        return Result.success(adminService.addStudent(student));
    }

    @PostMapping("/users/teachers")
    public Result addTeacher(@RequestBody Teacher teacher) {
        return Result.success(adminService.addTeacher(teacher));
    }

    @PutMapping("/users/students/{studentId}")
    public Result updateStudent(@PathVariable String studentId, @RequestBody Student student) {
        student.setStudentId(studentId); // Ensure ID from path is used
        if (adminService.updateStudent(student)) {
            return Result.success(student);
        } else {
            return Result.error("更新学生信息失败");
        }
    }

    @DeleteMapping("/users/students/{studentId}")
    public Result deleteStudent(@PathVariable String studentId) {
        if (adminService.deleteStudent(studentId)) {
            return Result.success("删除学生成功");
        } else {
            return Result.error("删除学生失败");
        }
    }

    @PutMapping("/users/teachers/{teacherId}")
    public Result updateTeacher(@PathVariable String teacherId, @RequestBody Teacher teacher) {
        teacher.setTeacherId(teacherId); // Ensure ID from path is used
        if (adminService.updateTeacher(teacher)) {
            return Result.success(teacher);
        } else {
            return Result.error("更新教师信息失败");
        }
    }

    @DeleteMapping("/users/teachers/{teacherId}")
    public Result deleteTeacher(@PathVariable String teacherId) {
        if (adminService.deleteTeacher(teacherId)) {
            return Result.success("删除教师成功");
        } else {
            return Result.error("删除教师失败");
        }
    }

    @PutMapping("/users/reset-password")
    public Result resetPassword(@RequestBody Map<String, String> data) {
        String userType = data.get("userType");
        String userId = data.get("userId");
        String newPassword = data.get("newPassword");
        return Result.success(adminService.resetPassword(userType, userId, newPassword));
    }

    // 课程审核
    @GetMapping("/courses/pending")
    public Result getPendingCourses() {
        return Result.success(adminService.getPendingCourses());
    }

    @PutMapping("/courses/approve/{courseId}")
    public Result approveCourse(@PathVariable("courseId") String courseId) {
        return Result.success(adminService.approveCourse(courseId));
    }

    @PutMapping("/courses/reject/{courseId}")
    public Result rejectCourse(@PathVariable("courseId") String courseId, @RequestBody Map<String, String> data) {
        String reason = data.get("reason");
        return Result.success(adminService.rejectCourse(courseId, reason));
    }

    // 统计数据
    @GetMapping("/statistics")
    public Result getStatistics() {
        return Result.success(adminService.getSystemStatistics());
    }

    // Admin direct course management
    @PostMapping("/courses")
    public Result adminAddCourse(@RequestBody Course course) {
        if (adminService.adminAddCourse(course)) {
            return Result.success(course);
        } else {
            return Result.error("管理员添加课程失败，课程编号可能已存在");
        }
    }

    @PutMapping("/courses/{courseId}")
    public Result adminUpdateCourse(@PathVariable String courseId, @RequestBody Course course) {
        course.setCourseId(courseId); // Ensure ID from path is used
        if (adminService.adminUpdateCourse(course)) {
            return Result.success(course);
        } else {
            return Result.error("管理员更新课程失败，课程可能不存在");
        }
    }

    @DeleteMapping("/courses/{courseId}")
    public Result adminDeleteCourse(@PathVariable String courseId) {
        if (adminService.adminDeleteCourse(courseId)) {
            return Result.success("管理员删除课程成功");
        } else {
            return Result.error("管理员删除课程失败");
        }
    }

    // System Configuration
    @GetMapping("/system-config")
    public Result getSystemConfig() {
        SystemConfig config = adminService.getSystemConfig();
        if (config != null) {
            return Result.success(config);
        } else {
            // This case should ideally not happen if a default row is always present
            return Result.error("系统配置未找到"); 
        }
    }

    @PutMapping("/system-config")
    public Result updateSystemConfig(@RequestBody SystemConfig config) {
        if (adminService.updateSystemConfig(config)) {
            return Result.success(config);
        } else {
            return Result.error("更新系统配置失败");
        }
    }
}