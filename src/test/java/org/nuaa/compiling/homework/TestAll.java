package org.nuaa.compiling.homework;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.nuaa.compiling.homework.core.TestLL;
import org.nuaa.compiling.homework.core.TestPreProcessing;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/11/20 15:34
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestLL.class, TestPreProcessing.class})
public class TestAll {
}
