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

    private String localRepository;
    private Map<String, Map<String, List<String>>> incrementalClass;

    public JavaParserHelper(String localRepository) {
        this.localRepository = localRepository;
        this.incrementalClass = new HashMap<>(16);
    }

    /**
     * 根据diff行号匹配抽象语法树中的方法
     * @TODO 需要优化的地方
     *          1、对差异行排序, 已执行过的差异行匹配跳过
     *          2、忽略差异行是方法的匹配
     *
     * @param beanList 差异类信息集合
     */
    public void matchMethod(List<JGitBean> beanList) throws FileNotFoundException {

        StringBuffer buffer = new StringBuffer();

        for (JGitBean bean : beanList) {

            Map<String, List<String>> incrementalMethod = new HashMap<>(16);

            File file = new File(localRepository, bean.getClassName());
            if (!file.exists()) {
                continue;
            }
            JavaParser javaParser = new JavaParser();

            ParseResult<CompilationUnit> parseResult = javaParser.parse(file);

            // 所有注释
            Optional<CommentsCollection> commentsCollection = parseResult.getCommentsCollection();

            Optional<CompilationUnit> result = parseResult.getResult();

            CompilationUnit unit = result.get();

            String packageName = unit.getPackageDeclaration().get().getNameAsString();

            TypeDeclaration<?> type = unit.getType(0);

            String className = type.getNameAsString();

            List<MethodDeclaration> methodList = type.getMethods();

            List<Integer> ends = new ArrayList<>(11);

            for (MethodDeclaration method : methodList) {

                // 每个方法的范围(起始行, 结束行)
                Range range = method.getRange().get();
                List<String> methodLines = new ArrayList<>(11);

                // 匹配方法 key为旧分支类中的方法对应行集合, 只通过旧行来匹配方法名称。具体的行数由value差异行在新分支中获取
                for (Map.Entry<String, String> entry : bean.getLine().entrySet()) {

                    int endA = Integer.parseInt(entry.getKey());

                    if (endA >= range.begin.line && endA <= range.end.line) {

                        ends.add(endA);

                        methodLines.add(entry.getValue());
                        // key: 方法名称, value: "远程分支差异行, 连续行数", ...
                        incrementalMethod.put(method.getNameAsString(), methodLines);
                    }
                }
            }

            List<String> otherLines = new ArrayList<>(11);

            for (Map.Entry<String, String> entry : bean.getLine().entrySet()) {

                if (!ends.contains(Integer.parseInt(entry.getKey()))) {
                    otherLines.add(entry.getValue());
                }
            }
            if (otherLines.size() > 0) {
                incrementalMethod.put("[otherLine]", otherLines);
            }

            String replace = packageName.replace(".", "/");

            String javaPath = buffer.append(replace)
                    .append("/")
                    .append(className)
                    .toString();

            incrementalClass.put(javaPath, incrementalMethod);
            buffer.delete(0, buffer.length());
        }
    }

    public Map<String, Map<String, List<String>>> getIncrementalClass() {
        return incrementalClass;
    }
}
