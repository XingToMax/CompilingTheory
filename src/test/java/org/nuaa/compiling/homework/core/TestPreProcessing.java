package org.nuaa.compiling.homework.core;

import org.junit.Test;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/11/20 15:36
 */
public class TestPreProcessing {
    String resourcePath = this.getClass().getResource("/").getPath();
    @Test
    public void test() throws Exception {
        PreProcessing.execute(resourcePath + "XmlParser.java", resourcePath + "XmlParser.java.pre");
        PreProcessing.execute(resourcePath + "Demo.java", resourcePath + "Demo.java.pre");
        PreProcessing.execute(resourcePath + "ParamDemo.java", resourcePath + "ParamDemo.java.pre");
        PreProcessing.execute(resourcePath + "ComplexDemo.java", resourcePath + "ComplexDemo.java.pre");
    }
}
