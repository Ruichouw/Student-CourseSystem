import request from './request';

export function getStudentById(studentId) {
  return request({
    url: `/api/students/${studentId}`,
    method: 'get'
  });
}

export function getAllStudents() {
  return request({
    url: '/api/students',
    method: 'get'
  });
}

export function searchStudents(data) {
  return request({
    url: '/api/students/search',
    method: 'post',
    data
  });
}

export function addStudent(data) {
  return request({
    url: '/api/students',
    method: 'post',
    data
  });
}

export function updateStudent(data) {
  return request({
    url: '/api/students',
    method: 'put',
    data
  });
}

export function deleteStudent(studentId) {
  return request({
    url: `/api/students/${studentId}`,
    method: 'delete'
  });
}

export function studentLogin(data) {
  return request({
    url: '/api/students/login',
    method: 'post',
    data
  });
} 