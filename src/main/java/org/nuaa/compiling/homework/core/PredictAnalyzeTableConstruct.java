package org.nuaa.compiling.homework.core;

import org.nuaa.compiling.homework.bean.GrammarBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/11/19 17:29
 */
public class PredictAnalyzeTableConstruct {
    public static void construct(GrammarBean grammar) {
        Map<String, String> table = initTable(grammar);
        
    }

    /**
     * 初始化table
     * @param grammar
     * @return
     */
    private static Map<String, String> initTable(GrammarBean grammar) {
        Map<String, String> table = new HashMap<>();
        for (String notEnd : grammar.getNotEndLabelSet()) {
            for (String end : grammar.getEndLabelSet()) {
                table.put(notEnd + "_" + end, null);
            }
        }
        grammar.setPredictAnalyzeTable(table);
        return table;
    }
}
