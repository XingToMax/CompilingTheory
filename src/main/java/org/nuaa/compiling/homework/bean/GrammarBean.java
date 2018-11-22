package org.nuaa.compiling.homework.bean;

import org.nuaa.compiling.homework.util.GrammarProductionParser;

import java.util.*;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/11/13 23:13
 */
public class GrammarBean {
    /**
     * 所有label的集合
     */
    private Set<String> labelSet;
    /**
     * 非终结符集合
     */
    private Set<String> notEndLabelSet;
    /**
     * 终结符集合
     */
    private Set<String> endLabelSet;

    /**
     * 开始符号
     */
    private String startLabel;

    /**
     * 产生式
     */
    private List<String> production;

    /**
     * 具体拆分的产生式表
     */
    private Map<String, List<List<String>>> productionMap;

    private Map<String, Set<String>> firstSetMap;
    private Map<String, Set<String>> followSetMap;

    /**
     * 预测分析表
     */
    private Map<String, String> predictAnalyzeTable;
    private List<String[]> processRecord;


    public GrammarBean() {
        this.labelSet = new HashSet<>();
        this.notEndLabelSet = new HashSet<>();
        this.endLabelSet = new HashSet<>();
        this.production = new LinkedList<>();
        productionMap = new HashMap<>();
    }

    public GrammarBean(String endLabelString, String notEndLabelString, String startLabel, List<String> production) {
        this();
        initEndLabelSet(endLabelString);
        initNotEndLabelSet(notEndLabelString);
        if (!notEndLabelSet.contains(startLabel)) {
            // TODO : throw Exception for input invalid
        }
        this.startLabel = startLabel;
        this.production = production;
        for (String single : production) {
            List<String> list = GrammarProductionParser.getSingleProductionList(this, single);
            List<List<String>> split = new LinkedList<>();
            for (String in : list.subList(1, list.size())) {
                split.add(GrammarProductionParser.splitProductionToLabels(this, in));
            }
            productionMap.put(list.get(0), split);
        }
    }

    /**
     * 是否为终结符
     * @param label
     * @return
     */
    public boolean isEndLabel(String label) {
        return endLabelSet.contains(label);
    }

    /**
     * 是否非终结符
     * @param label
     * @return
     */
    public boolean isNotEndLabel(String label) {
        return notEndLabelSet.contains(label);
    }

    public boolean isLabel(String label) {
        return labelSet.contains(label);
    }

    /**
     * 初始化终极符集，输入符号使用","拼接成字符串
     * @param endLabelDescription
     */
    private void initEndLabelSet(String endLabelDescription) {
        String[] labels = endLabelDescription.split(",");
        for (String label : labels) {
            addEnd(label);
        }
    }

    /**
     * 初始化非终结符集
     * @param notEndLabelDescription
     */
    private void initNotEndLabelSet(String notEndLabelDescription) {
        String[] labels = notEndLabelDescription.split(",");
        for (String label : labels) {
            addNotEnd(label);
        }
    }

    /**
     * 增加非终结符
     * @param label
     */
    private void addNotEnd(String label) {
        if (!notEndLabelSet.contains(label)) {
            notEndLabelSet.add(label);
            addLabel(label);
        }
    }
    /**
     * 增加终结符
     * @param label
     */
    private void addEnd(String label) {
        if (!endLabelSet.contains(label)) {
            endLabelSet.add(label);
            addLabel(label);
        }
    }

    private void addLabel(String label) {
        if (!labelSet.contains(label)) {
            labelSet.add(label);
        }
    }

    // ===============================================
    // getter and setter
    //

    public Set<String> getNotEndLabelSet() {
        return notEndLabelSet;
    }

    public void setNotEndLabelSet(Set<String> notEndLabelSet) {
        this.notEndLabelSet = notEndLabelSet;
    }

    public Set<String> getEndLabelSet() {
        return endLabelSet;
    }

    public void setEndLabelSet(Set<String> endLabelSet) {
        this.endLabelSet = endLabelSet;
    }

    public String getStartLabel() {
        return startLabel;
    }

    public void setStartLabel(String startLabel) {
        this.startLabel = startLabel;
    }

    public List<String> getProduction() {
        return production;
    }

    public void setProduction(List<String> production) {
        this.production = production;
    }

    public Set<String> getLabelSet() {
        return labelSet;
    }

    public void setLabelSet(Set<String> labelSet) {
        this.labelSet = labelSet;
    }

    public Map<String, List<List<String>>> getProductionMap() {
        return productionMap;
    }

    public void setProductionMap(Map<String, List<List<String>>> productionMap) {
        this.productionMap = productionMap;
    }

    public Map<String, Set<String>> getFirstSetMap() {
        return firstSetMap;
    }

    public void setFirstSetMap(Map<String, Set<String>> firstSetMap) {
        this.firstSetMap = firstSetMap;
    }

    public Map<String, Set<String>> getFollowSetMap() {
        return followSetMap;
    }

    public void setFollowSetMap(Map<String, Set<String>> followSetMap) {
        this.followSetMap = followSetMap;
    }

    public Map<String, String> getPredictAnalyzeTable() {
        return predictAnalyzeTable;
    }

    public void setPredictAnalyzeTable(Map<String, String> predictAnalyzeTable) {
        this.predictAnalyzeTable = predictAnalyzeTable;
    }

    public List<String[]> getProcessRecord() {
        return processRecord;
    }

    public void setProcessRecord(List<String[]> processRecord) {
        this.processRecord = processRecord;
    }
}
