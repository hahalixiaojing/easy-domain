package cn.easylib.domain.visual.entity;

import cn.easylib.domain.visual.EntityItem;
import cn.easylib.domain.visual.MockEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityTest {

    @Test
    public void testField() {

        AbstractEntityFieldFinder abstractEntityFieldFinder = new AbstractEntityFieldFinder() {
            @Override
            protected void initFieldList() {
//                addField(MockEntity::getId,"业务唯一标识");
//                addField(MockEntity::getName, "名称");
//                addField(MockEntity::getAgeTest, "年龄");
//                addField(MockEntity::getAge, "年龄");
                addField(MockEntity::getMockValueObject, "模拟值对象");
//                addField(MockValueObject::getName, "模拟值对象名称");
//                addField(MockValueObject::isYes, "模拟值对象Yes");
//                addField(MockEntity::getEntityItems, "集合测试", EntityItem.class);
                addField(EntityItem::getA, "A");
            }
        };

        EntityParser entityParser = new EntityParser();
        entityParser.registerEntity(MockEntity.class, abstractEntityFieldFinder);


        List<EntityDescriptor> parse = entityParser.parse(MockEntity.class);

        System.out.println(JSON.toJSONString(parse, SerializerFeature.PrettyFormat));


    }

    @Test
    public void actionParamTest() {
        EntityTest2 entityTest2 = new EntityTest2(1L, "张三");

        entityTest2.updateForUse("李四");

        Boolean validate = entityTest2.validate(new EntityTest2EntityRule());



    }

    @Test
    public void regXTest() {
        String test = "(Lcn/easylib/domain/visual/MockEntity;)Ljava/lang/Long;";


        Pattern compile = Pattern.compile("\\(L(.+);\\)");

        Matcher matcher = compile.matcher(test);

        System.out.println(matcher.group(1));

    }
}

