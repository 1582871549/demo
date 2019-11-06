/**
 * FileName: JavaParserHelper
 * Author:   大橙子
 * Date:     2019/7/16 10:21
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
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br> 
 * 封装好的抽象语法树
 *
 * 官方文档 https://www.javadoc.io/doc/com.github.javaparser/javaparser-core/3.14.7
 * github  https://github.com/Javaparser/Javaparser
 *
 * @author dujianwei
 * @create 2019/7/16
 * @since 1.0.0
 */
public class JavaParserHelper {

    /**
     * 根据diff行号匹配抽象语法树中的方法
     * @TODO 需要优化的地方
     *          1、对差异行排序, 已执行过的差异行匹配跳过
     *          2、忽略差异行是方法的匹配
     *
     * @param insertMap 差异类信息集合
     * @param repositoryPath 本地存储库路径
     */
    public static Map<String, Map<String, String>> matchMethod(Map<String, List<Integer>> insertMap, String repositoryPath) throws FileNotFoundException {

        StringBuilder buffer = new StringBuilder();

        Map<String, Map<String, String>> incrementalClass = new HashMap<>(16);

        // 遍历差异类信息
        for (Map.Entry<String, List<Integer>> entry : insertMap.entrySet()) {

            Map<String, String> incrementalMethod = new HashMap<>();

            File diffClass = new File(repositoryPath, entry.getKey());

            if (!diffClass.exists()) {
                continue;
            }
            JavaParser javaParser = new JavaParser();

            ParseResult<CompilationUnit> parseResult = javaParser.parse(diffClass);

            /*
             * 获取当前类文件的所有注释
             *
             * 存在差异注释导致的
             */
            Optional<CommentsCollection> commentsCollection = parseResult.getCommentsCollection();

            Optional<CompilationUnit> result = parseResult.getResult();

            CompilationUnit unit = result.get();

            Optional<PackageDeclaration> packageDeclaration = unit.getPackageDeclaration();

            Optional<PackageDeclaration> o = Optional.empty();

            // 测试包中的文件无法获取包名, 所以跳过比对
            if (packageDeclaration == o) {
                continue;
            }

            String packageName = packageDeclaration.get().getNameAsString();

            TypeDeclaration<?> type = unit.getType(0);

            String className = type.getNameAsString();

            List<MethodDeclaration> methodList = type.getMethods();

            if (entry.getValue() == null || entry.getValue().size() == 0) {
                for (MethodDeclaration method : methodList) {
                    incrementalMethod.put(method.getNameAsString(), null);
                }
            } else {
                // 遍历每个差异代码块的结束行
                for (Integer diffLine : entry.getValue()) {

                    // 遍历该类所有方法, 取出方法的范围对本地分支的差异代码行lineA进行差异方法匹配
                    for (MethodDeclaration method : methodList) {

                        // 每个方法的范围(起始行, 结束行)
                        Range range = method.getRange().get();

                        // 方法名称
                        String methodName = method.getNameAsString();

                        // 结束行匹配方法范围则记录当前的方法名称
                        if (diffLine >= range.begin.line && diffLine <= range.end.line) {

                            // 记录匹配方法
                            incrementalMethod.put(methodName, null);
                        }
                    }
                }
            }


            String replace = packageName.replace(".", "/");

            String classPath = buffer.append(replace)
                    .append("/")
                    .append(className)
                    .toString();

            incrementalClass.put(classPath, incrementalMethod);
            buffer.delete(0, buffer.length());
        }
        return incrementalClass;
    }
}
