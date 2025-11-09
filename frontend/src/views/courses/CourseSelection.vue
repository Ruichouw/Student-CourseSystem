<script setup>
import { ref, onMounted, computed } from "vue";
import { getAllCourses } from "@/api/course";
import { selectCourse, getSelectionsByStudentId } from "@/api/courseSelection";
import { useUserStore } from "@/stores/user";
import { ElMessage, ElMessageBox } from "element-plus";

const userStore = useUserStore();
const user = computed(() => userStore.getUser);

const courses = ref([]);
const selectedCourses = ref([]);
const loading = ref(false);
const selectLoading = ref(false);
const searchQuery = ref("");

// 获取学生已选课程
const fetchSelectedCourses = async () => {
  if (!user.value || !user.value.studentId) return;
  try {
    const res = await getSelectionsByStudentId(user.value.studentId);
    if (res.code === 1) {
      selectedCourses.value = res.data || [];
    } else {
      ElMessage.error(res.msg || "获取已选课程失败");
    }
  } catch (error) {
    console.error("Error fetching selected courses:", error);
    ElMessage.error("获取已选课程失败");
  }
};

// 获取所有课程并标记已选状态
const fetchCourses = async () => {
  loading.value = true;
  try {
    const res = await getAllCourses();
    if (res.code === 1) {
      const allCourses = res.data || [];

      // 获取已选课程
      await fetchSelectedCourses();
      const selectedIds = selectedCourses.value.map((c) => c.courseId);

      // 合并标志位
      courses.value = allCourses.map((course) => ({
        ...course,
        isSelected: selectedIds.includes(course.courseId),
      }));
    } else {
      ElMessage.error(res.msg || "获取课程列表失败");
    }
  } catch (error) {
    console.error("Error fetching courses:", error);
    ElMessage.error("获取课程列表失败");
  } finally {
    loading.value = false;
  }
};

// 页面加载时获取课程数据
onMounted(() => {
  fetchCourses();
});

// 搜索过滤课程
const filteredCourses = computed(() => {
  if (!searchQuery.value) return courses.value;
  const query = searchQuery.value.toLowerCase();
  return courses.value.filter(
    (course) =>
      course.courseId.toLowerCase().includes(query) ||
      course.name.toLowerCase().includes(query) ||
      (course.teacherName && course.teacherName.toLowerCase().includes(query))
  );
});

// 格式化选课人数显示
const formatAvailability = (course) => {
  const available = course.maxStudents - course.currentStudents;
  const percentage = Math.round(
    (course.currentStudents / course.maxStudents) * 100
  );
  return `${course.currentStudents}/${course.maxStudents} (${percentage}%)`;
};

// 判断课程是否可选
const isAvailable = (course) => {
  return course.currentStudents < course.maxStudents;
};

// 选课逻辑
const handleSelect = async (course) => {
  if (!user.value || !user.value.studentId) {
    ElMessage.warning("请先登录");
    return;
  }

  if (course.isSelected) {
    ElMessage.info("您已选择该课程");
    return;
  }

  if (course.currentStudents >= course.maxStudents) {
    ElMessage.warning("该课程已满");
    return;
  }

  try {
    await ElMessageBox.confirm(
      `确定要选择 ${course.name} 课程吗？`,
      "选课确认",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    );

    selectLoading.value = true;
    const res = await selectCourse(user.value.studentId, course.courseId);

    if (res.code === 1) {
      ElMessage.success("选课成功");
      await fetchCourses(); // 刷新课程状态
    } else {
      ElMessage.error(res.msg || "选课失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("Error selecting course:", error);
      ElMessage.error("选课失败");
    }
  } finally {
    selectLoading.value = false;
  }
};
</script>

<template>
  <div class="course-selection-container">
    <div class="course-header">
      <h2>选课中心</h2>
      <el-input
        v-model="searchQuery"
        placeholder="搜索课程"
        prefix-icon="el-icon-search"
        clearable
        style="width: 300px"
      />
    </div>

    <el-card shadow="hover" class="course-card">
      <el-table
        :data="filteredCourses"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="courseId" label="课程编号" width="120" />
        <el-table-column prop="name" label="课程名称" width="180" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="hours" label="学时" width="80" />
        <el-table-column prop="teacherName" label="授课教师" width="120" />
        <el-table-column label="选课人数" width="120">
          <template #default="{ row }">
            {{ formatAvailability(row) }}
          </template>
        </el-table-column>
        <el-table-column label="课程描述" min-width="200">
          <template #default="{ row }">
            <el-tooltip
              class="item"
              effect="dark"
              :content="row.description"
              placement="top-start"
            >
              <div class="ellipsis-text">{{ row.description }}</div>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="!row.isSelected"
              type="primary"
              size="small"
              :disabled="!isAvailable(row)"
              :loading="selectLoading"
              @click="handleSelect(row)"
            >
              选课
            </el-button>

            <el-button v-else type="success" size="small" disabled>
              已选课
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.course-selection-container {
  padding: 20px;
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.course-header h2 {
  margin: 0;
  font-size: 22px;
}
.ellipsis-text {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2; /* 显示两行，多余的省略 */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  line-height: 1.5;
  max-height: 3em;
  cursor: pointer;
}

.course-card {
  margin-bottom: 20px;
}
</style>
