# 学生选课管理系统 - 时序图

## 1. 学生选课流程时序图

```plantuml
@startuml
actor 学生 as Student
boundary "选课页面" as SelectionPage
control "选课控制器" as CourseController
entity "课程服务" as CourseService
entity "选课服务" as SelectionService
database "数据库" as DB

Student -> SelectionPage: 1. 访问选课页面
activate SelectionPage

SelectionPage -> CourseController: 2. 请求可选课程列表
activate CourseController

CourseController -> CourseService: 3. 查询可选课程
activate CourseService

CourseService -> DB: 4. 查询课程数据
activate DB
DB --> CourseService: 5. 返回课程数据
deactivate DB

CourseService --> CourseController: 6. 返回课程列表
deactivate CourseService

CourseController --> SelectionPage: 7. 显示可选课程
deactivate CourseController

SelectionPage --> Student: 8. 展示可选课程列表

Student -> SelectionPage: 9. 选择课程并点击选课
activate SelectionPage

SelectionPage -> CourseController: 10. 提交选课请求
activate CourseController

CourseController -> CourseService: 11. 检查课程是否可选
activate CourseService

CourseService -> DB: 12. 查询课程容量信息
activate DB
DB --> CourseService: 13. 返回课程容量信息
deactivate DB

CourseService --> CourseController: 14. 返回课程可选状态
deactivate CourseService

alt 课程已满
    CourseController --> SelectionPage: 15a. 返回选课失败：课程已满
    SelectionPage --> Student: 16a. 提示选课失败
else 存在时间冲突
    CourseController -> SelectionService: 15b. 检查时间冲突
    activate SelectionService
    SelectionService -> DB: 查询学生已选课程
    activate DB
    DB --> SelectionService: 返回已选课程
    deactivate DB
    SelectionService --> CourseController: 返回冲突结果
    deactivate SelectionService
    CourseController --> SelectionPage: 16b. 返回选课失败：时间冲突
    SelectionPage --> Student: 17b. 提示选课失败
else 选课成功
    CourseController -> SelectionService: 15c. 创建选课记录
    activate SelectionService
    
    SelectionService -> DB: 16c. 写入选课记录
    activate DB
    DB --> SelectionService: 17c. 写入成功
    deactivate DB
    
    SelectionService -> CourseService: 18c. 更新课程选课人数
    activate CourseService
    CourseService -> DB: 19c. 更新课程信息
    activate DB
    DB --> CourseService: 20c. 更新成功
    deactivate DB
    CourseService --> SelectionService: 21c. 更新完成
    deactivate CourseService
    
    SelectionService --> CourseController: 22c. 选课成功
    deactivate SelectionService
    
    CourseController --> SelectionPage: 23c. 返回选课成功
    SelectionPage --> Student: 24c. 提示选课成功
end

deactivate CourseController
deactivate SelectionPage

@enduml
```

## 2. 教师录入成绩流程时序图

```plantuml
@startuml
actor 教师 as Teacher
boundary "成绩管理页面" as GradePage
control "成绩控制器" as GradeController
entity "课程服务" as CourseService
entity "成绩服务" as GradeService
database "数据库" as DB

Teacher -> GradePage: 1. 访问成绩管理页面
activate GradePage

GradePage -> GradeController: 2. 请求教师课程列表
activate GradeController

GradeController -> CourseService: 3. 查询教师课程
activate CourseService

CourseService -> DB: 4. 查询课程数据
activate DB
DB --> CourseService: 5. 返回课程数据
deactivate DB

CourseService --> GradeController: 6. 返回课程列表
deactivate CourseService

GradeController --> GradePage: 7. 显示课程列表
deactivate GradeController

GradePage --> Teacher: 8. 展示课程列表

Teacher -> GradePage: 9. 选择课程
activate GradePage

GradePage -> GradeController: 10. 请求选课学生列表
activate GradeController

GradeController -> GradeService: 11. 查询选课学生
activate GradeService

GradeService -> DB: 12. 查询选课记录
activate DB
DB --> GradeService: 13. 返回选课记录
deactivate DB

GradeService --> GradeController: 14. 返回学生列表
deactivate GradeService

GradeController --> GradePage: 15. 显示学生列表
deactivate GradeController

GradePage --> Teacher: 16. 展示学生列表

Teacher -> GradePage: 17. 输入学生成绩
activate GradePage

loop 对每个学生
    Teacher -> GradePage: 18. 输入成绩
    GradePage -> GradeController: 19. 提交成绩
    activate GradeController
    
    GradeController -> GradeService: 20. 保存成绩
    activate GradeService
    
    GradeService -> DB: 21. 更新成绩数据
    activate DB
    DB --> GradeService: 22. 更新成功
    deactivate DB
    
    GradeService --> GradeController: 23. 保存成功
    deactivate GradeService
    
    GradeController --> GradePage: 24. 返回保存结果
    deactivate GradeController
    
    GradePage --> Teacher: 25. 显示保存成功提示
end

deactivate GradePage

@enduml
```

