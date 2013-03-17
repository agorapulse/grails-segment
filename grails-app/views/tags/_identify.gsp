<%@ page import="grails.converters.JSON" %>
<r:script>
    analytics.identify('${userId}', ${traits ? traits as JSON : '{}'}, ${context ? context as JSON : '{}'});
</r:script>