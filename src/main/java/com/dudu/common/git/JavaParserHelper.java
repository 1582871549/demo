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
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.CommentsCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
     * @param beanList 差异类信息集合
     * @param localRepository 本地存储库路径
     */
    public static Map<String, List<String>> matchMethod(List<JGitBean> beanList, String localRepository) throws FileNotFoundException {

        StringBuilder buffer = new StringBuilder();
        Map<String, List<String>> incrementalClass = new HashMap<>(16);

        // 遍历差异类信息
        for (JGitBean bean : beanList) {

            List<String> incrementalMethod = new ArrayList<>();

            File diffClass = new File(localRepository, bean.getClassName());

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

            String packageName = unit.getPackageDeclaration().get().getNameAsString();

            TypeDeclaration<?> type = unit.getType(0);

            String className = type.getNameAsString();

            List<MethodDeclaration> methodList = type.getMethods();

            // 遍历该类所有方法, 取出方法的范围对本地分支的差异代码行lineA进行差异方法匹配
            for (MethodDeclaration method : methodList) {

                // 每个方法的范围(起始行, 结束行)
                Range range = method.getRange().get();

                // 方法名称
                String methodName = method.getNameAsString();

                // 遍历每个差异代码块的结束行
                for (Integer diffLine : bean.getLineB()) {

                    // 结束行匹配方法范围则记录当前的方法名称
                    if (diffLine >= range.begin.line && diffLine <= range.end.line) {

                        // 记录匹配方法
                        incrementalMethod.add(methodName);

                        // 每个方法只记录一次, 防止重复数据
                        break;
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