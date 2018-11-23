package org.nuaa.compiling.homework.gui;

import org.nuaa.compiling.homework.bean.GrammarBean;
import org.nuaa.compiling.homework.constant.GrammarConst;
import org.nuaa.compiling.homework.core.GrammarSetConstruct;
import org.nuaa.compiling.homework.core.PredictAnalyzeTableConstruct;
import org.nuaa.compiling.homework.util.GrammarProductionParser;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2018/11/21 21:13
 */
public class LLAnalyze {
    private JTabbedPane tabbedPane1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextArea textArea1;
    private JButton buildButton;
    private JList list2;
    private JList list1;
    private JTable table1;
    private JTextField textField4;
    private JTextField textField3;
    private JButton judgeButton;
    private JTable table2;
    private JLabel notEndLabelText;
    private JLabel endLabelText;
    private JLabel startLabelText;
    private JLabel productionLabelText;
    private JLabel inputStringLabel;
    private JTextArea textArea4;
    private JTable table3;
    private JTable table4;
    private JButton importButton;
    private JLabel statusIcon;
    private JPanel inputPanel;
    private JScrollPane firstPanel;
    private JScrollPane followPanel;
    private JScrollPane prePanel;
    private JPanel appPanel;
    private JScrollPane anaPanel;
    private GrammarBean grammar;

