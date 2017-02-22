package models;

/**
 * Created by wangziqing on 17/2/20.
 */
public class ReqContext {
    private String name;

    public ReqContext(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
