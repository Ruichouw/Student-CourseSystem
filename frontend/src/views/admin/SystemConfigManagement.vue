<template>
  <div class="system-config-admin">
    <div class="page-header">
      <h2>系统配置管理</h2>
    </div>
    <el-card class="config-card" v-loading="loading">
      <el-form :model="configForm" :rules="rules" ref="configFormRef" label-width="150px" :disabled="!isEditing">
        <el-form-item label="选课开始时间" prop="courseSelectionStartTime">
          <el-date-picker
            v-model="configForm.courseSelectionStartTime"
            type="datetime"
            placeholder="选择选课开始日期时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="选课结束时间" prop="courseSelectionEndTime">
          <el-date-picker
            v-model="configForm.courseSelectionEndTime"
            type="datetime"
            placeholder="选择选课结束日期时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%;"
          />
        </el-form-item>
        
        <!-- Add other system configurations here as needed -->

      </el-form>
      <div class="form-actions">
        <el-button v-if="!isEditing" type="primary" @click="handleEdit">编辑配置</el-button>
        <template v-if="isEditing">
          <el-button type="success" @click="handleSubmit" :loading="loading">保存更改</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </template>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { getSystemConfig, updateSystemConfig } from '@/api/admin';
import { ElMessage } from 'element-plus';

const configForm = reactive({
  id: null, // Assuming a single row with a fixed ID or the backend handles it
  courseSelectionStartTime: '',
  courseSelectionEndTime: ''
});
const originalConfigForm = reactive({}); // To store original values for reset

const loading = ref(false);
const isEditing = ref(false);
const configFormRef = ref(null);

const rules = {
  courseSelectionStartTime: [
    { required: true, message: '请选择选课开始时间', trigger: 'change' }
  ],
  courseSelectionEndTime: [
    { required: true, message: '请选择选课结束时间', trigger: 'change' },
    {
      validator: (rule, value, callback) => {
        if (value && configForm.courseSelectionStartTime && new Date(value) <= new Date(configForm.courseSelectionStartTime)) {
          callback(new Error('选课结束时间必须晚于开始时间'));
        } else {
          callback();
        }
      },
      trigger: 'change'
    }
  ]
};

const fetchConfig = async () => {
  loading.value = true;
  try {
    const response = await getSystemConfig();
    if (response && response.code === 1 && response.data) {
      Object.assign(configForm, response.data);
      Object.assign(originalConfigForm, response.data); 
    } else {
      ElMessage.error(response.msg || '获取系统配置失败');
      // Initialize with defaults if fetch fails or no data, so form is usable
      configForm.courseSelectionStartTime = '';
      configForm.courseSelectionEndTime = '';
    }
  } catch (error) {
    console.error("Error fetching system config:", error);
    ElMessage.error('获取系统配置出错: ' + (error.message || '网络错误'));
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchConfig();
});

const handleEdit = () => {
  isEditing.value = true;
};

const handleCancel = () => {
  Object.assign(configForm, originalConfigForm);
  isEditing.value = false;
  if (configFormRef.value) {
    configFormRef.value.clearValidate(); // Clear validation messages
  }
};

const handleSubmit = async () => {
  if (!configFormRef.value) return;
  await configFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        const payload = { ...configForm };
        // Ensure dates are in a consistent format if necessary, e.g., ISO string
        // payload.courseSelectionStartTime = new Date(payload.courseSelectionStartTime).toISOString();
        // payload.courseSelectionEndTime = new Date(payload.courseSelectionEndTime).toISOString();
        
        const response = await updateSystemConfig(payload);
        if (response && response.code === 1) {
          ElMessage.success('系统配置更新成功');
          Object.assign(originalConfigForm, payload); // Update original with saved data
          isEditing.value = false;
        } else {
          ElMessage.error(response.msg || '配置更新失败');
        }
      } catch (error) {
        console.error("Error updating system config:", error);
        ElMessage.error('配置更新出错: ' + (error.message || '网络错误'));
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
.system-config-admin {
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
.config-card {
  max-width: 700px;
  margin: 0 auto;
}
.form-actions {
  text-align: right;
  margin-top: 20px;
}
</style> 