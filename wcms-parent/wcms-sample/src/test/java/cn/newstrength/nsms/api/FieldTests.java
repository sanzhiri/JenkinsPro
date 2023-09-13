/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.nsms.api;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.field.api.dto.Field;
import cn.newstrength.wcms.field.api.service.FieldService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 类描述
 * <p>详细描述</p>
 *
 * @author kyrie 2021/1/27 3:33 下午
 * @since jdk1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FieldTests {
    private final static Logger logger = LoggerFactory.getLogger(FieldTests.class);
    @Autowired
    private FieldService fieldService;

    @Test
    public void queryFieldByInfoTypeId(){
        Long infoTypeId = 1L;
        List<Field> fields = fieldService.createFieldQuery().infoTypeId(infoTypeId).list();
        logger.info("信息类型 {} 下的扩展字段个数有 {}",infoTypeId,fields.size());
        fields.forEach(item->{
            logger.info(item.toString());
        });
    }

    @Test
    public void querySingle(){
        Long infoTypeId = 1L;
        String field = "fullName";
        Field fieldObj = fieldService.createFieldQuery().infoTypeId(infoTypeId).field(field).singleResult();
        logger.info("单个扩展字段查询结果 {}",fieldObj.toString());
    }
}
