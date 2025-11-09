package com.itjay.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itjay.pojo.Teacher;

@Mapper
public interface TeacherMapper {
    // 教师登录
    Teacher login(@Param("teacherId") String teacherId, @Param("password") String password);
    
    // 根据ID查询教师
    Teacher getById(String teacherId);
    
    // 查询所有教师
    List<Teacher> getAll();
    
    // 插入教师
    int insert(Teacher teacher);
    
    // 更新教师信息
    int update(Teacher teacher);
    
    // 删除教师
    int delete(String teacherId);
    
    // 统计教师总数
    int count();
}