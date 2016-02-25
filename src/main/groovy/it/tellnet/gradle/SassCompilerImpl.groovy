package it.tellnet.gradle

import com.vaadin.sass.internal.ScssStylesheet
import com.vaadin.sass.internal.handler.SCSSDocumentHandlerImpl
import com.vaadin.sass.internal.handler.SCSSErrorHandler
/**
 * @author Radu Andries
 */
class SassCompilerImpl {
    File scss
    File outDir
    ProjectAwareResolver resolver
    Boolean minify = false
    Boolean silent = false
    Boolean setCharSet = true


    def exec(){
        SCSSErrorHandler handler
        if(silent)
            handler = new SilentErrorHandler()
        else
            handler = new SCSSErrorHandler()
        ScssStylesheet sass = ScssStylesheet.get(scss.absolutePath, null, new SCSSDocumentHandlerImpl(),handler)
        sass.setFile(scss)

        // setting charset breaks JavaFX CSS generation
        // and should be turned off for JavaFX applications
        if ( setCharSet ) {
            sass.setCharset('UTF-8')
        }
        sass.addResolver(resolver.getFSResolver())
        sass.addResolver(resolver)
        def basename = scss.getName().replaceAll('\\.scss','.css')
        sass.compile()
        new File(outDir.getAbsolutePath() + File.separator + basename).withWriter {
            sass.write(it, minify)
        }
    }


}
