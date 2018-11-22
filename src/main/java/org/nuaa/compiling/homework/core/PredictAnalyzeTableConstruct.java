package org.nuaa.compiling.homework.core;

import org.nuaa.compiling.homework.bean.GrammarBean;
import org.nuaa.compiling.homework.constant.GrammarConst;
import org.nuaa.compiling.homework.util.GrammarProductionParser;

import java.util.*;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/11/19 17:29
 */
public class PredictAnalyzeTableConstruct {
    public static Map<String, String> construct(GrammarBean grammar) {
        Map<String, String> table = initTable(grammar);
        // 遍历非终结符进行填表
        for (String notEndLabel : grammar.getNotEndLabelSet()) {
            for (String label : grammar.getFirstSetMap().get(notEndLabel)) {
                // 若该label为$，则需要考虑follow集
                if (GrammarConst.END_LABEL.equals(label)) {
                    // 遍历follow集，构造填表
                    for (String followLabel : grammar.getFollowSetMap().get(notEndLabel)) {
                        table.put(notEndLabel + "_" + followLabel, notEndLabel + "=>$");
                    }
                } else {
                    table.put(notEndLabel + "_" + label, findProduction(notEndLabel, label, grammar));
                }
            }
        }
        return table;
    }

    /**
     * 分析，应在分析预测表构造完成后进行分析
     * @param target
     * @param grammar
     * @return
     */
    public static boolean analyze(String target, GrammarBean grammar) {
        // 输入串为空，默认接收
        if (target == null || target.isEmpty()) {
            return true;
        }

        /**
         * 用于记录分析过程
         */
        List<String[]> processRecord = new LinkedList<>();

        // 构造符号栈和输入串
        LinkedList<String> inputString = new LinkedList<>();
        LinkedList<String> labelStack = new LinkedList<>();
        // 初始化符号栈
        labelStack.push("#");
        labelStack.push(grammar.getStartLabel());
        // 初始化输入串
        for (int i = 0; i < target.length(); i++) {
            inputString.add(String.valueOf(target.charAt(i)));
        }
        inputString.add("#");

        // 初始化分析
        generateRecord(processRecord, labelStack, inputString, "");

        // 匹配直至符号串为空
        while (!labelStack.isEmpty()) {
            // 构造单元格
            String cell = labelStack.getFirst() + "_" + inputString.getFirst();
            String production = grammar.getPredictAnalyzeTable().get(cell);
            // 无法推导，出错
            if (production == null || inputString.isEmpty()) {
                // TODO : 构造出错信息
                generateRecord(processRecord, labelStack, inputString, "error");
                grammar.setProcessRecord(processRecord);
                return false;
            }
            List<String> list = GrammarProductionParser.splitCompleteProductionToLabels(grammar, production);
            // 栈顶出栈
            labelStack.removeFirst();
            // 若不为$, 将推导符号倒序入栈
            if (!list.contains(GrammarConst.END_LABEL)) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    labelStack.push(list.get(i));
                }
            }

            generateRecord(processRecord, labelStack, inputString, production);

            // 同时移除匹配元素
            while (!labelStack.isEmpty() && !inputString.isEmpty() && labelStack.getFirst().equals(inputString.getFirst())) {
                labelStack.removeFirst();
                inputString.removeFirst();
                generateRecord(processRecord, labelStack, inputString, "");
            }
        }

        grammar.setProcessRecord(processRecord);
        // 顺利匹配结束，返回匹配成功
        return true;
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
            table.put(notEnd + "_#", null);
        }
        grammar.setPredictAnalyzeTable(table);
        return table;
    }

    /**
     * 查找由非终结符推出终结符的产生式
     * @param notEndLabel
     * @param endLabel
     * @param grammar
     * @return
     */
    private static String findProduction(String notEndLabel, String endLabel, GrammarBean grammar) {
        List<List<String>> productionList = grammar.getProductionMap().get(notEndLabel);
        // 首先考虑可以直接推出的情况
        for (List<String> list : productionList) {
            if (list.size() > 0 && list.get(0).equals(endLabel)) {
                return GrammarProductionParser.constructProduction(notEndLabel, list);
            }
        }

        // 推出的非终结符的首符集含有非终结符
        for (List<String> list : productionList) {
            if (list.size() > 0 && grammar.getFirstSetMap().get(list.get(0)).contains(endLabel)) {
                return GrammarProductionParser.constructProduction(notEndLabel, list);
            }
        }

        // 未找到，应该出错，因为私有方法，程序不会执行到该步
        return null;
    }

    /**
     * 生成记录
     * @param record
     * @param labelStack
     * @param inputString
     * @param production
     */
    private static void generateRecord(List<String[]> record, LinkedList<String> labelStack, LinkedList<String> inputString, String production) {
        String[] row = new String[4];
        row[0] = String.valueOf(record.size());
        String labelStackString = "";
        for (String in : labelStack) {
            labelStackString = in + labelStackString;
        }
        row[1] = labelStackString;
        StringBuilder inputStringBuilder = new StringBuilder();
        for (String in : inputString) {
            inputStringBuilder.append(in);
        }
        row[2] = inputStringBuilder.toString();
        row[3] = production;
        record.add(row);
    }
}
