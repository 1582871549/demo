/**
 * FileName: CodeCounter
 * Author:   大橙子
 * Date:     2019/7/11 19:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.git;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * <p>
 *     统计区分变更代码
 * </p>
 *
 * @author 大橙子
 * @create 2019/7/11
 * @since 1.0.0
 */
public class CodeCounter {

    private final List<File> javaList;

    private long files = 0;
    private long codeLines = 0;
    private long commentLines = 0;
    private long blankLines = 0;

    public CodeCounter() {
        this.javaList = new ArrayList<>(11);
    }

    /**
     * 代码行数统计
     */
    public static void main(String[] args) {

        // String file = CodeCounter.class.getResource("/").getFile();
        // String path = file.replace("target/classes", "src");

        List<String> pathList = new ArrayList<>();

        pathList.add("D:\\aaa\\org.jacoco.core.src.org.jacoco.core.analysis.Analyzer.java");

        CodeCounter counter = new CodeCounter();

        // 空对象处理
        if (pathList == null || pathList.size() == 0 || Collections.EMPTY_LIST.equals(pathList)) {
            throw new RuntimeException("list can't be empty");
        }

        counter.analyzePathList(pathList);

        counter.countCode();

    }

    private void countCode() {
        for (File file : javaList) {
            count(file);
        }
        System.out.println("统计文件：" + files);
        System.out.println("代码行数：" + codeLines);
        System.out.println("注释行数：" + commentLines);
        System.out.println("空白行数：" + blankLines);
    }

    /**
     * 加载所有java文件到javaList中
     *
     * @param pathList exec文件的文件或文件夹
     */
    private void analyzePathList(List<String> pathList) {

        // 遍历文件或文件夹
        for (String path : pathList) {

            File file = new File(path);

            // 目标不存在则跳过
            if (!file.exists()){
                continue;
            }

            if (file.isDirectory()) {

                List<String> list = new ArrayList<>(11);

                // 遍历该目录下所有文件及文件夹
                File[] files = file.listFiles();

                // 如果目录下不存在文件，则跳过
                if (files == null || files.length == 0) {
                    continue;
                }

                for (File f : files) {
                    list.add(f.getAbsolutePath());
                }

                analyzePathList(list);

            } else {

                // 将匹配文件加载到javaList中
                if (file.getName().endsWith(".java")) {
                    javaList.add(file);
                }
            }
        }
    }

    /**
     * 统计方法
     * @param f
     */
    private void count(File f) {

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            boolean flag = false;
            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.trim(); // 除去注释前的空格
                System.out.println(line);
                if (line.matches("^[ ]*$")) { // 匹配空行
                    blankLines++;
                } else if (line.startsWith("//")) {
                    commentLines++;
                } else if (line.startsWith("/*") && line.endsWith("*/")) {
                    commentLines++;
                } else if (line.startsWith("/*") && !line.endsWith("*/")) {
                    commentLines++;
                    flag = true;
                } else if (flag) {
                    commentLines++;
                    if (line.endsWith("*/")) {
                        flag = false;
                    }
                } else {
                    codeLines++;
                }
            }
            files++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
