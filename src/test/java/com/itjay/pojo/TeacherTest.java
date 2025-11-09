package com.itjay.pojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void getTeacherId() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId("tea001");
        assertEquals("tea001", teacher.getTeacherId());
    }

    @Test
    void setTeacherId() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId("tea001");
        assertNotEquals("tea002", teacher.getTeacherId());
    }

    @Test
    void getName() {
        Teacher teacher = new Teacher();
        teacher.setName("李老师");
        assertEquals("李老师", teacher.getName());
    }

    @Test
    void setName() {
        Teacher teacher = new Teacher();
        teacher.setName("李老师");
        assertNotEquals("王老师", teacher.getName());
    }

    @Test
    void getGender() {
        Teacher teacher = new Teacher();
        teacher.setGender("男");
        assertEquals("男", teacher.getGender());
    }

    @Test
    void setGender() {
        Teacher teacher = new Teacher();
        teacher.setGender("男");
        assertNotEquals("女", teacher.getGender());
    }

    @Test
    void getDepartment() {
        Teacher teacher = new Teacher();
        teacher.setDepartment("计算机学院");
        assertEquals("计算机学院", teacher.getDepartment());
    }

    @Test
    void setDepartment() {
        Teacher teacher = new Teacher();
        teacher.setDepartment("计算机学院");
        assertNotEquals("数学学院", teacher.getDepartment());
    }

    @Test
    void getTitle() {
        Teacher teacher = new Teacher();
        teacher.setTitle("教授");
        assertEquals("教授", teacher.getTitle());
    }

    @Test
    void setTitle() {
        Teacher teacher = new Teacher();
        teacher.setTitle("教授");
        assertNotEquals("副教授", teacher.getTitle());
    }

    @Test
    void getPassword() {
        Teacher teacher = new Teacher();
        teacher.setPassword("password123");
        assertEquals("password123", teacher.getPassword());
    }

    @Test
    void setPassword() {
        Teacher teacher = new Teacher();
        teacher.setPassword("password123");
        assertNotEquals("wrongpassword", teacher.getPassword());
    }

    @Test
    void getEmail() {
        Teacher teacher = new Teacher();
        teacher.setEmail("li@example.com");
        assertEquals("li@example.com", teacher.getEmail());
    }

    @Test
    void setEmail() {
        Teacher teacher = new Teacher();
        teacher.setEmail("li@example.com");
        assertNotEquals("wang@example.com", teacher.getEmail());
    }

    @Test
    void getPhone() {
        Teacher teacher = new Teacher();
        teacher.setPhone("13900139000");
        assertEquals("13900139000", teacher.getPhone());
    }

    @Test
    void setPhone() {
        Teacher teacher = new Teacher();
        teacher.setPhone("13900139000");
        assertNotEquals("13800138000", teacher.getPhone());
    }

    @Test
    void testEquals() {
        Teacher teacher1 = new Teacher();
        teacher1.setTeacherId("tea001");
        teacher1.setName("李老师");

        Teacher teacher2 = new Teacher();
        teacher2.setTeacherId("tea001");
        teacher2.setName("李老师");

        Teacher teacher3 = new Teacher();
        teacher3.setTeacherId("tea002");
        teacher3.setName("王老师");

        assertEquals(teacher1, teacher2);
        assertNotEquals(teacher1, teacher3);
        assertNotEquals(null, teacher1);
        assertNotEquals(new Object(), teacher1);
    }

    @Test
    void canEqual() {
        Teacher teacher1 = new Teacher();
        teacher1.setTeacherId("tea001");

        Teacher teacher2 = new Teacher();
        teacher2.setTeacherId("tea001");

        assertTrue(teacher1.canEqual(teacher2));
        assertTrue(teacher2.canEqual(teacher1));
    }

    @Test
    void testHashCode() {
        Teacher teacher1 = new Teacher();
        teacher1.setTeacherId("tea001");
        teacher1.setName("李老师");

        Teacher teacher2 = new Teacher();
        teacher2.setTeacherId("tea001");
        teacher2.setName("李老师");

        Teacher teacher3 = new Teacher();
        teacher3.setTeacherId("tea002");
        teacher3.setName("王老师");

        assertEquals(teacher1.hashCode(), teacher2.hashCode());
        assertNotEquals(teacher1.hashCode(), teacher3.hashCode());
    }

    @Test
    void testToString() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId("tea001");
        teacher.setName("李老师");
        teacher.setDepartment("计算机学院");

        String result = teacher.toString();

        assertTrue(result.contains("tea001"));
        assertTrue(result.contains("李老师"));
        assertTrue(result.contains("计算机学院"));
        assertFalse(result.contains("password123"));
    }

    @Test
    void testAllArgsConstructor() {
        Teacher teacher = new Teacher("tea001", "李老师", "男", "计算机学院",
                "教授", "password123", "li@example.com", "13900139000");

        assertEquals("tea001", teacher.getTeacherId());
        assertEquals("李老师", teacher.getName());
        assertEquals("男", teacher.getGender());
        assertEquals("计算机学院", teacher.getDepartment());
        assertEquals("教授", teacher.getTitle());
        assertEquals("password123", teacher.getPassword());
        assertEquals("li@example.com", teacher.getEmail());
        assertEquals("13900139000", teacher.getPhone());
    }

    @Test
    void testNoArgsConstructor() {
        Teacher teacher = new Teacher();
        assertNotNull(teacher);
        assertNull(teacher.getTeacherId());
        assertNull(teacher.getName());
        assertNull(teacher.getGender());
        assertNull(teacher.getDepartment());
        assertNull(teacher.getTitle());
        assertNull(teacher.getPassword());
        assertNull(teacher.getEmail());
        assertNull(teacher.getPhone());
    }
}