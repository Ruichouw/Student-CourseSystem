package com.itjay.controller;
import com.itjay.pojo.Result;
import com.itjay.pojo.Student;
import com.itjay.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String studentId) {
        Student student = studentService.getById(studentId);
        if (student != null) {
            return Result.success(student);
        } else {
            return Result.error("学生不存在");
        }
    }

    @GetMapping
    public Result getAll() {
        return Result.success(studentService.getAll());
    }

    @PostMapping("/search")
    public Result search(@RequestBody Student student) {
        return Result.success(studentService.getByCondition(student));
    }

    @PostMapping
    public Result add(@RequestBody Student student) {
        if (studentService.getById(student.getStudentId()) != null) {
            return Result.error("学号已存在");
        }
        return Result.success(studentService.add(student));
    }

    @PutMapping
    public Result update(@RequestBody Student student) {
        return Result.success(studentService.update(student));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String studentId) {
        return Result.success(studentService.delete(studentId));
    }

    @PostMapping("/login")
    public Result login(@RequestBody Student student) {
        Student result = studentService.login(student.getStudentId(), student.getPassword());
        if (result != null) {
            return Result.success(result);
        } else {
            return Result.error("学号或密码错误");
        }
    }
} 