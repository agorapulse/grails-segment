<%@ page import="grails.converters.JSON" expressionCodec="raw" %>
<script type="text/javascript">
    analytics.group('${groupId}', ${traits ? traits as JSON : '{}'});
</script>