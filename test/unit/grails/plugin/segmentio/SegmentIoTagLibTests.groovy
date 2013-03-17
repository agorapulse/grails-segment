package grails.plugin.segmentio

import grails.test.mixin.TestFor
import grails.util.Environment
import spock.lang.Specification

@TestFor(SegmentioTagLib)
class SegmentioTagLibSpec extends Specification {

    def "should be disabled by default" () {
        assert tagLib.enabled == false
    }

    def "should be disabled for PRODUCTION when no config is provided" () {
        when:
        setEnvironment(Environment.PRODUCTION)

        then:
        assert tagLib.enabled == false
    }

    def "should be enabled for PRODUCTION by default"() {
        when:
        setEnvironment(Environment.PRODUCTION)
        buildConfig(apiKey: 'apiKey')

        then:
        assert tagLib.enabled == true
    }

    def "should be disabled for NON-PRODUCTION by default" () {
        when:
        setEnvironment(Environment.CUSTOM)
        buildConfig(apiKey: 'apiKey')

        then:
        assert tagLib.enabled == false
    }

    def "should be enabled when config enables SegmentIo" () {
        when:
        buildConfig(apiKey: 'apiKey', enabled: true)

        then:
        assert tagLib.enabled == true
    }

    def "should be disabled for PRODUCTION when config disables SegmentIo" () {
        when:
        setEnvironment(Environment.PRODUCTION)
        buildConfig(apiKey: 'apiKey', enabled: false)

        then:
        assert tagLib.enabled == false
    }

    // PRIVATE

    private buildConfig(Map config) {
        tagLib.grailsApplication.config = [
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