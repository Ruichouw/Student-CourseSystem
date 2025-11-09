import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false, title: '登录' }
  },
  // 学生端路由
  {
    path: '/student',
    redirect: '/student/home'
  },
  {
    path: '/student/home',
    name: 'StudentHome',
    component: () => import('@/views/Home.vue'),
    meta: { requiresAuth: true, userType: 'student', title: '学生首页' },
    redirect: '/student/courses',
    children: [
      {
        path: '/student/courses',
        name: 'StudentCourses',
        component: () => import('@/views/courses/CourseList.vue'),
        meta: { requiresAuth: true, userType: 'student', title: '课程列表' }
      },
      {
        path: '/student/selection',
        name: 'CourseSelection',
        component: () => import('@/views/courses/CourseSelection.vue'),
        meta: { requiresAuth: true, userType: 'student', title: '选课中心' }
      },
      {
        path: '/student/my-courses',
        name: 'MyCourses',
        component: () => import('@/views/courses/MyCourses.vue'),
        meta: { requiresAuth: true, userType: 'student', title: '我的课程' }
      },
      {
        path: '/student/grades',
        name: 'MyGrades',
        component: () => import('@/views/courses/MyGrades.vue'),
        meta: { requiresAuth: true, userType: 'student', title: '成绩查询' }
      },
      {
        path: '/student/profile',
        name: 'StudentProfile',
        component: () => import('@/views/student/Profile.vue'),
        meta: { requiresAuth: true, userType: 'student', title: '个人信息' }
      }
    ]
  },
  // 教师端路由
  {
    path: '/teacher',
    redirect: '/teacher/home'
  },
  {
    path: '/teacher/home',
    name: 'TeacherHome',
    component: () => import('@/views/teacher/TeacherHome.vue'),
    meta: { requiresAuth: true, userType: 'teacher', title: '教师首页' },
    redirect: '/teacher/courses',
    children: [
      {
        path: '/teacher/courses',
        name: 'TeacherCourses',
        component: () => import('@/views/teacher/CourseManagement.vue'),
        meta: { requiresAuth: true, userType: 'teacher', title: '课程管理' }
      },
      {
        path: '/teacher/students',
        name: 'TeacherStudents',
        component: () => import('@/views/teacher/StudentManagement.vue'),
        meta: { requiresAuth: true, userType: 'teacher', title: '学生管理' }
      },
      {
        path: '/teacher/grades',
        name: 'TeacherGrades',
        component: () => import('@/views/teacher/GradeManagement.vue'),
        meta: { requiresAuth: true, userType: 'teacher', title: '成绩管理' }
      },
      {
        path: '/teacher/profile',
        name: 'TeacherProfile',
        component: () => import('@/views/teacher/Profile.vue'),
        meta: { requiresAuth: true, userType: 'teacher', title: '个人信息' }
      }
    ]
  },
  // 管理员端路由
  {
    path: '/admin',
    redirect: '/admin/home'
  },
  {
    path: '/admin/home',
    name: 'AdminHome',
    component: () => import('@/views/admin/AdminHome.vue'),
    meta: { requiresAuth: true, userType: 'admin', title: '管理员首页' },
    redirect: '/admin/students',
    children: [
      {
        path: '/admin/students',
        name: 'AdminStudents',
        component: () => import('@/views/admin/StudentManagement.vue'),
        meta: { requiresAuth: true, userType: 'admin', title: '学生管理' }
      },
      {
        path: '/admin/teachers',
        name: 'AdminTeachers',
        component: () => import('@/views/admin/TeacherManagement.vue'),
        meta: { requiresAuth: true, userType: 'admin', title: '教师管理' }
      },
      {
        path: '/admin/courses',
        name: 'AdminCourses',
        component: () => import('@/views/admin/CourseList.vue'),
        meta: { requiresAuth: true, userType: 'admin', title: '课程列表' }
      },
      {
        path: '/admin/course-approval',
        name: 'CourseApproval',
        component: () => import('@/views/admin/CourseApproval.vue'),
        meta: { requiresAuth: true, userType: 'admin', title: '课程审核' }
      },
      {
        path: '/admin/statistics',
        name: 'SystemStatistics',
        component: () => import('@/views/admin/Statistics.vue'),
        meta: { requiresAuth: true, userType: 'admin', title: '系统统计' }
      },
      {
        path: '/admin/profile',
        name: 'AdminProfile',
        component: () => import('@/views/admin/Profile.vue'),
        meta: { requiresAuth: true, userType: 'admin', title: '个人信息' }
      },
      {
        path: '/admin/system-config',
        name: 'SystemConfigManagement',
        component: () => import('@/views/admin/SystemConfigManagement.vue'),
        meta: { requiresAuth: true, userType: 'admin', title: '系统配置' }
      }
    ]
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { requiresAuth: false, title: '页面不存在' }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isAuthenticated) {
    next('/login')
  } else if (to.path === '/login' && userStore.isAuthenticated) {
    const userType = userStore.getUserType; // Get user type from store
    if (userType === 'student') {
      next('/student/home');
    } else if (userType === 'teacher') {
      next('/teacher/home');
    } else if (userType === 'admin') {
      next('/admin/home');
    } else {
      // Fallback if userType is unknown or invalid after being authenticated
      console.warn('Authenticated user with unknown type trying to access /login. Logging out.');
      userStore.logout(); // Perform logout
      next('/login'); // Redirect back to login
    }
  } else {
    next()
  }
})

export default router