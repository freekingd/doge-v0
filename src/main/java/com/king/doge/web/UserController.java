package com.king.doge.web;

import com.king.doge.comm.Constants;
import com.king.doge.comm.Result;
import com.king.doge.comm.ResultGenerator;
import com.king.doge.model.User;
import com.king.doge.service.UserService;
import com.king.doge.utiles.DateUtil;
import com.king.doge.utiles.FileUtil;
import com.king.doge.utiles.PageResult;
import com.king.doge.utiles.PoiUtil;
import com.king.doge.web.annotation.TokenToUser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.king.doge.config.WebConfiguration.LOGIN_KEY;
import static com.king.doge.config.WebConfiguration.LOGIN_USER;

/**
 * 用户管理
 * Created by zhuru on 2019/1/9.
 */
@RestController
@RequestMapping("/fly/users")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody User user, HttpServletRequest request) {
        Result result = ResultGenerator.genFailResult("登陆失败！");
        if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
            result.setMessage("登录名或密码不能为空！");
        }
        User user1 = userService.updateTokenAndLogin(user.getUserName(), user.getPassword());
        if (user1 != null) {
            result = ResultGenerator.genSuccessResult(user1);
            request.getSession().setAttribute(LOGIN_KEY, user1.getId());
            request.getSession().setAttribute(LOGIN_USER, user1);
            request.getSession().setAttribute("user", user1.getUserName());
        }
        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute(LOGIN_KEY);
        request.getSession().removeAttribute(LOGIN_USER);
        Result result = ResultGenerator.genFailResult("注销成功！");
        return result;
    }

    /**
     * 查询用户列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam Map<String, Object> param) {
        if (StringUtils.isEmpty(param.get("page")) || StringUtils.isEmpty(param.get("limit"))) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数错误❌！");
        }
        PageResult pageResult = userService.findUsers(param);
        return ResultGenerator.genSuccessResult(pageResult);
    }

    /**
     * 保存新增用户
     * @param User
     * @param loginUser
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result addUser(@RequestBody User User, @TokenToUser User loginUser) {
        if (null == loginUser) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "未登录");
        }
        if (StringUtils.isEmpty(User.getUserName()) || StringUtils.isEmpty(User.getPassword())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数错误！");
        }
        User tempUser = userService.findByUserName(User.getUserName());
        if (tempUser != null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "该用户名已存在！");
        }
        if ("admin".equals(User.getUserName().trim())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "不能添加admin用户！");
        }
        if (userService.save(User).getId() > 0) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("新增失败！");
    }

    /**
     * 修改用户密码
     * @param User
     * @param loginUser
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result editUser(@RequestBody User User, @TokenToUser User loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "未登录");
        }
        if (StringUtils.isEmpty(User.getPassword())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "请输入新密码！");
        }
        User tempUser = userService.findById(User.getId());
        if (tempUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "该用户不存在！");
        }
        if ("admin".equals(tempUser.getUserName().trim())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "不能修改admin用户的密码！");
        }
        tempUser.setPassword(User.getPassword());
        if (userService.updatePassword(tempUser) > 0) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("修改失败");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids, @TokenToUser User loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "未登录！");
        }
        if (ids.length < 1) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数异常！");
        }
        if (userService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    /**
     * 批量导入用户V1
     * <p>
     * 批量导入用户(直接导入)
     */
    @RequestMapping(value = "/importV1", method = RequestMethod.POST)
    public Result saveByExcelFileV1(@RequestParam("file") MultipartFile multipartFile) {
        File file = FileUtil.convertMultipartFileToFile(multipartFile);
        if (file == null) {
            return ResultGenerator.genFailResult("导入失败");
        }
        int importResult = userService.importUsersByExcelFile(file);
        if (importResult > 0) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(importResult);
            return result;
        } else {
            return ResultGenerator.genFailResult("导入失败");
        }
    }

    /**
     * 批量导入用户V2
     * <p>
     * 批量导入用户(根据文件url导入)
     */
    @RequestMapping(value = "/importV2", method = RequestMethod.POST)
    public Result saveByExcelFileV2(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return ResultGenerator.genFailResult("fileUrl不能为空");
        }
        File file = FileUtil.downloadFile(fileUrl);
        if (file == null) {
            return ResultGenerator.genFailResult("文件不存在");
        }
        int importResult = userService.importUsersByExcelFile(file);
        if (importResult > 0) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(importResult);
            return result;
        } else {
            return ResultGenerator.genFailResult("导入失败");
        }
    }


    /**
     * 文件导出
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportUsers(HttpServletRequest request, HttpServletResponse response) {
        List<User> userList = userService.getUsersForExport();
        //单元格表头
        String[] excelHeader = {"用户id", "用户名", "账号状态", "添加时间"};
        //字段名称
        String[] fileds = {"userId", "userName", "status", "createTime"};
        //单元格宽度内容格式
        int[] formats = {4, 2, 1, 1};
        //单元格宽度
        int[] widths = {256 * 14, 512 * 14, 256 * 14, 512 * 14};
        try {
            List<Map<String, Object>> excelData = new ArrayList<Map<String, Object>>();
            if (userList != null && userList.size() > 0) {
                for (User user : userList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", user.getId());
                    map.put("userName", user.getUserName());
                    map.put("status", user.getIsDeleted() == 0 ? "正常账号" : "废弃账号");
                    map.put("createTime", DateUtil.getDateString(user.getCreateTime()));
                    excelData.add(map);
                }
            }
            String excelName = "用户数据_" + System.currentTimeMillis();
            PoiUtil.exportFile(excelName, excelHeader, fileds, formats, widths, excelData, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