    public static void execute() {
        JFrame frame = new JFrame("编译原理演示");
        LLAnalyze analyze = new LLAnalyze();
        frame.setContentPane(analyze.tabbedPane1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(3200, 1600));
        frame.pack();
        frame.setVisible(true);

        // modify ui
        Font font = new Font(null, Font.BOLD, 40);
        analyze.tabbedPane1.setFont(font);
        analyze.endLabelText.setFont(font);
        analyze.notEndLabelText.setFont(font);
        analyze.startLabelText.setFont(font);
        analyze.productionLabelText.setFont(font);
        analyze.textField1.setFont(font);
        analyze.textField2.setFont(font);
        analyze.textField4.setFont(font);
        analyze.textArea1.setFont(font);
        analyze.buildButton.setFont(font);
        analyze.table1.setFont(font);
        analyze.table2.setFont(font);
        analyze.table3.setFont(font);
        analyze.table4.setFont(font);
        analyze.judgeButton.setFont(font);
        analyze.importButton.setFont(font);
        analyze.textArea4.setFont(font);
        analyze.textArea4.setLineWrap(true);
        analyze.textArea4.setWrapStyleWord(true);
        analyze.inputStringLabel.setFont(font);
        analyze.table1.getTableHeader().setPreferredSize(new Dimension(analyze.table1.getTableHeader().getWidth(), 50));
        analyze.table2.getTableHeader().setPreferredSize(new Dimension(analyze.table1.getTableHeader().getWidth(), 50));
        analyze.table3.getTableHeader().setPreferredSize(new Dimension(analyze.table1.getTableHeader().getWidth(), 50));
        analyze.table4.getTableHeader().setPreferredSize(new Dimension(analyze.table1.getTableHeader().getWidth(), 50));
        analyze.table1.getTableHeader().setFont(font);
        analyze.table2.getTableHeader().setFont(font);
        analyze.table3.getTableHeader().setFont(font);
        analyze.table4.getTableHeader().setFont(font);
        analyze.table1.setRowHeight(50);
        analyze.table2.setRowHeight(50);
        analyze.table3.setRowHeight(50);
        analyze.table4.setRowHeight(50);
        analyze.inputPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 300, 50));
        analyze.firstPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 300, 50));
        analyze.followPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 300, 50));
        analyze.prePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 300, 50));
        analyze.appPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 300, 50));
        analyze.anaPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 300, 50));
        analyze.tabbedPane1.repaint();

        analyze.buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String notEndLabel = analyze.textField1.getText();
                String endLabel = analyze.textField2.getText();
                String production = analyze.textArea1.getText();
                String startLabel = analyze.textField4.getText();

                System.out.println(notEndLabel);
                System.out.println(endLabel);
                System.out.println(production);
                System.out.println(startLabel);
                List<String> productionList = new ArrayList<>();
                String[] list = production.split("\n");
                for (String in : list) {
                    productionList.add(in);
                }
                GrammarBean grammar = null;
                try {
                    grammar = new GrammarBean(endLabel, notEndLabel, startLabel, productionList);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(analyze.tabbedPane1, "组建失败，初始化语法错误", "组建状态",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                analyze.setGrammar(grammar);
                Map<String, Set<String>> firstSet = null;
                try {
                    firstSet = GrammarSetConstruct.constructFirstSet(grammar);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(analyze.tabbedPane1, "组建失败，构建FIRST语法错误", "组建状态",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String[][] firstTable = new String[firstSet.size()][2];
                int rowNum = 0;
                for (Map.Entry<String, Set<String>> entry : firstSet.entrySet()) {
                    firstTable[rowNum][0] = entry.getKey();
                    StringBuilder builder = new StringBuilder();
                    for (String in : entry.getValue()) {
                        builder.append(in);
                        builder.append(" ");
                    }
                    firstTable[rowNum][1] = builder.toString();
                    rowNum++;
                }
                analyze.table3.setModel(new DefaultTableModel(
                        firstTable, new String[]{"符号", "集合"}
                ));

                Map<String, Set<String>> followSet = null;
                try {
                    followSet = GrammarSetConstruct.constructFollowSet(grammar);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(analyze.tabbedPane1, "组建失败，构建FOLLOW语法错误", "组建状态",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String[][] followTable = new String[followSet.size()][2];
                rowNum = 0;
                for (Map.Entry<String, Set<String>> entry : followSet.entrySet()) {
                    followTable[rowNum][0] = entry.getKey();
                    StringBuilder builder = new StringBuilder();
                    for (String in : entry.getValue()) {
                        builder.append(in);
                        builder.append(" ");
                    }
                    followTable[rowNum][1] = builder.toString();
                    rowNum++;
                }
                analyze.table4.setModel(
                        new DefaultTableModel(
                                followTable, new String[]{
                                    "符号", "集合"
                                }
                        )
                );
                try {
                    PredictAnalyzeTableConstruct.construct(grammar);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(analyze.tabbedPane1, "组建失败，构建分析预测表语法错误", "组建状态",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<String> head = new ArrayList<>();
                head.add(" ");
                for (String in : grammar.getEndLabelSet()) {
                    head.add(in);
                }
                head.remove(GrammarConst.END_LABEL);
                head.add("#");
                List<String> row = new ArrayList<>();
                for (String in : grammar.getNotEndLabelSet()) {
                    row.add(in);
                }
                Object[][] objects = new Object[row.size()][head.size() + 1];
                for (int i = 0; i < row.size(); i++) {
                    objects[i][0] = row.get(i);
                    for (int j = 1; j < head.size(); j++) {
                        String temp = grammar.getPredictAnalyzeTable().get(row.get(i) + "_" + head.get(j));
                        objects[i][j] = temp != null ? temp : "";
                    }
                }
                DefaultTableModel model = new DefaultTableModel(
                        objects, head.toArray()
                );
                analyze.table1.setModel(model);
                analyze.table1.repaint();
                frame.repaint();
                JOptionPane.showMessageDialog(analyze.tabbedPane1, "组建成功", "组建状态",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        analyze.judgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputString = analyze.textArea4.getText();
                boolean result = PredictAnalyzeTableConstruct.analyze(inputString, analyze.getGrammar());
//                if (result) {
//                    JOptionPane.showMessageDialog(analyze.tabbedPane1, "accept", "状态",JOptionPane.WARNING_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(analyze.tabbedPane1, "not accept", "状态",JOptionPane.WARNING_MESSAGE);
//                }

                List<String[]> record = analyze.getGrammar().getProcessRecord();
                Object[][] table = new Object[record.size()][4];
                int rowNum = 0;
                for (String[] row : record) {
                    int colNum = 0;
                    for (String cell : row) {
                        table[rowNum][colNum] = cell;
                        colNum++;
                    }
                    rowNum++;
                }
                table[table.length - 1][3] = result ? "accept" : "error";
                analyze.table2.setModel(new DefaultTableModel(
                        table, new Object[]{"步骤", "符号栈", "输入串", "所用产生式"}
                ));
                analyze.tabbedPane1.setSelectedIndex(5);

                if (result) {
                    analyze.statusIcon.setIcon(new ImageIcon(this.getClass().getResource("/").getPath() + "right.png"));
                } else {
                    analyze.statusIcon.setIcon(new ImageIcon(this.getClass().getResource("/").getPath() + "error.png"));
                }
                analyze.table2.repaint();
            }
        });

        analyze.importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file.isDirectory()){
                    System.out.println("文件夹:"+file.getAbsolutePath());
                }else if(file.isFile()){
                    System.out.println("文件:"+file.getAbsolutePath());
                }

                try {
                    FileInputStream inputStream = new FileInputStream(file.getAbsolutePath());
                    Scanner scanner = new Scanner(inputStream);

                    analyze.textField1.setText(filterNull(scanner.nextLine()));
                    analyze.textField2.setText(filterNull(scanner.nextLine()));
                    analyze.textField4.setText(filterNull(scanner.nextLine()));
                    StringBuilder builder = new StringBuilder();
                    while (scanner.hasNext()) {
                        String line = scanner.nextLine();
                        if (!line.contains("=>")) {
                            break;
                        }
                        builder.append(line);
                        builder.append("\n");
                    }
                    analyze.textArea1.setText(builder.toString());
                    inputStream.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static String filterNull(String content) {
        return content != null ? content : "";
    }

    public GrammarBean getGrammar() {
        return grammar;
    }

    public void setGrammar(GrammarBean grammar) {
        this.grammar = grammar;
    }

}
