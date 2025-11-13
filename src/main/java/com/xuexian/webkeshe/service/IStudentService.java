package com.xuexian.webkeshe.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.entity.Student;
import com.xuexian.webkeshe.entity.User;
import com.xuexian.webkeshe.vo.PageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuexian.webkeshe.vo.Result;

import java.util.List;

import static com.baomidou.mybatisplus.extension.toolkit.Db.page;

public interface IStudentService {

    PageVO<Student> getStudentList(PageDTO pageDTO);

    Result addStu(Student student);

    void removeBatchByIds(List<Integer> ids);

    Result updateStu(Student student);
}
