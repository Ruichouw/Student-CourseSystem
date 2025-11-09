<template>
  <div class="statistics-admin">
    <div class="page-header">
      <h2>系统数据统计</h2>
    </div>
    <div v-if="loading && !stats" class="loading-placeholder">数据加载中...</div>
    <div v-if="!loading && !stats" class="loading-placeholder">无法加载统计数据。</div>
    
    <div v-if="stats" class="stats-container">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-title">学生总数</div>
            <div class="stat-value">{{ stats.totalStudents || 0 }}</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-title">教师总数</div>
            <div class="stat-value">{{ stats.totalTeachers || 0 }}</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-title">课程总数</div>
            <div class="stat-value">{{ stats.totalCourses || 0 }}</div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <el-card>
            <div ref="departmentDistributionChart" style="width: 100%; height: 400px;"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div ref="popularCoursesChart" style="width: 100%; height: 400px;"></div>
          </el-card>
        </el-col>
      </el-row>
       <el-row :gutter="20" style="margin-top: 20px;">
         <el-col :span="24">
            <el-card v-if="stats.courseStatusCounts">
                <template #header>
                    <div>课程状态分布</div>
                </template>
                <el-descriptions :column="3" border>
                    <el-descriptions-item label="待审批 (Pending)">{{ stats.courseStatusCounts.PENDING_APPROVAL || 0 }}</el-descriptions-item>
                    <el-descriptions-item label="已批准 (Approved)">{{ stats.courseStatusCounts.APPROVED || 0 }}</el-descriptions-item>
                    <el-descriptions-item label="已拒绝 (Rejected)">{{ stats.courseStatusCounts.REJECTED || 0 }}</el-descriptions-item>
                </el-descriptions>
            </el-card>
         </el-col>
       </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getSystemStatistics } from '@/api/admin';
import { ElMessage, ElLoading } from 'element-plus';
import * as echarts from 'echarts/core';
import { BarChart, PieChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent, GridComponent, LegendComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([TitleComponent, TooltipComponent, GridComponent, LegendComponent, BarChart, PieChart, CanvasRenderer]);

const stats = ref(null);
const loading = ref(false);

const departmentDistributionChart = ref(null);
const popularCoursesChart = ref(null);

const fetchStatistics = async () => {
  loading.value = true;
  const loadingInstance = ElLoading.service({
    lock: true,
    text: '加载统计数据中...',
    background: 'rgba(0, 0, 0, 0.7)',
  });
  try {
    const response = await getSystemStatistics();
    if (response && response.code === 1) {
      stats.value = response.data;
      // Wait for next tick to ensure DOM elements are available for ECharts
      await new Promise(resolve => setTimeout(resolve, 0)); 
      renderCharts();
    } else {
      ElMessage.error(response.msg || '获取统计数据失败');
    }
  } catch (error) {
    console.error("Error fetching statistics:", error);
    ElMessage.error('获取统计数据出错: ' + (error.message || '网络错误'));
  } finally {
    loading.value = false;
    loadingInstance.close();
  }
};

const renderCharts = () => {
  if (!stats.value) return;

  // Department Distribution Chart
  if (stats.value.departmentDistribution && departmentDistributionChart.value) {
    const deptChartInstance = echarts.init(departmentDistributionChart.value);
    const deptData = stats.value.departmentDistribution.map(item => ({
      name: item.department,
      value: item.count
    }));
    deptChartInstance.setOption({
      title: {
        text: '学生院系分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c}人 ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: deptData.map(item => item.name)
      },
      series: [
        {
          name: '院系分布',
          type: 'pie',
          radius: '50%',
          data: deptData,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    });
  }

  // Popular Courses Chart
  if (stats.value.popularCourses && popularCoursesChart.value) {
    const popularChartInstance = echarts.init(popularCoursesChart.value);
    const popularData = stats.value.popularCourses; // Expects { courseName, selectedCount }
    popularChartInstance.setOption({
      title: {
        text: '热门课程排行 (Top 10)',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: popularData.map(item => item.courseName),
        axisLabel: { interval: 0, rotate: 30 }
      },
      yAxis: {
        type: 'value',
        name: '选课人数'
      },
      series: [
        {
          name: '选课人数',
          type: 'bar',
          data: popularData.map(item => item.selectedCount),
          barWidth: '60%'
        }
      ],
      grid: { bottom: 100 } // Add some padding at the bottom for rotated labels
    });
  }
};

onMounted(() => {
  fetchStatistics();
});

</script>

<style scoped>
.statistics-admin {
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
.loading-placeholder {
    text-align: center;
    padding: 50px;
    font-size: 18px;
    color: #909399;
}
.stats-container {
  /* Add styles if needed */
}
.stat-card {
  text-align: center;
}
.stat-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
</style> 