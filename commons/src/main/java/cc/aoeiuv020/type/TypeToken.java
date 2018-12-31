package cc.aoeiuv020.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 用来获得泛型参数类型，
 * <p>
 * Created by AoEiuV020 on 2018.09.07-22:35:35.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class TypeToken<T> {
    public final Type type;

    protected TypeToken() {
        type = getSuperclassTypeParameter();
    }

    private Type getSuperclassTypeParameter() {
        Type superclass = getClass().getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }
}
