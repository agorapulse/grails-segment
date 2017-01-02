package grails.plugin.segment

import com.segment.analytics.Analytics
import com.segment.analytics.messages.*
import grails.util.Environment
import org.joda.time.DateTime
import org.springframework.beans.factory.InitializingBean

class SegmentService implements InitializingBean {

    def grailsApplication

    private Analytics analytics

    void afterPropertiesSet() {
        if (!enabled) {
            log.info "Segment is not enabled"
        } else {
            log.debug "Initializing Segment service"
            analytics = Analytics.builder(config.apiKey).build()
        }
    }

    /**
     * Flushes the current contents of the queue
     */
    void flush() {
        if (enabled) {
            analytics.flush()
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
            MessageBuilder builder = AliasMessage.builder(from)
                    .userId(to.toString())
            analytics.enqueue(builder)
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
            MessageBuilder builder = GroupMessage.builder(groupId.toString())
                    .userId(userId.toString())
                    .traits(traits)
            addOptions(builder, options ?: defaultOptions)
            analytics.enqueue(builder)
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
            MessageBuilder builder = IdentifyMessage.builder()
                    .userId(userId.toString())
                    .traits(traits)
            addOptions(builder, options ?: defaultOptions, timestamp)
            analytics.enqueue(builder)
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
            MessageBuilder builder = PageMessage.builder(name)
                    .userId(userId.toString())
                    .properties(properties + [category: category])
            addOptions(builder, options ?: defaultOptions, timestamp)
            analytics.enqueue(builder)
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
            MessageBuilder builder = ScreenMessage.builder(name)
                    .userId(userId.toString())
                    .properties(properties + [category: category])
            addOptions(builder, options ?: defaultOptions, timestamp)
            analytics.enqueue(builder)
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
            MessageBuilder builder = TrackMessage.builder(event)
                    .userId(userId.toString())
                    .properties(properties)
            addOptions(builder, options ?: defaultOptions, timestamp)
            analytics.enqueue(builder)
        }
    }

    // PRIVATE

    private static MessageBuilder addOptions(MessageBuilder builder, Map options, DateTime timestamp = null) {
        if (timestamp) {
            builder.timestamp(timestamp.toDate())
        }
        if (options.anonymousId) {
            builder.anonymousId(UUID.fromString(options.anonymousId))
        }
        if (options.integrations) {
            options.integrations.each { String key, Boolean enabled ->
                builder.enableIntegration(key, enabled)
            }
        }
        if (options.ip || options.language || options.userAgent) {
            Map context = [:]
            if (options.ip) {
                context += [ip: options.ip]
            }
            if (options.language) {
                context += [language: options.language]
            }
            if (options.userAgent) {
                context += [userAgent: options.userAgent]
            }
            builder.context(context)
        }
        builder
    }

    private def getConfig() {
        if (grailsApplication.config.grails?.plugin?.segment) {
            grailsApplication.config.grails?.plugin?.segment
        } else if (grailsApplication.config.grails?.plugin?.segmentio) {
            // Legacy config
            grailsApplication.config.grails?.plugin?.segmentio
        }
    }

    private def getDefaultOptions() {
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
}