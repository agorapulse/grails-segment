package grails.plugin.segmentio

import grails.test.mixin.TestFor
import grails.util.Environment
import spock.lang.Specification

@TestFor(SegmentioService)
class SegmentioServiceSpec extends Specification {

    def "should be disabled by default" () {
        !service.enabled
    }

    def "should be disabled for PRODUCTION when no config is provided" () {
        when:
        setEnvironment(Environment.PRODUCTION)

        then:
        !service.enabled
    }

    def "should be enabled for PRODUCTION by default"() {
        when:
        setEnvironment(Environment.PRODUCTION)
        buildConfig(apiKey: 'apiKey')

        then:
        service.enabled
    }

    def "should be disabled for NON-PRODUCTION by default" () {
        when:
        setEnvironment(Environment.CUSTOM)
        buildConfig(apiKey: 'apiKey')

        then:
        !service.enabled
    }

    def "should be enabled when config enables SegmentIo" () {
        when:
        buildConfig(apiKey: 'apiKey', enabled: true)

        then:
        service.enabled
    }

    def "should be disabled for PRODUCTION when config disables SegmentIo" () {
        when:
        setEnvironment(Environment.PRODUCTION)
        buildConfig(apiKey: 'apiKey', enabled: false)

        then:
        !service.enabled
    }

    // PRIVATE

    private buildConfig(Map config) {
        service.grailsApplication.config = [
                grails: [
                        plugin: [
                                segmentio: config
                        ]
                ]
        ]
    }

    private setEnvironment(environment) {
        Environment.metaClass.static.getCurrent = { ->
            return environment
        }
    }

}
