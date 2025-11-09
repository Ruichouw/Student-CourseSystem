<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useUserStore } from '@/stores/user'
// 导入更新后的API
import { 
  getTeacherCourses, 
  proposeCourse, 
  updateProposedCourse, 
  proposeUpdateToApprovedCourse 
} from '@/api/teacher'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const user = userStore.getUser

const courses = ref([])
const loading = ref(false)

// 课程表单 - 更新字段
const courseForm = reactive({
  courseId: '', // 课程编号
  name: '',     // 课程名称
  credit: null,  // 学分
  hours: null,   // 学时
  description: '', // 课程描述
  maxStudents: null, // 最大选课人数
  syllabus: '',      // 教学大纲
  textbook: '',      // 教材
  scheduleInfo: '',  // 时间安排
  // teacherId will be set from userStore or currentCourse, status handled by API logic
})

// 对话框控制
const dialogVisible = ref(false)
const dialogMode = ref('create') // 'create', 'editPending', 'proposeUpdate'
const currentEditingCourse = ref(null) // Stores the course being edited/updated

// 获取教师课程列表
const fetchCourses = async () => {
  if (!user || !user.teacherId) {
    ElMessage.warning('教师信息不完整，无法获取课程列表');
    return;
  }
  loading.value = true
  try {
    // Assuming the backend returns a list of courses directly under `data` or `Result.success(data)` structure
    const response = await getTeacherCourses(user.teacherId)
    // Adapt this based on your actual API response structure for Result.java
    if (response && response.data) { // Assuming Result.java has { code, msg, data }
        courses.value = response.data
    } else if (response && response.code === 1 && response.data === null) { // Success but no data
        courses.value = [];
    } else {
      ElMessage.error(response.msg || '获取课程列表失败');
      courses.value = []; // Clear courses on error
    }
  } catch (error) {
    console.error("Error fetching courses:", error);
    ElMessage.error('获取课程列表出错: ' + (error.message || '请检查网络连接或联系管理员'));
    courses.value = [];
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  Object.keys(courseForm).forEach(key => {
    if (typeof courseForm[key] === 'string') {
      courseForm[key] = ''
    } else if (typeof courseForm[key] === 'number') {
      courseForm[key] = null
    } else {
      courseForm[key] = null
    }
  });
  currentEditingCourse.value = null;
}

// 打开对话框
const openDialog = (mode, course = null) => {
  resetForm();
  dialogMode.value = mode;
  if (mode === 'create') {
    // Default values for new course if any, courseId can be set by user
  } else if (course) {
    currentEditingCourse.value = JSON.parse(JSON.stringify(course)); // Deep copy for editing
    Object.assign(courseForm, currentEditingCourse.value);
  }
  dialogVisible.value = true;
}

// 提交表单
const handleSubmit = async () => {
  if (!user || !user.teacherId) {
    ElMessage.error('教师ID未找到，无法提交');
    return;
  }

  const payload = { ...courseForm, teacherId: user.teacherId };
  let success = false;
  let message = '';

  try {
    let response;
    if (dialogMode.value === 'create') {
      response = await proposeCourse(payload);
      message = '新课程申请已提交';
    } else if (dialogMode.value === 'editPending') {
      if (!currentEditingCourse.value || !currentEditingCourse.value.courseId) return;
      response = await updateProposedCourse(currentEditingCourse.value.courseId, payload);
      message = '课程申请修改已提交';
    } else if (dialogMode.value === 'proposeUpdate') {
      if (!currentEditingCourse.value || !currentEditingCourse.value.courseId) return;
      response = await proposeUpdateToApprovedCourse(currentEditingCourse.value.courseId, payload);
      message = '对已批准课程的修改申请已提交';
    }

    // Assuming Result.java structure { code: 1 for success, msg: ..., data: ...}
    if (response && response.code === 1) {
      ElMessage.success(message);
      success = true;
    } else {
      ElMessage.error(response.msg || '操作失败');
    }
  } catch (error) {
    console.error("Form submission error:", error);
    ElMessage.error('操作出错: ' + (error.message || '请稍后再试'));
  }

  if (success) {
    dialogVisible.value = false;
    fetchCourses(); // Refresh list
  }
}

onMounted(() => {
  if (userStore.isAuthenticated && user) { // Ensure user is loaded
     fetchCourses();
  } else {
    // Handle case where user data might not be immediately available
    // Could use a watcher on userStore.user or an event
    ElMessage.warning('等待用户信息加载...');
    // Potentially call fetchCourses once user is available via a watcher or event system
  }
});

const getDialogTitle = () => {
  if (dialogMode.value === 'create') return '申请新课程';
  if (dialogMode.value === 'editPending') return '编辑课程申请';
  if (dialogMode.value === 'proposeUpdate') return '申请修改已批准课程';
  return '课程信息';
};
</script>

<template>
  <div class="course-management">
    <div class="page-header">
      <h2>课程管理</h2>
      <el-button type="primary" @click="openDialog('create')">
        <el-icon><el-icon-plus /></el-icon>&nbsp;申请新课程
      </el-button>
    </div>
    
    <el-table :data="courses" v-loading="loading" border stripe>
      <el-table-column prop="courseId" label="课程编号" width="120" sortable />
      <el-table-column prop="name" label="课程名称" width="180" sortable />
      <el-table-column prop="credit" label="学分" width="80" />
      <el-table-column prop="hours" label="学时" width="80" />
      <el-table-column prop="currentStudents" label="已选人数" width="100" />
      <el-table-column prop="maxStudents" label="最大人数" width="100" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{row}">
          <el-tag 
            :type="row.status === 'APPROVED' ? 'success' : (row.status === 'PENDING_APPROVAL' ? 'warning' : (row.status === 'REJECTED' ? 'danger' : 'info'))"
          >
            {{ row.status === 'APPROVED' ? '已批准' : (row.status === 'PENDING_APPROVAL' ? '待审核' : (row.status === 'REJECTED' ? '已拒绝' : row.status)) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="rejectionReason" label="拒绝理由" width="150" />
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{row}">
          <el-button 
            v-if="row.status === 'PENDING_APPROVAL'"
            size="small" 
            type="primary" 
            @click="openDialog('editPending', row)"
          >
            编辑申请
          </el-button>
          <el-button 
            v-if="row.status === 'APPROVED'"
            size="small" 
            type="warning"
            @click="openDialog('proposeUpdate', row)"
          >
            申请修改
          </el-button>
          <el-button 
            v-if="row.status === 'REJECTED'"
            size="small" 
            @click="openDialog('create', row)" 
          >
            重新申请
          </el-button>
          <!-- Add other actions like view details if needed -->
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 课程信息对话框 -->
    <el-dialog 
      :title="getDialogTitle()" 
      v-model="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="courseForm" label-width="100px" ref="courseFormRef">
        <el-form-item label="课程编号" prop="courseId" required>
          <el-input v-model="courseForm.courseId" :disabled="dialogMode !== 'create'" placeholder="例如：C001" />
        </el-form-item>
        <el-form-item label="课程名称" prop="name" required>
          <el-input v-model="courseForm.name" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学分" prop="credit" required>
              <el-input-number v-model="courseForm.credit" :min="0" :precision="1" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学时" prop="hours" required>
              <el-input-number v-model="courseForm.hours" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="最大人数" prop="maxStudents" required>
          <el-input-number v-model="courseForm.maxStudents" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="课程描述" prop="description">
          <el-input v-model="courseForm.description" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="教学大纲" prop="syllabus">
          <el-input v-model="courseForm.syllabus" type="textarea" rows="3" placeholder="课程的主要内容、学习目标等" />
        </el-form-item>
        <el-form-item label="教材" prop="textbook">
          <el-input v-model="courseForm.textbook" placeholder="主要参考教材及作者" />
        </el-form-item>
        <el-form-item label="时间安排" prop="scheduleInfo">
          <el-input v-model="courseForm.scheduleInfo" placeholder="例如：每周一3-4节，X教学楼Y教室" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.course-management {
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