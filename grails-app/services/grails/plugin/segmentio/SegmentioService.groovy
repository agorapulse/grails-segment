package grails.plugin.segmentio

import com.github.segmentio.Analytics
import com.github.segmentio.Config
import com.github.segmentio.models.Context
import com.github.segmentio.models.Options
import com.github.segmentio.models.Props
import com.github.segmentio.models.Traits
import grails.util.Environment
import org.joda.time.DateTime
import org.springframework.beans.factory.InitializingBean

class SegmentioService implements InitializingBean {

    def grailsApplication

    private Analytics analytics

    void afterPropertiesSet() {
        if (!enabled) {
            log.info "Segment.io is not enabled"
        } else {
            log.debug "Initializing Segment.io service"
            Config options = new Config()
            if (config.maxQueueSize) {
                options.maxQueueSize = config.maxQueueSize // default to 10000 (10 000 messages)
            }
            if (config.timeout) {
                options.timeout = config.timeout // default to 10000 (10s)
            }
            if (config.retries) {
                options.retries = config.retries // default to 2
            }
            if (config.backoff) {
                options.backoff = config.backoff // default to 1000 (1s)
            }
            Analytics.initialize(config.serverApiKey ?: config.apiKey, options)
            analytics = new Analytics()
        }
    }

    /**
     * Flushes the current contents of the queue
     */
    void flush() {
        if (enabled) {
            Analytics.flush()
        }
    }

    /**
     * Alias method lets you merge two user profiles, including their actions and traits.
     *
     * @param from
     *            the user's id after they are logged in. It's the same id as
     *            which you would recognize a signed-in user in your system.
     *
     * @param to
     *            new user id
     */
    void alias(def from, def to) {
        if (enabled) {
            log.debug "Alias from=$from to=$to"
            analytics.alias(
                    from.toString(),
                    to.toString()
            )
        }
    }

    /**
     * Group method lets you associate a user with a group.
     *
     * @param userId
     *            the user's id after they are logged in. It's the same id as
     *            which you would recognize a signed-in user in your system.
     *
     * @param groupId
     *            The ID for this group in your database.
     *
     * @param options
     *            a custom object which allows you to set a timestamp,
     *            an anonymous cookie id, or enable specific integrations.
     *
     */
    void group(def userId, def groupId, Map traits = [:], Map options = [:]) {
        if (enabled) {
            log.debug "Group userId=$userId groupId=$groupId traits=$traits"
            analytics.group(
                    userId.toString(),
                    groupId.toString(),
                    traits ? new Traits(*traits.collect { k, v -> [k, v] }.flatten()) : null,
                    buildOptions(options)
            )
        }
    }

    /**
     * identify lets you tie a user to their actions and record traits about them.
     *
     * @param userId
     *            The ID for this user in your database.
     *
     * @param traits
     *            A dictionary of traits you know about the user. Things like: email,
     *            name or friends.
     *
     * @param timestamp
     *            A DateTime representing when the identify took place.
     *            If the identify just happened, leave it blank and we'll use
     *            the server's time. If you are importing data from the past,
     *            make sure you provide this argument.
     *
     * @param options
     *            A custom object which allows you to set a timestamp, an anonymous cookie id,
     *            or enable specific integrations.
     *
     */
    void identify(def userId, Map traits = [:], DateTime timestamp = null, Map options = [:]) {
        if (enabled) {
            log.debug "Identify userId=$userId traits=$traits timestamp=$timestamp options=$options"
            analytics.identify(
                    userId.toString(),
                    traits ? new Traits(*traits.collect { k, v -> [k, v] }.flatten()) : null,
                    buildOptions(options, timestamp)
            )
        }
    }

