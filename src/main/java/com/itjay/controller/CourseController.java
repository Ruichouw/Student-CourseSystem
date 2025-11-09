package com.itjay.controller;
import com.itjay.pojo.Course;
import com.itjay.pojo.Result;
import com.itjay.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String courseId) {
        Course course = courseService.getById(courseId);
        if (course != null) {
            return Result.success(course);
        } else {
            return Result.error("课程不存在");
        }
    }

    @GetMapping
    public Result getAll() {
        return Result.success(courseService.getAllWithTeacher());
    }

    @PostMapping("/search")
    public Result search(@RequestBody Course course) {
        return Result.success(courseService.getByCondition(course));
    }

    @PostMapping
    public Result add(@RequestBody Course course) {
        if (courseService.getById(course.getCourseId()) != null) {
            return Result.error("课程编号已存在");
        }
        return Result.success(courseService.add(course));
    }

    @PutMapping
    public Result update(@RequestBody Course course) {
        return Result.success(courseService.update(course));
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String courseId) {
        return Result.success(courseService.delete(courseId));
    }
} 