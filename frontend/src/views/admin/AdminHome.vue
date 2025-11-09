<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import Breadcrumb from '@/components/Breadcrumb.vue'

const router = useRouter()
const userStore = useUserStore()

const user = computed(() => userStore.getUser)

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

const isCollapse = ref(false)
const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

const activeMenu = computed(() => {
  return router.currentRoute.value.path
})
</script>

<template>
  <el-container class="layout-container">
    <!-- Sidebar -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar-container">
      <div class="logo-container">
        <h3 v-if="!isCollapse">管理后台</h3>
        <h3 v-else>管</h3>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/admin/students">
          <el-icon><el-icon-avatar /></el-icon>
          <template #title>学生管理</template>
        </el-menu-item>

        <el-menu-item index="/admin/teachers">
          <el-icon><el-icon-school /></el-icon>
          <template #title>教师管理</template>
        </el-menu-item>
        
        <el-menu-item index="/admin/courses">
          <el-icon><el-icon-collection /></el-icon>
          <template #title>课程管理</template>
        </el-menu-item>

        <el-menu-item index="/admin/course-approval">
          <el-icon><el-icon-finished /></el-icon>
          <template #title>课程审核</template>
        </el-menu-item>

        <el-menu-item index="/admin/system-config">
          <el-icon><el-icon-setting /></el-icon>
          <template #title>系统配置</template>
        </el-menu-item>

        <el-menu-item index="/admin/statistics">
          <el-icon><el-icon-pie-chart /></el-icon>
          <template #title>系统统计</template>
        </el-menu-item>
        
        <el-menu-item index="/admin/profile">
          <el-icon><el-icon-user /></el-icon>
          <template #title>个人信息</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <!-- Main Content -->
    <el-container>
      <!-- Header -->
      <el-header class="header-container">
        <div class="header-left">
          <el-icon class="toggle-icon" @click="toggleSidebar">
            <el-icon-menu />
          </el-icon>
          <breadcrumb />
        </div>
        
        <div class="header-right">
          <el-dropdown trigger="click">
            <div class="user-info">
              <span>{{ user?.name || '管理员' }}</span>
              <el-icon><el-icon-arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/admin/profile')">
                  个人信息
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <!-- Main Content -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar-container {
  background-color: #304156;
  transition: width 0.3s;
  overflow-x: hidden;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3649;
}

.logo-container h3 {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  white-space: nowrap;
}

.header-container {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.toggle-icon {
  font-size: 20px;
  cursor: pointer;
  margin-right: 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-info span {
  margin-right: 8px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>