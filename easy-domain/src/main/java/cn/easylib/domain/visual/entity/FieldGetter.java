package cn.easylib.domain.visual.entity;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public interface FieldGetter<T, R> extends Function<T, R>, Serializable {

    default FieldInfo getFieldName(String description) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Method method = this.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(true);
        SerializedLambda serializedLambda = (SerializedLambda) method.invoke(this);
        String methodName = serializedLambda.getImplMethodName();

        String replace = serializedLambda.getImplClass().replace('/', '.');

        Class<?> aClass = Class.forName(replace);


        int lastIndex = serializedLambda.getInstantiatedMethodType().lastIndexOf("/");
        String returnType = serializedLambda.getInstantiatedMethodType().substring(lastIndex + 1,
                serializedLambda.getInstantiatedMethodType().length() - 1);


        if (methodName.startsWith("get")) {
            methodName = methodName.substring(3);
        } else if (methodName.startsWith("is")) {
            methodName = methodName.substring(2);
        }

        String fixMethodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);

        return new FieldInfo(fixMethodName, description, returnType, aClass);

    }
}
