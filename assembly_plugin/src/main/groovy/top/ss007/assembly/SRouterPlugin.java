package top.ss007.assembly;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;

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
        boolean isApp=project.getPlugins().hasPlugin(AppPlugin.class);
        if (!isApp)
            return;

        project.getExtensions().findByType(AppExtension.class)
                .registerTransform(new SRouterTransform());
    }
}
