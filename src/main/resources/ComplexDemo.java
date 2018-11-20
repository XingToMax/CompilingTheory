package compiling.homework.tomax;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/10/17 21:49
 */
public class ComplexDemo {
    // main
    public static void main(String[] args) {
        int i = 1;
        /**/String name = "//"; /*branch */ if /* 段注释 */(i /*段注释*/% 2 == 0) {
            System.out.println("odd");
        } else {String str = "/**/"; /*add*/ i++/*段注释*/; str = "//"; // 行注释
            System.out.println();
        }
    }
}
