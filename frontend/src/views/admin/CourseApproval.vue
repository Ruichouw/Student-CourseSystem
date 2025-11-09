<template>
  <div class="course-approval-admin">
    <div class="page-header">
      <h2>课程审批管理</h2>
    </div>

    <el-table :data="pendingCourses" v-loading="loading" border stripe empty-text="暂无待审批课程">
      <el-table-column prop="courseId" label="课程ID" width="100" sortable />
      <el-table-column prop="name" label="课程名称" width="180" />
      <el-table-column label="申请教师" width="150">
         <template #default="{ row }">
          <!-- Assuming row.teacher object or row.teacherName and row.teacherId exist -->
          {{ row.teacher ? getTeacherDisplay(row.teacher) : (row.teacherName ? `${row.teacherName} (${row.teacherId})` : row.teacherId || 'N/A') }}
        </template>
      </el-table-column>
      <el-table-column prop="credit" label="学分" width="70" sortable />
      <el-table-column prop="hours" label="学时" width="70" sortable />
      <el-table-column prop="maxStudents" label="人数上限" width="100" />
      
      <el-table-column label="详情" width="80" align="center">
        <template #default="{ row }">
          <el-popover placement="right" title="课程详情" width="400" trigger="click">
            <template #reference>
              <el-button size="small" type="info">查看详情</el-button>
            </template>
            <div class="course-details-popover">
              <p><strong>课程ID:</strong> {{ row.courseId }}</p>
              <p><strong>课程名称:</strong> {{ row.name }}</p>
              <p><strong>授课教师:</strong> {{ row.teacher ? getTeacherDisplay(row.teacher) : (row.teacherName ? `${row.teacherName} (${row.teacherId})` : row.teacherId || 'N/A') }}</p>
              <p><strong>学分:</strong> {{ row.credit }}</p>
              <p><strong>学时:</strong> {{ row.hours }}</p>
              <p><strong>人数上限:</strong> {{ row.maxStudents }}</p>
              <p><strong>课程描述:</strong> {{ row.description || '-' }}</p>
              <p><strong>教学大纲:</strong> <pre>{{ row.syllabus || '-' }}</pre></p>
              <p><strong>教材信息:</strong> {{ row.textbook || '-' }}</p>
              <p><strong>时间安排:</strong> {{ row.scheduleInfo || '-' }}</p>
              <p><strong>当前状态:</strong> {{ row.status }}</p>
               <p v-if="row.status === 'REJECTED'"><strong>拒绝理由:</strong> {{row.rejectionReason}}</p>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="success" @click="handleApprove(row)">批准</el-button>
          <el-button size="small" type="danger" @click="openRejectDialog(row)">拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="rejectionDialogVisible"
      title="输入拒绝理由"
      width="500px"
      draggable
      @close="() => { rejectionForm.reason = ''; if(rejectionFormRef) rejectionFormRef.resetFields(); }"
    >
      <el-form :model="rejectionForm" ref="rejectionFormRef" @submit.prevent>
        <el-form-item 
            label="拒绝理由" 
            prop="reason" 
            :rules="[{ required: true, message: '拒绝理由不能为空', trigger: 'blur' }]"
        >
          <el-input 
            type="textarea" 
            :rows="4" 
            v-model="rejectionForm.reason" 
            placeholder="请输入拒绝该课程申请的理由"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRejectSubmit" :loading="loading">确认拒绝</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getPendingCourses, approveCourse, rejectCourse } from '@/api/admin';
import { getTeacherById } from '@/api/teacher'; // To fetch teacher details for display

const pendingCourses = ref([]);
const loading = ref(false);
const rejectionDialogVisible = ref(false);
const currentCourseForRejection = ref(null);
const rejectionForm = reactive({
  courseId: null,
  reason: ''
});
const rejectionFormRef = ref(null);

const fetchPendingCourses = async () => {
  loading.value = true;
  try {
    const response = await getPendingCourses();
    if (response && response.code === 1) {
      // Enrich courses with teacher names if not already present
      const coursesData = response.data || [];
      // Assuming teacher details might not be directly in pending course data
      // and we might need to fetch them or they are already part of the object.
      // For now, assuming teacherId is present and we might display it directly
      // or enhance it if teacherName is also provided by getPendingCourses.
      // If not, a more complex enrichment might be needed post-fetch.
      pendingCourses.value = coursesData;
    } else {
      ElMessage.error(response.msg || '获取待审批课程列表失败');
      pendingCourses.value = [];
    }
  } catch (error) {
    console.error("Error fetching pending courses:", error);
    ElMessage.error('获取待审批课程列表出错: ' + (error.message || '网络错误'));
    pendingCourses.value = [];
  } finally {
    loading.value = false;
  }
};

// Function to get teacher name - assuming teacherId is available on course object
// This is a placeholder, ideally backend would provide teacherName directly
// or we fetch all teachers once and map.
// For simplicity, if teacherName is not on course, we show ID.
const getTeacherDisplay = (teacher) => {
    if (!teacher) return 'N/A';
    if (teacher.teacherName) return `${teacher.teacherName} (${teacher.teacherId})`;
    return teacher.teacherId || 'N/A';
}

onMounted(() => {
  fetchPendingCourses();
});

const handleApprove = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要批准课程 "${row.name}" (ID: ${row.courseId}) 吗？`,
      '确认批准',
      {
        confirmButtonText: '批准',
        cancelButtonText: '取消',
        type: 'success',
      }
    );
    loading.value = true;
    const response = await approveCourse(row.courseId);
    if (response && response.code === 1) {
      ElMessage.success('课程已批准');
      fetchPendingCourses(); // Refresh list
    } else {
      ElMessage.error(response.msg || '批准失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error("Error approving course:", error);
      ElMessage.error('批准操作出错: ' + (error.message || '网络错误'));
    }
  } finally {
    loading.value = false;
  }
};

const openRejectDialog = (row) => {
  currentCourseForRejection.value = row;
  rejectionForm.courseId = row.courseId;
  rejectionForm.reason = row.rejectionReason || ''; // Pre-fill if already rejected and being re-evaluated (though typically not from pending)
  rejectionDialogVisible.value = true;
  if(rejectionFormRef.value) {
    rejectionFormRef.value.resetFields(); // Clear previous validation
    rejectionForm.reason = row.rejectionReason || ''; // re-assign after resetFields if needed
  }
};

const handleRejectSubmit = async () => {
  if (!rejectionFormRef.value) return;
  await rejectionFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        const response = await rejectCourse(rejectionForm.courseId, rejectionForm.reason);
        if (response && response.code === 1) {
          ElMessage.success('课程已拒绝');
          rejectionDialogVisible.value = false;
          fetchPendingCourses(); // Refresh list
        } else {
          ElMessage.error(response.msg || '拒绝失败');
        }
      } catch (error) {
        console.error("Error rejecting course:", error);
        ElMessage.error('拒绝操作出错: ' + (error.message || '网络错误'));
      } finally {
        loading.value = false;
      }
    } else {
      ElMessage.warning('请输入拒绝理由');
      return false;
    }
  });
};

</script>

<style scoped>
.course-approval-admin {
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
.course-details-popover p {
    margin: 5px 0;
    font-size: 14px;
}
.course-details-popover pre {
    background-color: #f5f5f5;
    padding: 5px;
    border-radius: 4px;
    white-space: pre-wrap; /* Behaves like <pre> but wraps text */
    word-wrap: break-word; /*Ensures long words break to next line */
    font-family: inherit;
    font-size: 13px;
}
</style> 