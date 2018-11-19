package org.nuaa.compiling.homework.core;

import org.nuaa.compiling.homework.bean.GrammarBean;
import org.nuaa.compiling.homework.constant.GrammarConst;

import java.util.*;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/11/13 23:46
 */
public class GrammarSetConstruct {
    /**
     * 构建grammar的首符集
     * @param grammar
     * @return
     */
    public static Map<String, Set<String>> constructFirstSet(GrammarBean grammar) {
        // 初始化first集
        Map<String, Set<String>> firstSet = new HashMap<>();
        // 遍历非终结符集，依次构造其首符集
        for (String label : grammar.getNotEndLabelSet()) {
            deepConstructSignleLabelFirstSet(label, grammar, firstSet);
        }
        // 返回
        grammar.setFirstSetMap(firstSet);
        return firstSet;
    }

    /**
     * 构造grammar的follow集
     * @param grammar
     * @after constructFirstSet
     * @return
     */
    public static Map<String, Set<String>> constructFollowSet(GrammarBean grammar) {
        Map<String, Set<String>> followSetMap = new HashMap<>();
        // init
        for (String label : grammar.getNotEndLabelSet()) {
            followSetMap.put(label, new HashSet<>());
            if (label.equals(grammar.getStartLabel())) {
                followSetMap.get(label).add("#");
            }
        }

        // 扩充首符集
        Map<String, Set<String>> firstSetMap = grammar.getFirstSetMap();
        for (String label : grammar.getEndLabelSet()) {
            firstSetMap.put(label, new HashSet<String>(){{add(label);}});
        }
        // 是否产生改变
        boolean flag = true;
        while (flag) {
            flag = false;
            for (String label : grammar.getNotEndLabelSet()) {
                List<List<String>> productionList = grammar.getProductionMap().get(label);
                for (List<String> production : productionList) {
                    for (int i = 0; i < production.size() - 1; i++) {
                        if (grammar.isNotEndLabel(production.get(i))) {
                            int originSize = followSetMap.get(production.get(i)).size();
                            // 将first(next)加入到follow(current)中
                            for (String in : firstSetMap.get(production.get(i + 1))) {
                                if (!in.equals(GrammarConst.END_LABEL)) {
                                    followSetMap.get(production.get(i)).add(in);
                                }
                            }
                            // 更新状态
                            flag = flag || followSetMap.get(production.get(i)).size() > originSize;

                            // check labels after is $ or not
                            if (checkIsEmpty(production.subList(i + 1, production.size()), grammar)) {
                                for (String in : followSetMap.get(label)) {
                                    followSetMap.get(production.get(i)).add(in);
                                }
                            }
                        }
                    }
                    if (grammar.isNotEndLabel(production.get(production.size() - 1))) {
                        int originSize = followSetMap.get(production.get(production.size() - 1)).size();
                        for (String in : followSetMap.get(label)) {
                            followSetMap.get(production.get(production.size() - 1)).add(in);
                        }
                        flag = flag || followSetMap.get(production.get(production.size() - 1)).size() > originSize;
                    }
                }
            }
        }
        grammar.setFollowSetMap(followSetMap);
        return followSetMap;
    }

    /**
     * 构造单个非终结符的首符集
     * @param label
     * @param grammar
     * @param finishSet
     */
    private static void deepConstructSignleLabelFirstSet(String label, GrammarBean grammar, Map<String, Set<String>> finishSet) {
        // 已经构造完，不需要再考虑
        if (finishSet.containsKey(label)) {
            return;
        }
        Set<String> firstSet = new HashSet<>();
        List<List<String>> productionList = grammar.getProductionMap().get(label);
        // 遍历产生式
        for (List<String> production : productionList) {
            // 若产生式的首符号是终结符，直接加入到首符集中
            if (grammar.isEndLabel(production.get(0))) {
                firstSet.add(production.get(0));
            // 若产生式首符是非终结符，将其首符集加入
            } else {
                // 若该非终结符首符集不存在，先构造
                if (!finishSet.containsKey(production.get(0))) {
                    deepConstructSignleLabelFirstSet(production.get(0), grammar, finishSet);
                }
                // 将其首符集加入
                for (String in : finishSet.get(production.get(0))) {
                    firstSet.add(in);
                }
            }
        }
        // 将构造完的结果存入最终集合
        finishSet.put(label, firstSet);
    }

    /**
     * 判断连续的label是否均含有$
     * @param labels
     * @param grammar
     * @return
     */
    private static boolean checkIsEmpty(List<String> labels, GrammarBean grammar) {
        for (String in : labels) {
            if (!grammar.getFirstSetMap().get(in).contains("$")) {
                return false;
            }
        }
        return true;
    }
}
