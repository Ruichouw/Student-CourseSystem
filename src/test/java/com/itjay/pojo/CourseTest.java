package com.itjay.pojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void getCourseId() {
        Course course = new Course();
        course.setCourseId("course001");
        assertEquals("course001", course.getCourseId());
    }

    @Test
    void setCourseId() {
        Course course = new Course();
        course.setCourseId("course001");
        assertNotEquals("course002", course.getCourseId());
    }

    @Test
    void getName() {
        Course course = new Course();
        course.setName("Java编程");
        assertEquals("Java编程", course.getName());
    }

    @Test
    void setName() {
        Course course = new Course();
        course.setName("Java编程");
        assertNotEquals("Python编程", course.getName());
    }

    @Test
    void getCredit() {
        Course course = new Course();
        course.setCredit(3.0f);
        assertEquals(3.0f, course.getCredit());
    }

    @Test
    void setCredit() {
        Course course = new Course();
        course.setCredit(3.0f);
        assertNotEquals(4.0f, course.getCredit());
    }

    @Test
    void getHours() {
        Course course = new Course();
        course.setHours(48);
        assertEquals(48, course.getHours());
    }

    @Test
    void setHours() {
        Course course = new Course();
        course.setHours(48);
        assertNotEquals(36, course.getHours());
    }

    @Test
    void getTeacherId() {
        Course course = new Course();
        course.setTeacherId("tea001");
        assertEquals("tea001", course.getTeacherId());
    }

    @Test
    void setTeacherId() {
        Course course = new Course();
        course.setTeacherId("tea001");
        assertNotEquals("tea002", course.getTeacherId());
    }

    @Test
    void getDescription() {
        Course course = new Course();
        course.setDescription("Java编程基础");
        assertEquals("Java编程基础", course.getDescription());
    }

    @Test
    void setDescription() {
        Course course = new Course();
        course.setDescription("Java编程基础");
        assertNotEquals("Python编程基础", course.getDescription());
    }

    @Test
    void getMaxStudents() {
        Course course = new Course();
        course.setMaxStudents(50);
        assertEquals(50, course.getMaxStudents());
    }

    @Test
    void setMaxStudents() {
        Course course = new Course();
        course.setMaxStudents(50);
        assertNotEquals(60, course.getMaxStudents());
    }

    @Test
    void getCurrentStudents() {
        Course course = new Course();
        course.setCurrentStudents(30);
        assertEquals(30, course.getCurrentStudents());
    }

    @Test
    void setCurrentStudents() {
        Course course = new Course();
        course.setCurrentStudents(30);
        assertNotEquals(40, course.getCurrentStudents());
    }

    @Test
    void getSyllabus() {
        Course course = new Course();
        course.setSyllabus("教学大纲");
        assertEquals("教学大纲", course.getSyllabus());
    }

    @Test
    void setSyllabus() {
        Course course = new Course();
        course.setSyllabus("教学大纲");
        assertNotEquals("不同大纲", course.getSyllabus());
    }

    @Test
    void getTextbook() {
        Course course = new Course();
        course.setTextbook("教材");
        assertEquals("教材", course.getTextbook());
    }

    @Test
    void setTextbook() {
        Course course = new Course();
        course.setTextbook("教材");
        assertNotEquals("不同教材", course.getTextbook());
    }

    @Test
    void getScheduleInfo() {
        Course course = new Course();
        course.setScheduleInfo("周一 1-2节");
        assertEquals("周一 1-2节", course.getScheduleInfo());
    }

    @Test
    void setScheduleInfo() {
        Course course = new Course();
        course.setScheduleInfo("周一 1-2节");
        assertNotEquals("周二 3-4节", course.getScheduleInfo());
    }

    @Test
    void getStatus() {
        Course course = new Course();
        course.setStatus("APPROVED");
        assertEquals("APPROVED", course.getStatus());
    }

    @Test
    void setStatus() {
        Course course = new Course();
        course.setStatus("APPROVED");
        assertNotEquals("PENDING", course.getStatus());
    }

    @Test
    void getRejectionReason() {
        Course course = new Course();
        course.setRejectionReason("课程内容不符合要求");
        assertEquals("课程内容不符合要求", course.getRejectionReason());
    }

    @Test
    void setRejectionReason() {
        Course course = new Course();
        course.setRejectionReason("课程内容不符合要求");
        assertNotEquals("其他拒绝原因", course.getRejectionReason());
    }

    @Test
    void getTeacherName() {
        Course course = new Course();
        course.setTeacherName("李老师");
        assertEquals("李老师", course.getTeacherName());
    }

    @Test
    void setTeacherName() {
        Course course = new Course();
        course.setTeacherName("李老师");
        assertNotEquals("王老师", course.getTeacherName());
    }

    @Test
    void testEquals() {
        Course course1 = new Course();
        course1.setCourseId("course001");
        course1.setName("Java编程");

        Course course2 = new Course();
        course2.setCourseId("course001");
        course2.setName("Java编程");

        Course course3 = new Course();
        course3.setCourseId("course002");
        course3.setName("Python编程");

        assertEquals(course1, course2);
        assertNotEquals(course1, course3);
        assertNotEquals(null, course1);
        assertNotEquals(new Object(), course1);
    }

    @Test
    void canEqual() {
        Course course1 = new Course();
        course1.setCourseId("course001");

        Course course2 = new Course();
        course2.setCourseId("course001");

        assertTrue(course1.canEqual(course2));
        assertTrue(course2.canEqual(course1));
    }

    @Test
    void testHashCode() {
        Course course1 = new Course();
        course1.setCourseId("course001");
        course1.setName("Java编程");

        Course course2 = new Course();
        course2.setCourseId("course001");
        course2.setName("Java编程");

        Course course3 = new Course();
        course3.setCourseId("course002");
        course3.setName("Python编程");

        assertEquals(course1.hashCode(), course2.hashCode());
        assertNotEquals(course1.hashCode(), course3.hashCode());
    }

    @Test
    void testToString() {
        Course course = new Course();
        course.setCourseId("course001");
        course.setName("Java编程");
        course.setTeacherName("李老师");

        String result = course.toString();

        assertTrue(result.contains("course001"));
        assertTrue(result.contains("Java编程"));
        assertTrue(result.contains("李老师"));
    }

    @Test
    void testAllArgsConstructor() {
        Course course = new Course("course001", "Java编程", 3.0f, 48, "tea001",
                "Java编程基础", 50, 30, "教学大纲", "教材", "周一 1-2节", "APPROVED", null, "李老师");

        assertEquals("course001", course.getCourseId());
        assertEquals("Java编程", course.getName());
        assertEquals(3.0f, course.getCredit());
        assertEquals(48, course.getHours());
        assertEquals("tea001", course.getTeacherId());
        assertEquals("Java编程基础", course.getDescription());
        assertEquals(50, course.getMaxStudents());
        assertEquals(30, course.getCurrentStudents());
        assertEquals("教学大纲", course.getSyllabus());
        assertEquals("教材", course.getTextbook());
        assertEquals("周一 1-2节", course.getScheduleInfo());
        assertEquals("APPROVED", course.getStatus());
        assertNull(course.getRejectionReason());
        assertEquals("李老师", course.getTeacherName());
    }

    @Test
    void testNoArgsConstructor() {
        Course course = new Course();
        assertNotNull(course);
        assertNull(course.getCourseId());
        assertNull(course.getName());
        assertNull(course.getCredit());
        assertNull(course.getHours());
        assertNull(course.getTeacherId());
        assertNull(course.getDescription());
        assertNull(course.getMaxStudents());
        assertNull(course.getCurrentStudents());
        assertNull(course.getSyllabus());
        assertNull(course.getTextbook());
        assertNull(course.getScheduleInfo());
        assertNull(course.getStatus());
        assertNull(course.getRejectionReason());
        assertNull(course.getTeacherName());
    }
}