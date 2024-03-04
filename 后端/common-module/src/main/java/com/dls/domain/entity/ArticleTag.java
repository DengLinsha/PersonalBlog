package com.dls.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="article_tag")
//新增博客文章
public class ArticleTag implements Serializable {

    // 确保序列化和反序列化操作能够在类的结构发生变化时正确地进行版本匹配
    private static final long serialVersionUID = 625337492348897098L;

    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 标签id
     */
    private Long tagId;

}