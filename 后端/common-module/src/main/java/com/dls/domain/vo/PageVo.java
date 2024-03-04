package com.dls.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {
    // 内容
    private List rows;
    // 总条数
    private Long total;
}
