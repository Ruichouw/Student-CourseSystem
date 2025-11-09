<script setup>
import { ref, onMounted, computed } from 'vue'
import { getGradesByStudentId } from '@/api/courseSelection'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const user = computed(() => userStore.getUser)

const grades = ref([])
const loading = ref(false)

const fetchGrades = async () => {
  if (!user.value || !user.value.studentId) {
    ElMessage.warning('请先登录')
    return
  }
  
  loading.value = true
  try {
    const res = await getGradesByStudentId(user.value.studentId)
    if (res.code === 1) {
      grades.value = res.data || []
    } else {
      ElMessage.error(res.msg || '获取成绩失败')
    }
  } catch (error) {
    console.error('Error fetching grades:', error)
    ElMessage.error('获取成绩失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchGrades()
})

const getGradeLevel = (score) => {
  if (score === null || score === undefined) return '未评分'
  if (score >= 90) return '优秀'
  if (score >= 80) return '良好'
  if (score >= 70) return '中等'
  if (score >= 60) return '及格'
  return '不及格'
}

const getGradeStyle = (score) => {
  if (score === null || score === undefined) return ''
  if (score >= 90) return 'color: #67C23A;'
  if (score >= 80) return 'color: #409EFF;'
  if (score >= 70) return 'color: #E6A23C;'
  if (score >= 60) return 'color: #F56C6C;'
  return 'color: #F56C6C; font-weight: bold;'
}

const totalCredits = computed(() => {
  return grades.value
    .filter(course => course.score !== null && course.score >= 60)
    .reduce((sum, course) => sum + (course.credit || 0), 0)
})

const averageScore = computed(() => {
  const validGrades = grades.value.filter(course => course.score !== null)
  if (validGrades.length === 0) return 0
  
  const totalScore = validGrades.reduce((sum, course) => sum + course.score, 0)
  return (totalScore / validGrades.length).toFixed(2)
})

const weightedAverage = computed(() => {
  const validGrades = grades.value.filter(course => course.score !== null)
  if (validGrades.length === 0) return 0
  
  const totalWeightedScore = validGrades.reduce((sum, course) => 
    sum + (course.score * (course.credit || 1)), 0)
  const totalCredits = validGrades.reduce((sum, course) => 
    sum + (course.credit || 1), 0)
  
  return (totalWeightedScore / totalCredits).toFixed(2)
})
</script>

<template>
  <div class="grades-container">
    <div class="course-header">
      <div>
        <h2>成绩查询</h2>
      </div>
      <el-button type="primary" @click="fetchGrades">刷新</el-button>
    </div>
    
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>获得学分</span>
            </div>
          </template>
          <div class="stat-value">{{ totalCredits }}</div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>平均分</span>
            </div>
          </template>
          <div class="stat-value">{{ averageScore }}</div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>加权平均分</span>
            </div>
          </template>
          <div class="stat-value">{{ weightedAverage }}</div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card shadow="hover" class="grades-card">
      <el-table
        :data="grades"
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
        <el-table-column label="成绩" width="100">
          <template #default="{ row }">
            <span :style="getGradeStyle(row.score)">
              {{ row.score !== null ? row.score : '未评分' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="等级" width="100">
          <template #default="{ row }">
            <span :style="getGradeStyle(row.score)">
              {{ getGradeLevel(row.score) }}
            </span>
          </template>
        </el-table-column>
      </el-table>
      
      <div v-if="grades.length === 0 && !loading" class="empty-data">
        <el-empty description="暂无成绩记录" />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.grades-container {
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

.stat-cards {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
  text-align: center;
  padding: 10px 0;
}

.grades-card {
  margin-bottom: 20px;
}

.empty-data {
  padding: 30px 0;
}
</style> 