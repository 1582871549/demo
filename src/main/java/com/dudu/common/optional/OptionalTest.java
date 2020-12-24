/**
 * FileName: OptionalTest
 * Author:   大橙子
 * Date:     2019/7/30 10:20
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.optional;

import java.util.List;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/7/30
 * @since 1.0.0
 */
public class OptionalTest {

    List<String> strings = null;
    ICar car = new BaoMaCar();

    public static void main(String[] args) {

        OptionalTest test = new OptionalTest();

        Optional<OptionalTest> optional = Optional.of(test);

        System.out.println(optional.isPresent());

        optional.ifPresent(a -> System.out.println(a.getCar().getClass().getName()));

        optional.ifPresent(a -> Optional.ofNullable(a.getStrings()).ifPresent(b -> System.out.println("StringList:" + (b == null))));

        optional.ifPresent(a -> Optional.ofNullable(a.getCar()).ifPresent(b -> System.out.println("car:" + (b == null))));

        // map---------------------------

        Optional<ICar> opt1 = optional.map(a -> a.getCar());

        System.out.println("opt1   " + opt1.get());

        Optional<Integer> opt2 = optional.map(a -> a.getCar()).map(b -> b.getI());

        System.out.println("opt2   " + opt2.get());

        Optional<Integer> opt3 = optional.map(a -> a.getStrings()).map(b -> b.size());

        System.out.println("opt3   " + opt3);

        // 主动包裹Optional对象
        Optional<ICar> opt4 = optional.flatMap(a -> Optional.of(a.getCar()));

        System.out.println("opt4   " + opt4);

        Optional opt5 = optional.flatMap(a -> Optional.of(a.getCar())).flatMap(b -> Optional.ofNullable(b.getI()));

        System.out.println("opt5   " + opt5);

        // filter---------------------------

        Optional<OptionalTest> fil1 = optional.filter(a -> a.getCar() != null).filter(b -> b.getClass().getName() != null);

        System.out.println("fil1   " + (fil1.isPresent() ? fil1.get().getClass().getName() : false));

        Optional<OptionalTest> fil2 = optional.filter(a -> a.getStrings() != null);

        System.out.println("fil2   " + (fil2.isPresent() ? fil2.get() : "null"));

        // orElse---------------------------

        Optional<Object> optional2 = Optional.ofNullable(null);

        System.out.println(optional2);

        System.out.println(optional2.orElse(test));

        System.out.println(optional2.orElseGet(() -> new OptionalTest()));

        System.out.println(optional2.orElseThrow(() -> new RuntimeException("orElseThrow")));
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    public ICar getCar() {
        return car;
    }

    public void setCar(ICar car) {
        this.car = car;
    }

    class BaoMaCar implements ICar {

        public Integer getI() {
            return i;
        }

        public void setI(Integer i) {
            this.i = i;
        }

        Integer i = 4;

    }

}
