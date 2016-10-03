Segment.io Grails Plugin
=========================

[![Build Status](https://travis-ci.org/benorama/grails-segmentio.png)](https://travis-ci.org/agorapulse/grails-segmentio)

# Introduction

The **Segment.io Plugin** allows you to integrate [Segment.io](http://segment.io) in your [Grails](http://grails.org) application.

Segment.io lets you send your analytics data to any service you want, without you having to integrate with each one individually.

It provides the following Grails artefacts:
* **SegmentioService** - A server side service client to call [Segment.io APIs](https://segment.io/api/rest).
* **SegmentioTagLib** - A collection of tags to use [Segment.io Analytics.js Library](https://segment.io/libraries/analytics.js) in your GSPs.


# Installation

Declare the plugin dependency in the BuildConfig.groovvy file, as shown here:

```groovy
grails.project.dependency.resolution = {
		inherits("global") { }
		log "info"
		repositories {
				//your repositories
		}
		dependencies {
				// Latest httpclient used by SegmentIO java lib - Might be required to resolve dependency issues with older httpclient used by other plugins
				build 'org.apache.httpcomponents:httpcore:4.3.2'
                build 'org.apache.httpcomponents:httpclient:4.3.5'
                runtime 'org.apache.httpcomponents:httpcore:4.3.2'
                runtime 'org.apache.httpcomponents:httpclient:4.3.5'
		}
		plugins {
				//here go your plugin dependencies
				runtime ':segmentio:1.0.7.4'
		}
}
```


# Config

Create a [Segment.io](http://segment.io) account, in order to get your own _apiKey_ (for client-side API calls).

Add your Segment.io site _apiKey_  to your _grails-app/conf/Config.groovy_:

```groovy
grails.plugin.segmentio.apiKey = {API_KEY} // Write key
```
By default the Segment.io will only be enabled for Production environments.  If you need it to be enabled for other environments, make sure that it is explicitly enabled in your configs.

```groovy
grails.plugin.segmentio.enabled = true
```

If you want a different `apiKey` for server side tracking, you can specify a server side API key.

```groovy
grails.plugin.segmentio.serverApiKey = {API_KEY} // Write key, for server side tracking
```

If you're using Intercom, you can automatically enable Intercom secure mode (for `segmentio:identify`) by adding you Intercom secret key:

```groovy
grails.plugin.segmentio.intercomSecretKey = {INTERCOM_SECRET_KEY}
```

Server side client uses the default following config:

```groovy
// Queue size limit
grails.plugin.segmentio.maxQueueSize = 10000
// The amount of milliseconds that passes before a request is marked as timed out
grails.plugin.segmentio.timeout = 10000
// How many times to retry the request.
grails.plugin.segmentio.retries = 2
// Backoff in milliseconds between retries.
grails.plugin.segmentio.backoff = 1000
```

# Usage

## SegmentioService

You can inject _segmentioService_ in any of your Grails artefacts (controllers, services...) in order to call [Segment.io APIs](https://segment.io/api/rest).

```groovy
def segmentioService

// Identify and set traits
segmentioService.identify('bob@bob.com', [gender: 'male'])

// Identify and set traits with past date (JodaTime DateTime representing when the identify took place)
segmentioService.identify(
    'bob@bob.com',
    [gender: 'male'],
    new DateTime(2012, 3, 26, 12, 0, 0, 0)
)

// Identify and set traits with past date and context
segmentioService.identify(
    'bob@bob.com', [gender: 'male'],
    new DateTime(2012, 3, 26, 12, 0, 0, 0),
    [
        integrations: [
            'All': false,
            'Mixpanel': true,
            'KISSmetrics': true
        ],
        ip: '192.168.0.10'
    ]
)

// Track an event
segmentioService.track('bob@bob.com', 'Signed up')

// Track an event and set properties
segmentioService.track(
    'bob@bob.com',
    'Signed up',
    [plan: 'Pro', amount: 99.95]
)

// Track a past event and set properties with past date
segmentioService.track(
    'bob@bob.com', 'Signed up',
    [plan: 'Pro', amount: 99.95],
    new DateTime(2012, 3, 26, 12, 0, 0, 0)
)

// Track a past event and set properties with past date and context
segmentioService.track(
    'bob@bob.com',
    'Signed up',
    [plan: 'Pro', amount: 99.95],
    new DateTime(2012, 3, 26, 12, 0, 0, 0),
    [
        integrations: [
            'All': false,
            'Mixpanel': true,
            'KISSmetrics': true
        ],
        ip: '192.168.0.10'
    ]
)

// Group
segmentioService.group('bob@bob.com', 'companyId', [
    name: 'The company name',
    website: 'http://www.company.com'
])

// Record page view
segmentioService.page('Pricing')

// Record page view with extra info
segmentioService.page('bob@bob.com', 'Pricing', 'Business', [
    title: 'Segment.io Pricing',
    path: '/pricing'
])

// Record screen view
segmentioService.screen('bob@bob.com', 'Register', 'Business', [
    type: 'facebook'
])

// Alias identity
segmentioService.alias('bob@bob.com', 'bob')
```

## SegmentioTagLib

To use [Segment.io Analytics.js Library](http://support.segmentio.com/apis/javascript), you must first initialize it in page header (most probably in you layout GSP).

# JS Lib initialization

```jsp
<!DOCTYPE html>
<html>
<head>
    <segmentio:initJS/>
```

Or with default page view tracking disabled:

```jsp
    <segmentio:initJS pageTracked="false"/>
```


# Identification and event recording

Once initialized, you can use [Segment.io Javascript Library](https://segment.io/libraries/analytics.js) in your GSP views.

```jsp
<!-- Identify current user -->
<segmentio:identify userId="bob@bob.com"/>

<!-- Identify current user and set traits -->
<segmentio:identify userId="bob@bob.com" traits="${[gender: 'male']}"/>

<!-- Identify a group and set traits -->
<segmentio:group groupId="power_users" traits="${[plan: 'silver']}"/>

<!-- Identify current user with context -->
<segmentio:identify
    userId="bob@bob.com"
    context="${[integrations: ['All': false, 'Mixpanel': true, 'KISSmetrics': true]]}"/>

<!-- Track an event -->
<segmentio:track event="Signed Up"/>

<!-- Track an event and set properties -->
<segmentio:track event="Signed Up" properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Track an event with context -->
<segmentio:track
    event="Signed Up"
    context="${[integrations: ['All': false, 'Google Analytics': true, 'Customer.io': true]]}"/>

<!-- Track a link click -->
<segmentio:trackLink
    event="Signed Up"
    link="\$('a.signup-link')"
    properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Track a form submission -->
<segmentio:trackForm
    event="Signed Up"
    form="\$('form.signup-form')"
    properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Page view -->
<segmentio:page category="Some category" name="Page title"/>

<!-- Page view with name -->
<segmentio:page name="Page title"/>

<!-- Page view with category and name -->
<segmentio:page category="Some category" name="Page title"/>

<!-- Alias identity -->
<segmentio:alias originalId="bob@bob.com" newId="bob"/>
```

It will generate the corresponding javascript code that will be automatically deferred to page footer thanks to [Grails Resources framework](https://github.com/grails-plugins/grails-resources).


# Latest releases

* 2016-10-01 **v1.0.7.4** : new default `options` can be set in config (done on client side)
* 2016-10-01 **v1.0.7.3** : new default `options` can be set in config
* 2016-07-05 **v1.0.7.2** : new `active` options context param added
* 2015-07-27 **v1.0.7.1** : new `serverApiKey`conf param added
* 2015-03-22 **v1.0.7** : analytics-java lib upgraded to segmentio 1.0.7
* 2014-09-31 **v1.0.4** : analytics-java lib upgraded to segmentio 1.0.4
* 2014-09-04 **v1.0.0** : analytics-java lib upgraded to segmentio 1.0.0 + group(), page() and screen() methods added
* 2014-05-14 **V0.4.3** : init js updated (snippet version 2.0.9)
* 2014-03-27 **V0.4.2** : analytics-java lib updated to segmentio 0.4.2 (retry count + timeout added)
* 2014-03-13 **V0.4.0.5** : new `segmentioService.alias(from, to)` method.
* 2014-03-08 **V0.4.0.4** : typo fix in `segmentio:page` tag.
* 2014-03-05 **V0.4.0.3** : page tracking enabled by default in `segmentio:initJS` tag.
* 2014-02-28 **V0.4.0.2** : Intercom secure mode integration
* 2014-02-28 **V0.4.0.1** :
    - Analytics JS initialization code updated to 2.0.8,
    - segmentio:pageview renamed to segmentio:page (BREAKING),
    - apiSecret config param removed,
    - if `timestamp` is not provided to `segmentioService.track`, default to server current time.
* 2014-02-20 **V0.4.0** : Grails Resources plugin dependency removed + SegmentIO Analytics java lib upgraded to 0.4.0
* 2013-10-29 **V0.3.1.1** : Minor issue fixes (initialize undefined taglib attributes)
* 2013-09-03 **V0.3.1** : updated to segmentio 0.3.1
* 2013-05-11 **V0.2.0** : updated to segmentio 0.2.0 (thanks to pull request by tuler)
* 2013-03-25 **V0.1.7** : initial release

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-segmentio/issues) section on GitHub.
