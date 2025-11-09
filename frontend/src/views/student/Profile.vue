<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { getStudentById, updateStudent } from '@/api/student'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const currentUser = computed(() => userStore.getUser)

const studentForm = reactive({
  studentId: '',
  name: '',
  gender: '',
  age: '',
  department: '',
  major: '',
  className: '',
  password: '',
  email: '',
  phone: ''
})

const originalStudent = ref({})
const loading = ref(false)
const updating = ref(false)

const fetchStudent = async () => {
  if (!currentUser.value || !currentUser.value.studentId) {
    ElMessage.warning('请先登录')
    return
  }
  
  loading.value = true
  try {
    const res = await getStudentById(currentUser.value.studentId)
    if (res.code === 1 && res.data) {
      Object.assign(studentForm, res.data)
      originalStudent.value = { ...res.data }
    } else {
      ElMessage.error(res.msg || '获取学生信息失败')
    }
  } catch (error) {
    console.error('Error fetching student info:', error)
    ElMessage.error('获取学生信息失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStudent()
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' },
    { type: 'number', message: '年龄必须为数字', trigger: 'blur', transform: val => Number(val) }
  ],
  department: [
    { required: true, message: '请输入院系', trigger: 'blur' }
  ],
  major: [
    { required: true, message: '请输入专业', trigger: 'blur' }
  ],
  className: [
    { required: true, message: '请输入班级', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ]
}

const formRef = ref(null)

const handleUpdate = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      updating.value = true
      try {
        // 如果密码字段为空，不更新密码
        const updateData = { ...studentForm }
        if (!updateData.password) {
          delete updateData.password
        }
        
        const res = await updateStudent(updateData)
        if (res.code === 1) {
          ElMessage.success('更新成功')
          // 更新本地用户数据
          userStore.user = { ...userStore.user, ...updateData }
          localStorage.setItem('user', JSON.stringify(userStore.user))
          originalStudent.value = { ...updateData }
        } else {
          ElMessage.error(res.msg || '更新失败')
        }
      } catch (error) {
        console.error('Error updating student info:', error)
        ElMessage.error('更新失败')
      } finally {
        updating.value = false
      }
    }
  })
}

const handleReset = () => {
  Object.assign(studentForm, originalStudent.value)
  ElMessage.info('已重置表单')
}
</script>

<template>
  <div class="profile-container" v-loading="loading">
    <el-card shadow="hover" class="profile-card">
      <template #header>
        <div class="card-header">
          <h2>个人信息</h2>
        </div>
      </template>
      
      <el-form 
        ref="formRef" 
        :model="studentForm" 
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号" prop="studentId">
              <el-input v-model="studentForm.studentId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="studentForm.name" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="studentForm.gender" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input v-model.number="studentForm.age" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="院系" prop="department">
              <el-input v-model="studentForm.department" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-input v-model="studentForm.major" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="班级" prop="className">
              <el-input v-model="studentForm.className" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="studentForm.password" type="password" placeholder="不修改请留空" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="studentForm.email" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="phone">
              <el-input v-model="studentForm.phone" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item>
          <el-button type="primary" @click="handleUpdate" :loading="updating">保存修改</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.profile-container {
  padding: 20px;
}

.profile-card {
  max-width: 980px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 22px;
}
</style> 