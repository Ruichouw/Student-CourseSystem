# 学生选课管理系统 - 对象图

```plantuml
@startuml

object "student1: Student" as student1 {
  studentId = "2023001"
  name = "张三"
  gender = "男"
  age = 20
  department = "计算机学院"
  major = "软件工程"
  className = "软工2301"
  password = "password"
  email = "zhangsan@example.com"
  phone = "13800000001"
}

object "teacher1: Teacher" as teacher1 {
  teacherId = "T2023001"
  name = "李教授"
  gender = "女"
  department = "计算机学院" 
  title = "副教授"
  password = "password"
  email = "liprofessor@example.com"
  phone = "13900000001"
}

object "course1: Course" as course1 {
  courseId = "C001"
  name = "Java程序设计"
  credit = 4.0
  hours = 64
  teacherId = "T2023001"
  description = "Java编程基础与实践"
  maxStudents = 50
  currentStudents = 2
}

object "course2: Course" as course2 {
  courseId = "C002"
  name = "数据库原理"
  credit = 3.5
  hours = 56
  teacherId = "T2023001"
  description = "数据库设计与应用"
  maxStudents = 40
  currentStudents = 1
}

object "selection1: CourseSelection" as selection1 {
  id = 1
  studentId = "2023001"
  courseId = "C001"
  selectionTime = "2023-09-01 10:30:00"
  grade = 85.5
  studentName = "张三"
  courseName = "Java程序设计"
}

object "selection2: CourseSelection" as selection2 {
  id = 2
  studentId = "2023001"
  courseId = "C002"
  selectionTime = "2023-09-01 11:20:00"
  grade = null
  studentName = "张三"
  courseName = "数据库原理"
}

object "admin1: Admin" as admin1 {
  adminId = "A001"
  name = "管理员"
  password = "admin123"
}

student1 --> selection1
student1 --> selection2

course1 --> selection1
course2 --> selection2

teacher1 --> course1
teacher1 --> course2

@enduml
```

## 对象图说明

这个对象图展示了系统中的具体实例及其关系：

1. **学生对象**：
   - 张三（student1）是一名软件工程专业的学生

2. **教师对象**：
   - 李教授（teacher1）是计算机学院的一名副教授

3. **课程对象**：
   - Java程序设计（course1）由李教授教授，学分为4.0，课时为64
   - 数据库原理（course2）也由李教授教授，学分为3.5，课时为56

4. **选课记录对象**：
   - 张三选了Java程序设计课程，并获得了85.5分
   - 张三也选了数据库原理课程，但尚未有成绩

5. **管理员对象**：
   - 系统中有一个管理员账号

该对象图展示了一个具体的选课场景，表明学生如何选择课程、教师如何教授课程以及选课后成绩记录的情况。 