    /**
     * Page method lets you record webpage visits from your web servers.
     *
     * @param userId
     *            The ID for this user in your database.
     *
     * @param name
     *            The webpage name you’re tracking. We recommend human-readable
     *            names like Login or Register.
     *
     * @param category
     *           The webpage category. If you’re making a news app, the category
     *           could be Sports.
     *
     * @param properties
     *            A dictionary of properties for the webpage visit. If the event
     *            was Login, it might have properties like path or title.
     *
     * @param timestamp
     *            a DateTime object representing when the track took
     *            place. If the event just happened, leave it blank and we'll
     *            use the server's time. If you are importing data from the
     *            past, make sure you provide this argument.
     *
     * @param options
     *            A custom object which allows you to set a timestamp, an anonymous
     *            cookie id, or enable specific integrations.
     *
     */
    void page(def userId, String name, String category, Map properties = [:], DateTime timestamp = null, Map options = [:]) {
        if (enabled) {
            log.debug "Page userId=$userId name=$name category=$category properties=$properties timestamp=$timestamp options=$options"
            analytics.page(
                    userId.toString(),
                    name,
                    category,
                    properties ? new Props(*properties.collect { k, v -> [k, v] }.flatten()) : null,
                    buildOptions(options, timestamp)
            )
        }
    }

    /**
     * screen lets you record mobile screen views from your web servers.
     *
     * @param userId
     *            The ID for this user in your database.
     *
     * @param name
     *            The screen name you’re tracking. We recommend human-readable
     *            names like Login or Register.
     *
     * @param category
     *           The webpage category. If you’re making a news app, the category
     *           could be Sports.
     *
     * @param properties
     *            A dictionary of properties for the screen view. If the screen
     *            is Restaurant Reviews, it might have properties like reviewCount
     *            or restaurantName.
     *
     * @param timestamp
     *            A DateTime object representing when the track took
     *            place. If the event just happened, leave it blank and we'll
     *            use the server's time. If you are importing data from the
     *            past, make sure you provide this argument.
     *
     * @param options
     *            A custom object which allows you to set a timestamp, an anonymous
     *            cookie id, or enable specific integrations.
     *
     */
    void screen(def userId, String name, String category, Map properties = [:], DateTime timestamp = null, Map options = [:]) {
        if (enabled) {
            log.debug "Screen userId=$userId name=$name category=$category properties=$properties timestamp=$timestamp options=$options"
            analytics.screen(
                    userId.toString(),
                    name,
                    category,
                    properties ? new Props(*properties.collect { k, v -> [k, v] }.flatten()) : null,
                    buildOptions(options, timestamp)
            )
        }
    }

    /**
     * track lets you record the actions your users perform.
     *
     * @param userId
     *            The ID for this user in your database.
     *
     * @param event
     *            The name of the event you’re tracking. We recommend human-readable
     *            names like Played Song or Updated Status.
     *
     * @param properties
     *            A dictionary of properties for the event. If the event was Added to Cart,
     *            it might have properties like price or product.
     *
     * @param timestamp
     *            a DateTime object representing when the track took
     *            place. If the event just happened, leave it blank and we'll
     *            use the server's time. If you are importing data from the
     *            past, make sure you provide this argument.
     *
     * @param options
     *            A custom object which allows you to set a timestamp, an anonymous cookie id,
     *            or enable specific integrations.
     *
     */
    void track(def userId, String event, Map properties = [:], DateTime timestamp = null, Map options = [:]) {
        if (enabled) {
            log.debug "Tracking userId=$userId event=$event properties=$properties timestamp=$timestamp options=$options"
            analytics.track(
                    userId.toString(),
                    event,
                    properties ? new Props(*properties.collect { k, v -> [k, v] }.flatten()) : null,
                    buildOptions(options, timestamp)
            )
        }
    }

    // PRIVATE

    private static Options buildOptions(Map options, DateTime timestamp = null) {
        Options sioOptions = new Options()
        if (timestamp) {
            sioOptions.timestamp = timestamp
        }
        if (options.anonymousId) {
            sioOptions.anonymousId = options.anonymousId
        }
        if (options.providers) {
            options.providers.each { String integration, Boolean enabled ->
                sioOptions.setIntegration(integration, enabled)
            }
        }
        if (options.ip || options.language || options.userAgent) {
            Context sioContext = new Context()
            if (options.ip) {
                sioContext.ip = options.ip
            }
            if (options.language) {
                sioContext.language = options.language
            }
            if (options.userAgent) {
                sioContext.userAgent = options.userAgent
            }
            sioOptions.context = sioContext
        }
        sioOptions
    }

    private def getConfig() {
        grailsApplication.config.grails?.plugin?.segmentio
    }

    private boolean isEnabled() {
        boolean configEnabled = false
        if (config?.apiKey || config?.serverApiKey) {
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
