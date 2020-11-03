package com.gwy.manager.mapper;

import com.gwy.manager.entity.Target;
import java.util.List;

public interface TargetMapper {
    int deleteByPrimaryKey(Integer targetId);

    int insert(Target record);

    Target selectByPrimaryKey(Integer targetId);

    List<Target> selectAll();

    int updateByPrimaryKey(Target record);

    /**
     * 获得学生评价的指标
     * @return
     */
    List<Target> getStudentTargets();

    /**
     * 获得教师评价的指标
     * @return
     */
    List<Target> getTeacherTargets();
}