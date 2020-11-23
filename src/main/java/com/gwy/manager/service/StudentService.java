package com.gwy.manager.service;

import com.gwy.manager.dto.ResultVO;
import com.gwy.manager.entity.Student;

import java.util.List;

/**
 * @author Tracy
 * @date 2020/11/3 16:12
 */
public interface StudentService {

    /**
     * 添加学生
     * @param student 预添加
     * @return  影响行数
     */
    int addStudent(Student student);

    /**
     * 修改学生信息
     * @param student 预修改
     * @return  影响行数
     */
    int updateStudent(Student student);

    /**
     * 修改学生密码
     * @param studentNo 学号
     * @param password  密码
     * @param vrCode    验证码
     * @return  结果集
     */
    ResultVO updatePassword(String studentNo, String password, String vrCode);

    /**
     * 批量添加学生
     * @param students 学生列表
     * @return  返回行数
     */
    int addStudentBatch(List<Student> students);

    /**
     * 学生登录
     * @param studentNo
     * @param password
     * @return
     */
    //ResultVO login(String studentNo, String password);

    /**
     * 获得学生信息
     * @param studentNo 学号
     * @return  结果集
     */
    ResultVO getStudentInfo(String studentNo);

    /**
     * 发送验证码至Redis,验证码5min有效
     * @param studentNo 学生id
     * @return  结果集
     */
    ResultVO sendCode(String studentNo);

    /**
     * 获取某学院的所有学生
     * @param deptId 学院id
     * @return  结果集
     */
    ResultVO getStudentsByDept(String deptId);

    /**
     * 获取某个班级的所有学生
     * @param classId 班号
     * @return  结果集
     */
    ResultVO getStudentByClass(String classId);

    /**
     * 根据学生名字进行模糊匹配
     * @param deptId 学院id
     * @param name  学生姓名
     * @return  结果集
     */
    ResultVO getStudentsMatchName(String deptId, String name);
}
