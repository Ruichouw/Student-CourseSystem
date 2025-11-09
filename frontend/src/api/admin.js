import request from './request';

export function adminLogin(data) {
  return request({
    url: '/api/admin/login',
    method: 'post',
    data
  });
}

export function getAdminProfile(adminId) {
  return request({
    url: `/api/admin/profile/${adminId}`,
    method: 'get'
  });
}

export function updateAdminProfile(data) {
  return request({
    url: '/api/admin/profile',
    method: 'put',
    data
  });
}

export function addStudent(data) {
  return request({
    url: '/api/admin/users/students',
    method: 'post',
    data
  });
}

export function updateStudent(studentId, data) {
  return request({
    url: `/api/admin/users/students/${studentId}`,
    method: 'put',
    data
  });
}

export function deleteStudent(studentId) {
  return request({
    url: `/api/admin/users/students/${studentId}`,
    method: 'delete'
  });
}

export function addTeacher(data) {
  return request({
    url: '/api/admin/users/teachers',
    method: 'post',
    data
  });
}

export function updateTeacher(teacherId, data) {
  return request({
    url: `/api/admin/users/teachers/${teacherId}`,
    method: 'put',
    data
  });
}

export function deleteTeacher(teacherId) {
  return request({
    url: `/api/admin/users/teachers/${teacherId}`,
    method: 'delete'
  });
}

export function resetPassword(data) {
  return request({
    url: '/api/admin/users/reset-password',
    method: 'put',
    data
  });
}

export function getPendingCourses() {
  return request({
    url: '/api/admin/courses/pending',
    method: 'get'
  });
}

export function approveCourse(courseId) {
  return request({
    url: `/api/admin/courses/approve/${courseId}`,
    method: 'put'
  });
}

export function rejectCourse(courseId, reason) {
  return request({
    url: `/api/admin/courses/reject/${courseId}`,
    method: 'put',
    data: { reason }
  });
}

export function getSystemStatistics() {
  return request({
    url: '/api/admin/statistics',
    method: 'get'
  });
}

export function adminAddCourse(data) {
  return request({
    url: '/api/admin/courses',
    method: 'post',
    data
  });
}

export function adminUpdateCourse(courseId, data) {
  return request({
    url: `/api/admin/courses/${courseId}`,
    method: 'put',
    data
  });
}

export function adminDeleteCourse(courseId) {
  return request({
    url: `/api/admin/courses/${courseId}`,
    method: 'delete'
  });
}

export function getSystemConfig() {
  return request({
    url: '/api/admin/system-config',
    method: 'get'
  });
}

export function updateSystemConfig(data) {
  return request({
    url: '/api/admin/system-config',
    method: 'put',
    data
  });
}

export function getAllStudents() {
  return request({
    url: '/api/admin/users/students',
    method: 'get'
  });
}

export function getAllTeachers() {
  return request({
    url: '/api/admin/users/teachers',
    method: 'get'
  });
}

export function getAllCoursesForAdmin() {
  return request({
    url: '/api/admin/courses/all',
    method: 'get'
  });
}