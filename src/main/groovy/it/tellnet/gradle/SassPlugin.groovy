package it.tellnet.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * @author Radu Andries
 */
class SassPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.configurations.create('sassCompile')
        project.configurations.create('sassRuntime')
        project.extensions.create('sass', SassPluginExtension)

        project.tasks.create('sassCompile', SassCompileTask.class)
        project.tasks.getByName('sassCompile').dependsOn(project.configurations.getByName('sassCompile'))
        project.tasks.getByName('sassCompile').dependsOn(project.configurations.getByName('sassRuntime'))
        project.tasks.create('sassWatchStart', SassWatchStartTask.class)
        project.tasks.getByName('sassWatchStart').dependsOn(project.tasks.getByName('sassCompile'))
        project.tasks.create('sassWatchStop', SassWatchEndTask.class)
    }
}
