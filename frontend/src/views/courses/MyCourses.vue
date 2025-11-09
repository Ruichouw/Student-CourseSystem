<script setup>
import { ref, onMounted, computed } from 'vue'
import { getSelectionsByStudentId, dropCourse } from '@/api/courseSelection'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const user = computed(() => userStore.getUser)

const myCourses = ref([])
const loading = ref(false)
const dropLoading = ref(false)

const fetchMyCourses = async () => {
  if (!user.value || !user.value.studentId) {
    ElMessage.warning('请先登录')
    return
  }
  
  loading.value = true
  try {
    const res = await getSelectionsByStudentId(user.value.studentId)
    if (res.code === 1) {
      myCourses.value = res.data || []
    } else {
      ElMessage.error(res.msg || '获取我的课程失败')
    }
  } catch (error) {
    console.error('Error fetching my courses:', error)
    ElMessage.error('获取我的课程失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchMyCourses()
})

const handleDrop = async (course) => {
  if (!user.value || !user.value.studentId) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要退选 ${course.courseName} 课程吗？`,
      '退课确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    dropLoading.value = true
    const res = await dropCourse(user.value.studentId, course.courseId)
    
    if (res.code === 1) {
      ElMessage.success('退课成功')
      // 更新课程列表
      await fetchMyCourses()
    } else {
      ElMessage.error(res.msg || '退课失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Error dropping course:', error)
      ElMessage.error('退课失败')
    }
  } finally {
    dropLoading.value = false
  }
}

const totalCredits = computed(() => {
  return myCourses.value.reduce((sum, course) => sum + (course.credit || 0), 0)
})
</script>

<template>
  <div class="my-courses-container">
    <div class="course-header">
      <div>
        <h2>我的课程</h2>
        <p class="credit-info">总学分: {{ totalCredits }}</p>
      </div>
      <el-button type="primary" @click="fetchMyCourses">刷新</el-button>
    </div>
    
    <el-card shadow="hover" class="course-card">
      <el-table
        :data="myCourses"
        border
        stripe
        style="width: 100%"
        v-loading="loading"
        :header-cell-style="{ textAlign: 'center' }"
        :cell-style="{ textAlign: 'center' }"
      >
        <el-table-column prop="courseId" label="课程编号" width="120" />
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="teacherName" label="授课教师" />
        <el-table-column prop="selectionTime" label="选课时间">
          <template #default="{ row }">
            {{ new Date(row.selectionTime).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button
              type="danger"
              size="small"
              :loading="dropLoading"
              @click="handleDrop(row)"
            >
              退课
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div v-if="myCourses.length === 0 && !loading" class="empty-data">
        <el-empty description="暂无选课记录" />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.my-courses-container {
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

.credit-info {
  margin-top: 8px;
  color: #606266;
}

.course-card {
  margin-bottom: 20px;
}

.empty-data {
  padding: 30px 0;
}
</style> 