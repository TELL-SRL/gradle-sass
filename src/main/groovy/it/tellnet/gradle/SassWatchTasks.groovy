package it.tellnet.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.nio.file.*

import static java.nio.file.StandardWatchEventKinds.*

/**
 * @author Radu Andries
 */
class SassWatchStartTask extends DefaultTask implements Runnable {

    static running = false

    private SassPluginExtension ext
    private ProjectAwareResolver resolver

    SassWatchStartTask() {
        this.ext = project.getExtensions().getByName('sass') as SassPluginExtension;
        this.resolver = new ProjectAwareResolver(project)
    }

    @TaskAction
    def runTask() {
        if(!running){
            running = true
            new Thread(this).start();
        }
    }

    @Override
    void run() {
        println "Starting to watch " + ext.sassDir
        def watcher = FileSystems.getDefault().newWatchService();
        Paths.get(project.file(ext.sassDir).absolutePath).register(watcher, ENTRY_CREATE, ENTRY_MODIFY)
        WatchKey key = watcher.take()
        while(true){
            HashSet<String> files = new HashSet<>()
            for(WatchEvent<?> event : key.pollEvents()) {
                if (!running)
                    break;
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == OVERFLOW) {
                    continue;
                }

                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                String filename = ev.context().toString();
                if (!filename.endsWith('scss'))
                    continue;
                println "Recompiling...."
                ((SassCompileTask) project.getTasks().getByName('sassCompile')).compileAllCss()
            }
            Thread.sleep(250)
        }
    }
}

public class SassWatchEndTask extends DefaultTask {
    @TaskAction
    def stopEverything(){
        SassWatchStartTask.running = false
    }
}
