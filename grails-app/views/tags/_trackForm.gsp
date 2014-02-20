<%@ page import="grails.converters.JSON" %>
<script type="text/javascript">
    analytics.trackForm(${form}, '${event}', ${properties ? properties as JSON : '{}'}, ${context ? context as JSON : '{}'});
</script>