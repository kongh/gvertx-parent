package models;


import com.gvertx.web.params.WithArgumentExtractor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhaowenlei on 16-4-15.
 */
@WithArgumentExtractor(ReqExtractor.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface CurrentRequest {
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
