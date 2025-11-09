<script setup>
import { ref, onMounted, computed, reactive } from 'vue';
import { useUserStore } from '@/stores/user';
import { getAdminProfile, updateAdminProfile } from '@/api/admin';
import { ElMessage } from 'element-plus';

const userStore = useUserStore();
const user = computed(() => userStore.getUser);

const adminForm = reactive({
  adminId: '',
  adminName: '',
  // Add other admin-specific fields if any, e.g., role, contact
  // For now, keeping it simple with name. Password changes are typically separate.
});
const originalAdminForm = reactive({});
const loading = ref(false);
const isEditing = ref(false);
const adminFormRef = ref(null);

// Basic rules, can be expanded
const rules = {
  adminName: [
    { required: true, message: '管理员名称不能为空', trigger: 'blur' }
  ]
};

const fetchAdminProfile = async () => {
  if (!user.value || !user.value.adminId) {
    ElMessage.error('无法获取管理员ID，请重新登录');
    return;
  }
  loading.value = true;
  try {
    const response = await getAdminProfile(user.value.adminId);
    if (response && response.code === 1 && response.data) {
      Object.assign(adminForm, response.data);
      Object.assign(originalAdminForm, response.data);
    } else {
      ElMessage.error(response.msg || '获取管理员信息失败');
    }
  } catch (error) {
    console.error("Error fetching admin profile:", error);
    ElMessage.error('获取管理员信息出错: ' + (error.message || '请检查网络'));
  } finally {
    loading.value = false;
  }
};

const handleEdit = () => {
  isEditing.value = true;
};

const handleCancel = () => {
  Object.assign(adminForm, originalAdminForm);
  isEditing.value = false;
  if(adminFormRef.value) adminFormRef.value.clearValidate();
};

const handleSubmit = async () => {
  if (!adminFormRef.value) return;
  await adminFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        const payload = { ...adminForm };
        // The API updateAdminProfile likely takes the whole admin object.
        // Ensure payload matches what API expects, especially ID.
        // payload.adminId = user.value.adminId; // Already in adminForm if fetched correctly

        const response = await updateAdminProfile(payload);
        if (response && response.code === 1) {
          ElMessage.success('个人信息更新成功');
          Object.assign(originalAdminForm, payload);
          isEditing.value = false;
          // Update user store if name changed, etc.
          if (userStore.user && payload.adminName !== userStore.user.name) {
             userStore.setUser({ ...user.value, name: payload.adminName, username: payload.adminName });
          }
        } else {
          ElMessage.error(response.msg || '信息更新失败');
        }
      } catch (error) {
        console.error("Error updating admin profile:", error);
        ElMessage.error('信息更新出错: ' + (error.message || '请检查网络'));
      } finally {
        loading.value = false;
      }
    } else {
      ElMessage.warning('请检查表单输入项');
      return false;
    }
  });
};

let unwatchUser = null;
onMounted(() => {
  if (userStore.isAuthenticated && user.value && user.value.adminId) {
    fetchAdminProfile();
  } else {
    unwatchUser = userStore.$subscribe((mutation, state) => {
      if (state.user && state.user.adminId && state.isAuthenticated) {
        fetchAdminProfile();
        if (unwatchUser) unwatchUser();
      }
    });
    if (!userStore.isAuthenticated) {
        ElMessage.error('用户未登录或登录已过期，无法加载个人信息');
    }
  }
});

import { onBeforeUnmount } from 'vue';
onBeforeUnmount(() => {
  if (unwatchUser) {
    unwatchUser();
  }
});

</script>

<template>
  <div class="profile-management-admin" v-loading="loading">
    <div class="page-header">
      <h2>管理员个人信息</h2>
    </div>
    <el-card class="profile-card">
      <el-form :model="adminForm" :rules="rules" ref="adminFormRef" label-width="100px" :disabled="!isEditing">
        <el-form-item label="管理员ID">
          <el-input v-model="adminForm.adminId" disabled />
        </el-form-item>
        <el-form-item label="用户名/昵称" prop="adminName">
          <el-input v-model="adminForm.adminName" placeholder="请输入用户名或昵称"/>
        </el-form-item>
        
        <!-- Add other admin profile fields here if they exist and are editable -->
        <!-- Example: <el-form-item label="角色" prop="role"><el-input v-model="adminForm.role" /></el-form-item> -->

      </el-form>
      <div class="form-actions">
        <el-button v-if="!isEditing" type="primary" @click="handleEdit">编辑信息</el-button>
        <template v-if="isEditing">
          <el-button type="success" @click="handleSubmit" :loading="loading">保存更改</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </template>
      </div>
       <el-alert 
        title="密码管理提示"
        description='如需修改登录密码，请联系超级管理员或使用系统提供的"忘记密码/修改密码"特定功能（如果支持）。此页面不直接处理密码更改。'
        type="info"
        show-icon 
        :closable="false"
        style="margin-top: 20px;"
       />
    </el-card>
  </div>
</template>

<style scoped>
.profile-management-admin {
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
.profile-card {
  max-width: 600px;
  margin: 0 auto;
}
.form-actions {
  text-align: right;
  margin-top: 20px;
}
</style>