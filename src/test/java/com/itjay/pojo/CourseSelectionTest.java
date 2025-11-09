package com.itjay.pojo;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CourseSelectionTest {

    @Test
    void getId() {
        CourseSelection selection = new CourseSelection();
        selection.setId(1);
        assertEquals(1, selection.getId());
    }

    @Test
    void setId() {
        CourseSelection selection = new CourseSelection();
        selection.setId(1);
        assertNotEquals(2, selection.getId());
    }

    @Test
    void getStudentId() {
        CourseSelection selection = new CourseSelection();
        selection.setStudentId("stu001");
        assertEquals("stu001", selection.getStudentId());
    }

    @Test
    void setStudentId() {
        CourseSelection selection = new CourseSelection();
        selection.setStudentId("stu001");
        assertNotEquals("stu002", selection.getStudentId());
    }

    @Test
    void getCourseId() {
        CourseSelection selection = new CourseSelection();
        selection.setCourseId("course001");
        assertEquals("course001", selection.getCourseId());
    }

    @Test
    void setCourseId() {
        CourseSelection selection = new CourseSelection();
        selection.setCourseId("course001");
        assertNotEquals("course002", selection.getCourseId());
    }

    @Test
    void getSelectionTime() {
        Date now = new Date();
        CourseSelection selection = new CourseSelection();
        selection.setSelectionTime(now);
        assertEquals(now, selection.getSelectionTime());
    }

    @Test
    void setSelectionTime() {
        Date now = new Date();
        CourseSelection selection = new CourseSelection();
        selection.setSelectionTime(now);
        Date differentTime = new Date(now.getTime() + 1000);
        assertNotEquals(differentTime, selection.getSelectionTime());
    }

    @Test
    void getScore() {
        CourseSelection selection = new CourseSelection();
        selection.setScore(85.5f);
        assertEquals(85.5f, selection.getScore());
    }

    @Test
    void setScore() {
        CourseSelection selection = new CourseSelection();
        selection.setScore(85.5f);
        assertNotEquals(90.0f, selection.getScore());
    }

    @Test
    void getStudentName() {
        CourseSelection selection = new CourseSelection();
        selection.setStudentName("张三");
        assertEquals("张三", selection.getStudentName());
    }

    @Test
    void setStudentName() {
        CourseSelection selection = new CourseSelection();
        selection.setStudentName("张三");
        assertNotEquals("李四", selection.getStudentName());
    }

    @Test
    void getCourseName() {
        CourseSelection selection = new CourseSelection();
        selection.setCourseName("Java编程");
        assertEquals("Java编程", selection.getCourseName());
    }

    @Test
    void setCourseName() {
        CourseSelection selection = new CourseSelection();
        selection.setCourseName("Java编程");
        assertNotEquals("Python编程", selection.getCourseName());
    }

    @Test
    void getCredit() {
        CourseSelection selection = new CourseSelection();
        selection.setCredit(3.0f);
        assertEquals(3.0f, selection.getCredit());
    }

    @Test
    void setCredit() {
        CourseSelection selection = new CourseSelection();
        selection.setCredit(3.0f);
        assertNotEquals(4.0f, selection.getCredit());
    }

    @Test
    void getTeacherName() {
        CourseSelection selection = new CourseSelection();
        selection.setTeacherName("李老师");
        assertEquals("李老师", selection.getTeacherName());
    }

    @Test
    void setTeacherName() {
        CourseSelection selection = new CourseSelection();
        selection.setTeacherName("李老师");
        assertNotEquals("王老师", selection.getTeacherName());
    }

    @Test
    void getGender() {
        CourseSelection selection = new CourseSelection();
        selection.setGender("男");
        assertEquals("男", selection.getGender());
    }

    @Test
    void setGender() {
        CourseSelection selection = new CourseSelection();
        selection.setGender("男");
        assertNotEquals("女", selection.getGender());
    }

    @Test
    void getAge() {
        CourseSelection selection = new CourseSelection();
        selection.setAge(20);
        assertEquals(20, selection.getAge());
    }

    @Test
    void setAge() {
        CourseSelection selection = new CourseSelection();
        selection.setAge(20);
        assertNotEquals(21, selection.getAge());
    }

    @Test
    void getDepartment() {
        CourseSelection selection = new CourseSelection();
        selection.setDepartment("计算机学院");
        assertEquals("计算机学院", selection.getDepartment());
    }

    @Test
    void setDepartment() {
        CourseSelection selection = new CourseSelection();
        selection.setDepartment("计算机学院");
        assertNotEquals("数学学院", selection.getDepartment());
    }

    @Test
    void getMajor() {
        CourseSelection selection = new CourseSelection();
        selection.setMajor("软件工程");
        assertEquals("软件工程", selection.getMajor());
    }

    @Test
    void setMajor() {
        CourseSelection selection = new CourseSelection();
        selection.setMajor("软件工程");
        assertNotEquals("计算机科学", selection.getMajor());
    }

    @Test
    void getClassName() {
        CourseSelection selection = new CourseSelection();
        selection.setClassName("软工1班");
        assertEquals("软工1班", selection.getClassName());
    }

    @Test
    void setClassName() {
        CourseSelection selection = new CourseSelection();
        selection.setClassName("软工1班");
        assertNotEquals("软工2班", selection.getClassName());
    }

    @Test
    void getEmail() {
        CourseSelection selection = new CourseSelection();
        selection.setEmail("zhangsan@example.com");
        assertEquals("zhangsan@example.com", selection.getEmail());
    }

    @Test
    void setEmail() {
        CourseSelection selection = new CourseSelection();
        selection.setEmail("zhangsan@example.com");
        assertNotEquals("lisi@example.com", selection.getEmail());
    }

    @Test
    void getPhone() {
        CourseSelection selection = new CourseSelection();
        selection.setPhone("13800138000");
        assertEquals("13800138000", selection.getPhone());
    }

    @Test
    void setPhone() {
        CourseSelection selection = new CourseSelection();
        selection.setPhone("13800138000");
        assertNotEquals("13900139000", selection.getPhone());
    }

    @Test
    void testEquals() {
        CourseSelection selection1 = new CourseSelection();
        selection1.setId(1);
        selection1.setStudentId("stu001");

        CourseSelection selection2 = new CourseSelection();
        selection2.setId(1);
        selection2.setStudentId("stu001");

        CourseSelection selection3 = new CourseSelection();
        selection3.setId(2);
        selection3.setStudentId("stu002");

        assertEquals(selection1, selection2);
        assertNotEquals(selection1, selection3);
        assertNotEquals(null, selection1);
        assertNotEquals(new Object(), selection1);
    }

    @Test
    void canEqual() {
        CourseSelection selection1 = new CourseSelection();
        selection1.setId(1);

        CourseSelection selection2 = new CourseSelection();
        selection2.setId(1);

        assertTrue(selection1.canEqual(selection2));
        assertTrue(selection2.canEqual(selection1));
    }

    @Test
    void testHashCode() {
        CourseSelection selection1 = new CourseSelection();
        selection1.setId(1);
        selection1.setStudentId("stu001");

        CourseSelection selection2 = new CourseSelection();
        selection2.setId(1);
        selection2.setStudentId("stu001");

        CourseSelection selection3 = new CourseSelection();
        selection3.setId(2);
        selection3.setStudentId("stu002");

        assertEquals(selection1.hashCode(), selection2.hashCode());
        assertNotEquals(selection1.hashCode(), selection3.hashCode());
    }

    @Test
    void testToString() {
        CourseSelection selection = new CourseSelection();
        selection.setId(1);
        selection.setStudentId("stu001");
        selection.setCourseId("course001");

        String result = selection.toString();

        assertTrue(result.contains("1"));
        assertTrue(result.contains("stu001"));
        assertTrue(result.contains("course001"));
    }

    @Test
    void testNoArgsConstructor() {
        CourseSelection selection = new CourseSelection();
        assertNotNull(selection);
        assertNull(selection.getId());
        assertNull(selection.getStudentId());
        assertNull(selection.getCourseId());
        assertNull(selection.getSelectionTime());
        assertNull(selection.getScore());
    }
}