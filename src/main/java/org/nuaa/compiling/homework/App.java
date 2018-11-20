package org.nuaa.compiling.homework;

import org.nuaa.compiling.homework.bean.GrammarBean;
import org.nuaa.compiling.homework.core.GrammarSetConstruct;
import org.nuaa.compiling.homework.core.PredictAnalyzeTableConstruct;

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
        List<String> prduction = new LinkedList<>();
        prduction.add("E=>TP");
        prduction.add("P=>+TP|$");
        prduction.add("T=>FQ");
        prduction.add("Q=>*FQ|$");
        prduction.add("F=>(E)|i");
        GrammarBean grammar = new GrammarBean( "+,$,*,(,),i","E,T,P,Q,F", "E",prduction);
        GrammarSetConstruct.constructFirstSet(grammar);
        GrammarSetConstruct.constructFollowSet(grammar);
        PredictAnalyzeTableConstruct.construct(grammar);
        System.out.println(PredictAnalyzeTableConstruct.analyze("(i*i+i)", grammar));
    }
}
