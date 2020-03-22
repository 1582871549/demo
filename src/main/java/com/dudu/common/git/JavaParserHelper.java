package com.dudu.common.git;

import com.dudu.entity.bo.DiffClassBO;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

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
     *
     * @param diffClassBOMap 差异类信息集合
     * @param projectPath 本地存储库路径
     */
    public static Map<String, Map<String, String>> matchMethod(Map<String, List<DiffClassBO>> diffClassBOMap,
                                                                   String projectPath) throws FileNotFoundException {

        Map<String, Map<String, String>> diffClassMap = new HashMap<>(16);

        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, List<DiffClassBO>> entry : diffClassBOMap.entrySet()) {

            String classPath = entry.getKey();

            File classFile = new File(projectPath, classPath);

            if (!classFile.exists()) {
                continue;
            }

            JavaParser javaParser = new JavaParser();

            ParseResult<CompilationUnit> parseResult = javaParser.parse(classFile);

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

            List<DiffClassBO> diffClassBOS = entry.getValue();

            if (diffClassBOS == null || diffClassBOS.isEmpty()) {
                continue;
            }

            List<MethodDeclaration> methodList = type.getMethods();

            Map<String, String> diffMethodMap = new HashMap<>();

            for (MethodDeclaration method : methodList) {

                Range range = method.getRange().get();
                int methodBegin = range.begin.line;
                int methodEnd = range.end.line;
                String methodName = method.getNameAsString();

                for (DiffClassBO diffClassBO : diffClassBOS) {

                    int diffBegin = diffClassBO.getDiffBegin();
                    int diffEnd = diffClassBO.getDiffEnd();

                    if (diffBegin >= methodBegin && diffEnd <= methodEnd
                            || diffBegin <= methodBegin && diffEnd >= methodEnd
                            || diffBegin < methodBegin && diffEnd >= methodBegin
                            || diffBegin < methodEnd && diffEnd >= methodEnd) {
                        diffMethodMap.put(methodName, null);
                    }
                }
            }

            String allClassName = builder.append(packageName)
                    .append(".")
                    .append(className)
                    .toString();

            diffClassMap.put(allClassName, diffMethodMap);

            builder.delete(0, builder.length());
        }
        return diffClassMap;
    }

}
