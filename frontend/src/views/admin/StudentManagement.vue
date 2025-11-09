<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getAllStudents, addStudent, updateStudent, deleteStudent } from '@/api/admin';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();
// Although admin info might not be directly used here, good to have for consistency or future use
// const admin = computed(() => userStore.getUser);

const students = ref([]);
const loading = ref(false);
const dialogVisible = ref(false);
const dialogMode = ref('add'); // 'add' or 'edit'
const currentStudent = ref(null); // For storing student being edited

const studentForm = reactive({
  studentId: '',
  name: '',
  gender: '',
  department: '',
  major: '',
  className: '',
  email: '',
  phone: '',
  enrollmentYear: null,
  // password related fields are usually handled separately (e.g. reset password) or on creation
  // For simplicity in this CRUD, we might include password on add, but not usually on edit.
  password: '' // Only for add mode
});

const rules = {
  studentId: [{ required: true, message: '学号不能为空', trigger: 'blur' }],
  name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
  gender: [{ required: true, message: '性别不能为空', trigger: 'change' }],
  department: [{ required: true, message: '院系不能为空', trigger: 'blur' }],
  major: [{ required: true, message: '专业不能为空', trigger: 'blur' }],
  className: [{ required: true, message: '班级不能为空', trigger: 'blur' }],
  enrollmentYear: [
    { required: true, message: '入学年份不能为空', trigger: 'blur' },
    { type: 'number', message: '入学年份必须是数字', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value && (value < 1900 || value > new Date().getFullYear() + 5)) {
            callback(new Error('请输入一个合理的入学年份'));
        } else {
            callback();
        }
    }, trigger: 'blur'}
  ],
  password: [{ required: false, message: '密码不能为空', trigger: 'blur' }] // Password optional on edit, required on add set dynamically
};

const studentFormRef = ref(null);

const fetchStudents = async () => {
  loading.value = true;
  try {
    const response = await getAllStudents();
    if (response && response.code === 1) {
      students.value = (response.data || []).map(student => ({
        ...student,
        // Extract enrollment year from student ID (first 4 digits)
        enrollmentYear: student.studentId && student.studentId.length >= 4 ? 
          parseInt(student.studentId.substring(0, 4)) : null
      }));
    } else {
      ElMessage.error(response.msg || '获取学生列表失败');
    }
  } catch (error) {
    console.error("Error fetching students:", error);
    ElMessage.error('获取学生列表出错: ' + (error.message || '网络错误'));
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchStudents();
});

const resetForm = () => {
  if (studentFormRef.value) {
    studentFormRef.value.resetFields();
  }
  studentForm.studentId = ''; // Explicitly clear if not covered by resetFields
  studentForm.password = '';
};

const handleAdd = () => {
  resetForm();
  dialogMode.value = 'add';
  rules.password[0].required = true; // Password required for new students
  currentStudent.value = null;
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  resetForm();
  dialogMode.value = 'edit';
  rules.password[0].required = false; // Password not required for editing student details
  currentStudent.value = { ...row };
  Object.assign(studentForm, row);
  studentForm.password = ''; // Clear password field for edit mode
  dialogVisible.value = true;
};

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除学生 ${row.name} (学号: ${row.studentId}) 吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );
    loading.value = true;
    const response = await deleteStudent(row.studentId);
    if (response && response.code === 1) {
      ElMessage.success('学生删除成功');
      fetchStudents(); // Refresh list
    } else {
      ElMessage.error(response.msg || '删除失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error("Error deleting student:", error);
      ElMessage.error('删除出错: ' + (error.message || '网络错误'));
    }
  } finally {
    loading.value = false;
  }
};

const handleSubmit = async () => {
  if (!studentFormRef.value) return;
  await studentFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        let response;
        const payload = { ...studentForm };
        if (dialogMode.value === 'add') {
          response = await addStudent(payload);
        } else {
          // For update, do not send password if it's empty
          if (!payload.password) {
            delete payload.password;
          }
          response = await updateStudent(currentStudent.value.studentId, payload);
        }

        if (response && response.code === 1) {
          ElMessage.success(dialogMode.value === 'add' ? '学生添加成功' : '学生信息更新成功');
          dialogVisible.value = false;
          fetchStudents(); // Refresh list
        } else {
          ElMessage.error(response.msg || (dialogMode.value === 'add' ? '添加失败' : '更新失败'));
        }
      } catch (error) {
        console.error("Error submitting student form:", error);
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

</script>

<template>
  <div class="student-management-admin">
    <div class="page-header">
      <h2>学生用户管理</h2>
      <el-button type="primary" @click="handleAdd">新增学生</el-button>
    </div>

    <el-table :data="students" v-loading="loading" border stripe empty-text="暂无学生数据" style="width: 100%">
      <el-table-column prop="studentId" label="学号" width="120" sortable align="center" />
      <el-table-column prop="name" label="姓名" width="100" align="center" />
      <el-table-column prop="gender" label="性别" width="70" align="center" />
      <el-table-column prop="enrollmentYear" label="入学年份" width="100" sortable align="center" />
      <el-table-column prop="department" label="院系" width="150" align="center" />
      <el-table-column prop="major" label="专业" width="150" align="center" />
      <el-table-column prop="className" label="班级" width="120" align="center" />
      <el-table-column prop="email" label="邮箱" min-width="180" align="center" />
      <el-table-column prop="phone" label="手机号" width="130" align="center" />
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="warning" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'add' ? '新增学生' : '编辑学生信息'"
      width="600px"
      @close="resetForm"
      draggable
    >
      <el-form :model="studentForm" :rules="rules" ref="studentFormRef" label-width="100px">
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="studentForm.studentId" :disabled="dialogMode === 'edit'" placeholder="请输入学号" />
          <div class="form-tip" v-if="dialogMode === 'add'">注意：前四位数字将被识别为入学年份</div>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="studentForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="初始密码" prop="password" v-if="dialogMode === 'add'">
          <el-input v-model="studentForm.password" type="password" placeholder="为空则使用默认密码" show-password/>
        </el-form-item>
        <el-alert title='编辑模式下如需重置密码，请使用"重置密码"功能。此处不提供密码修改。' type="info" show-icon :closable="false" v-if="dialogMode === 'edit'" style="margin-bottom:15px;"/>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="studentForm.gender" placeholder="请选择性别" style="width: 100%;">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="院系" prop="department">
          <el-input v-model="studentForm.department" placeholder="请输入所属院系" />
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="studentForm.major" placeholder="请输入专业" />
        </el-form-item>
        <el-form-item label="班级" prop="className">
          <el-input v-model="studentForm.className" placeholder="请输入班级" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="studentForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="studentForm.phone" placeholder="请输入手机号" />
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

<style scoped>
.student-management-admin {
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

.form-tip {
  color: #909399;
  font-size: 12px;
  line-height: 1.4;
  margin-top: 4px;
}
</style>