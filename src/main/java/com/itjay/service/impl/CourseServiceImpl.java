package com.itjay.service.impl;


import com.itjay.mapper.CourseMapper;
import com.itjay.pojo.Course;
import com.itjay.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Course getById(String courseId) {
        return courseMapper.selectById(courseId);
    }

    @Override
    public List<Course> getAll() {
        return courseMapper.selectAll();
    }

    @Override
    public List<Course> getAllWithTeacher() {
        return courseMapper.selectAllWithTeacher();
    }

    @Override
    public List<Course> getByCondition(Course course) {
        return courseMapper.selectByCondition(course);
    }

    @Override
    public boolean add(Course course) {
        // 初始化选课人数
        if (course.getCurrentStudents() == null) {
            course.setCurrentStudents(0);
        }
        return courseMapper.insert(course) > 0;
    }

    @Override
    public boolean update(Course course) {
        return courseMapper.update(course) > 0;
    }

    @Override
    public boolean delete(String courseId) {
        return courseMapper.deleteById(courseId) > 0;
    }

    @Override
    public boolean updateCurrentStudents(String courseId) {
        return courseMapper.updateCurrentStudents(courseId) > 0;
    }

    @Override
    public List<Course> getByStatus(String status) {
        return courseMapper.getByStatus(status);
    }

    @Override
    public List<Course> getByTeacherId(String teacherId) {
        return courseMapper.getByTeacherId(teacherId);
    }

    @Override
    public int count() {
        return courseMapper.count();
    }

    @Override
    public List<Course> getPopularCourses(int limit) {
        return courseMapper.getPopularCourses(limit);
    }
} 