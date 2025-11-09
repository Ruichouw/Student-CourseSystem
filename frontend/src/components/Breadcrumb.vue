<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const breadcrumbs = ref([])

const getBreadcrumbs = () => {
  // 重置面包屑数组
  breadcrumbs.value = []
  
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  
  // 如果不是在首页，添加首页链接
  if (route.path !== '/home' && route.path !== '/courses') {
    breadcrumbs.value.push({
      path: '/home',
      meta: { title: '首页' }
    })
  }
  
  // 添加当前路径的匹配项
  breadcrumbs.value = [...breadcrumbs.value, ...matched]
}

watch(() => route.path, getBreadcrumbs, { immediate: true })

const handleLink = (item) => {
  router.push(item.path)
}
</script>

<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item v-for="(item, index) in breadcrumbs" :key="index">
      <span v-if="index === breadcrumbs.length - 1" class="no-redirect">
        {{ item.meta.title }}
      </span>
      <a v-else @click.prevent="handleLink(item)">
        {{ item.meta.title }}
      </a>
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<style scoped>
.no-redirect {
  color: #97a8be;
  cursor: text;
}
</style> 