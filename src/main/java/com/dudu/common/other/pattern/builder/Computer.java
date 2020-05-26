package com.dudu.common.other.pattern.builder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

/**
 *
 * 旨在创建一个属性不可变的实例
 *
 * 1. @Builder注解的bug
 *
 *     当我们只用@Builder注解时
 *     lombok为当前类生成的构造器是“default”的(不添加权限修饰符，默认为“default”的)。可以被同package的类调用(default限制不同package类的调用)
 *
 *     我们之所以用构建器模式，是希望用户用构建器提供的方法去创建实例。
 *     所以，我们需要将此构造器设为private的。
 *     这时就需要用到“@AllArgsConstructor(access = AccessLevel.PRIVATE)”
 *
 * 2. 为什么使用构建器模式
 *
 *     若一个类具有大量的成员变量，我们就需要提供一个全参的构造器或大量的set方法。
 *     这让实例的创建和赋值，变得很麻烦，且不直观。
 *
 *     我们通过构建器，可以让变量的赋值变成链式调用，
 *     而且调用的方法名对应着成员变量的名称。让对象的创建和赋值都变得很简洁、直观。
 *
 * 3. 链式方法赋值，一定要用构建器模式吗？
 *
 *     不一定要用到构建器模式，之所以使用构建器模式，是因为我们要创造的对象是一个成员变量不可变的对象。
 *
 *     类的成员变量都由final修饰，因为需要在实例创建时就把值确定下来。
 *     但在类具有大量成员变量的时候，我们是不希望用户直接调用全参构造器的。
 *
 *     所以我们使用了构建器中间类。这个类为了实现链式赋值，才将变量设为非final的。
 *     无论构建器实例怎么赋值，怎么改变，当你调用build方法时，就会返回一个成员变量不可变的基础类实例。
 *
 * 4. 那如果有大量属性，但不需要它是成员变量不可变的对象，我们还需要构建器模式吗？
 *
 *     不需要，我们可以参考构建器，把代码赋值改成链式的即可
 *
 * @author mengli
 * @create 2019/11/15
 * @since 1.0.0
 */
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Computer {

    private final String CPU;           // cpu
    private final String videoCard;     // 显卡
    private final String processor;     // 处理器
    private final boolean SSD;           // 固态硬盘

}
