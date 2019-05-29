package top.ss007.assembly;

import com.android.build.gradle.BaseExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Created by Ben.Wang
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2019/5/27 13:47
 * @description
 */
public class SRouterPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.out.println("***********plugin start**********");
        project.getExtensions().findByType(BaseExtension.class)
                .registerTransform(new SRouterTransform());
    }
}
