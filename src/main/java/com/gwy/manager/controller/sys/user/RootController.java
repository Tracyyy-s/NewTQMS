package com.gwy.manager.controller.sys.user;

import com.alibaba.fastjson.JSONObject;
import com.gwy.manager.constant.PageHelperConst;
import com.gwy.manager.dto.ResultVO;
import com.gwy.manager.enums.ResponseDataMsg;
import com.gwy.manager.enums.SysLogType;
import com.gwy.manager.service.impl.*;
import com.gwy.manager.util.DateUtilCustom;
import com.gwy.manager.util.PageHelperUtil;
import com.gwy.manager.util.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tracy
 * @date 2020/11/10 16:36
 */
@RestController
@CrossOrigin
@RequestMapping("/root")
public class RootController {

    @Autowired
    private DeptServiceImpl deptService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private PermissionServiceImpl permissionService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private SysLogServiceImpl logService;

    /**
     * root用户修改密码
     * @param map   请求体
     * @return  结果集
     */
    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_root')")
    public ResultVO changePassword(@RequestBody Map<String, String> map) {

        String password = map.get("password");
        String newPassword = map.get("newPassword");
        return userService.updateRootPassword(password, newPassword);
    }

    /**
     * root用户获得所有管理员
     * @return 结果集
     */
    @PostMapping("/getAllAdmins")
    @PreAuthorize("hasAuthority('user_role_list')")
    public String getAllAdmins(@RequestBody Map<String, Object> map) {

        PageHelperUtil.pageMsg(map);
        int pageNum = (int) map.get(PageHelperConst.pageNum);
        int pageSize = (int) map.get(PageHelperConst.pageSize);
        return JSONObject.toJSONStringWithDateFormat(userService.getAllAdmin(pageNum, pageSize), DateUtilCustom.DATE_PATTERN);
    }

    /**
     * root用户获得所有用户
     * @return  结果集
     */
    @PostMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('user_role_list')")
    public String getAllUsers(@RequestBody Map<String, Object> map) {

        PageHelperUtil.pageMsg(map);
        int pageNum = (int) map.get(PageHelperConst.pageNum);
        int pageSize = (int) map.get(PageHelperConst.pageSize);
        return JSONObject.toJSONStringWithDateFormat(userService.getAllUsers(pageNum, pageSize), DateUtilCustom.DATE_PATTERN);
    }

    /**
     * root用户获得所有学生信息
     * @param map   请求体
     * @return  结果集
     */
    @PostMapping("/getAllStudents")
    public String getAllStudents(@RequestBody Map<String, Object> map) {

        PageHelperUtil.pageMsg(map);
        int pageNum = (int) map.get(PageHelperConst.pageNum);
        int pageSize = (int) map.get(PageHelperConst.pageSize);
        return JSONObject.toJSONStringWithDateFormat(studentService.getAllStudents(pageNum, pageSize), DateUtilCustom.DATE_PATTERN);
    }

    /**
     * root用户获得所有角色信息
     * @return  结果集
     */
    @PostMapping("/getAllRoles")
    @PreAuthorize("hasAnyAuthority('user_role_list', 'role_permission_modify')")
    public ResultVO getAllRoles() {

        return roleService.getAllRoles();
    }

    /**
     * root用户获得所有权限信息
     * @return  结果集
     */
    @PostMapping("/getAllPermissions")
    @PreAuthorize("hasAuthority('role_permission_modify')")
    public ResultVO getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    /**
     * root用户获得某用户的所有角色
     * @param map   请求体
     * @return  结果集
     */
    @PostMapping("/getUserRoles")
    @PreAuthorize("hasAuthority('user_role_list')")
    public ResultVO getUserRole(@RequestBody Map<String, String> map) {

        String userId = map.get("userId");
        return userRoleService.getUserRoles(userId);
    }

