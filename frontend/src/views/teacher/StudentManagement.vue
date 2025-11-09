<script setup>
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { getTeacherCourses, getCourseStudents } from '@/api/teacher'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const user = computed(() => userStore.getUser)

const courses = ref([])
const students = ref([])
const loadingCourses = ref(false)
const loadingStudents = ref(false)
const selectedCourseId = ref('')

// 获取教师课程列表
const fetchCourses = async () => {
  if (!user.value || !user.value.teacherId) {
    return;
  }
  loadingCourses.value = true;
  try {
    const response = await getTeacherCourses(user.value.teacherId);
    if (response && response.code === 1) {
      courses.value = response.data || [];
      if (courses.value.length > 0) {
        if (!selectedCourseId.value || !courses.value.find(c => c.courseId === selectedCourseId.value)) {
             selectedCourseId.value = courses.value[0].courseId; // Default to first course if current selection is invalid or not set
        }
        await fetchStudentsForCourse(selectedCourseId.value);
      } else {
        students.value = []; 
        selectedCourseId.value = ''; // Clear selection if no courses
      }
    } else {
      ElMessage.error(response.msg || '获取课程列表失败');
      courses.value = [];
      students.value = [];
    }
  } catch (error) {
    console.error("Error fetching teacher courses:", error);
    ElMessage.error('获取课程列表出错: ' + (error.message || '请检查网络'));
    courses.value = [];
    students.value = [];
  } finally {
    loadingCourses.value = false;
  }
}

// 获取选课学生列表
const fetchStudentsForCourse = async (courseId) => {
  if (!courseId) {
    students.value = [];
    return;
  }
  loadingStudents.value = true;
  try {
    const response = await getCourseStudents(courseId);
    if (response && response.code === 1) {
      students.value = response.data || [];
    } else {
      ElMessage.error(response.msg || '获取学生列表失败');
      students.value = [];
    }
  } catch (error) {
    console.error("Error fetching students for course:", error);
    ElMessage.error('获取学生列表出错: ' + (error.message || '请检查网络'));
    students.value = [];
  } finally {
    loadingStudents.value = false;
  }
}

// 课程变更
const handleCourseChange = (courseId) => {
  if (courseId) {
    fetchStudentsForCourse(courseId);
  } else {
    students.value = []; // Clear students if no course is selected
  }
}

let unwatchUser = null;

onMounted(() => {
  if (userStore.isAuthenticated && user.value && user.value.teacherId) {
    fetchCourses();
  } else {
    // Fallback or watcher if user data loads asynchronously after component mount
    unwatchUser = userStore.$subscribe((mutation, state) => {
      if (state.user && state.user.teacherId && state.isAuthenticated) {
        fetchCourses();
        if (unwatchUser) unwatchUser(); // Stop watching once data is available and fetched
      }
    });
    if (!userStore.isAuthenticated) {
        ElMessage.error('用户未登录或登录已过期');
    }
  }
});

import { onBeforeUnmount } from 'vue';
onBeforeUnmount(() => {
  if (unwatchUser) {
    unwatchUser(); // Clean up a watcher when component is unmounted
  }
});

</script>

<template>
  <div class="student-management">
    <div class="page-header">
      <h2>学生管理</h2>
      <el-select 
        v-model="selectedCourseId" 
        placeholder="请选择课程"
        @change="handleCourseChange"
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
      <el-table-column prop="selectionTime" label="选课时间" width="180" align="center" />
      <el-table-column prop="score" label="成绩" width="100" sortable align="center">
        <template #default="{row}">
          <el-tag :type="row.score == null ? 'info' : (row.score >= 60 ? 'success' : 'danger')">
            {{ row.score !== null ? row.score : '未录入' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped>
.student-management {
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