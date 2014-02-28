<%@ page import="grails.converters.JSON" expressionCodec="raw" %>
<script type="text/javascript">
    analytics.trackLink(${link}, '${event}', ${properties ? properties as JSON : '{}'}, ${context ? context as JSON : '{}'});
</script>