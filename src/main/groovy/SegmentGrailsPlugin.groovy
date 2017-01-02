import grails.plugins.*

class SegmentGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.0.0.RC2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def author = "Benoit Hediard"
    def authorEmail = "ben@benorama.com"
    def title = "Segment Plugin"
    def description = "The Segment Plugin allows your Grails application to use Segment.com. Segment.com lets you send your analytics data to any service you want, without you having to integrate with each one individually."

    def documentation = "https://github.com/agorapulse/grails-segmentio"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ system: "github", url: "https://github.com/agorapulse/grails-segmentio/issues" ]
    def scm = [  url: "https://github.com/agorapulse/grails-segmentio" ]

}
