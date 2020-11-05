package com.gwy.manager.controller.admin;

import com.gwy.manager.dto.Response;
import com.gwy.manager.dto.ResultVO;
import com.gwy.manager.entity.Target;
import com.gwy.manager.mapper.AdminMapper;
import com.gwy.manager.service.impl.AdminServiceImpl;
import com.gwy.manager.service.impl.TargetServiceImpl;
import com.gwy.manager.service.impl.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author Tracy
 * @date 2020/11/3 16:43
 */
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private TargetServiceImpl targetService;

    @Autowired
    private TeacherServiceImpl teacherService;

    /**
     * 管理员登录
     * @param adminNo   管理员账号
     * @param password  管理员密码
     * @return  返回结果集
     */
    @PostMapping("/login")
    public ResultVO login(@RequestParam("adminNo") String adminNo,
                          @RequestParam("password") String password,
                          HttpSession session) {

        ResultVO resultVO = adminService.login(adminNo, password);

        if (resultVO.getResultCode().equals(Response.SUCCESS.getCode())) {
            session.setAttribute("admin", resultVO.getData());
        }

        return resultVO;
    }

    /**
     * 管理员修改密码
     * @param adminNo   管理员账号
     * @param password  管理员密码
     * @return  返回结果集
     */
    @PostMapping("/updatePassword")
    public ResultVO updatePassword(@RequestParam("adminNo") String adminNo,
                                   @RequestParam("password") String password) {

        return adminService.updatePassword(adminNo, password);
    }

    /**
     * 获得评价指标
     * @return  返回结果集
     */
    @PostMapping("/getTargets")
    public ResultVO getTargets(@RequestParam("userType") String userType) {
        return targetService.getTargets(userType);
    }

    /**
     * 添加评价指标
     * @param target    预添加指标
     * @return  结果集
     */
    @PostMapping("/addTarget")
    public ResultVO addStudentTarget(@RequestParam("target") Target target,
                                     @RequestParam("targetType") String targetType) {

        int intTargetType;
        try {
            intTargetType = Integer.parseInt(targetType);
        } catch (NumberFormatException e) {

            ResultVO resultVO = new ResultVO();
            resultVO.setData("NumberFormatException");

            return resultVO;
        }

        return targetService.addTarget(target, intTargetType);
    }

    /**
     * 修改评价指标信息
     * @param target    预修改
     * @return  返回结果集
     */
    @PostMapping("/updateTarget")
    public ResultVO updateTarget(Target target) {
        return targetService.updateTarget(target);
    }

    /**
     * 删除指标
     * @param targetId  指标id
     * @return  返回结果集
     */
    @PostMapping("/deleteTarget")
    public ResultVO deleteTarget(@RequestParam("targetId") String targetId) {

        int intTargetId;
        try {
            intTargetId = Integer.parseInt(targetId);
        } catch (NumberFormatException e) {
            ResultVO resultVO = new ResultVO();
            resultVO.setData("NumberFormatException");
            return resultVO;
        }

        return targetService.deleteTarget(intTargetId);
    }

    /**
     * 获得学院内所有的教师
     * @param deptId    学院id
     * @return  结果集
     */
    @PostMapping("/getTeachersByDept")
    public ResultVO getTeachersByDept(@RequestParam("deptId") String deptId) {
        return teacherService.getTeachersOfDept(deptId);
    }

    /**
     * 在学院内模糊匹配教师姓名
     * @param deptId 学院id
     * @param name  匹配名字
     * @return  结果集
     */
    @PostMapping("/getTeachersMatchNameInDept")
    public ResultVO getTeachersMatchNameInDept(@RequestParam("deptId") String deptId,
                                              @RequestParam("name") String name) {
        return teacherService.getTeacherMatchNameInDept(deptId, name);
    }
}
