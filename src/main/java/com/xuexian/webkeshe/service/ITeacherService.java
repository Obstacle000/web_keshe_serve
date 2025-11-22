package com.xuexian.webkeshe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.entity.Student;
import com.xuexian.webkeshe.entity.Teacher;
import com.xuexian.webkeshe.vo.PageVO;
import com.xuexian.webkeshe.vo.Result;

import java.util.List;

public interface ITeacherService extends IService<Teacher> {
    PageVO<Teacher> getTeacherList(PageDTO pageDTO);

    Result addTeacher(Teacher teacher);

    Result updateTeacher(Teacher teacher);

    void removeBatchByIds(List<Integer> ids);
}
