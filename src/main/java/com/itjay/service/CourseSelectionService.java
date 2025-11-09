package com.itjay.service;


import com.itjay.pojo.CourseSelection;

import java.util.List;

public interface CourseSelectionService {
    // 根据ID查询选课记录
    CourseSelection getById(Integer id);
    
    // 根据学生ID查询选课记录（包含课程信息）
    List<CourseSelection> getByStudentId(String studentId);
    
    // 根据课程ID查询选课记录（包含学生信息）
    List<CourseSelection> getByCourseId(String courseId);
    
    // 根据学生ID和课程ID查询选课记录
    CourseSelection getByStudentAndCourse(String studentId, String courseId);
    
    // 选课
    boolean selectCourse(String studentId, String courseId);
    
    // 退课
    boolean dropCourse(String studentId, String courseId);
    
    // 录入成绩
    boolean updateScore(Integer id, Float score);
    
    // 查询学生的成绩单
    List<CourseSelection> getGradesByStudentId(String studentId);
} 