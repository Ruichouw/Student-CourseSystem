import request from './request';

export function teacherLogin(data) {
  return request({
    url: '/api/teachers/login',
    method: 'post',
    data
  });
}

export function getTeacherById(teacherId) {
  return request({
    url: `/api/teachers/${teacherId}`,
    method: 'get'
  });
}

export function updateTeacher(data) {
  return request({
    url: '/api/teachers',
    method: 'put',
    data
  });
}

export function getTeacherCourses(teacherId) {
  return request({
    url: `/api/teachers/${teacherId}/courses`,
    method: 'get'
  });
}

export function getCourseStudents(courseId) {
  return request({
    url: `/api/teachers/courses/${courseId}/students`,
    method: 'get'
  });
}

export function updateStudentGrade(data) {
  return request({
    url: '/api/teachers/grades',
    method: 'put',
    data
  });
}

export function proposeCourse(data) {
  return request({
    url: '/api/teachers/courses/propose',
    method: 'post',
    data
  });
}

export function updateProposedCourse(courseId, data) {
  return request({
    url: `/api/teachers/courses/propose/${courseId}`,
    method: 'put',
    data
  });
}

export function proposeUpdateToApprovedCourse(courseId, data) {
  return request({
    url: `/api/teachers/courses/propose-update/${courseId}`,
    method: 'put',
    data
  });
}