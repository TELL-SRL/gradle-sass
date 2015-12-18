package it.tellnet.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author Radu Andries
 */
class SassCompileTask extends DefaultTask {


    @TaskAction
    def compileAllCss(){
        SassPluginExtension ext = project.getExtensions().getByName('sass') as SassPluginExtension;
        def tree = project.fileTree(dir: ext.sassDir, include: '**/*.scss')
        def resolv = new ProjectAwareResolver(project)
        File f = project.file(ext.cssDir)
        f.mkdirs()
        tree.each {
            if(it.name.startsWith('_')){
                //We should ignore this file.
                return
            }
            def scss = new SassCompilerImpl()
            scss.resolver = resolv
            scss.scss = it
            scss.outDir = f
            scss.minify = ext.minify
            scss.silent = ext.silenceErrors
            scss.exec()
        }
    }


}
