package org.nuaa.compiling.homework.core;

import org.junit.Before;
import org.junit.Test;
import org.nuaa.compiling.homework.bean.GrammarBean;

import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/11/20 17:32
 */
public class TestLL {
    GrammarBean grammar = null;

    @Before
    public void setup() {
        List<String> prduction = new LinkedList<>();
        prduction.add("E=>TP");
        prduction.add("P=>+TP|$");
        prduction.add("T=>FQ");
        prduction.add("Q=>*FQ|$");
        prduction.add("F=>(E)|i");
        grammar = new GrammarBean( "+,$,*,(,),i","E,T,P,Q,F", "E",prduction);
        GrammarSetConstruct.constructFirstSet(grammar);
        GrammarSetConstruct.constructFollowSet(grammar);
        PredictAnalyzeTableConstruct.construct(grammar);
    }

    @Test
    public void test() {
        assertEquals(true, PredictAnalyzeTableConstruct.analyze("(i*i+i)", grammar));
        assertEquals(false, PredictAnalyzeTableConstruct.analyze("i(i*i+i)", grammar));
    }
}
