package models;


import com.gvertx.core.params.WithArgumentExtractor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhaowenlei on 16-4-15.
 */
@WithArgumentExtractor(ReqExtractor2.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface CurrentRequest2 {
    /**
     * 是否需要登录验证
     * @return
     */
    boolean authenticated() default true;

    /**
     * 是否需要签名
     * @return
     */
    boolean signatured() default true;
}
