# Gradle Sass Plugin

This is a plugin for sass preprocessing in gradle. It uses 
[vaadin-sass-compiler](https://github.com/vaadin/sass-compiler).

## Usage
You should clone this repository locally and install the jar in the local maven repository

        ./gradlew publishToMavenLocal
        
In your build.gradle add the following at the start of the script:

        buildscript {
            repositories {
                mavenLocal()
            }
            dependencies {
                classpath "it.tellnet:gradle-sass:+"
            }
        }
        
Proceed to configure the plugin.

## WebJars support
This plugin supports webjars. There are 2 additional configurations added to the project:

 * sassCompile
 * sassRuntime
 
Example:

        sassRuntime 'org.webjars.npm:github-com-mrkelly-lato:0.3.0'
        sassRuntime 'org.webjars.bower:open-iconic:1.1.1'
 
These configurations just resolve dependencies and add them to the search path for scss files.
Thus it is possible to add webjars like bootstrap-sass to dependencies. In order to add sassRuntime
deps to java runtime deps use the following snippet

        configurations.runtime.extendsFrom configurations.sassRuntime



## Defined Tasks

There are 3 tasks defined:

 * *sassCompile* - Compiles sass files to css
 * *sassWatchStart* - Watches sassDir for changes and compiles them on changes. Depends on sassCompile
 * *sassWatchStop* - Stops the thread started by *sassWatchStart*
 
 
## Configuration

To configure this plugin you can declare a block as follows:

    sass {
        //The input directory
        sassDir = 'src/main/sass' //default value
        //The output directory
        cssDir = 'build/sass' //default value
        //Minify output css
        minify = false //default value
        //Vaadin sass compiler is pretty verbose about errors
        silenceErrors = false //default value
        // Ability to turn off charset setting (helpful for JavaFX css generation)
        setCharSet = true // default value 
        
        /*
        //You can add scan directories to look for SCSS files in the JARs
        //Search in dep jars looks for these directories
        searchDirectories '/META-INF/resources/webjars/open-iconic/1.1.1/font/css/',
                          '/META-INF/resources/webjars/github-com-mrkelly-lato/0.3.0/scss/'
        */
        
    }