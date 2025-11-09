import axios from 'axios';
import { ElMessage } from 'element-plus';

const service = axios.create({
  baseURL: '/',
  timeout: 30000
});

// Request interceptor
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data;
    
    // 返回所有响应，让各组件自行处理状态码
    return res;
  },
  error => {
    console.error('Response error:', error);
    ElMessage({
      message: error.message || 'Request Error',
      type: 'error',
      duration: 5 * 1000
    });
    return Promise.reject(error);
  }
);

export default service; 