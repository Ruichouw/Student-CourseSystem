package com.itjay.controller;

import com.itjay.pojo.Result;
import com.itjay.pojo.Teacher;
import com.itjay.service.TeacherService;
import com.itjay.pojo.Course;
import com.itjay.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String teacherId) {
        Teacher teacher = teacherService.getById(teacherId);
        if (teacher != null) {
            return Result.success(teacher);
        } else {
            return Result.error("教师不存在");
        }
    }

    @GetMapping
    public Result getAll() {
        return Result.success(teacherService.getAll());
    }

    @PostMapping
    public Result add(@RequestBody Teacher teacher) {
        if (teacherService.getById(teacher.getTeacherId()) != null) {
            return Result.error("教师工号已存在");
        }
        return Result.success(teacherService.add(teacher));
    }

    @PutMapping
    public Result update(@RequestBody Teacher teacher) {
        return Result.success(teacherService.update(teacher));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String teacherId) {
        return Result.success(teacherService.delete(teacherId));
    }

    @PostMapping("/login")
    public Result login(@RequestBody Teacher teacher) {
        Teacher result = teacherService.login(teacher.getTeacherId(), teacher.getPassword());
        if (result != null) {
            return Result.success(result);
        } else {
            return Result.error("工号或密码错误");
        }
    }

    // 获取教师个人信息（用于个人资料页面）
    @GetMapping("/profile/{id}")
    public Result getProfile(@PathVariable("id") String teacherId) {
        Teacher teacher = teacherService.getById(teacherId);
        if (teacher != null) {
            return Result.success(teacher);
        } else {
            return Result.error("教师不存在");
        }
    }

    // 更新教师个人信息（用于个人资料页面）
    @PutMapping("/profile")
    public Result updateProfile(@RequestBody Teacher teacher) {
        if (teacherService.update(teacher)) {
            return Result.success(teacher);
        } else {
            return Result.error("更新教师信息失败");
        }
    }

    // 获取教师授课的课程列表
    @GetMapping("/{id}/courses")
    public Result getTeacherCourses(@PathVariable("id") String teacherId) {
        return Result.success(teacherService.getTeacherCourses(teacherId));
    }

    // 获取选修某课程的学生名单
    @GetMapping("/courses/{courseId}/students")
    public Result getCourseStudents(@PathVariable("courseId") String courseId) {
        return Result.success(teacherService.getCourseStudents(courseId));
    }

    // 录入/修改学生成绩
    @PutMapping("/grades")
    public Result updateGrade(@RequestBody Map<String, Object> gradeData) {
        String studentId = (String) gradeData.get("studentId");
        String courseId = (String) gradeData.get("courseId");
        Float score = Float.valueOf(gradeData.get("score").toString());
        return Result.success(teacherService.updateStudentGrade(studentId, courseId, score));
    }

    // 教师提交新的课程进行审核
    @PostMapping("/courses/propose")
    public Result proposeCourse(@RequestBody Course course) {
        // Ensure teacherId is set, ideally from authenticated principal
        // For now, assume it's in the course object and is correct.
        // Or, it could be part of the path, e.g., /api/teachers/{teacherId}/courses/propose

        if (course.getTeacherId() == null || course.getTeacherId().isEmpty()) {
            return Result.error("教师ID不能为空");
        }
        // Check if teacher exists
        if (teacherService.getById(course.getTeacherId()) == null) {
            return Result.error("指定的教师ID不存在");
        }

        if (courseService.getById(course.getCourseId()) != null) {
            return Result.error("课程编号已存在");
        }

        course.setStatus("PENDING_APPROVAL");
        if (course.getCurrentStudents() == null) {
            course.setCurrentStudents(0);
        }
        // Clear any potential rejection reason from a previous submission
        course.setRejectionReason(null); 

        if (courseService.add(course)) {
            return Result.success(course);
        } else {
            return Result.error("提交课程申请失败");
        }
    }

    // 教师更新自己提交的、尚未审核的课程 (PENDING_APPROVAL)
    @PutMapping("/courses/propose/{courseId}")
    public Result updateProposedCourse(@PathVariable("courseId") String courseId, @RequestBody Course updatedCourseData) {
        Course existingCourse = courseService.getById(courseId);
        if (existingCourse == null) {
            return Result.error("课程不存在");
        }

        // Basic authorization: ensure the course belongs to the teacher making the request
        // This should be enhanced with actual logged-in user check
        if (updatedCourseData.getTeacherId() == null || !updatedCourseData.getTeacherId().equals(existingCourse.getTeacherId())) {
             return Result.error("无权修改此课程或教师ID不匹配");
        }
        
        if (!"PENDING_APPROVAL".equals(existingCourse.getStatus())) {
            return Result.error("只能修改待审核状态的课程");
        }

        // Preserve original courseId and teacherId from path/existing record, not from body, to prevent hijacking
        updatedCourseData.setCourseId(courseId);
        updatedCourseData.setTeacherId(existingCourse.getTeacherId()); 
        updatedCourseData.setStatus("PENDING_APPROVAL"); // Keep status as pending
        // Clear any potential rejection reason from a previous submission
        updatedCourseData.setRejectionReason(null); 

        if (courseService.update(updatedCourseData)) {
            return Result.success(updatedCourseData);
        } else {
            return Result.error("更新课程申请失败");
        }
    }

    // 教师对自己已获批的课程提出修改申请，修改后课程将重新进入审核流程
    @PutMapping("/courses/propose-update/{courseId}")
    public Result proposeUpdateToApprovedCourse(@PathVariable("courseId") String courseId, @RequestBody Course updatedCourseData) {
        Course existingCourse = courseService.getById(courseId);
        if (existingCourse == null) {
            return Result.error("课程不存在");
        }

        // Basic authorization check
        if (updatedCourseData.getTeacherId() == null || !updatedCourseData.getTeacherId().equals(existingCourse.getTeacherId())) {
            return Result.error("无权修改此课程或教师ID不匹配");
        }

        if (!"APPROVED".equals(existingCourse.getStatus())) {
            return Result.error("只能对已批准的课程提交修改申请");
        }

        // Preserve original courseId and teacherId
        updatedCourseData.setCourseId(courseId);
        updatedCourseData.setTeacherId(existingCourse.getTeacherId());
        updatedCourseData.setStatus("PENDING_APPROVAL"); // Set status to pending for re-approval
        // Clear any potential rejection reason from a previous submission
        updatedCourseData.setRejectionReason(null);

        if (courseService.update(updatedCourseData)) {
            return Result.success(updatedCourseData);
        } else {
            return Result.error("提交课程修改申请失败");
        }
    }
}