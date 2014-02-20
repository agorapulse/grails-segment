<%@ page import="grails.converters.JSON" %>
<script type="text/javascript">>
    analytics.group('${groupId}', ${traits ? traits as JSON : '{}'});
</script>