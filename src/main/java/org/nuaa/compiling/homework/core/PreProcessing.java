package org.nuaa.compiling.homework.core;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @Author: ToMax
 * @Description: 预处理程序
 * @Date: Created in 2018/10/17 12:01
 */
public class PreProcessing {
    /**
     * 执行将目标文件中的代码行依次读入，去除注释内容、空白字符内容后输出
     * @param inpath
     * @param outpath
     * @throws IOException
     */
    public static void execute(String inpath, String outpath) throws Exception {
        FileInputStream input = new FileInputStream(inpath);
        FileWriter output = new FileWriter(outpath);
        Scanner scanner = new Scanner(input);

        String line = null;
        // 依次处理行
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            line = optimizeLine(scanner, line);
            // 有换行且不去头尾空字符不去空行
            // output.write(line + '\n');
            // 无换行且去头尾空字符
            // output.write(line.trim());
            // 有换行不去头尾空字符去空行
            if(!line.trim().isEmpty()) {
                output.write(line + '\n');
            }
        }

        // close
        input.close();
        output.close();
    }

    /**
     * 去除行内的注释、空白开头、结尾内容
     * @param scanner
     * @param line
     * @return
     */
    private static String optimizeLine(Scanner scanner, String line) {
        // 统计已经出现的引号的数量
        int quotationCount = 0;
        // 遍历行，寻找注释
        for (int i = 0; i < line.length(); i++) {
            // 出现引号，更新计数器
            if (line.charAt(i) == '"') {
                quotationCount++;
            }
            // 出现斜杠，可能是注释
            if (line.charAt(i) == '/') {
                // 继续判断下一个字符，确定为段注释还是行注释
                i++;
                // 下标位移，需要判断是否越界，且该字符不在字符串内
                if (i < line.length() && quotationCount % 2 == 0) {
                    // 若是行注释，且已出现的引号为偶数闭合，则该行剩下内容全为注释，返回
                    if (line.charAt(i) == '/') {
                        return line.substring(0, i - 1);
                    }
                    // 若是段注释，作相应处理
                    if (line.charAt(i) == '*') {
                        return paramCommentProcess(scanner, line, i + 1);
                    }
                } else {
                    // 若是i已到末尾且以单个/结尾说明语法错误，不过这里主要是
                    // 去注释，故而忽略语法错误
                    // 需要换原i
                    i--;
                }
            }
        }
        // 不包含注释，去除头尾空格后返回
        return line;
    }

    /**
     * 清除段注释内的内容
     * @param scanner
     * @param line 段注释开始所在行
     * @param index 段注释开始位置
     * @return
     */
    private static String paramCommentProcess(Scanner scanner, String line, int index) {
        // 获取该行闭合段注释下标
        int temp = line.indexOf("*/", index);
        // 若存在，去除段注释
        if (temp != -1) {
            return (line.substring(0, index - 2)) + optimizeLine(scanner, line.substring(temp + 2));
        }
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            index = line.indexOf("*/");
            if (index != -1) {
                return line.substring(index + 2, line.length());
            }
        }
        // 此时其实段注释未闭合，不过暂时忽略语法错误
        return "";
    }
}
