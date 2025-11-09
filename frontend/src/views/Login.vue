<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loginForm = reactive({
  userId: '',
  password: '',
  userType: 'student' // 新增：用户类型
})

const rules = {
  userId: [
    { required: true, message: '请输入账号', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
  ],
  userType: [
    { required: true, message: '请选择登录类型', trigger: 'change' },
  ]
}

const loading = ref(false)
const formRef = ref(null)

const userTypeOptions = [
  { label: '学生', value: 'student' },
  { label: '教师', value: 'teacher' },
  { label: '管理员', value: 'admin' }
]

const getUserTypeLabel = (type) => {
  const option = userTypeOptions.find(opt => opt.value === type)
  return option ? option.label : '账号'
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const success = await userStore.login(loginForm)
        if (success) {
          ElMessage.success('登录成功')
          // 根据用户类型跳转到不同页面
          if (loginForm.userType === 'student') {
            router.push('/student/home')
          } else if (loginForm.userType === 'teacher') {
            router.push('/teacher/home')
          } else if (loginForm.userType === 'admin') {
            router.push('/admin/home')
          }
        }
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>学生选课管理系统</h2>
        <p>Student Course Management System</p>
      </div>
      
      <el-form 
        ref="formRef"
        :model="loginForm" 
        :rules="rules" 
        class="login-form" 
        label-position="top"
      >
        <el-form-item label="登录类型" prop="userType">
          <el-select 
            v-model="loginForm.userType" 
            placeholder="请选择登录类型"
            style="width: 100%"
          >
            <el-option
              v-for="option in userTypeOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="getUserTypeLabel(loginForm.userType) + '账号'" prop="userId">
          <el-input 
            v-model="loginForm.userId" 
            :placeholder="'请输入' + getUserTypeLabel(loginForm.userType) + '账号'"
            prefix-icon="el-icon-user"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading" 
            class="login-button" 
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  color: #333;
  margin-bottom: 10px;
  font-weight: 600;
}

.login-header p {
  color: #666;
  font-size: 14px;
}

.user-type-group {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.user-type-radio {
  flex: 1;
  text-align: center;
  margin-right: 0;
}

.login-button {
  width: 100%;
  height: 45px;
  font-size: 16px;
  border-radius: 6px;
}

.login-form {
  margin-top: 20px;
}

:deep(.el-form-item__label) {
  color: #333;
  font-weight: 500;
}

:deep(.el-input__inner) {
  height: 45px;
  border-radius: 6px;
}
</style>