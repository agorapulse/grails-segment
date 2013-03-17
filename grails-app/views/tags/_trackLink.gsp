<%@ page import="grails.converters.JSON" %>
<r:script>
    analytics.trackLink(${link}, '${event}', ${properties ? properties as JSON : '{}'}, ${context ? context as JSON : '{}'});
</r:script>