## 3. 学生退课流程时序图

```plantuml
@startuml
actor 学生 as Student
boundary "已选课程页面" as CoursesPage
control "选课控制器" as CourseController
entity "选课服务" as SelectionService
entity "课程服务" as CourseService
database "数据库" as DB

Student -> CoursesPage: 1. 访问已选课程页面
activate CoursesPage

CoursesPage -> CourseController: 2. 请求已选课程
activate CourseController

CourseController -> SelectionService: 3. 查询学生选课记录
activate SelectionService

SelectionService -> DB: 4. 查询数据库
activate DB
DB --> SelectionService: 5. 返回选课数据
deactivate DB

SelectionService --> CourseController: 6. 返回课程列表
deactivate SelectionService

CourseController --> CoursesPage: 7. 显示已选课程
deactivate CourseController

CoursesPage --> Student: 8. 展示已选课程

Student -> CoursesPage: 9. 点击退课按钮
activate CoursesPage

CoursesPage -> CourseController: 10. 发送退课请求
activate CourseController

CourseController -> SelectionService: 11. 检查是否可退课
activate SelectionService

SelectionService -> DB: 12. 查询退课期限设置
activate DB
DB --> SelectionService: 13. 返回系统设置
deactivate DB

alt 不在退课期限内
    SelectionService --> CourseController: 14a. 退课失败：超出期限
    CourseController --> CoursesPage: 15a. 返回退课失败
    CoursesPage --> Student: 16a. 提示退课失败
else 可以退课
    SelectionService -> DB: 14b. 删除选课记录
    activate DB
    DB --> SelectionService: 15b. 删除成功
    deactivate DB
    
    SelectionService -> CourseService: 16b. 更新课程人数
    activate CourseService
    CourseService -> DB: 17b. 减少选课人数
    activate DB
    DB --> CourseService: 18b. 更新成功
    deactivate DB
    CourseService --> SelectionService: 19b. 更新完成
    deactivate CourseService
    
    SelectionService --> CourseController: 20b. 退课成功
    deactivate SelectionService
    
    CourseController --> CoursesPage: 21b. 返回退课成功
    CoursesPage --> Student: 22b. 提示退课成功并更新页面
end

deactivate CourseController
deactivate CoursesPage

@enduml
```

## 时序图说明

### 学生选课流程

1. **发起请求阶段**:
   - 学生访问选课页面，系统通过控制器访问课程服务
   - 从数据库获取可选课程数据并显示给学生

2. **选课操作阶段**:
   - 学生选择课程并提交选课请求
   - 系统先检查课程容量是否已满
   - 然后检查是否存在时间冲突
   - 如满足选课条件，则创建选课记录

3. **结果处理阶段**:
   - 更新课程选课人数
   - 返回选课结果给学生（成功或失败原因）

### 教师录入成绩流程

1. **课程选择阶段**:
   - 教师访问成绩管理页面，获取自己教授的课程列表
   - 教师选择需要录入成绩的课程

2. **学生列表获取**:
   - 系统查询选择该课程的学生列表
   - 显示学生信息和已有成绩（如果有）

3. **成绩录入阶段**:
   - 教师为每个学生输入成绩
   - 系统逐个保存成绩数据
   - 显示保存结果反馈

### 学生退课流程

1. **查看已选课程**:
   - 学生访问已选课程页面
   - 系统查询并显示学生已选课程列表

2. **退课操作**:
   - 学生点击退课按钮
   - 系统检查是否在退课期限内

3. **处理退课**:
   - 如果在期限内，删除选课记录
   - 更新课程选课人数
   - 返回退课结果给学生 