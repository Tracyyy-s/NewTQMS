package com.gwy.manager.controller.sys.student;

import com.alibaba.fastjson.JSONObject;
import com.gwy.manager.dto.ResultVO;
import com.gwy.manager.entity.StudentAssess;
import com.gwy.manager.service.impl.*;
import com.gwy.manager.util.DateUtilCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Tracy
 * @date 2020/11/2 19:24
 */
@CrossOrigin
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private TeacherCourseServiceImpl teacherCourseService;

    @Autowired
    private TermTargetServiceImpl termTargetService;

    @Autowired
    private StudentAssessServiceImpl studentAssessService;

    /**
     * 修改密码
     * @param map   请求体
     * @return  结果集
     */
    @PostMapping("/updatePassword")
    @PreAuthorize("hasAuthority('studentInfo')")
    public ResultVO updatePassword(@RequestBody Map<String, String> map) {

        String studentNo = map.get("studentNo");
        String password = map.get("password");
        String vrCode = map.get("vrCode");
        return studentService.updatePassword(studentNo, password, vrCode);
    }

    /**
     * 获得学生信息
     * @param map   请求体
     * @return  结果集
     */
    @PostMapping("/getStudentInfo")
    @PreAuthorize("hasAnyAuthority('studentInfo','studentListImporting')")
    public String getStudentInfo(@RequestBody Map<String, String> map) {

        String studentNo = map.get("studentNo");
        return JSONObject.toJSONStringWithDateFormat(studentService.getStudentInfo(studentNo), DateUtilCustom.DATE_PATTERN);
    }

    /**
     * 获得学生某学期的课程
     * @param map   请求体
     * @return  结果集
     */
    @PostMapping("/getTermCourses")
    @PreAuthorize("hasAnyAuthority('studentSchedule', 'Evaluation')")
    public ResultVO getTermCourses(@RequestBody Map<String, String> map) {

        String studentNo = map.get("studentNo");
        String termId = map.get("termId");
        return teacherCourseService
                .getCoursesByStudentAndTerm(studentNo, termId);
    }

    /**
     * 获得学生学期的评价指标
     * @param map   请求体
     * @return  结果集
     */
    @PostMapping("/getTermTargets")
    public ResultVO getStudentTermTargets(@RequestBody Map<String, String> map) {

        String termId = map.get("termId");
        return termTargetService.getStudentTermTargets(termId);
    }

    /**
     * 学生发送课程评价请求
     * @param studentAssess 学生评价
     * @return 返回结果
     */
    @PostMapping("/postAssess")
    @PreAuthorize("hasAuthority('Evaluation')")
    public ResultVO postAssess(@RequestBody StudentAssess studentAssess) {
        return studentAssessService.addStudentAssess(studentAssess);
    }

    /**
     * 学生发送验证码请求
     * @param map   请求体
     * @return  结果集
     */
    @PostMapping("/sendCode")
    @PreAuthorize("hasAuthority('studentInfo')")
    public ResultVO postCode(@RequestBody Map<String, String> map) {
        String studentNo = map.get("studentNo");
        return studentService.sendCode(studentNo);
    }
}