    /**
     * root用户通过角色id获得权限
     * @param map   请求体
     * @return  结果集
     */
    @PostMapping("/getPermissionsByRoleId")
    @PreAuthorize("hasAuthority('role_permission_modify')")
    public ResultVO getPermissionsByRoleId(@RequestBody Map<String, String> map) {

        int roleId;
        try {
            roleId = Integer.parseInt(map.get("roleId"));
        } catch (Exception e) {
            return ResultVOUtil.error(ResponseDataMsg.Fail.getMsg());
        }
        return permissionService.getPermissionsByRoleId(roleId);
    }

    /**
     * root用户修改用户的角色
     * @param map   请求体
     * @return  结果集
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/updateUserRole")
    @PreAuthorize("hasAuthority('user_role_list')")
    public ResultVO updateUserRole(@RequestBody Map<String, Object> map) {

        List<Integer> roleIds;
        String userId = (String) map.get("userId");
        try {
            roleIds = (List<Integer>)map.get("data");

        } catch (Exception e) {
            return ResultVOUtil.error(ResponseDataMsg.BadRequest.getMsg());
        }
        return userRoleService.updateUserRole(userId, roleIds);
    }

    /**
     * root用户修改角色权限
     * @param map   请求体
     * @return  结果集
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/updateRolePermission")
    @PreAuthorize("hasAuthority('role_permission_modify')")
    public ResultVO updateRolePermission(@RequestBody Map<String, Object> map) {

        Integer roleId = Integer.parseInt((String)map.get("roleId"));
        List<Integer> permissionIds;
        try {
            permissionIds = (List<Integer>)map.get("data");
        } catch (NumberFormatException e) {
            return ResultVOUtil.error(ResponseDataMsg.BadRequest.getMsg());
        }
        return permissionService.updateRolePermission(roleId, permissionIds);
    }

    /**
     * root用户更新管理员可管理的学院
     * @param map   请求体
     * @return  结果集
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/updateAvailableDeptIds")
    public ResultVO updateAvailableDeptIds(@RequestBody Map<String, Object> map) {

        List<String> list;
        try {
            list = (List<String>)map.get("deptIdList");
        } catch (Exception e) {
            ResultVO resultVO;
            resultVO = ResultVOUtil.error(ResponseDataMsg.BadRequest.getMsg());

            return resultVO;
        }

        String userId = (String) map.get("userId");
        return userService.updateAvailableDeptIds(userId, list);
    }

    /**
     * root用户获得所有学院信息
     * @return  结果集
     */
    @PostMapping("/getAllDepts")
    public ResultVO getAllDepts() {

        return deptService.getAllDepts();
    }

    /**
     * 获取所有日志类别
     * @return  结果集
     */
    @PostMapping("/getLogTypes")
    @PreAuthorize("hasAuthority('Log')")
    public ResultVO getLogTypes() {

        SysLogType[] types = SysLogType.values();
        List<Map<String, String>> typeList = new ArrayList<>();
        for (SysLogType type : types) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("value", type.getType());
            map.put("label", type.getTypeExplain());
            typeList.add(map);
        }
        return ResultVOUtil.success(typeList);
    }

    /**
     * 获得所有日志类型已经相应的数量
     * @return  结果集
     */
    @PostMapping("/getLogTypeAndCount")
    @PreAuthorize("hasAuthority('Log')")
    public ResultVO getLogTypeAndCount() {
        return logService.getLogTypeAndCount();
    }

    /**
     * 通过日志类型获得所有日志
     * @param map   请求体
     * @return  结果集
     */
    @PostMapping("/getLogInfoByData")
    @PreAuthorize("hasAuthority('Log')")
    public String getLogs(@RequestBody Map<String, Object> map) {

        PageHelperUtil.pageMsg(map);
        int pageNum = (int) map.get(PageHelperConst.pageNum);
        int pageSize = (int) map.get(PageHelperConst.pageSize);
        String type = (String) map.get("type");
        return JSONObject.toJSONStringWithDateFormat(logService.getLogInfoByType(type, pageNum, pageSize), DateUtilCustom.TIME_PATTERN);
    }
}
