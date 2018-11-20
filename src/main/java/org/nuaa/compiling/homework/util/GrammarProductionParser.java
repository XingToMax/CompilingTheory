package org.nuaa.compiling.homework.util;

import org.nuaa.compiling.homework.bean.GrammarBean;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/11/13 23:32
 */
public class GrammarProductionParser {
    /**
     * 初步拆分产生式，第一个为左边的符号，后续为该符号推出的各产生式
     * @param grammar
     * @param production
     * @return
     */
    public static List<String> getSingleProductionList(GrammarBean grammar, String production) {
        // TODO : production check by regex
        List<String> singleProductionList = new LinkedList<>();
        String[] productionSplit = production.split("=>");
        if (!grammar.isNotEndLabel(productionSplit[0])) {
            // TODO : throw Exception for input invalid
        }
        singleProductionList.add(productionSplit[0]);
        for (String segment : productionSplit[1].split("\\|")) {
            singleProductionList.add(segment);
        }
        return singleProductionList;
    }

    /**
     * 将单个产生式的右部拆分为符号集
     * @param grammar
     * @param production
     * @return
     */
    public static List<String> splitProductionToLabels(GrammarBean grammar, String production) {
        List<String> labels = new LinkedList<>();
        int index = 0;
        String temp = "";
        while (index < production.length()) {
            temp += production.charAt(index);
            if (grammar.isLabel(temp)) {
                labels.add(temp);
                temp = "";
            }
            index++;
        }
        return labels;
    }

    /**
     * 增加适配
     * @param grammar
     * @param production
     * @return
     */
    public static List<String> splitCompleteProductionToLabels(GrammarBean grammar, String production) {
        // TODO : 输入校验
        production = production.split("=>")[1];
        return splitProductionToLabels(grammar, production);
    }


    /**
     * 构造完整的产生式
     * @param label
     * @param segment
     * @return
     */
    public static String constructProduction(String label, List<String> segment) {
        StringBuilder builder = new StringBuilder();
        builder.append(label);
        builder.append("=>");
        for (String in : segment) {
            builder.append(in);
        }
        return builder.toString();
    }
}
