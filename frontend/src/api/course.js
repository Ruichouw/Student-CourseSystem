import request from './request';

export function getCourseById(courseId) {
  return request({
    url: `/api/courses/${courseId}`,
    method: 'get'
  });
}

export function getAllCourses() {
  return request({
    url: '/api/courses',
    method: 'get'
  });
}

export function searchCourses(data) {
  return request({
    url: '/api/courses/search',
    method: 'post',
    data
  });
}

export function addCourse(data) {
  return request({
    url: '/api/courses',
    method: 'post',
    data
  });
}

export function updateCourse(data) {
  return request({
    url: '/api/courses',
    method: 'put',
    data
  });
}

export function deleteCourse(courseId) {
  return request({
    url: `/api/courses/${courseId}`,
    method: 'delete'
  });
} 