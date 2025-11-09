<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import { getTeacherById, updateTeacher } from '@/api/teacher'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const user = userStore.getUser
const loading = ref(false)
const saving = ref(false)

const teacherForm = reactive({
  teacherId: '',
  name: '',
  email: '',
  phone: '',
  department: '',
  title: '',
  password: '',
  confirmPassword: ''
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' }
  ],
  password: [
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { validator: (rule, value, callback) => {
      if (value && value !== teacherForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ]
}

const formRef = ref(null)

// 获取教师信息
const fetchTeacherInfo = async () => {
  if (!user || !user.teacherId) {
    ElMessage.warning('用户信息不完整')
    return
  }
  
  loading.value = true
  try {
    const response = await getTeacherById(user.teacherId)
    if (response && response.code === 1 && response.data) {
      const teacherData = response.data
      teacherForm.teacherId = teacherData.teacherId
      teacherForm.name = teacherData.name
      teacherForm.email = teacherData.email
      teacherForm.phone = teacherData.phone
      teacherForm.department = teacherData.department
      teacherForm.title = teacherData.title
      // 不回显密码
      teacherForm.password = ''
      teacherForm.confirmPassword = ''
    } else {
      ElMessage.error(response?.msg || '获取教师信息失败')
    }
  } catch (error) {
    console.error('Error fetching teacher info:', error)
    ElMessage.error('获取教师信息出错: ' + (error.message || '请检查网络连接'))
  } finally {
    loading.value = false
  }
}

// 更新教师信息
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      saving.value = true
      try {
        // 如果没有修改密码，不提交密码字段
        const submitData = { ...teacherForm }
        if (!submitData.password) {
          delete submitData.password
        }
        delete submitData.confirmPassword // 不需要提交确认密码
        
        const response = await updateTeacher(submitData)
        if (response && response.code === 1) {
          ElMessage.success('个人信息更新成功')
          // 如果更新了姓名，需要更新本地存储的用户信息
          if (user.name !== submitData.name) {
            const updatedUser = { ...user, name: submitData.name }
            userStore.$patch({ user: updatedUser })
            localStorage.setItem('user', JSON.stringify(updatedUser))
          }
        } else {
          ElMessage.error(response?.msg || '更新教师信息失败')
        }
      } catch (error) {
        console.error('Error updating teacher info:', error)
        ElMessage.error('更新教师信息出错: ' + (error.message || '请检查网络连接'))
      } finally {
        saving.value = false
      }
    }
  })
}

onMounted(() => {
  fetchTeacherInfo()
})
</script>

<template>
  <div class="profile-container">
    <div class="page-header">
      <h2>个人信息</h2>
    </div>
    
    <el-card class="profile-card" v-loading="loading">
      <el-form 
        ref="formRef"
        :model="teacherForm"
        :rules="rules"
        label-width="100px"
        label-position="right"
      >
        <el-form-item label="教师工号" prop="teacherId">
          <el-input v-model="teacherForm.teacherId" disabled />
        </el-form-item>
        
        <el-form-item label="姓名" prop="name">
          <el-input v-model="teacherForm.name" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="teacherForm.email" type="email" />
        </el-form-item>
        
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="teacherForm.phone" />
        </el-form-item>
        
        <el-form-item label="所属院系" prop="department">
          <el-input v-model="teacherForm.department" />
        </el-form-item>
        
        <el-form-item label="职称" prop="title">
          <el-input v-model="teacherForm.title" />
        </el-form-item>
        
        <el-divider content-position="center">修改密码（不修改请留空）</el-divider>
        
        <el-form-item label="新密码" prop="password">
          <el-input v-model="teacherForm.password" type="password" show-password />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="teacherForm.confirmPassword" type="password" show-password />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="saving">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.profile-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
}

.profile-card {
  max-width: 600px;
  margin: 0 auto;
}
</style>