<template>
  <div class="teacher-management-admin">
    <div class="page-header">
      <h2>教师用户管理</h2>
      <el-button type="primary" @click="handleAdd">新增教师</el-button>
    </div>

    <el-table :data="teachers" v-loading="loading" border stripe empty-text="暂无教师数据" style="width: 100%">
      <el-table-column prop="teacherId" label="教师工号" width="120" sortable align="center" />
      <el-table-column prop="name" label="姓名" width="100" align="center" />
      <el-table-column prop="gender" label="性别" width="70" align="center" />
      <el-table-column prop="department" label="院系" width="150" align="center" />
      <el-table-column prop="title" label="职称" width="100" align="center" />
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
      :title="dialogMode === 'add' ? '新增教师' : '编辑教师信息'"
      width="600px"
      @close="resetForm"
      draggable
    >
      <el-form :model="teacherForm" :rules="rules" ref="teacherFormRef" label-width="100px">
        <el-form-item label="教师工号" prop="teacherId">
          <el-input v-model="teacherForm.teacherId" :disabled="dialogMode === 'edit'" placeholder="请输入教师工号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="teacherForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="初始密码" prop="password" v-if="dialogMode === 'add'">
          <el-input v-model="teacherForm.password" type="password" placeholder="为空则使用默认密码" show-password/>
        </el-form-item>
        <el-alert title='编辑模式下如需重置密码，请使用"重置密码"功能。此处不提供密码修改。' type="info" show-icon :closable="false" v-if="dialogMode === 'edit'" style="margin-bottom:15px;"/>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="teacherForm.gender" placeholder="请选择性别" style="width: 100%;">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="院系" prop="department">
          <el-input v-model="teacherForm.department" placeholder="请输入所属院系" />
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="teacherForm.title" placeholder="请输入职称" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="teacherForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="teacherForm.phone" placeholder="请输入手机号" />
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
import { getAllTeachers, addTeacher, updateTeacher, deleteTeacher } from '@/api/admin';

const teachers = ref([]);
const loading = ref(false);
const dialogVisible = ref(false);
const dialogMode = ref('add'); // 'add' or 'edit'
const currentTeacher = ref(null);

const teacherForm = reactive({
  teacherId: '',
  name: '',
  gender: '',
  department: '',
  title: '',      // 职称
  email: '',      // 邮箱
  phone: '',      // 手机号
  password: ''    // Only for add mode
});

const rules = {
  teacherId: [{ required: true, message: '教师工号不能为空', trigger: 'blur' }],
  name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
  gender: [{ required: true, message: '性别不能为空', trigger: 'change' }],
  department: [{ required: true, message: '院系不能为空', trigger: 'blur' }],
  title: [{ required: true, message: '职称不能为空', trigger: 'blur' }],
  email: [
    { required: false, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: false, message: '手机号不能为空', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  password: [{ required: false, message: '密码不能为空', trigger: 'blur' }] // Dynamically set to true for add mode
};

const teacherFormRef = ref(null);

const fetchTeachers = async () => {
  loading.value = true;
  try {
    const response = await getAllTeachers();
    if (response && response.code === 1) {
      teachers.value = response.data || [];
    } else {
      ElMessage.error(response.msg || '获取教师列表失败');
    }
  } catch (error) {
    console.error("Error fetching teachers:", error);
    ElMessage.error('获取教师列表出错: ' + (error.message || '网络错误'));
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchTeachers();
});

const resetForm = () => {
  if (teacherFormRef.value) {
    teacherFormRef.value.resetFields();
  }
  teacherForm.teacherId = '';
  teacherForm.password = '';
};

const handleAdd = () => {
  resetForm();
  dialogMode.value = 'add';
  rules.password[0].required = true;
  currentTeacher.value = null;
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  resetForm();
  dialogMode.value = 'edit';
  rules.password[0].required = false;
  currentTeacher.value = { ...row };
  Object.assign(teacherForm, row);
  teacherForm.password = ''; // Clear password for edit mode
  dialogVisible.value = true;
};

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除教师 ${row.name} (工号: ${row.teacherId}) 吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );
    loading.value = true;
    const response = await deleteTeacher(row.teacherId);
    if (response && response.code === 1) {
      ElMessage.success('教师删除成功');
      fetchTeachers(); // Refresh list
    } else {
      ElMessage.error(response.msg || '删除失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error("Error deleting teacher:", error);
      ElMessage.error('删除出错: ' + (error.message || '网络错误'));
    }
  } finally {
    loading.value = false;
  }
};

const handleSubmit = async () => {
  if (!teacherFormRef.value) return;
  await teacherFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        let response;
        const payload = { ...teacherForm };
        if (dialogMode.value === 'add') {
          response = await addTeacher(payload);
        } else {
          if (!payload.password) {
            delete payload.password;
          }
          response = await updateTeacher(currentTeacher.value.teacherId, payload);
        }

        if (response && response.code === 1) {
          ElMessage.success(dialogMode.value === 'add' ? '教师添加成功' : '教师信息更新成功');
          dialogVisible.value = false;
          fetchTeachers(); // Refresh list
        } else {
          ElMessage.error(response.msg || (dialogMode.value === 'add' ? '添加失败' : '更新失败'));
        }
      } catch (error) {
        console.error("Error submitting teacher form:", error);
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

<style scoped>
.teacher-management-admin {
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