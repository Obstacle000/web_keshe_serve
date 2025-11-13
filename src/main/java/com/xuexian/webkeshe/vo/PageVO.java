package com.xuexian.webkeshe.vo;

import com.xuexian.webkeshe.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class PageVO<T> {
    private Long total;
    private List<T> data;
}
