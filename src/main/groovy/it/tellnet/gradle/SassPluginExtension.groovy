package it.tellnet.gradle

/**
 * @author Radu Andries
 */
class SassPluginExtension {
    List<String> searchDirectoriesList = []
    String sassDir = 'src/main/sass'
    String cssDir = 'build/sass'
    boolean minify = false
    boolean silenceErrors = false

    void searchDirectories(String... paths){
        if(paths!=null)
            paths.each {
                searchDirectoriesList.add(it)
            }
    }
}
