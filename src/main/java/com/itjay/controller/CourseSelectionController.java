package com.itjay.controller;
import com.itjay.pojo.Course;
import com.itjay.pojo.Result;
import com.itjay.service.CourseSelectionService;
import com.itjay.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/selections")
@CrossOrigin
public class CourseSelectionController {

    @Autowired
    private CourseSelectionService courseSelectionService;
    
    @Autowired
    private CourseService courseService;

    @GetMapping("/student/{studentId}")
    public Result getByStudentId(@PathVariable String studentId) {
        return Result.success(courseSelectionService.getByStudentId(studentId));
    }

    @GetMapping("/course/{courseId}")
    public Result getByCourseId(@PathVariable String courseId) {
        return Result.success(courseSelectionService.getByCourseId(courseId));
    }

    @PostMapping("/select")
    public Result selectCourse(@RequestBody Map<String, String> params) {
        String studentId = params.get("studentId");
        String courseId = params.get("courseId");
        
        // 检查参数
        if (studentId == null || courseId == null) {
            return Result.error("参数错误");
        }
        
        // 检查是否已经选过这门课
        if (courseSelectionService.getByStudentAndCourse(studentId, courseId) != null) {
            return Result.error("您已经选过这门课程");
        }
        
        // 检查课程是否已满
        Course course = courseService.getById(courseId);
        if (course == null) {
            return Result.error("课程不存在");
        }
        
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            return Result.error("课程已满");
        }
        
        boolean result = courseSelectionService.selectCourse(studentId, courseId);
        if (result) {
            return Result.success(true);
        } else {
            return Result.error("选课失败");
        }
    }

    @PostMapping("/drop")
    public Result dropCourse(@RequestBody Map<String, String> params) {
        String studentId = params.get("studentId");
        String courseId = params.get("courseId");
        
        // 检查参数
        if (studentId == null || courseId == null) {
            return Result.error("参数错误");
        }
        
        // 检查是否已经选过这门课
        if (courseSelectionService.getByStudentAndCourse(studentId, courseId) == null) {
            return Result.error("您没有选过这门课程");
        }
        
        boolean result = courseSelectionService.dropCourse(studentId, courseId);
        if (result) {
            return Result.success(true);
        } else {
            return Result.error("退课失败");
        }
    }

    @PostMapping("/score")
    public Result updateScore(@RequestBody Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        Float score = Float.parseFloat(params.get("score").toString());
        
        // 检查参数
        if (id == null || score == null) {
            return Result.error("参数错误");
        }
        
        // 检查分数范围
        if (score < 0 || score > 100) {
            return Result.error("分数范围应在0-100之间");
        }
        
        boolean result = courseSelectionService.updateScore(id, score);
        if (result) {
            return Result.success(true);
        } else {
            return Result.error("成绩录入失败");
        }
    }

    @GetMapping("/grades/{studentId}")
    public Result getGradesByStudentId(@PathVariable String studentId) {
        return Result.success(courseSelectionService.getGradesByStudentId(studentId));
    }
} 