package org.nuaa.compiling.homework;

import org.nuaa.compiling.homework.bean.GrammarBean;
import org.nuaa.compiling.homework.core.GrammarSetConstruct;
import org.nuaa.compiling.homework.core.PredictAnalyzeTableConstruct;
import org.nuaa.compiling.homework.gui.LLAnalyze;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/11/13 23:13
 */
public class App {
    public static void main(String[] args) {
        // LL(1)分析
        LLAnalyze.execute();
    }
}
