<%@ page import="grails.converters.JSON" %>
<r:script>
    analytics.track('${event}', ${properties ? properties as JSON : '{}'}, ${context ? context as JSON : '{}'});
</r:script>