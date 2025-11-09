import { defineStore } from 'pinia'
import { studentLogin } from '@/api/student'
import { teacherLogin } from '@/api/teacher'
import { adminLogin } from '@/api/admin'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')) || null,
    userType: localStorage.getItem('userType') || null,
    isLoggedIn: !!localStorage.getItem('user')
  }),
  
  getters: {
    getUser: (state) => state.user,
    getUserType: (state) => state.userType,
    isAuthenticated: (state) => state.isLoggedIn
  },
  
  actions: {
    async login(credentials) {
      try {
        let response;
        
        // 根据用户类型调用不同的登录API
        switch (credentials.userType) {
          case 'student':
            response = await studentLogin({
              studentId: credentials.userId,
              password: credentials.password
            });
            break;
          case 'teacher':
            response = await teacherLogin({
              teacherId: credentials.userId,
              password: credentials.password
            });
            break;
          case 'admin':
            response = await adminLogin({
              adminId: credentials.userId,
              password: credentials.password
            });
            break;
          default:
            throw new Error('无效的用户类型');
        }
        
        if (response.code === 1) {
          this.user = response.data
          this.userType = credentials.userType
          this.isLoggedIn = true
          localStorage.setItem('user', JSON.stringify(response.data))
          localStorage.setItem('userType', credentials.userType)
          return true
        } else {
          ElMessage.error(response.msg || '登录失败')
          return false
        }
      } catch (error) {
        ElMessage.error('登录出错：' + error.message)
        return false
      }
    },
    
    logout() {
      this.user = null
      this.userType = null
      this.isLoggedIn = false
      localStorage.removeItem('user')
      localStorage.removeItem('userType')
    }
  }
})