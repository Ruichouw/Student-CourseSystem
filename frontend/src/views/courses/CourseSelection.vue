<script setup>
import { ref, onMounted, computed } from 'vue'
import { getAllCourses } from '@/api/course'
import { selectCourse } from '@/api/courseSelection'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const user = computed(() => userStore.getUser)

const courses = ref([])
const loading = ref(false)
const searchQuery = ref('')
const selectLoading = ref(false)

const fetchCourses = async () => {
  loading.value = true
  try {
    const res = await getAllCourses()
    if (res.code === 1) {
      courses.value = res.data || []
    } else {
      ElMessage.error(res.msg || '获取课程列表失败')
    }
  } catch (error) {
    console.error('Error fetching courses:', error)
    ElMessage.error('获取课程列表失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCourses()
})

const filteredCourses = computed(() => {
  if (!searchQuery.value) return courses.value
  
  const query = searchQuery.value.toLowerCase()
  return courses.value.filter(course => 
    course.courseId.toLowerCase().includes(query) ||
    course.name.toLowerCase().includes(query) ||
    (course.teacherName && course.teacherName.toLowerCase().includes(query))
  )
})

const formatAvailability = (course) => {
  const available = course.maxStudents - course.currentStudents
  const percentage = Math.round((course.currentStudents / course.maxStudents) * 100)
  return `${course.currentStudents}/${course.maxStudents} (${percentage}%)`
}

const handleSelect = async (course) => {
  if (!user.value || !user.value.studentId) {
    ElMessage.warning('请先登录')
    return
  }
  
  if (course.currentStudents >= course.maxStudents) {
    ElMessage.warning('该课程已满')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要选择 ${course.name} 课程吗？`,
      '选课确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    selectLoading.value = true
    const res = await selectCourse(user.value.studentId, course.courseId)
    
    if (res.code === 1) {
      ElMessage.success('选课成功')
      // 更新课程列表
      await fetchCourses()
    } else {
      ElMessage.error(res.msg || '选课失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Error selecting course:', error)
      ElMessage.error('选课失败')
    }
  } finally {
    selectLoading.value = false
  }
}

const isAvailable = (course) => {
  return course.currentStudents < course.maxStudents
}
</script>

<template>
  <div class="course-selection-container">
    <div class="course-header">
      <h2>选课中心</h2>
      <el-input
        v-model="searchQuery"
        placeholder="搜索课程"
        prefix-icon="el-icon-search"
        clearable
        style="width: 300px"
      />
    </div>
    
    <el-card shadow="hover" class="course-card">
      <el-table
        :data="filteredCourses"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="courseId" label="课程编号" width="120" />
        <el-table-column prop="name" label="课程名称" width="180" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="hours" label="学时" width="80" />
        <el-table-column prop="teacherName" label="授课教师" width="120" />
        <el-table-column label="选课人数" width="120">
          <template #default="{ row }">
            {{ formatAvailability(row) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="课程描述" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              :disabled="!isAvailable(row)"
              :loading="selectLoading"
              @click="handleSelect(row)"
            >
              选课
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.course-selection-container {
  padding: 20px;
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.course-header h2 {
  margin: 0;
  font-size: 22px;
}

.course-card {
  margin-bottom: 20px;
}
</style> 