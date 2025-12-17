package com.xuexian.webkeshe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.dto.getClassDTO;
import com.xuexian.webkeshe.entity.ClassInfo;
import com.xuexian.webkeshe.vo.PageVO;
import com.xuexian.webkeshe.vo.Result;

import java.util.List;

public interface IClassService extends IService<ClassInfo> {
    // 分页查询班级列表
    PageVO<ClassInfo> getClassList(getClassDTO getClassDTO);

    // 新增班级
    Result addClass(ClassInfo classInfo);

    // 修改班级
    Result updateClass(ClassInfo classInfo);

    // 批量删除班级
    Result deleteClass(List<Integer> ids);


}