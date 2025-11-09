package com.itjay.service;


import com.itjay.pojo.Student;

import java.util.List;

public interface StudentService {
    // 根据ID查询学生
    Student getById(String studentId);
    
    // 查询所有学生
    List<Student> getAll();
    
    // 根据条件查询学生
    List<Student> getByCondition(Student student);
    
    // 添加学生
    boolean add(Student student);
    
    // 更新学生
    boolean update(Student student);
    
    // 删除学生
    boolean delete(String studentId);
    
    // 学生登录
    Student login(String studentId, String password);
} 