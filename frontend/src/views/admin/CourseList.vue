<template>
  <div class="course-list-admin">
    <div class="page-header">
      <h2>课程列表管理</h2>
      <el-button type="primary" @click="handleAdd">新增课程</el-button>
    </div>

    <el-table :data="courses" v-loading="loading" border stripe empty-text="暂无课程数据">
      <el-table-column prop="courseId" label="课程ID" width="100" sortable />
      <el-table-column prop="name" label="课程名称" width="180" />
      <el-table-column prop="credit" label="学分" width="70" sortable />
      <el-table-column prop="hours" label="学时" width="70" sortable />
      <el-table-column label="授课教师" width="120">
        <template #default="{ row }">
          {{ getTeacherName(row.teacherId) }} ({{ row.teacherId }})
        </template>
      </el-table-column>
      <el-table-column prop="maxStudents" label="人数上限" width="100" sortable />
      <el-table-column prop="status" label="课程状态" width="120">
         <template #default="{ row }">
            <el-tag 
                :type="row.status === 'APPROVED' ? 'success' : (row.status === 'PENDING_APPROVAL' ? 'warning' : 'danger')"
            >
                {{ courseStatusOptions.find(opt => opt.value === row.status)?.label || row.status }}
            </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="warning" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'add' ? '新增课程' : '编辑课程信息'"
      width="700px"
      @close="resetForm"
      draggable
      top="5vh"
    >
      <el-form :model="courseForm" :rules="rules" ref="courseFormRef" label-width="100px">
        <el-row :gutter="20">
            <el-col :span="12">
                <el-form-item label="课程名称" prop="name">
                    <el-input v-model="courseForm.name" placeholder="请输入课程名称" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                 <el-form-item label="授课教师" prop="teacherId">
                    <el-select v-model="courseForm.teacherId" placeholder="请选择授课教师" filterable style="width: 100%;">
                        <el-option 
                            v-for="teacher in teachers" 
                            :key="teacher.teacherId" 
                            :label="`${teacher.teacherName} (${teacher.teacherId})`" 
                            :value="teacher.teacherId"
                        />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-form-item label="学分" prop="credit">
                    <el-input-number v-model="courseForm.credit" :min="0.5" :step="0.5" placeholder="学分" style="width: 100%;"/>
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item label="学时" prop="hours">
                    <el-input-number v-model="courseForm.hours" :min="1" placeholder="总学时" style="width: 100%;"/>
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item label="人数上限" prop="maxStudents">
                    <el-input-number v-model="courseForm.maxStudents" :min="1" placeholder="最大选课人数" style="width: 100%;"/>
                </el-form-item>
            </el-col>
        </el-row>
         <el-form-item label="课程状态" prop="status">
          <el-select v-model="courseForm.status" placeholder="选择课程状态" style="width: 100%;">
            <el-option
              v-for="item in courseStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="拒绝理由" prop="rejectionReason" v-if="courseForm.status === 'REJECTED'">
            <el-input type="textarea" v-model="courseForm.rejectionReason" placeholder="请输入拒绝理由 (仅当状态为已拒绝时填写)"/>
        </el-form-item>

        <el-form-item label="课程描述" prop="description">
          <el-input type="textarea" :rows="3" v-model="courseForm.description" placeholder="请输入课程描述" />
        </el-form-item>
        <el-form-item label="教学大纲" prop="syllabus">
          <el-input type="textarea" :rows="3" v-model="courseForm.syllabus" placeholder="请输入教学大纲" />
        </el-form-item>
        <el-form-item label="教材信息" prop="textbook">
          <el-input type="textarea" :rows="2" v-model="courseForm.textbook" placeholder="请输入教材信息" />
        </el-form-item>
        <el-form-item label="时间安排" prop="scheduleInfo">
          <el-input type="textarea" :rows="2" v-model="courseForm.scheduleInfo" placeholder="例如：每周一 1-2节，地点：教A101" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          {{ dialogMode === 'add' ? '立即创建' : '保存更改' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  getAllCoursesForAdmin,
  adminAddCourse,
  adminUpdateCourse,
  adminDeleteCourse
} from '@/api/admin';
import { getAllTeachers } from '@/api/admin'; // For teacher selection in form

const courses = ref([]);
const teachers = ref([]); // For teacher selection
const loading = ref(false);
const dialogVisible = ref(false);
const dialogMode = ref('add'); // 'add' or 'edit'
const currentCourse = ref(null);

const courseForm = reactive({
  courseId: null,
  name: '',
  credit: null,
  hours: null,
  teacherId: '', // Will be selected from a dropdown
  teacherName: '', // For display, can be auto-filled or managed
  description: '',
  maxStudents: null,
  syllabus: '',
  textbook: '',
  scheduleInfo: '',
  status: 'APPROVED', // Admin directly adds/edits approved courses here
  rejectionReason: null // Should be null for approved courses
});

const courseStatusOptions = [
  { value: 'APPROVED', label: '已批准' },
  { value: 'PENDING_APPROVAL', label: '待审批' },
  { value: 'REJECTED', label: '已拒绝' },
];

const rules = {
  name: [{ required: true, message: '课程名称不能为空', trigger: 'blur' }],
  credit: [
    { required: true, message: '学分不能为空', trigger: 'blur' },
    { type: 'number', message: '学分必须是数字', trigger: 'blur' },
    { validator: (r,val,cb) => val > 0 ? cb() : cb(new Error('学分必须大于0')), trigger: 'blur'}
  ],
  hours: [
    { required: true, message: '学时不能为空', trigger: 'blur' },
    { type: 'number', message: '学时必须是数字', trigger: 'blur' },
    { validator: (r,val,cb) => val > 0 ? cb() : cb(new Error('学时必须大于0')), trigger: 'blur'}
  ],
  teacherId: [{ required: true, message: '必须分配授课教师', trigger: 'change' }],
  maxStudents: [
    { required: true, message: '最大选课人数不能为空', trigger: 'blur' },
    { type: 'number', message: '必须是数字', trigger: 'blur' },
    { validator: (r,val,cb) => val > 0 ? cb() : cb(new Error('人数必须大于0')), trigger: 'blur'}
  ],
  status: [{ required: true, message: '课程状态不能为空', trigger: 'change' }]
};

const courseFormRef = ref(null);

const fetchInitialData = async () => {
  loading.value = true;
  try {
    const [coursesResponse, teachersResponse] = await Promise.all([
      getAllCoursesForAdmin(),
      getAllTeachers() // Fetch teachers for the dropdown
    ]);

    if (coursesResponse && coursesResponse.code === 1) {
      courses.value = coursesResponse.data || [];
    } else {
      ElMessage.error(coursesResponse.msg || '获取课程列表失败');
    }

    if (teachersResponse && teachersResponse.code === 1) {
      teachers.value = teachersResponse.data || [];
    } else {
      ElMessage.error(teachersResponse.msg || '获取教师列表失败');
    }

  } catch (error) {
    console.error("Error fetching initial data:", error);
    ElMessage.error('获取初始数据出错: ' + (error.message || '网络错误'));
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchInitialData();
});

const resetForm = () => {
  if (courseFormRef.value) {
    courseFormRef.value.resetFields();
  }
  // Explicitly reset non-field properties or complex objects if necessary
  courseForm.courseId = null;
  courseForm.syllabus = '';
  courseForm.textbook = '';
  courseForm.scheduleInfo = '';
  courseForm.description = '';
  courseForm.status = 'APPROVED'; 
  courseForm.rejectionReason = null;
};

const handleAdd = () => {
  resetForm();
  dialogMode.value = 'add';
  currentCourse.value = null;
  courseForm.status = 'APPROVED'; // Default for admin adding courses
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  resetForm();
  dialogMode.value = 'edit';
  currentCourse.value = { ...row };
  Object.assign(courseForm, row);
  // Ensure numeric fields that might be null/undefined are handled if el-input-number expects number
  courseForm.credit = Number(row.credit) || null;
  courseForm.hours = Number(row.hours) || null;
  courseForm.maxStudents = Number(row.maxStudents) || null;

  dialogVisible.value = true;
};

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除课程 ${row.name} (ID: ${row.courseId}) 吗？选课记录等关联数据可能会受影响。`,
      '确认删除',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );
    loading.value = true;
    const response = await adminDeleteCourse(row.courseId);
    if (response && response.code === 1) {
      ElMessage.success('课程删除成功');
      fetchInitialData(); // Refresh list
    } else {
      ElMessage.error(response.msg || '删除失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error("Error deleting course:", error);
      ElMessage.error('删除出错: ' + (error.message || '网络错误'));
    }
  } finally {
    loading.value = false;
  }
};

const handleSubmit = async () => {
  if (!courseFormRef.value) return;
  await courseFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        let response;
        const payload = { ...courseForm };

        // Ensure rejectionReason is null if status is not REJECTED
        if (payload.status !== 'REJECTED') {
            payload.rejectionReason = null;
        }

        if (dialogMode.value === 'add') {
          response = await adminAddCourse(payload);
        } else {
          response = await adminUpdateCourse(currentCourse.value.courseId, payload);
        }

        if (response && response.code === 1) {
          ElMessage.success(dialogMode.value === 'add' ? '课程添加成功' : '课程信息更新成功');
          dialogVisible.value = false;
          fetchInitialData(); // Refresh list
        } else {
          ElMessage.error(response.msg || (dialogMode.value === 'add' ? '添加失败' : '更新失败'));
        }
      } catch (error) {
        console.error("Error submitting course form:", error);
        ElMessage.error('操作失败: ' + (error.message || '网络错误'));
      } finally {
        loading.value = false;
      }
    } else {
      ElMessage.warning('请检查表单输入项');
      return false;
    }
  });
};

const getTeacherName = (teacherId) => {
    const teacher = teachers.value.find(t => t.teacherId === teacherId);
    return teacher ? teacher.teacherName : '未知教师';
};

</script>

<style scoped>
.course-list-admin {
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