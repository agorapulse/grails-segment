<%@ page import="grails.converters.JSON" %>
<script type="text/javascript">
    analytics.track('${event}', ${properties ? properties as JSON : '{}'}, ${context ? context as JSON : '{}'});
</script>