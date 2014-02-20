grails.project.work.dir = 'target'
grails.project.source.level = 1.6

grails.project.dependency.resolution = {
    inherits 'global'
    log 'warn'
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        compile 'com.github.segmentio:analytics:0.4.0'
        compile 'joda-time:joda-time:2.3'
    }
    plugins {
        build(':release:3.0.1') {
            export = false
        }
    }
}