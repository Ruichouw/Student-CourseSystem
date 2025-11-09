package com.itjay.mapper;

import com.itjay.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {
    // 管理员登录
    Admin login(@Param("adminId") String adminId, @Param("password") String password);
    
    // 根据ID查询管理员
    Admin getById(String adminId);
    
    // 更新管理员信息
    int update(Admin admin);
    
    // 统计管理员总数
    int count();
    
    // 插入管理员
    int insert(Admin admin);
    
    // 删除管理员
    int delete(String adminId);
}