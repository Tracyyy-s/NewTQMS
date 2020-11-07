package com.gwy.manager.service.impl;

import com.gwy.manager.dto.Response;
import com.gwy.manager.entity.Dept;
import com.gwy.manager.entity.ExcelSheetPO;
import com.gwy.manager.dto.ResultVO;
import com.gwy.manager.entity.Teacher;
import com.gwy.manager.mapper.DeptMapper;
import com.gwy.manager.mapper.TeacherMapper;
import com.gwy.manager.service.TeacherService;
import com.gwy.manager.util.ExcelHeaderFormat;
import com.gwy.manager.util.ExcelUtil;
import com.gwy.manager.util.MD5Util;
import com.gwy.manager.util.MultipartFileToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * @author Tracy
 * @date 2020/11/1 23:13
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private ExcelHeaderFormat headerFormat;

    @Override
    public Teacher getTeacher(String teacherNo) {
        return teacherMapper.selectByPrimaryKey(teacherNo);
    }

    @Override
    public ResultVO getTeacherByTnoInDept(String deptId, String tno) {

        ResultVO resultVO = new ResultVO();

        Teacher teacher = teacherMapper.selectByPrimaryKey(tno);

        //未找到教师
        if (teacher == null) {
            resultVO.setData("Not Found");
        } else if (!deptId.equals(teacher.getDeptId())) {
            //教师学院id不等于管理员学院id
            resultVO.setData("Permission Deny");
        } else {
            //添加教师
            resultVO.success(teacher);
        }

        return resultVO;
    }

    @Override
    public ResultVO login(String teacherNo, String password) {

        ResultVO resultVO = new ResultVO();

        Teacher teacher = teacherMapper.selectByPrimaryKey(teacherNo);

        //未找到教师
        if (teacher == null) {
            resultVO.setData("Not Found Teacher");
        } else if (!MD5Util.inputToDb(password).equals(teacher.getPassword())) {
            //密码错误
            resultVO.setData("Password Incorrect");
        } else {
            resultVO.success("Success");
        }

        return resultVO;
    }

    @Override
    public ResultVO updateTeacher(Teacher teacher) {

        ResultVO resultVO = new ResultVO();

        int i = teacherMapper.updateByPrimaryKey(teacher);
        if (i == 0) {
            resultVO.setData("Fail");
        } else {
            resultVO.success("Success");
        }

        return resultVO;
    }

    @Override
    public ResultVO updatePassword(String teacherNo, String password) {

        ResultVO resultVO = new ResultVO();

        int i = teacherMapper.updatePassword(teacherNo, MD5Util.inputToDb(password));
        if (i == 0) {
            resultVO.setData("Fail");
        } else {
            resultVO.success("Success");
        }

        return resultVO;
    }

    @Override
    public ResultVO getTeachersOfDept(String deptId) {

        ResultVO resultVO = new ResultVO();

        //获得学院内的所有教师
        List<Teacher> teachers = teacherMapper.selectByDeptId(deptId);
        //未找到
        if (teachers.size() == 0) {
            resultVO.setData("Not Found");
        } else {
            resultVO.success(teachers);
        }

        return resultVO;
    }

    @Override
    public ResultVO getTeacherMatchNameInDept(String deptId, String name) {

        ResultVO resultVO = new ResultVO();

        //根据学院号对教师名进行模糊匹配
        List<Teacher> teachers = teacherMapper.getTeachersMatchNameInDept(deptId, name);
        if (teachers.size() == 0) {
            resultVO.setData("Not Found");
        } else {
            resultVO.success(teachers);
        }

        return resultVO;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResultVO importTeachersByFile(String headerType, MultipartFile file) {


        ResultVO resultVO = headerFormat.importBeansByFile(headerType, file);

        if (resultVO.getResultCode().equals(Response.SUCCESS.getCode())) {
            Object data = resultVO.getData();
            Map<String, Object> map = (Map<String, Object>)data;

            List<Teacher> teachers = new ArrayList<>();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Map<String, Object> dataMap = (Map<String, Object>)entry.getValue();
                teachers.addAll((List<Teacher>)dataMap.get("dataList"));
            }
        }

        return resultVO;
    }

}
