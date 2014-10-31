grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.fork = [
        test: false, //[maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
        run: false, // [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
        war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
        console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    inherits 'global'
    log 'warn'
    repositories {
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        // Latest httpcore and httpmime for coveralls plugin
        build 'org.apache.httpcomponents:httpcore:4.3.2'
        build 'org.apache.httpcomponents:httpclient:4.3.5'
        build 'org.apache.httpcomponents:httpmime:4.3.5'
        // Latest Segment.io analytics java client
        compile 'com.github.segmentio:analytics:1.0.4'
        // Latest Joda Time
        compile 'joda-time:joda-time:2.3'
    }
    plugins {
        build(':release:3.0.1',
                ':rest-client-builder:1.0.3',
                ':coveralls:0.1') {
            export = false
        }
        test(':code-coverage:2.0.3-2') {
            export = false
        }
    }
}