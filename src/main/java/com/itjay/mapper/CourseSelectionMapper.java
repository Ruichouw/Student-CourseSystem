package com.itjay.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itjay.pojo.CourseSelection;

@Mapper
public interface CourseSelectionMapper {
    // 根据ID查询选课记录
    CourseSelection selectById(Integer id);
    
    // 根据学生ID查询选课记录（包含课程信息）
    List<CourseSelection> selectByStudentId(String studentId);
    
    // 根据课程ID查询选课记录（包含学生信息）
    List<CourseSelection> selectByCourseId(String courseId);
    
    // 根据学生ID和课程ID查询选课记录
    CourseSelection selectByStudentAndCourse(@Param("studentId") String studentId, @Param("courseId") String courseId);
    
    // 插入选课记录
    int insert(CourseSelection courseSelection);
    
    // 更新选课记录（主要是更新成绩）
    int update(CourseSelection courseSelection);
    
    // 删除选课记录
    int deleteById(Integer id);
    
    // 根据学生ID和课程ID删除选课记录
    int deleteByStudentAndCourse(@Param("studentId") String studentId, @Param("courseId") String courseId);
    
    // 查询学生的成绩单
    List<CourseSelection> selectGradesByStudentId(String studentId);
    
    // 根据课程ID查询学生列表（包含学生信息）
    List<CourseSelection> getStudentsByCourseId(String courseId);
    
    // 更新学生成绩
    int updateScore(@Param("studentId") String studentId, @Param("courseId") String courseId, @Param("score") Float score);
    
    // 统计选课总数
    int count();
}