Segment Grails Plugin
=====================

[![Build Status](https://travis-ci.org/agorapulse/grails-segmentio.png)](https://travis-ci.org/agorapulse/grails-segmentio)

# Introduction

The **Segment Plugin** allows you to integrate [Segment](http://segment.com) in your [Grails](http://grails.org) application.

Segment.io lets you send your analytics data to any service you want, without you having to integrate with each one individually.

It provides the following Grails artefacts:
* **SegmentService** - A server side service client to call [Segment APIs](https://segment.com/docs/libraries/).
* **SegmentTagLib** - A collection of tags to use [Segment Analytics.js Library](https://segment.com/docs/libraries/analytics.js/) in your GSPs.


# Installation

Declare the plugin dependency in the _build.gradle_ file, as shown here:

```groovy
repositories {
    ...
    maven { url "http://dl.bintray.com/agorapulse/plugins" }
}
dependencies {
    ...
    compile "org.grails.plugins:segment:2.0.0.RC2"
}
```


# Config

Create a [Segment](http://segment.com) account, in order to get your own _apiKey_ (for client-side API calls).

Add your Segment.io site _apiKey_  to your _grails-app/conf/application.yml_:

```yml
grails:
    plugin:
        segment:
            apiKey: {API_KEY} // Write key
```
By default, Segment integration will only be enabled for Production environments.  If you need it to be enabled for other environments, make sure that it is explicitly enabled in your configs.

```yml
grails:
    plugin:
        segment:
            enabled: true
```

If you're using Intercom, you can automatically enable Intercom secure mode (for `segment:identify`) by adding you Intercom secret key:

```yml
grails:
    plugin:
        segment:
            intercomSecretKey: {INTERCOM_SECRET_KEY}
```

Server side client uses the default following config:

```yml
grails:
    plugin:
        segment:
            # Queue size limit
            maxQueueSize: 10000
            # The amount of milliseconds that passes before a request is marked as timed out
            timeout: 10000
            # How many times to retry the request.
            retries: 2
            # Backoff in milliseconds between retries.
            backoff = 1000
```

# Usage

## SegmentService

You can inject _segmentService_ in any of your Grails artefacts (controllers, services...) in order to call [Segment APIs](https://segment.com/docs/libraries/).

```groovy
def segmentService

// Identify and set traits
segmentService.identify('bob@bob.com', [gender: 'male'])

// Identify and set traits with past date (JodaTime DateTime representing when the identify took place)
segmentService.identify(
    'bob@bob.com',
    [gender: 'male'],
    new DateTime(2012, 3, 26, 12, 0, 0, 0)
)

// Identify and set traits with past date and context
segmentService.identify(
    'bob@bob.com', [gender: 'male'],
    new DateTime(2012, 3, 26, 12, 0, 0, 0),
    [
        providers: [
            'All': false,
            'Mixpanel': true,
            'KISSmetrics': true
        ],
        ip: '192.168.0.10'
    ]
)

// Track an event
segmentService.track('bob@bob.com', 'Signed up')

// Track an event and set properties
segmentService.track(
    'bob@bob.com',
    'Signed up',
    [plan: 'Pro', amount: 99.95]
)

// Track a past event and set properties with past date
segmentService.track(
    'bob@bob.com', 'Signed up',
    [plan: 'Pro', amount: 99.95],
    new DateTime(2012, 3, 26, 12, 0, 0, 0)
)

// Track a past event and set properties with past date and context
segmentService.track(
    'bob@bob.com',
    'Signed up',
    [plan: 'Pro', amount: 99.95],
    new DateTime(2012, 3, 26, 12, 0, 0, 0),
    [
        providers: [
            'All': false,
            'Mixpanel': true,
            'KISSmetrics': true
        ],
        ip: '192.168.0.10'
    ]
)

// Group
segmentService.group('bob@bob.com', 'companyId', [
    name: 'The company name',
    website: 'http://www.company.com'
])

// Record page view
segmentService.page('Pricing')

// Record page view with extra info
segmentService.page('bob@bob.com', 'Pricing', 'Business', [
    title: 'Segment.io Pricing',
    path: '/pricing'
])

// Record screen view
segmentService.screen('bob@bob.com', 'Register', 'Business', [
    type: 'facebook'
])

// Alias identity
segmentService.alias('bob@bob.com', 'bob')
```

## SegmentTagLib

To use [Segment Analytics.js Library](https://segment.com/docs/libraries/analytics.js/), you must first initialize it in page header (most probably in you layout GSP).

# JS Lib initialization

```jsp
<!DOCTYPE html>
<html>
<head>
    <segment:initJS/>
```

Or with default page view tracking disabled:

```jsp
    <segment:initJS pageTracked="false"/>
```


# Identification and event recording

Once initialized, you can use [Segment Analytics.js Library](https://segment.com/docs/libraries/analytics.js/) in your GSP views.

```jsp
<!-- Identify current user -->
<segment:identify userId="bob@bob.com"/>

<!-- Identify current user and set traits -->
<segment:identify userId="bob@bob.com" traits="${[gender: 'male']}"/>

<!-- Identify a group and set traits -->
<segment:group groupId="power_users" traits="${[plan: 'silver']}"/>

<!-- Identify current user with context -->
<segment:identify
    userId="bob@bob.com"
    context="${[providers: ['All': false, 'Mixpanel': true, 'KISSmetrics': true]]}"/>

<!-- Track an event -->
<segment:track event="Signed Up"/>

<!-- Track an event and set properties -->
<segment:track event="Signed Up" properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Track an event with context -->
<segment:track
    event="Signed Up"
    context="${[providers: ['All': false, 'Google Analytics': true, 'Customer.io': true]]}"/>

<!-- Track a link click -->
<segment:trackLink
    event="Signed Up"
    link="\$('a.signup-link')"
    properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Track a form submission -->
<segment:trackForm
    event="Signed Up"
    form="\$('form.signup-form')"
    properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Page view -->
<segment:page category="Some category" name="Page title"/>

<!-- Page view with name -->
<segment:page name="Page title"/>

<!-- Page view with category and name -->
<segment:page category="Some category" name="Page title"/>

<!-- Alias identity -->
<segment:alias originalId="bob@bob.com" newId="bob"/>
```

It will generate the corresponding javascript code that will be automatically deferred to page footer thanks to [Grails Resources framework](https://github.com/grails-plugins/grails-resources).


# Latest releases

* 2015-03-22 **v1.0.7** : analytics-java lib upgraded to segmentio 1.0.7
* 2014-09-31 **v1.0.4** : analytics-java lib upgraded to segmentio 1.0.4
* 2014-09-04 **v1.0.0** : analytics-java lib upgraded to segmentio 1.0.0 + group(), page() and screen() methods added
* 2014-05-14 **V0.4.3** : init js updated (snippet version 2.0.9)
* 2014-03-27 **V0.4.2** : analytics-java lib updated to segmentio 0.4.2 (retry count + timeout added)
* 2014-03-13 **V0.4.0.5** : new `segmentService.alias(from, to)` method.
* 2014-03-08 **V0.4.0.4** : typo fix in `segment:page` tag.
* 2014-03-05 **V0.4.0.3** : page tracking enabled by default in `segment:initJS` tag.
* 2014-02-28 **V0.4.0.2** : Intercom secure mode integration
* 2014-02-28 **V0.4.0.1** :
    - Analytics JS initialization code updated to 2.0.8,
    - segment:pageview renamed to segment:page (BREAKING),
    - apiSecret config param removed,
    - if `timestamp` is not provided to `segmentService.track`, default to server current time.
* 2014-02-20 **V0.4.0** : Grails Resources plugin dependency removed + SegmentIO Analytics java lib upgraded to 0.4.0
* 2013-10-29 **V0.3.1.1** : Minor issue fixes (initialize undefined taglib attributes)
* 2013-09-03 **V0.3.1** : updated to segmentio 0.3.1
* 2013-05-11 **V0.2.0** : updated to segmentio 0.2.0 (thanks to pull request by tuler)
* 2013-03-25 **V0.1.7** : initial release

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-segmentio/issues) section on GitHub.