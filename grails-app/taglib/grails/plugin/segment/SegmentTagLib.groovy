package grails.plugin.segment

import grails.util.Environment

class SegmentTagLib {

    static namespace = 'segment'

    /**
     * Initialize Segment.io
     * @attr enabled JS loading (default to config value)
     * @attr pageTracked Track page view (default to true)
     * @attr integrations A dictionary of the analytics integrations you want to enable. Each integration takes a string of your API key for the service. Or, if you want to supply additional settings, you can also pass a dictionary of provider-specific settings as well. You only need to call this method if you're using your own copy of analytics.js on it's own.
     */
    def initJS = { attrs ->
        if (enabled || attrs.enabled) {
            Map model = [
                    apiKey: config.apiKey,
                    pageTracked: attrs.containsKey('pageTracked') ? attrs.pageTracked.toString().toBoolean() : true
            ]
            out << render(template: '/tags/initJs', model: model, plugin: 'segment')
        }
    }

    /**
     * Alias identity
     *
     * @attr newId REQUIRED The new ID you want to associate the user with.
     * @attr originalId The original ID that the user was recognized by. This defaults to the currently identified user's ID if there is one. In most cases you don't need to pass this argument.
     */
    def alias = { attrs ->
        assert attrs.newId
        if (!attrs.originalId) attrs.originalId = ''
        if (enabled) {
            out << render(template: '/tags/alias', model: attrs, plugin: 'segment')
        }
    }

    /**
     * Identify current user
     *
     * @attr userId REQUIRED The ID you refer to the user by in your database.
     * @attr traits A map of traits you know about the user. Things like: email, name, subscriptionPlan, etc.
     * @attr context A map of of environment variables that aren't a trait of the user. Ex.: list of providers or userAgent
     */
    def identify = { attrs ->
        assert attrs.userId
        if (!attrs.context) attrs.context = defaultContext
        if (!attrs.traits) attrs.traits = [:]
        if (enabled) {
            if (intercomSecureModeEnabled) {
                def userHash = attrs.userId.toString().encodeAsIntercomHash()
                if (attrs.context.Intercom) {
                    attrs.context.Intercom += [userHash: userHash]
                } else {
                    attrs.context += [Intercom: [userHash: userHash]]
                }
            }
            out << render(template: '/tags/identify', model: attrs, plugin: 'segment')
        }
    }

    /**
     * Group
     *
     * @attr groupId REQUIRED The ID you refer to the group by in your database.
     * @attr traits A map of traits you know about the group. Things like: name, subscriptionPlan, etc.
     */
    def group = { attrs ->
        assert attrs.groupId
        if (!attrs.traits) attrs.traits = [:]
        if (enabled) {
            out << render(template: '/tags/group', model: attrs, plugin: 'segment')
        }
    }

    /**
     * Page view
     *
     * @attr category The category of the page. You must include the name if you send this. Useful if you have areas of site with lots of content.
     * @attr name The name of the of the page,
     * @attr properties A map of properties for the event. If the event was 'Added to Shopping Cart', it might have properties like price, productType, etc.
     * @attr context A map of of environment variables that aren't a property of the user. Ex.: list of providers or userAgent
     */
    def page = { attrs ->
        if (!attrs.category) attrs.category = ''
        if (!attrs.name) attrs.name = ''
        if (!attrs.context) attrs.context = defaultContext
        if (!attrs.properties) attrs.properties = [:]
        if (enabled) {
            out << render(template: '/tags/page', model: attrs, plugin: 'segment')
        }
    }

    /**
     * Ready
     *
     * @attr callback REQUIRED A callback you want to fire after analytics have loaded.
     */
    def ready = { attrs ->
        assert attrs.callback
        if (enabled) {
            out << render(template: '/tags/ready', model: attrs, plugin: 'segment')
        }
    }

    /**
     * Track event
     *
     * @attr event REQUIRED The name of the event you're tracking. We recommend using human-readable names like 'Played a Song' or 'Updated Status'.
     * @attr properties A map of properties for the event. If the event was 'Added to Shopping Cart', it might have properties like price, productType, etc.
     * @attr context A map of of environment variables that aren't a property of the user. Ex.: list of providers or userAgent
     */
    def track = { attrs ->
        assert attrs.event
        if (!attrs.context) attrs.context = defaultContext
        if (!attrs.properties) attrs.properties = [:]
        if (enabled) {
            out << render(template: '/tags/track', model: attrs, plugin: 'segment')
        }
    }

    /**
     * Track form
     *
     * @attr form REQUIRED The link DOM element you want to track clicks on. You can also pass an array of link elements, or a jQuery object. Note: This must be an element, not a CSS selector.
     * @attr event REQUIRED The name of the event which gets passed straight to the track method.
     * @attr properties A dictionary of properties which get passed straight to the track method. You can also pass a function here which returns a dictionary of properties instead, and which will be called with the link that was clicked.
     * @attr context A map of of environment variables that aren't a property of the user. Ex.: list of providers or userAgent
     */
    def trackForm = { attrs ->
        assert attrs.form
        assert attrs.event
        if (!attrs.context) attrs.context = defaultContext
        if (!attrs.properties) attrs.properties = [:]
        if (enabled) {
            out << render(template: '/tags/trackForm', model: attrs, plugin: 'segment')
        }
    }

    /**
     * Track link
     *
     * @attr link REQUIRED The link DOM element you want to track clicks on. You can also pass an array of link elements, or a jQuery object. Note: This must be an element, not a CSS selector.
     * @attr event REQUIRED The name of the event which gets passed straight to the track method.
     * @attr properties A dictionary of properties which get passed straight to the track method. You can also pass a function here which returns a dictionary of properties instead, and which will be called with the link that was clicked.
     * @attr context A map of of environment variables that aren't a property of the user. Ex.: list of providers or userAgent
     */
    def trackLink = { attrs ->
        assert attrs.link
        assert attrs.event
        if (!attrs.context) attrs.context = defaultContext
        if (!attrs.properties) attrs.properties = [:]
        if (enabled) {
            out << render(template: '/tags/trackLink', model: attrs, plugin: 'segment')
        }
    }

    // PRIVATE

    private def getConfig() {
        if (grailsApplication.config.grails?.plugin?.segment) {
            grailsApplication.config.grails?.plugin?.segment
        } else if (grailsApplication.config.grails?.plugin?.segmentio) {
            // Legacy config
            grailsApplication.config.grails?.plugin?.segmentio
        }
    }

    private def getDefaultContext() {
        config?.options ?: [:]
    }

    private boolean isEnabled() {
        boolean configEnabled = false
        if (config?.apiKey) {
            // default enabled for PROD
            configEnabled = (Environment.current == Environment.PRODUCTION)

            // if config specified, use that instead
            if (config.containsKey('enabled')) {
                configEnabled = config.enabled.toBoolean()
            }
        }
        configEnabled
    }

    private boolean isIntercomSecureModeEnabled() {
        if (config?.intercomSecretKey) {
            true
        } else {
            false
        }
    }

}
