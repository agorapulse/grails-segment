package grails.plugin.segmentio

import grails.test.mixin.TestFor
import grails.util.Environment
import spock.lang.Specification

@TestFor(SegmentioService)
class SegmentioServiceSpec extends Specification {

    def "should be disabled by default" () {
        assert service.enabled == false
    }

    def "should be disabled for PRODUCTION when no config is provided" () {
        when:
        setEnvironment(Environment.PRODUCTION)

        then:
        assert service.enabled == false
    }

    def "should be enabled for PRODUCTION by default"() {
        when:
        setEnvironment(Environment.PRODUCTION)
        buildConfig(apiSecret: 'apiSecret')

        then:
        assert service.enabled == true
    }

    def "should be disabled for NON-PRODUCTION by default" () {
        when:
        setEnvironment(Environment.CUSTOM)
        buildConfig(apiSecret: 'apiSecret')

        then:
        assert service.enabled == false
    }

    def "should be enabled when config enables SegmentIo" () {
        when:
        buildConfig(apiSecret: 'apiSecret', enabled: true)

        then:
        assert service.enabled == true
    }

    def "should be disabled for PRODUCTION when config disables SegmentIo" () {
        when:
        setEnvironment(Environment.PRODUCTION)
        buildConfig(apiSecret: 'apiSecret', enabled: false)

        then:
        assert service.enabled == false
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
