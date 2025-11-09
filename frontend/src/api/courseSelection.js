import request from './request';

export function getSelectionsByStudentId(studentId) {
  return request({
    url: `/api/selections/student/${studentId}`,
    method: 'get'
  });
}

export function getSelectionsByCourseId(courseId) {
  return request({
    url: `/api/selections/course/${courseId}`,
    method: 'get'
  });
}

export function selectCourse(studentId, courseId) {
  return request({
    url: '/api/selections/select',
    method: 'post',
    data: {
      studentId,
      courseId
    }
  });
}

export function dropCourse(studentId, courseId) {
  return request({
    url: '/api/selections/drop',
    method: 'post',
    data: {
      studentId,
      courseId
    }
  });
}

export function updateScore(id, score) {
  return request({
    url: '/api/selections/score',
    method: 'post',
    data: {
      id,
      score
    }
  });
}

export function getGradesByStudentId(studentId) {
  return request({
    url: `/api/selections/grades/${studentId}`,
    method: 'get'
  });
} 