<%@ page import="grails.converters.JSON" expressionCodec="raw" %>
<script type="text/javascript">
    analytics.identify('${userId}', ${traits ? traits as JSON : '{}'}, ${context ? context as JSON : '{}'});
</script>