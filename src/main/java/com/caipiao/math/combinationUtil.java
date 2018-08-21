package com.caipiao.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangjj
 * @create 2018-08-13 16:34
 **/
public class combinationUtil {

    /**
     * 组合选择（从列表中选择n个组合）
     * @param dataList 待选列表
     * @param n 选择个数
     * @author zhangjj
     * @Date 2018/8/13 16:34
     * @return
     * @exception
     *
     */
    public static List<List<String>> combinationSelect(String[] dataList, int n) {
        System.out.println(String.format("C(%d, %d) = %d",
                dataList.length, n, FactorialUtil.combination(n, dataList.length)));
        List<List<String>> list = new ArrayList<>();
        combinationSelect(dataList, 0, new String[n], 0, list);
        return list;
    }

    /**
     * 组合选择
     * @param dataList 待选列表
     * @param dataIndex 待选开始索引
     * @param resultList 前面（resultIndex-1）个的组合结果
     * @param resultIndex 选择索引，从0开始
     * @author zhangjj
     * @Date 2018/8/13 16:35
     * @return 
     * @exception 
     * 
     */
    private static void combinationSelect(String[] dataList, int dataIndex, String[] resultList, int resultIndex, List<List<String>> list) {
        int resultLen = resultList.length;
        int resultCount = resultIndex + 1;
        if (resultCount > resultLen) { // 全部选择完时，输出组合结果
//            System.out.println(Arrays.asList(resultList));
            List<String> lst = new ArrayList<>();
            lst.addAll(Arrays.asList(resultList));
            list.add(lst);
            return;
        }
        // 递归选择下一个
        for (int i = dataIndex; i < dataList.length + resultCount - resultLen; i++) {
            resultList[resultIndex] = dataList[i];
            combinationSelect(dataList, i + 1, resultList, resultIndex + 1, list);
        }
    }

    public static void main(String[] args) {
        List<List<String>> list = combinationSelect(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"}, 3);
        for(List<String> combination : list){
            System.out.println(combination);
        }
    }
}
