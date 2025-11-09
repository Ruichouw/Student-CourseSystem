package com.itjay.service.impl;


import com.itjay.mapper.StudentMapper;
import com.itjay.pojo.Student;
import com.itjay.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student getById(String studentId) {
        return studentMapper.selectById(studentId);
    }

    @Override
    public List<Student> getAll() {
        return studentMapper.selectAll();
    }

    @Override
    public List<Student> getByCondition(Student student) {
        return studentMapper.selectByCondition(student);
    }

    @Override
    public boolean add(Student student) {
        // 设置默认密码
        if (student.getPassword() == null || student.getPassword().isEmpty()) {
            student.setPassword("123456");

        }
        return studentMapper.insert(student) > 0;
    }

    @Override
    public boolean update(Student student) {
        return studentMapper.update(student) > 0;
    }

    @Override
    public boolean delete(String studentId) {
        return studentMapper.deleteById(studentId) > 0;
    }

    @Override
    public Student login(String studentId, String password) {
        return studentMapper.login(studentId, password);
    }
} 