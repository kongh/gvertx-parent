package aop;


import net.sf.cglib.proxy.Enhancer;

/**
 * Created by wangziqing on 17/3/9.
 */
public class Access {

    public static void main(String[] args) {
        System.setProperty("cglib.debugLocation", "/Users/wangziqing/github/gvertx-parent/gvertx-metrics/target/classes");

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserServiceImpl.class);
        enhancer.setCallback(new MyMethodInterceptor());

        UserServiceImpl userService1 = (UserServiceImpl)enhancer.create();
        UserServiceImpl userService2 = (UserServiceImpl)enhancer.create();
        System.out.println(userService1);
        System.out.println(userService2);
    }
}
