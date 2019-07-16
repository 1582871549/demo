/**
 * FileName: CommentRemover
 * Author:   大橙子
 * Date:     2019/7/15 14:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.git;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.CommentsCollection;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br> 
 * <p>
 *     删除java文件中的注释
 * </p>
 *
 * @author 大橙子
 * @create 2019/7/15
 * @since 1.0.0
 */
public class CommentRemover {

    private static final String FILE_PATH = "C:\\Users\\大橙子\\AppData\\Local\\Temp\\gitRepository6919285836525817515\\org.jacoco.core\\src\\org\\jacoco\\core\\analysis\\Analyzer.java";

    public static void main(String[] args) throws Exception {

        JavaParser javaParser = new JavaParser();

        ParseResult<CompilationUnit> result = javaParser.parse(new File(FILE_PATH));

        // 注释
        Optional<CommentsCollection> commentsCollection = result.getCommentsCollection();

        // 主题
        Optional<CompilationUnit> result2 = result.getResult();

        // result2.filter();

        CompilationUnit unit = result2.get();

        Optional<PackageDeclaration> packageDeclaration = unit.getPackageDeclaration();

        PackageDeclaration declaration = packageDeclaration.get();

        System.out.println("packageName:   " + declaration.getName());

        TypeDeclaration<?> type = unit.getType(0);

        System.out.println("className:   " + type.getNameAsString());

        List<MethodDeclaration> methods = type.getMethods();

        for (MethodDeclaration method : methods) {

            Optional<Range> range2 = method.getRange();

            Range range = range2.get();

            System.out.println("methodName:   " + method.getNameAsString()  +"   begin:   " + range.begin + "   end:   " +range.end);

            System.out.println(method);
        }


    }

}
