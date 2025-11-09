<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import { getTeacherCourses, getCourseStudents, updateStudentGrade } from '@/api/teacher'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const user = computed(() => userStore.getUser)

const courses = ref([])
const students = ref([])
const loadingCourses = ref(false)
const loadingStudents = ref(false)
const selectedCourseId = ref('')
const editingGrade = ref(null) // To store { studentId, courseId, score } temporarily

const fetchCourses = async () => {
  if (!user.value || !user.value.teacherId) {
    return
  }
  loadingCourses.value = true
  try {
    const response = await getTeacherCourses(user.value.teacherId)
    if (response && response.code === 1) {
      courses.value = response.data || []
      if (courses.value.length > 0) {
        if (!selectedCourseId.value || !courses.value.find(c => c.courseId === selectedCourseId.value)) {
          selectedCourseId.value = courses.value[0].courseId
        }
        // Students will be fetched by the watcher on selectedCourseId
      } else {
        students.value = []
        selectedCourseId.value = ''
      }
    } else {
      ElMessage.error(response.msg || '获取课程列表失败')
      courses.value = []
      students.value = []
    }
  } catch (error) {
    console.error("Error fetching teacher courses:", error)
    ElMessage.error('获取课程列表出错: ' + (error.message || '请检查网络'))
    courses.value = []
    students.value = []
  } finally {
    loadingCourses.value = false
  }
}

const fetchStudentsForCourse = async (courseId) => {
  if (!courseId) {
    students.value = []
    return
  }
  loadingStudents.value = true
  try {
    const response = await getCourseStudents(courseId)
    if (response && response.code === 1) {
      students.value = (response.data || []).map(s => ({ ...s, originalScore: s.score, isEditing: false }))
    } else {
      ElMessage.error(response.msg || '获取学生列表失败')
      students.value = []
    }
  } catch (error) {
    console.error("Error fetching students for course:", error)
    ElMessage.error('获取学生列表出错: ' + (error.message || '请检查网络'))
    students.value = []
  } finally {
    loadingStudents.value = false
  }
}

watch(selectedCourseId, (newCourseId) => {
  if (newCourseId) {
    fetchStudentsForCourse(newCourseId)
  } else {
    students.value = []
  }
})

const handleEditGrade = (row) => {
  // Cancel previous edit if any
  students.value.forEach(s => s.isEditing = false)
  row.isEditing = true
  editingGrade.value = { studentId: row.studentId, courseId: selectedCourseId.value, score: row.score === null || row.score === undefined ? '' : row.score }
}

const handleCancelEdit = (row) => {
  row.score = row.originalScore // Revert to original score
  row.isEditing = false
  editingGrade.value = null
}

const handleSaveGrade = async (row) => {
  if (editingGrade.value.score === '' || editingGrade.value.score === null || editingGrade.value.score === undefined) {
    ElMessage.warning('成绩不能为空')
    return
  }
  const score = parseInt(editingGrade.value.score, 10)
  if (isNaN(score) || score < 0 || score > 100) {
    ElMessage.error('请输入0-100之间的有效成绩数字')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要将 ${row.studentName} 同学在 ${courses.value.find(c => c.courseId === selectedCourseId.value)?.name || '该课程'} 的成绩更新为 ${score} 吗?`,
      '确认更新成绩',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    const payload = {
      studentId: row.studentId,
      courseId: selectedCourseId.value,
      score: score
    }
    const response = await updateStudentGrade(payload)
    if (response && response.code === 1) {
      ElMessage.success('成绩更新成功')
      row.score = score
      row.originalScore = score // Update original score as well
      row.isEditing = false
      editingGrade.value = null
    } else {
      ElMessage.error(response.msg || '成绩更新失败')
    }
  } catch (error) {
    if (error !== 'cancel') { // User clicked cancel
      console.error("Error updating grade:", error)
      ElMessage.error('成绩更新出错: ' + (error.message || '请检查网络'))
    } else {
      ElMessage.info('已取消更新')
    }
  } finally {
    row.isEditing = false // Ensure editing mode is exited
  }
}

let unwatchUser = null
onMounted(() => {
  if (userStore.isAuthenticated && user.value && user.value.teacherId) {
    fetchCourses()
  } else {
    unwatchUser = userStore.$subscribe((mutation, state) => {
      if (state.user && state.user.teacherId && state.isAuthenticated) {
        fetchCourses()
        if (unwatchUser) unwatchUser()
      }
    })
    if (!userStore.isAuthenticated) {
      ElMessage.error('用户未登录或登录已过期')
    }
  }
})

import { onBeforeUnmount } from 'vue'
onBeforeUnmount(() => {
  if (unwatchUser) {
    unwatchUser()
  }
})
</script>

<template>
  <div class="grade-management">
    <div class="page-header">
      <h2>成绩管理</h2>
      <el-select
        v-model="selectedCourseId"
        placeholder="请选择课程"
        style="width: 350px"
        :loading="loadingCourses"
        clearable
        filterable
      >
        <el-option
          v-for="course in courses"
          :key="course.courseId"
          :label="`${course.name} (${course.courseId})`"
          :value="course.courseId"
        />
        <template #empty>
          <div style="text-align: center; padding: 10px; color: #909399;">
            {{ loadingCourses ? '加载课程中...' : (courses.length === 0 ? '您目前没有有效的授课课程' : '无匹配课程') }}
          </div>
        </template>
      </el-select>
    </div>

    <el-table 
      :data="students" 
      v-loading="loadingStudents" 
      border 
      stripe 
      empty-text="请先选择一门课程，或该课程暂无学生"
      style="width: 100%"
    >
      <el-table-column prop="studentId" label="学号" width="120" sortable align="center" />
      <el-table-column prop="studentName" label="姓名" width="100" align="center" />
      <el-table-column prop="gender" label="性别" width="80" align="center" />
      <el-table-column prop="department" label="院系" width="150" align="center" />
      <el-table-column prop="major" label="专业" width="150" align="center" />
      <el-table-column prop="className" label="班级" width="120" align="center" />
      <el-table-column prop="email" label="邮箱" min-width="180" align="center" />
      <el-table-column prop="phone" label="手机号" width="130" align="center" />
      <el-table-column prop="score" label="成绩" width="150" sortable align="center">
        <template #default="{ row }">
          <div v-if="row.isEditing">
            <el-input
              v-model="editingGrade.score"
              type="number"
              placeholder="0-100"
              size="small"
              style="width: 80px; margin-right: 8px;"
              @keyup.enter="handleSaveGrade(row)"
              min="0"
              max="100"
            />
          </div>
          <span v-else>
            <el-tag :type="row.score === null || row.score === undefined ? 'info' : (row.score >= 60 ? 'success' : 'danger')">
              {{ row.score !== null && row.score !== undefined ? row.score : '未录入' }}
            </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <div v-if="row.isEditing">
            <el-button type="success" size="small" @click="handleSaveGrade(row)">保存</el-button>
            <el-button type="info" size="small" @click="handleCancelEdit(row)">取消</el-button>
          </div>
          <div v-else>
            <el-button type="primary" size="small" @click="handleEditGrade(row)">
              {{ (row.score === null || row.score === undefined) ? '录入成绩' : '修改成绩' }}
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped>
.grade-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
}
</style>