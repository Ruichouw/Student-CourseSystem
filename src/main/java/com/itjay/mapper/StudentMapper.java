package com.itjay.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.itjay.pojo.Student;


@Mapper
@MapperScan
public interface StudentMapper {
    // 根据ID查询学生
    Student selectById(String studentId);
    
    // 根据ID查询学生（为了与其他mapper保持一致）
    Student getById(String studentId);
    
    // 查询所有学生
    List<Student> selectAll();
    
    // 根据条件查询学生
    List<Student> selectByCondition(Student student);
    
    // 插入学生
    int insert(Student student);
    
    // 更新学生
    int update(Student student);
    
    // 删除学生
    int deleteById(String studentId);
    
    // 登录验证
    Student login(@Param("studentId") String studentId, @Param("password") String password);

    // 统计学生总数
    int count();

    // 获取各院系学生分布
    List<Map<String, Object>> getDepartmentDistribution();
}