package com.xuexian.webkeshe.controller.base;

import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.dto.UpdatePasDTO;
import com.xuexian.webkeshe.dto.UpdateStaDTO;

import com.xuexian.webkeshe.dto.UserDTO;
import com.xuexian.webkeshe.entity.College;
import com.xuexian.webkeshe.entity.Specialty;
import com.xuexian.webkeshe.entity.User;
import com.xuexian.webkeshe.mapper.ClassMapper;
import com.xuexian.webkeshe.mapper.CollegeMapper;
import com.xuexian.webkeshe.mapper.SpecialtyMapper;
import com.xuexian.webkeshe.service.IUserService;
import com.xuexian.webkeshe.util.PasswordEncoder;
import com.xuexian.webkeshe.util.UserHolder;
import com.xuexian.webkeshe.vo.PageVO;
import com.xuexian.webkeshe.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.xuexian.webkeshe.util.Code.PASSWORD_ERROR;
import static com.xuexian.webkeshe.util.Code.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api")
@Slf4j
public class BaseController {

    @Autowired
    private IUserService userService;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private SpecialtyMapper specialtyMapper;
    @Autowired
    private ClassMapper classMapper;

    @PostMapping("getAdminInfo")
    public Result getUserList(@RequestBody PageDTO pageDTO) {
        return Result.success(REQUEST_SUCCESS,userService.getUserList(pageDTO));
    }

    @PostMapping("/updateSta")
    public Result updateSta(@RequestBody UpdateStaDTO updateStaDTO) {
        User byId = userService.getById(updateStaDTO.getId());
        byId.setStatus(updateStaDTO.getStatus());
        userService.updateById(byId);
        return Result.success(REQUEST_SUCCESS,null);
    }

    @PostMapping("/updatePas")
    public Result updatePas(@RequestBody UpdatePasDTO updatePasDTO) {
        UserDTO user = UserHolder.getUser();
        User byId = userService.getById(user.getId());



        if(PasswordEncoder.matches(updatePasDTO.getOldPassword(), byId.getPassword())){
            byId.setPassword(PasswordEncoder.encode(updatePasDTO.getNewPassword()));
            userService.updateById(byId);
        }else {
            return Result.error(PASSWORD_ERROR,null);
        }
        return Result.success(REQUEST_SUCCESS,null);
    }

    @GetMapping("/getAllCollege")
    public Result getAllCollege() {

        List<College> all = null;
        try {
            all = collegeMapper.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(REQUEST_SUCCESS,all);
    }

    @PostMapping("/getSpecialtyByCollege")
    public Result getSpecialtyByCollege(Integer id) {
        return Result.success(REQUEST_SUCCESS,specialtyMapper.findAll(id));
    }

    @PostMapping("/getSpecialtyList")
    public Result getSpecialtyList(Integer id) {
        List<Specialty> all = null;
        try {
            all = specialtyMapper.getList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(REQUEST_SUCCESS,all);
    }

    @PostMapping("/getClassBySpecialty")
    public Result getClassBySpecialty(Integer id) {
        return Result.success(REQUEST_SUCCESS,classMapper.findAll(id));
    }


}
