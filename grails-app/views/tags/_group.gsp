<%@ page import="grails.converters.JSON" %>
<r:script>
    analytics.group('${groupId}', ${traits ? traits as JSON : '{}'});
</r:script>