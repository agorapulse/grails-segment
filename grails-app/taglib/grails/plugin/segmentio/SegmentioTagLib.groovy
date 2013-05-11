package grails.plugin.segmentio

import grails.util.Environment

class SegmentioTagLib {

    static namespace = 'segmentio'

    /**
     * Initialize Segment.io
     * @attr providers A dictionary of the analytics providers you want to enable. Each provider takes a string of your API key for the service. Or, if you want to supply additional settings, you can also pass a dictionary of provider-specific settings as well. You only need to call this method if you're using your own copy of analytics.js on it's own.
     */
    def initJS = { attrs ->
        if (enabled) {
            Map model = [
                    apiKey: config.apiKey
            ]
            out << render(template: '/tags/initJs', model: model, plugin: 'segmentio')
        }
    }

    /**
     * Alias identity
     *
     * @attr newId REQUIRED The new ID you want to associate the user with.
     * @attr originalId The original ID that the user was recognized by. This defaults to the currently identified user's ID if there is one. In most cases you don't need to pass this argument.
     */
    def alias = { attrs ->
        if (enabled) {
            out << render(template: '/tags/alias', model: attrs, plugin: 'segmentio')
        }
    }

    /**
     * Identitify current user
     *
     * @attr userId REQUIRED The ID you refer to the user by in your database.
     * @attr traits A map of traits you know about the user. Things like: email, name, subscriptionPlan, etc.
     * @attr context A map of of environment variables that aren't a trait of the user. Ex.: list of providers or userAgent
     */
    def identify = { attrs ->
        if (enabled) {
            out << render(template: '/tags/identify', model: attrs, plugin: 'segmentio')
        }
    }

    /**
     * Group
     *
     * @attr groupId REQUIRED The ID you refer to the group by in your database.
     * @attr traits A map of traits you know about the group. Things like: name, subscriptionPlan, etc.
     */
    def group = { attrs ->
        if (enabled) {
            out << render(template: '/tags/group', model: attrs, plugin: 'segmentio')
        }
    }

    /**
     * Page view
     *
     * @attr url The URL of the current page you're tracking. By default, services will use the current URL. If your app doesn't update the URL automatically, you'll want to pass it in here.
     */
    def pageview = { attrs ->
        if (enabled) {
            out << render(template: '/tags/pageview', model: attrs, plugin: 'segmentio')
        }
    }

    /**
     * Ready
     *
     * @attr callback REQUIRED A callback you want to fire after analytics have loaded.
     */
    def ready = { attrs ->
        if (enabled) {
            out << render(template: '/tags/ready', model: attrs, plugin: 'segmentio')
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
        if (enabled) {
            out << render(template: '/tags/track', model: attrs, plugin: 'segmentio')
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
        if (enabled) {
            out << render(template: '/tags/trackForm', model: attrs, plugin: 'segmentio')
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
        if (enabled) {
            out << render(template: '/tags/trackLink', model: attrs, plugin: 'segmentio')
        }
    }

    // PRIVATE

    private def getConfig() {
        grailsApplication.config.grails?.plugin?.segmentio
    }

    private boolean isEnabled() {
        boolean configEnabled = false
        if (config?.apiKey) {
            // default enabled for PROD
            configEnabled = (Environment.current == Environment.PRODUCTION)

            // if config specified, use that instead
            if (config.containsKey('enabled')) {
                configEnabled = config.enabled
            }
        }
        configEnabled
    }

}
