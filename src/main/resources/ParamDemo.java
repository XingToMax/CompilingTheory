package compiling.homework.tomax;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/10/17 21:19
 */
public class ParamDemo {
    /**
     * name
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        //
        new ParamDemo().setName("tomax");
    }
}
