import com.dudu.DemoApplication;
import com.dudu.entity.bean.ProjectDO;
import com.dudu.entity.bo.CoverageBO;
import com.dudu.service.CoverageSchedulerService;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.CommentsCollection;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 测试驱动开发
 * <p>
 * 没有测试之前不要写任何功能代码
 * 只编写恰好能够体现一个失败情况的测试代码
 * 只编写恰好能通过测试的功能代码
 * <p>
 * 测试的FIRST准则
 * <p>
 * 快速（Fast）测试应该够快，尽量自动化。
 * 独立（Independent） 测试应该应该独立。不要相互依赖
 * 可重复（Repeatable） 测试应该在任何环境上都能重复通过。
 * 自我验证（Self-Validating） 测试应该有bool输出。不要通过查看日志这种低效率方式来判断测试是否通过
 * 及时（Timely） 测试应该及时编写，在其对应的生产代码之前编写
 * <p>
 * 整洁代码准则
 * <p>
 * 优雅且高效、直截了当、减少依赖、只做好一件事
 * 简单直接
 * 可读、可维护、单元测试
 * 不要重复、单一职责、表达力
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class JGitTest {

    private static final String REMOTE_URL = "https://github.com/1582871549/demo.git";

    @Autowired
    private CoverageSchedulerService coverageSchedulerService;

    @Test
    public void cloneRepositoryTest() {

        String url = "https://github.com/1582871549/demo.git";
        Integer projectId = 1;
        String projectName = "test";
        String baseBranch = "master";
        String compareBranch = "dev";
        String serverAddress = "127.0.0.1";
        Integer serverPort = 4399;

        ProjectDO projectDO = new ProjectDO();
        projectDO.setUrl(url);
        projectDO.setProjectId(projectId);
        projectDO.setProjectName(projectName);
        projectDO.setBase(baseBranch);
        projectDO.setCompare(compareBranch);
        projectDO.setServerAddress(serverAddress);
        projectDO.setServerPort(serverPort);
        projectDO.setBranch(true);

        coverageSchedulerService.callCoverageService(projectDO);
    }

    @Test
    public void cloneRepositoryTest2() {

        String url = "https://github.com/1582871549/demo.git";
        Integer projectId = 1;
        String projectName = "test";
        String baseBranch = "v1.0";
        String compareBranch = "v3.0";
        String serverAddress = "127.0.0.1";
        Integer serverPort = 4399;

        ProjectDO projectDO = new ProjectDO();
        projectDO.setUrl(url);
        projectDO.setProjectId(projectId);
        projectDO.setProjectName(projectName);
        projectDO.setBase(baseBranch);
        projectDO.setCompare(compareBranch);
        projectDO.setServerAddress(serverAddress);
        projectDO.setServerPort(serverPort);
        projectDO.setBranch(false);

        coverageSchedulerService.callCoverageServiceTag(projectDO);
    }

    @Test
    public void listTags() throws GitAPIException {

        System.out.println("Listing remote repository " + REMOTE_URL);

        Collection<Ref> refs = Git.lsRemoteRepository()
                .setTags(true)
                .setRemote(REMOTE_URL)
                .call();

        for (Ref ref : refs) {
            System.out.println("Ref: " + ref);

            String tagName = ref.getName();
            String substring = tagName.substring(10, tagName.length());

            System.out.println(substring);
            // System.out.println(ref.getObjectId());

        }
    }

    @Test
    public void listBranchs() throws GitAPIException {

        System.out.println("Listing remote repository " + REMOTE_URL);

        Collection<Ref> refs = Git.lsRemoteRepository()
                .setHeads(true)
                .setRemote(REMOTE_URL)
                .call();

        for (Ref ref : refs) {
            System.out.println("Ref: " + ref);

            String refName = ref.getName();
            String branchName = refName.substring(11, refName.length());

            System.out.println(branchName);
            System.out.println(ref.getObjectId());

        }

    }

    @Test
    public void aa(){
        CoverageBO coverageBO = CoverageBO.builder()
                .url("url")
                .username("username")
                .password("password")
                .defaultBranch("defaultBranch")
                .projectPath("projectPath")
                .base("base")
                .compare("compare")
                .serverAddress("serverAddress")
                .serverPort(1111)
                .dumpPath("dumpPath")
                .build();

        String url = coverageBO.getUrl();


        System.out.println(coverageBO);
    }

    @Test
    public void listAllRef() throws GitAPIException {

        final Map<String, Ref> map = Git.lsRemoteRepository()
                .setRemote(REMOTE_URL)
                .callAsMap();

        for (Map.Entry<String, Ref> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Ref: " + entry.getValue());
        }
    }


    @Test
    public void outputStreamTest() {

        // ByteArrayOutputStream out = new ByteArrayOutputStream();
        //
        // try (DiffFormatter formatter = new DiffFormatter(out)) {
        //
        //     formatter.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
        //
        //     for (DiffEntry diff : diffs) {
        //
        //         //打印文件差异具体内容
        //         formatter.format(diff);
        //
        //         System.out.println(out.toString("UTF-8"));
        //         System.out.println("Diff: " + diff.getChangeType() + ": " + (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
        //
        //         out.reset();
        //     }
        // }
    }

    @Test
    public void javaParserTest() throws Exception {

        String filePath = "";


        JavaParser javaParser = new JavaParser();

        ParseResult<CompilationUnit> result = javaParser.parse(new File(filePath));

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
