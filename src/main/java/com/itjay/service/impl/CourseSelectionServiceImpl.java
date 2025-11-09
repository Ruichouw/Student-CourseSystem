package com.itjay.service.impl;


import com.itjay.mapper.CourseMapper;
import com.itjay.mapper.CourseSelectionMapper;
import com.itjay.pojo.Course;
import com.itjay.pojo.CourseSelection;
import com.itjay.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public CourseSelection getById(Integer id) {
        return courseSelectionMapper.selectById(id);
    }

    @Override
    public List<CourseSelection> getByStudentId(String studentId) {
        return courseSelectionMapper.selectByStudentId(studentId);
    }

    @Override
    public List<CourseSelection> getByCourseId(String courseId) {
        return courseSelectionMapper.selectByCourseId(courseId);
    }

    @Override
    public CourseSelection getByStudentAndCourse(String studentId, String courseId) {
        return courseSelectionMapper.selectByStudentAndCourse(studentId, courseId);
    }

    @Override
    @Transactional
    public boolean selectCourse(String studentId, String courseId) {
        // 检查是否已经选过这门课
        if (courseSelectionMapper.selectByStudentAndCourse(studentId, courseId) != null) {
            return false;
        }
        
        // 检查课程是否已满
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            return false;
        }
        
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            return false;
        }
        
        // 选课
        CourseSelection courseSelection = new CourseSelection();
        courseSelection.setStudentId(studentId);
        courseSelection.setCourseId(courseId);
        
        boolean result = courseSelectionMapper.insert(courseSelection) > 0;
        if (result) {
            // 更新课程选课人数
            courseMapper.updateCurrentStudents(courseId);
        }
        
        return result;
    }

    @Override
    @Transactional
    public boolean dropCourse(String studentId, String courseId) {
        boolean result = courseSelectionMapper.deleteByStudentAndCourse(studentId, courseId) > 0;
        if (result) {
            // 更新课程选课人数
            courseMapper.updateCurrentStudents(courseId);
        }
        return result;
    }

    @Override
    public boolean updateScore(Integer id, Float score) {
        CourseSelection courseSelection = new CourseSelection();
        courseSelection.setId(id);
        courseSelection.setScore(score);
        return courseSelectionMapper.update(courseSelection) > 0;
    }

    @Override
    public List<CourseSelection> getGradesByStudentId(String studentId) {
        return courseSelectionMapper.selectGradesByStudentId(studentId);
    }
} 