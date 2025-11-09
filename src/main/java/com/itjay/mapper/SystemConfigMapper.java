package com.itjay.mapper;

import com.itjay.pojo.SystemConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemConfigMapper {
    SystemConfig getConfig();
    int updateConfig(SystemConfig config);
} 