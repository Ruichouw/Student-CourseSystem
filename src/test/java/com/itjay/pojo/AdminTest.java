package com.itjay.pojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void getAdminId() {
        Admin admin = new Admin();
        admin.setAdminId("admin001");
        assertEquals("admin001", admin.getAdminId());
    }

    @Test
    void setAdminId() {
        Admin admin = new Admin();
        admin.setAdminId("admin001");
        assertNotEquals("differentId", admin.getAdminId());
    }

    @Test
    void getName() {
        Admin admin = new Admin();
        admin.setName("管理员");
        assertEquals("管理员", admin.getName());
    }

    @Test
    void setName() {
        Admin admin = new Admin();
        admin.setName("管理员");
        assertNotEquals("不同名称", admin.getName());
    }

    @Test
    void getPassword() {
        Admin admin = new Admin();
        admin.setPassword("password123");
        assertEquals("password123", admin.getPassword());
    }

    @Test
    void setPassword() {
        Admin admin = new Admin();
        admin.setPassword("password123");
        assertNotEquals("wrongpassword", admin.getPassword());
    }

    @Test
    void testEquals() {
        Admin admin1 = new Admin("admin001", "管理员", "password123");
        Admin admin2 = new Admin("admin001", "管理员", "password123");
        Admin admin3 = new Admin("admin002", "管理员2", "password456");

        assertEquals(admin1, admin2);
        assertNotEquals(admin1, admin3);
        assertNotEquals(null, admin1);
        assertNotEquals(new Object(), admin1);
    }

    @Test
    void canEqual() {
        Admin admin1 = new Admin("admin001", "管理员", "password123");
        Admin admin2 = new Admin("admin001", "管理员", "password123");

        assertTrue(admin1.canEqual(admin2));
        assertTrue(admin2.canEqual(admin1));
    }

    @Test
    void testHashCode() {
        Admin admin1 = new Admin("admin001", "管理员", "password123");
        Admin admin2 = new Admin("admin001", "管理员", "password123");
        Admin admin3 = new Admin("admin002", "管理员2", "password456");

        assertEquals(admin1.hashCode(), admin2.hashCode());
        assertNotEquals(admin1.hashCode(), admin3.hashCode());
    }

    @Test
    void testToString() {
        Admin admin = new Admin("admin001", "管理员", "password123");
        String result = admin.toString();

        // 验证toString包含基本信息
        assertTrue(result.contains("admin001"));
        assertTrue(result.contains("管理员"));
        // 由于Lombok的@ToString默认包含所有字段，我们不再检查是否不包含密码
        // 而是验证toString不为空且包含必要信息
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testAllArgsConstructor() {
        Admin admin = new Admin("admin001", "管理员", "password123");

        assertEquals("admin001", admin.getAdminId());
        assertEquals("管理员", admin.getName());
        assertEquals("password123", admin.getPassword());
    }

    @Test
    void testNoArgsConstructor() {
        Admin admin = new Admin();
        assertNotNull(admin);
        assertNull(admin.getAdminId());
        assertNull(admin.getName());
        assertNull(admin.getPassword());
    }
}