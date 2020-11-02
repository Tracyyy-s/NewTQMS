package com.gwy.manager.mapper;

import com.gwy.manager.entity.Admin;
import java.util.List;

/**
 * @author TRacy
 */
public interface AdminMapper {
    int deleteByPrimaryKey(String adminNo);

    int insert(Admin record);

    Admin selectByPrimaryKey(String adminNo);

    List<Admin> selectAll();

    int updateByPrimaryKey(Admin record);
}