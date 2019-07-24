/**
 * FileName: Test
 * Author:   大橙子
 * Date:     2019/7/23 9:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.git;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/7/23
 * @since 1.0.0
 */
public class Test {

    private List<Integer> infos = new ArrayList<>();

    private List<Range> method = new ArrayList<>();

    {
        for (int i = 130; i > 0; i--) {
            infos.add(i);
        }
    }

    {
        method.add(new Range("save", 20, 31));
        method.add(new Range("update", 36, 53));
        method.add(new Range("find", 3, 18));
        method.add(new Range("delete", 60, 100));
    }

    public static void main(String[] args) {

        Test test = new Test();

        // 有序集合 模拟差异行数据
        List<Integer> infos = test.getInfos();
        List<Range> method = test.getMethod();

        infos.sort(null);

        List<Integer> ends = new ArrayList<>();
        Map<String, Range> rangeMap = new HashMap<>();

        for (Range range : method) {

            int begin = range.getBegin();
            int end = range.getEnd();

            ends.add(end);
            rangeMap.put(String.valueOf(end), range);
        }

        ends.sort(null);

        Map<String, String> map = new HashMap<>();

        for (Integer endLine : ends) {

            // 有序范围
            Range rangeSort = rangeMap.get(String.valueOf(endLine));

            int begin = rangeSort.getBegin();
            int end = rangeSort.getEnd();
            String name = rangeSort.getName();

            System.out.println(rangeSort);

            int bi;
            int ei;

            while (end >= begin) {

                if ((bi = infos.indexOf(begin)) == -1) {
                    begin++;
                }
                if ((ei = infos.indexOf(end)) == -1) {
                    end--;
                }
                if (bi != -1 && ei != -1) {
                    map.put(name, bi + "," + ei);
                    break;
                }
            }
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {

            String name = entry.getKey();
            String[] index = Pattern.compile(",").split(entry.getValue());

            List<Integer> subList = infos.subList(Integer.parseInt(index[0]), Integer.parseInt(index[1]) + 1);

            Iterator<Integer> it = infos.iterator();
            while (it.hasNext()) {
                Integer next = it.next();

            }
            System.out.println("subList:   " + subList);
        }

        System.out.println("info sort:   " + infos);
        System.out.println("ends sort:   " + ends);
        System.out.println("map:   " + map);

    }

    static class Range {

        private String name;
        private int begin;
        private int end;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBegin() {
            return begin;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public Range(String name, int begin, int end) {
            this.name = name;
            this.begin = begin;
            this.end = end;
        }

        @Override
        public String toString() {
            return "Range{" +
                    "name='" + name + '\'' +
                    ", begin=" + begin +
                    ", end=" + end +
                    '}';
        }
    }

    public List<Integer> getInfos() {
        return infos;
    }

    public void setInfos(List<Integer> infos) {
        this.infos = infos;
    }

    public List<Range> getMethod() {
        return method;
    }

    public void setMethod(List<Range> method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Test{" +
                "infos=" + infos +
                ", method=" + method +
                '}';
    }
}
