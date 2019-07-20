package com.king.doge.web;

import com.king.doge.comm.Constants;
import com.king.doge.comm.Result;
import com.king.doge.comm.ResultGenerator;
import com.king.doge.model.Picture;
import com.king.doge.model.User;
import com.king.doge.service.PictureService;
import com.king.doge.utiles.PageResult;
import com.king.doge.web.annotation.TokenToUser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 图片管理
 * Created by zhuru on 2019/1/10.
 */
@RestController
@RequestMapping("/fly/pictures")
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 查询图片列表
     * @param param
     * @return
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> param) {
        if (StringUtils.isEmpty(param.get("page")) || StringUtils.isEmpty(param.get("limit"))) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数错误");
        }
        PageResult pageResult = pictureService.findPictures(param);
        if (pageResult == null) {
            return ResultGenerator.genFailResult("查询失败！");
        } else {
            return ResultGenerator.genSuccessResult(pageResult);
        }
    }

    /**
     * 保存图片
     * @param picture
     * @param loginUser
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody Picture picture, @TokenToUser User loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "请先登陆！");
        }
        if (StringUtils.isEmpty(picture.getPath()) || StringUtils.isEmpty(picture.getRemark())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数错误！");
        }
        if (pictureService.save(picture).getId() > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("保存失败");
        }
    }

    /**
     * 批量保存图片
     * @param pictures
     * @param loginUser
     * @return
     */
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Result saveBatch(@RequestBody Picture[] pictures, @TokenToUser User loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "请先登陆！");
        }
        if (pictures == null || pictures.length < 1) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数错误！");
        }
        if (pictureService.saveBatch(pictures)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("保存失败");
        }
    }

    /**
     * 根据id删除图片
     * @param id
     * @param loginUser
     * @return
     */
    @RequestMapping("/deleteById")
    public Result delete(@RequestBody Long id, @TokenToUser User loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "请先登录");
        }
        if (id < 1) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数错误");
        }
        if (pictureService.deleteById(id) > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    /**
     * 批量删除图片
     * @param ids
     * @param loginUser
     * @return
     */
    @RequestMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody Long[] ids, @TokenToUser User loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "请先登录");
        }
        if (ids == null || ids.length < 1) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数错误");
        }
        if (pictureService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    /**
     * 根据id获取图片信息
     * @param id
     * @param loginUser
     * @return
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id, @TokenToUser User loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "未登录！");
        }
        if (id < 1) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数异常！");
        }
        Picture picture = pictureService.findById(id);
        if (picture == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数异常！");
        }
        return ResultGenerator.genSuccessResult(picture);
    }

    /**
     * 更新图片
     * @param picture
     * @param loginUser
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Picture picture, @TokenToUser User loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "请先登陆");
        }
        if (picture.getId() == null || StringUtils.isEmpty(picture.getPath()) || StringUtils.isEmpty(picture.getRemark())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数错误");
        }
        Picture tempPicture = pictureService.findById(picture.getId());
        if (tempPicture == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数异常！");
        }
        if (pictureService.save(picture).getId() > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("更新失败");
        }
    }
}
