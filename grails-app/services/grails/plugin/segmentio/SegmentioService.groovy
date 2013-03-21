package grails.plugin.segmentio

import com.github.segmentio.Analytics
import com.github.segmentio.Options
import com.github.segmentio.models.Context
import com.github.segmentio.models.EventProperties
import com.github.segmentio.models.Traits
import grails.util.Environment
import org.joda.time.DateTime
import org.springframework.beans.factory.InitializingBean

class SegmentioService implements InitializingBean {

    def grailsApplication

    void afterPropertiesSet() {
        if (!enabled) {
            log.info "Segment.io is not enabled"
        } else {
            Options options = new Options()
            if (config.flushAfter) options.flushAfter = config.flushAfter // default to 20 (every 20 messages)
            if (config.flushAt) options.flushAt = config.flushAt // default to 10000 (if 10 seconds has passed since the last flush)
            if (config.maxQueueSize) options.maxQueueSize = config.maxQueueSize // default to 10000 (10 000 messages)
            Analytics.initialize(config.apiSecret, options)
        }

    }

    /**
     * Flushes the current contents of the queue
     */
    void flush() {
        if (enabled) Analytics.flush()
    }

    /**
     * Identifying a user ties all of their actions to an id, and associates
     * user traits to that id.
     *
     * @param userId
     *            the user's id after they are logged in. It's the same id as
     *            which you would recognize a signed-in user in your system.
     *
     * @param traits
     *            a dictionary with keys like subscriptionPlan or age. You only
     *            need to record a trait once, no need to send it again.
     *
     * @param timestamp
     *            a DateTime representing when the identify took place.
     *            If the identify just happened, leave it blank and we'll use
     *            the server's time. If you are importing data from the past,
     *            make sure you provide this argument.
     *
     * @param context
     *            an object that describes anything that doesn't fit into this
     *            event's properties (such as the user's IP)s
     *
     */
    void identify(def userId, Map traits = [:], DateTime timestamp = null, Map context = [:]) {
        if (enabled) {
            // if (context.providers) context.providers = (context.providers as JSON).toString()
            Analytics.identify(
                    userId.toString(),
                    traits ? new Traits(*traits.collect { k, v -> [k, v] }.flatten()) : null,
                    timestamp,
                    context ? new Context(*context.collect { k, v -> [k, v] }.flatten()) : null
            )
        }
    }

    /**
     * Whenever a user triggers an event, you’ll want to track it.
     *
     * @param userId
     *            the user's id after they are logged in. It's the same id as
     *            which you would recognize a signed-in user in your system.
     *
     * @param event
     *            describes what this user just did. It's a human readable
     *            description like "Played a Song", "Printed a Report" or
     *            "Updated Status".
     *
     * @param properties
     *            a dictionary with items that describe the event in more
     *            detail. This argument is optional, but highly
     *            recommended—you’ll find these properties extremely useful
     *            later.
     *
     * @param timestamp
     *            a DateTime object representing when the track took
     *            place. If the event just happened, leave it blank and we'll
     *            use the server's time. If you are importing data from the
     *            past, make sure you provide this argument.
     *
     * @param context
     *            an object that describes anything that doesn't fit into this
     *            event's properties (such as the user's IP)
     *
     */
    void track(def userId, String event, Map properties = [:], DateTime timestamp = null, Map context = [:]) {
        if (enabled) {
            // if (context.providers) context.providers = (context.providers as JSON).toString()
            Analytics.track(
                    userId.toString(),
                    event,
                    properties ? new EventProperties(*properties.collect { k, v -> [k, v] }.flatten()) : null,
                    timestamp,
                    context ? new Context(*context.collect { k, v -> [k, v] }.flatten()) : null
            )
        }
    }

    // PRIVATE

    private def getConfig() {
        grailsApplication.config.grails?.plugin?.segmentio
    }

    private boolean isEnabled() {
        boolean configEnabled = false
        if (config?.apiSecret) {
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