Segment Grails Plugin
=====================

[![Build Status](https://travis-ci.org/agorapulse/grails-segment.png)](https://travis-ci.org/agorapulse/grails-segment)
[![Download](https://api.bintray.com/packages/agorapulse/plugins/segment/images/download.svg)](https://bintray.com/agorapulse/plugins/segment/_latestVersion)

# Introduction

IMPORTANT: Project retirement
-------------------------------

**This project is retired. As Micronaut become core of the Grails starting at Grails 4, please, use [Micronaut Segment](https://agorapulse.github.io/micronaut-segment) instead.**

The **Segment Plugin** allows you to integrate [Segment](http://segment.com) in your [Grails](http://grails.org) application.

Segment lets you send your analytics data to any service you want, without you having to integrate with each one individually.

It provides the following Grails artefacts:
* **SegmentService** - A server side service client to call [Segment APIs](https://segment.com/docs/libraries/http/), which is a wrapper around the official Segment [Analytics for Java](https://segment.com/docs/libraries/java/) library.
* **SegmentTagLib** - A collection of tags to use [Segment Analytics.js Library](https://segment.com/docs/libraries/analytics.js/) in your GSPs.

# Installation

Declare the plugin dependency in the _build.gradle_ file, as shown here:

```groovy
dependencies {
    ...
    compile "org.grails.plugins:segment:2.1.1"
    ...
}
```


# Config

Create a [Segment](http://segment.com) account, in order to get your own _apiKey_ (for client-side API calls).

Add your Segment.io site _apiKey_  to your _grails-app/conf/application.yml_:

```yml
grails:
    plugin:
        segment:
            apiKey: {API_KEY} # Write key
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
        integrations: [
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
        integrations: [
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
segmentService.page('bob@bob.com', 'Pricing')

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
    context="${[integrations: ['All': false, 'Mixpanel': true, 'KISSmetrics': true]]}"/>

<!-- Track an event -->
<segment:track event="Signed Up"/>

<!-- Track an event and set properties -->
<segment:track event="Signed Up" properties="${[plan: 'Pro', revenue: 99.95]}"/>

<!-- Track an event with context -->
<segment:track
    event="Signed Up"
    context="${[integrations: ['All': false, 'Google Analytics': true, 'Customer.io': true]]}"/>

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

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-segment/issues) section on GitHub.
