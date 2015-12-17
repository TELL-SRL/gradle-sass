package it.tellnet.gradle

import com.google.common.cache.LoadingCache
import com.vaadin.sass.internal.ScssStylesheet
import com.vaadin.sass.internal.resolver.FilesystemResolver
import com.vaadin.sass.internal.resolver.ScssStylesheetResolver
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.w3c.css.sac.InputSource

import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * @author Radu Andries
 */
class ProjectAwareResolver implements ScssStylesheetResolver {
    private Project proj
    private Configuration compile
    private Configuration runtime
    private SassPluginExtension ext
    private Map<String, File> zips = [:]

    ProjectAwareResolver(Project project) {
        proj = project
        compile = project.configurations.getByName('sassCompile')
        runtime = project.configurations.getByName('sassRuntime')

        ext = project.getExtensions().getByName('sass') as SassPluginExtension

        ext.searchDirectoriesList.each { dirSearch ->
            if(!dirSearch.endsWith('/')){
                dirSearch += '/'
            }
            [compile,runtime].each {
                for(File jar : it){
                    def zip = new ZipFile(jar)
                    if(zip.entries().find { it.directory && dirSearch == '/' + it.name } != null ){
                        zips.put(dirSearch, jar)
                        break;
                    }
                }
            }
        }
    }



    @Override
    InputSource resolve(ScssStylesheet parent, String identifier) {
        for(String path: zips.keySet()){
            def noUnderscore = checkFile(path, identifier + '.scss')
            if(noUnderscore != null)
                return noUnderscore;
            def components = identifier.split('/')
            def out
            if(components.size() > 1){
                out = components[0..-2].join('/')
                out += '/_' + components.last()
            }else{
                out = '_' +  components.first()
            }
            def underscore = checkFile(path, out + '.scss')
            if(underscore != null)
                return underscore;
        }
        return null
    }

    public FilesystemResolver getFSResolver(){
        return new FilesystemResolver(proj.file(ext.sassDir).absolutePath)
    }

    private InputSource checkFile(String directory, String fileName){
        def f = zips.get(directory)

        def z = new ZipFile(f)
        ZipEntry entry = (ZipEntry) z.entries().find {
            '/' + it.name == directory+fileName
        }
        if(entry==null)
            return null
        InputStream ist = z.getInputStream(entry)
        def is = new InputSource();
        is.setByteStream(ist);
        is.setURI(fileName);
        return is
    }
}
