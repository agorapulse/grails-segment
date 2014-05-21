grails.project.work.dir = 'target'
grails.project.source.level = 1.6

grails.project.dependency.resolution = {
    inherits 'global'
    log 'warn'
    repositories {
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        // Latest httpcore and httpmime
        build 'org.apache.httpcomponents:httpcore:4.3.2'
        build 'org.apache.httpcomponents:httpclient:4.3.2'
        build 'org.apache.httpcomponents:httpmime:4.3.3'
        // Latest Segment.io analytics java client
        compile 'com.github.segmentio:analytics:0.4.2'
        // Latest Joda Time
        compile 'joda-time:joda-time:2.3'
    }
    plugins {
        build(':release:3.0.1',
                ':rest-client-builder:1.0.3',
                ':coveralls:0.1') {
            export = false
        }
    }
}