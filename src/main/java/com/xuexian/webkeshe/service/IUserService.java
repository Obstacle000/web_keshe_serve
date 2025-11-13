package com.xuexian.webkeshe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuexian.webkeshe.dto.AdminDTO;
import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.entity.User;
import com.xuexian.webkeshe.vo.PageVO;
import com.xuexian.webkeshe.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IUserService  extends IService<User> {
    List<String> getUserRoles(Long userId);



    Result addAdmin(AdminDTO adminDTO);


    PageVO<User> getUserList(PageDTO pageDTO);
}
