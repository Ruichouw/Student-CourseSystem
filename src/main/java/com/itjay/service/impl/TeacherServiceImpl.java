package com.itjay.service.impl;

import com.itjay.mapper.TeacherMapper;
import com.itjay.mapper.CourseMapper;
import com.itjay.mapper.CourseSelectionMapper;
import com.itjay.pojo.Teacher;
import com.itjay.pojo.Course;
import com.itjay.pojo.CourseSelection;
import com.itjay.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    
    @Autowired
    private TeacherMapper teacherMapper;
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    
    @Override
    public Teacher login(String teacherId, String password) {
        return teacherMapper.login(teacherId, password);
    }
    
    @Override
    public Teacher getById(String teacherId) {
        return teacherMapper.getById(teacherId);
    }
    
    @Override
    public List<Teacher> getAll() {
        return teacherMapper.getAll();
    }
    
    @Override
    public boolean add(Teacher teacher) {
        return teacherMapper.insert(teacher) > 0;
    }
    
    @Override
    public boolean update(Teacher teacher) {
        return teacherMapper.update(teacher) > 0;
    }
    
    @Override
    public boolean delete(String teacherId) {
        return teacherMapper.delete(teacherId) > 0;
    }
    
    @Override
    public List<Course> getTeacherCourses(String teacherId) {
        return courseMapper.getByTeacherId(teacherId);
    }
    
    @Override
    public List<CourseSelection> getCourseStudents(String courseId) {
        return courseSelectionMapper.getStudentsByCourseId(courseId);
    }
    
    @Override
    public boolean updateStudentGrade(String studentId, String courseId, Float score) {
        return courseSelectionMapper.updateScore(studentId, courseId, score) > 0;
    }
    
    // 提交课程教学计划
    public boolean submitCoursePlan(Course course) {
        // 设置状态为待审核
        course.setStatus("PENDING_APPROVAL");
        return courseMapper.update(course) > 0;
    }
    
    // 更新课程信息（容量、时间等）
    public boolean updateCourseInfo(Course course) {
        return courseMapper.update(course) > 0;
    }
}