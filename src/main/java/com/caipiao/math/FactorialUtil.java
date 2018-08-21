package com.caipiao.math;

/**
 * @author zhangjj
 * @create 2018-08-13 16:25
 **/
public class FactorialUtil {

    /**
     * 计算阶乘数，即n! = n * (n-1) * ... * 2 * 1
     * @param
     * @author zhangjj
     * @Date 2018/8/13 16:26
     * @return
     * @exception
     *
     */
    private static long factorial(int n) {
        long sum = 1;
        while( n > 0 ) {
            sum = sum * n--;
        }
        return sum;
    }

    /**
     * 排列计算公式A<sup>m</sup><sub>n</sub> = n!/(n - m)!
     * @param
     * @author zhangjj
     * @Date 2018/8/13 16:26
     * @return
     * @exception
     *
     */
    public static long arrangement(int m, int n) {
        return m <= n ? factorial(n) / factorial(n - m) : 0;
    }

    /**
     * 组合计算公式C<sup>m</sup><sub>n</sub> = n! / (m! * (n - m)!)
     * @param
     * @author zhangjj
     * @Date 2018/8/13 16:26
     * @return
     * @exception
     *
     */
    public static long combination(int m, int n) {
        return m <= n ? factorial(n) / (factorial(m) * factorial((n - m))) : 0;
    }

    public static void main(String[] args) {
        System.out.println(FactorialUtil. combination(3, 11));;
    }
}
