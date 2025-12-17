package com.xuexian.webkeshe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuexian.webkeshe.dto.PageDTO;
import com.xuexian.webkeshe.dto.getClassDTO;
import com.xuexian.webkeshe.entity.ClassInfo;
import com.xuexian.webkeshe.entity.Course;
import com.xuexian.webkeshe.entity.Student;
import com.xuexian.webkeshe.mapper.ClassMapper;
import com.xuexian.webkeshe.mapper.StudentMapper;
import com.xuexian.webkeshe.mapper.SpecialtyMapper;
import com.xuexian.webkeshe.service.IClassService;
import com.xuexian.webkeshe.vo.PageVO;
import com.xuexian.webkeshe.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.xuexian.webkeshe.util.Code.REQUEST_SUCCESS;

@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, ClassInfo> implements IClassService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageVO<ClassInfo> getClassList(getClassDTO getClassDTO) {
        PageDTO pageDTO = getClassDTO.getPage();
        String name = getClassDTO.getName();
        Integer speciality = getClassDTO.getSpecialityId();
        // 分页查询
        Page<ClassInfo> page = Page.of(pageDTO.getPageNo(), pageDTO.getPageSize());
        Page<ClassInfo> resultPage = null;
        QueryWrapper<ClassInfo> query = new QueryWrapper<ClassInfo>();
        query.orderByAsc("id");

        if (speciality != null) {
            query.eq("speciality_id", speciality);
        }
        // 关键:or
        if (name != null && !name.isEmpty()) {
            query.and(q -> q
                    .like("class_name", name)
                    .or()
                    .like("class_code", name)
            );
        }

        resultPage = page(page, query);


        // 补充专业名称（关联专业表）
        resultPage.getRecords().forEach(classInfo -> {
            if (classInfo.getSpecialtyId() != null) {
                String specialtyName = specialtyMapper.selectById(classInfo.getSpecialtyId()).getSpecialtyName();
                classInfo.setSpecialtyName(specialtyName); // 需要在ClassInfo实体添加specialtyName字段（非数据库字段）
            }
        });

        return new PageVO<>(resultPage.getTotal(), resultPage.getRecords());
    }

    @Override
    @Transactional
    public Result addClass(ClassInfo classInfo) {
        // 校验参数
        if (classInfo.getClassName() == null || classInfo.getClassCode() == null) {
            return Result.error("班级名称和编号不能为空");
        }

        // 校验班级编号唯一性
        QueryWrapper<ClassInfo> query = new QueryWrapper<>();
        query.eq("class_code", classInfo.getClassCode());
        if (count(query) > 0) {
            return Result.error("班级编号已存在");
        }



        // 保存数据
        save(classInfo);
        return Result.success(REQUEST_SUCCESS, "班级添加成功");
    }

    @Override
    @Transactional
    public Result updateClass(ClassInfo classInfo) {
        // 校验参数
        if (classInfo.getId() == null) {
            return Result.error("班级ID不能为空");
        }

        // 校验班级是否存在
        ClassInfo existing = getById(classInfo.getId());
        if (existing == null) {
            return Result.error("班级不存在");
        }

        // 校验班级编号唯一性（排除当前ID）
        QueryWrapper<ClassInfo> query = new QueryWrapper<>();
        query.eq("class_code", classInfo.getClassCode())
                .ne("id", classInfo.getId());
        if (count(query) > 0) {
            return Result.error("班级编号已存在");
        }

        // 更新时间
        classInfo.setUpdateTime(LocalDateTime.now());

        // 更新数据
        updateById(classInfo);
        return Result.success(REQUEST_SUCCESS, "班级更新成功");
    }

    @Override
    @Transactional
    public Result deleteClass(List<Integer> ids) {
        // 检查是否有关联学生
        for (Integer classId : ids) {
            QueryWrapper<Student> studentQuery = new QueryWrapper<>();
            studentQuery.eq("class_id", classId);
            if (studentMapper.selectCount(studentQuery) > 0) {
                return Result.error("班级下存在学生，无法删除");
            }
        }

        // 批量删除
        removeByIds(ids);
        return Result.success(REQUEST_SUCCESS, "班级删除成功");
    }


}