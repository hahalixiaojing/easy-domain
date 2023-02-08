package cn.easylib.domain.visual.entity;

import cn.easylib.domain.visual.MockEntity;
import cn.easylib.domain.visual.MockValueObject;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;

public class EntityTest {

    @Test
    public void testField() {

        AbstractEntityFieldFinder abstractEntityFieldFinder = new AbstractEntityFieldFinder() {
            @Override
            void initFieldList() {
                addField(MockEntity::getName, "名称");
                addField(MockEntity::getAgeTest, "年龄");
                addField(MockEntity::getAge, "年龄");
                addField(MockEntity::getMockValueObject, "模拟值对象");
                addField(MockValueObject::getName, "模拟值对象名称");
                addField(MockValueObject::isYes, "模拟值对象Yes");
            }
        };

        EntityParser entityParser = new EntityParser();
        entityParser.registerEntity(MockEntity.class, abstractEntityFieldFinder,
                "模拟测试类",
                "这是一个模拟测试类");


        List<EntityDescriptor> parse = entityParser.parse(MockEntity.class);

        System.out.println(JSON.toJSONString(parse));


    }
}

