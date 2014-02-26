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
				//your dependencies
		}
		plugins {
				//here go your plugin dependencies
				runtime ':segmentio:0.4.0'
		}
}
```


# Config

Create a [Segment.io](http://segment.io) account, in order to get your own _apiKey_ (for client-side API calls) and _apiSecret_ (for server-side API calls).

Add your Segment.io site _apiKey_  and _apiSecret_ to your _grails-app/conf/Config.groovy_:

```groovy
grails.plugin.segmentio.apiKey = {API_KEY} // Read key
grails.plugin.segmentio.apiSecret = {API_SECRET} // Write key
```
By default the Segment.io will only be enabled for Production environments.  If you need it to be enabled for other environments, make sure that it is explicitly enabled in your configs.

```groovy
grails.plugin.segmentio.enabled = true
```

Server side client uses the default following config:

```groovy
// Queue size limit
grails.plugin.segmentio.maxQueueSize = 10000
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
        providers: [
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
        providers: [
            'All': false,
            'Mixpanel': true,
            'KISSmetrics': true
        ],
        ip: '192.168.0.10'
    ]
)
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
    context="${[providers: ['All': false, 'Mixpanel': true, 'KISSmetrics': true]]}"/>

<!-- Track an event -->
<segmentio:track event="Signed Up"/>

<!-- Track an event and set properties -->
<segmentio:track event="Signed Up" properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Track an event with context -->
<segmentio:track
    event="Signed Up"
    context="${[providers: ['All': false, 'Google Analytics': true, 'Customer.io': true]]}"/>

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
<segmentio:pageview url="http://mydomain.com/somepath"/>

<!-- Alias identity -->
<segmentio:alias originalId="bob@bob.com" newId="bob"/>
```

It will generate the corresponding javascript code that will be automatically deferred to page footer thanks to [Grails Resources framework](https://github.com/grails-plugins/grails-resources).


# Latest releases

* 2014-02-20 **V0.4.0** : Grails Resources plugin dependency removed + SegmentIO Analytics java lib upgraded to 0.4.0
* 2013-10-29 **V0.3.1.1** : Minor issue fixes (initialize undefined taglib attributes)
* 2013-09-03 **V0.3.1** : updated to segmentio 0.3.1
* 2013-05-11 **V0.2.0** : updated to segmentio 0.2.0 (thanks to pull request by tuler)
* 2013-03-25 **V0.1.7** : initial release

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-segmentio/issues) section on GitHub.