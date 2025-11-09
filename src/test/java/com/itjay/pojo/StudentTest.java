package com.itjay.pojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void getStudentId() {
        Student student = new Student();
        student.setStudentId("stu001");
        assertEquals("stu001", student.getStudentId());
    }

    @Test
    void setStudentId() {
        Student student = new Student();
        student.setStudentId("stu001");
        assertNotEquals("stu002", student.getStudentId());
    }

    @Test
    void getName() {
        Student student = new Student();
        student.setName("张三");
        assertEquals("张三", student.getName());
    }

    @Test
    void setName() {
        Student student = new Student();
        student.setName("张三");
        assertNotEquals("李四", student.getName());
    }

    @Test
    void getGender() {
        Student student = new Student();
        student.setGender("男");
        assertEquals("男", student.getGender());
    }

    @Test
    void setGender() {
        Student student = new Student();
        student.setGender("男");
        assertNotEquals("女", student.getGender());
    }

    @Test
    void getAge() {
        Student student = new Student();
        student.setAge(20);
        assertEquals(20, student.getAge());
    }

    @Test
    void setAge() {
        Student student = new Student();
        student.setAge(20);
        assertNotEquals(21, student.getAge());
    }

    @Test
    void getDepartment() {
        Student student = new Student();
        student.setDepartment("计算机学院");
        assertEquals("计算机学院", student.getDepartment());
    }

    @Test
    void setDepartment() {
        Student student = new Student();
        student.setDepartment("计算机学院");
        assertNotEquals("数学学院", student.getDepartment());
    }

    @Test
    void getMajor() {
        Student student = new Student();
        student.setMajor("软件工程");
        assertEquals("软件工程", student.getMajor());
    }

    @Test
    void setMajor() {
        Student student = new Student();
        student.setMajor("软件工程");
        assertNotEquals("计算机科学", student.getMajor());
    }

    @Test
    void getClassName() {
        Student student = new Student();
        student.setClassName("软工1班");
        assertEquals("软工1班", student.getClassName());
    }

    @Test
    void setClassName() {
        Student student = new Student();
        student.setClassName("软工1班");
        assertNotEquals("软工2班", student.getClassName());
    }

    @Test
    void getPassword() {
        Student student = new Student();
        student.setPassword("123456");
        assertEquals("123456", student.getPassword());
    }

    @Test
    void setPassword() {
        Student student = new Student();
        student.setPassword("123456");
        assertNotEquals("654321", student.getPassword());
    }

    @Test
    void getEmail() {
        Student student = new Student();
        student.setEmail("zhangsan@example.com");
        assertEquals("zhangsan@example.com", student.getEmail());
    }

    @Test
    void setEmail() {
        Student student = new Student();
        student.setEmail("zhangsan@example.com");
        assertNotEquals("lisi@example.com", student.getEmail());
    }

    @Test
    void getPhone() {
        Student student = new Student();
        student.setPhone("13800138000");
        assertEquals("13800138000", student.getPhone());
    }

    @Test
    void setPhone() {
        Student student = new Student();
        student.setPhone("13800138000");
        assertNotEquals("13900139000", student.getPhone());
    }

    @Test
    void testEquals() {
        Student student1 = new Student();
        student1.setStudentId("stu001");
        student1.setName("张三");

        Student student2 = new Student();
        student2.setStudentId("stu001");
        student2.setName("张三");

        Student student3 = new Student();
        student3.setStudentId("stu002");
        student3.setName("李四");

        assertEquals(student1, student2);
        assertNotEquals(student1, student3);
        assertNotEquals(null, student1);
        assertNotEquals(new Object(), student1);
    }

    @Test
    void canEqual() {
        Student student1 = new Student();
        student1.setStudentId("stu001");

        Student student2 = new Student();
        student2.setStudentId("stu001");

        assertTrue(student1.canEqual(student2));
        assertTrue(student2.canEqual(student1));
    }

    @Test
    void testHashCode() {
        Student student1 = new Student();
        student1.setStudentId("stu001");
        student1.setName("张三");

        Student student2 = new Student();
        student2.setStudentId("stu001");
        student2.setName("张三");

        Student student3 = new Student();
        student3.setStudentId("stu002");
        student3.setName("李四");

        assertEquals(student1.hashCode(), student2.hashCode());
        assertNotEquals(student1.hashCode(), student3.hashCode());
    }

    @Test
    void testToString() {
        Student student = new Student();
        student.setStudentId("stu001");
        student.setName("张三");
        student.setDepartment("计算机学院");

        String result = student.toString();

        assertTrue(result.contains("stu001"));
        assertTrue(result.contains("张三"));
        assertTrue(result.contains("计算机学院"));
        assertFalse(result.contains("123456"));
    }

    @Test
    void testAllArgsConstructor() {
        Student student = new Student("stu001", "张三", "男", 20, "计算机学院",
                "软件工程", "软工1班", "123456", "zhangsan@example.com", "13800138000");

        assertEquals("stu001", student.getStudentId());
        assertEquals("张三", student.getName());
        assertEquals("男", student.getGender());
        assertEquals(20, student.getAge());
        assertEquals("计算机学院", student.getDepartment());
        assertEquals("软件工程", student.getMajor());
        assertEquals("软工1班", student.getClassName());
        assertEquals("123456", student.getPassword());
        assertEquals("zhangsan@example.com", student.getEmail());
        assertEquals("13800138000", student.getPhone());
    }

    @Test
    void testNoArgsConstructor() {
        Student student = new Student();
        assertNotNull(student);
        assertNull(student.getStudentId());
        assertNull(student.getName());
        assertNull(student.getGender());
        assertNull(student.getAge());
        assertNull(student.getDepartment());
        assertNull(student.getMajor());
        assertNull(student.getClassName());
        assertNull(student.getPassword());
        assertNull(student.getEmail());
        assertNull(student.getPhone());
    }
}