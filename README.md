Segment.io Grails Plugin
=========================

[![Build Status](https://travis-ci.org/benorama/grails-segmentio.png)](https://travis-ci.org/benorama/grails-segmentio)

# Introduction

The **Segment.io Plugin** allows you to integrate [Segment.io](http://segment.io) in your [Grails](http://grails.org) application.

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
				runtime ':segmentio:0.1.4'
		}
}
```


# Config

Create a [Segment.io](http://segment.io) account, in order to get your own _apiKey_ (for client-side API calls) and _apiSecret_ (for server-side API calls).

Add your Segment.io site _apiKey_  and _apiSecret_ to your _grails-app/conf/Config.groovy_:

```groovy
grails.plugin.segmentio.apiKey = {API_KEY}
grails.plugin.segmentio.apiSecret = {API_SECRET}
```
By default the Segment.io will only be enabled for Production environments.  If you need it to be enabled for other environments, make sure that it is explicitly enabled in your configs.

```groovy
grails.plugin.segmentio.enabled = true
```

Server side client uses the default following config:

```groovy
// Flush every 20 messages
grails.plugin.segmentio.flushAfter = 20
// Flush if 10 seconds has passed since the last flush
grails.plugin.segmentio.flushAt = 10000
// Queue size limit
grails.plugin.segmentio.maxQueueSize = 10000
```

During testing/development, it is recommanded to flush every time a message is submitted.

```groovy
// Flush every time a message is submitted
grails.plugin.segmentio.flushAfter = 1
``

# Usage

## SegmentioService

You can inject _segmentioService_ in any of your Grails artefacts (controllers, services...) in order to call [Segment.io APIs](https://segment.io/api/rest).

```groovy
def segmentioService

// Identify and set traits
segmentioService.identify('bob@bob.com', [gender: 'male'])

// Identify and set traits with date (JodaTime DateTime representing when the identify took place)
segmentioService.identify('bob@bob.com', [gender: 'male'], new DateTime(2012, 3, 26, 12, 0, 0, 0))

// Track an event
segmentioService.track('bob@bob.com', 'Signed up')

// Track an event and set properties
segmentioService.track('bob@bob.com', 'Signed up', [plan: 'Pro', amount: 99.95])

// Track a past event and set properties
segmentioService.track('bob@bob.com', 'Signed up', [plan: 'Pro', amount: 99.95], new DateTime(2012, 3, 26, 12, 0, 0, 0))
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

<!-- Identify current user with context -->
<segmentio:identify userId="bob@bob.com" context="${[providers: ['All': false, 'Mixpanel': true, 'KISSmetrics': true]]}"/>

<!-- Track an event -->
<segmentio:track event="Signed Up"/>

<!-- Track an event and set properties -->
<segmentio:track event="Signed Up" properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Track an event with context -->
<segmentio:track event="Signed Up" context="${[providers: ['All': false, 'Google Analytics': true, 'Customer.io': true]]}"/>

<!-- Track a link click -->
<segmentio:trackLink link="\$('a.signup-link')" event="Signed Up" properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Track a form submission -->
<segmentio:trackForm form="\$('form.signup-form')" event="Signed Up" properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Page view -->
<segmentio:pageview url="http://mydomain.com/somepath"/>

<!-- Alias identity -->
<segmentio:alias originalId="bob@bob.com" newId="bob"/>
```

It will generate the corresponding javascript code that will be automatically deferred to page footer thanks to [Grails Resources framework](https://github.com/grails-plugins/grails-resources).


# Latest releases

* 2013-03-18 **V0.1.4** : initial release

# Bugs

To report any bug, please use the project [Issues](http://github.com/benorama/grails-segmentio/issues) section on GitHub.


# Beta status

This is a **beta release**.
