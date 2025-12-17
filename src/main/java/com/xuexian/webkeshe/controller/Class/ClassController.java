// src/main/java/com/xuexian/webkeshe/controller/ClassController.java
package com.xuexian.webkeshe.controller.Class;

import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.dto.IdsDTO; // 假设已有该DTO（用于接收批量ID）
import com.xuexian.webkeshe.dto.UserDTO;
import com.xuexian.webkeshe.dto.getClassDTO;
import com.xuexian.webkeshe.entity.ClassInfo;
import com.xuexian.webkeshe.service.IClassService;
import com.xuexian.webkeshe.util.UserHolder;
import com.xuexian.webkeshe.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.xuexian.webkeshe.util.Code.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api") // 接口路径前缀（避免与其他接口冲突）
public class ClassController {

    @Autowired
    private IClassService classService;

    // 1. 获取班级列表（带分页和搜索）
    @PostMapping("/getClass")
    public Result getClassList(@RequestBody getClassDTO getClassDTO) {
        // 补充状态码参数（REQUEST_SUCCESS对应200）
        return Result.success(REQUEST_SUCCESS, classService.getClassList(getClassDTO));
    }

    // 2. 新增班级
    @PostMapping("/addClass")
    public Result addClass(@RequestBody ClassInfo classInfo) {
        UserDTO user = UserHolder.getUser();
        if(user.getRole()!= 2){
            return Result.error("权限不足");
        }
        return classService.addClass(classInfo);
    }

    // 3. 修改班级
    @PostMapping("/updateClass")
    public Result updateClass(@RequestBody ClassInfo classInfo) {
        UserDTO user = UserHolder.getUser();
        if(user.getRole()!= 2){
            return Result.error("权限不足");
        }
        return classService.updateClass(classInfo);
    }

    // 4. 批量删除班级
    @PostMapping("/delClass")
    public Result deleteClass(@RequestBody IdsDTO idsDTO) {
        UserDTO user = UserHolder.getUser();
        if(user.getRole()!= 2){
            return Result.error("权限不足");
        }
        return classService.deleteClass(idsDTO.getIds());
    }

}