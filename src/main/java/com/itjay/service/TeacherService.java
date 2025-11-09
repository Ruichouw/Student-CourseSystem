package com.itjay.service;

import com.itjay.pojo.Teacher;
import com.itjay.pojo.Course;
import com.itjay.pojo.CourseSelection;
import java.util.List;

public interface TeacherService {
    Teacher getById(String teacherId);
    List<Teacher> getAll();
    boolean add(Teacher teacher);
    boolean update(Teacher teacher);
    boolean delete(String teacherId);
    Teacher login(String teacherId, String password);
    List<Course> getTeacherCourses(String teacherId);
    List<CourseSelection> getCourseStudents(String courseId);
    boolean updateStudentGrade(String studentId, String courseId, Float score);
}