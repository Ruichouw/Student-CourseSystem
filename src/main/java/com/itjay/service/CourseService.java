package com.itjay.service;


import com.itjay.pojo.Course;

import java.util.List;

public interface CourseService {
    // 根据ID查询课程
    Course getById(String courseId);
    
    // 查询所有课程
    List<Course> getAll();
    
    // 查询所有课程（包含教师名称）
    List<Course> getAllWithTeacher();
    
    // 根据条件查询课程
    List<Course> getByCondition(Course course);
    
    // 添加课程
    boolean add(Course course);
    
    // 更新课程
    boolean update(Course course);
    
    // 删除课程
    boolean delete(String courseId);
    
    // 更新课程选课人数
    boolean updateCurrentStudents(String courseId);
    
    // 根据状态查询课程
    List<Course> getByStatus(String status);
    
    // 根据教师ID查询课程
    List<Course> getByTeacherId(String teacherId);
    
    // 统计课程总数
    int count();
    
    // 获取热门课程（按选课人数排序）
    List<Course> getPopularCourses(int limit);
} 