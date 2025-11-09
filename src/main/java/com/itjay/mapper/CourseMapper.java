package com.itjay.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itjay.pojo.Course;

@Mapper
public interface CourseMapper {
    // 根据ID查询课程
    Course selectById(String courseId);
    
    // 查询所有课程
    List<Course> selectAll();
    
    // 查询所有课程（包含教师名称）
    List<Course> selectAllWithTeacher();
    
    // 根据条件查询课程
    List<Course> selectByCondition(Course course);
    
    // 插入课程
    int insert(Course course);
    
    // 更新课程
    int update(Course course);
    
    // 删除课程
    int deleteById(String courseId);
    
    // 更新课程选课人数
    int updateCurrentStudents(String courseId);
    
    // 根据ID查询课程（为了与其他mapper保持一致）
    Course getById(String courseId);
    
    // 根据状态查询课程
    List<Course> getByStatus(String status);
    
    // 根据教师ID查询课程
    List<Course> getByTeacherId(String teacherId);
    
    // 统计课程总数
    int count();
    
    // 获取热门课程（按选课人数排序）
    List<Course> getPopularCourses(int limit);
}