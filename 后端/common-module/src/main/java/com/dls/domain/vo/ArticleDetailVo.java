package com.dls.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVo {
    private Long id;
    private String title;
    private Long viewCount;
    private String summary;
    //文章内容
    private String content;
    private String categoryName;
    private Long categoryId;
    private String thumbnail; // 缩略图
    private Date createTime;

}
