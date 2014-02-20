<%@ page import="grails.converters.JSON" %>
<script type="text/javascript">
    analytics.identify('${userId}', ${traits ? traits as JSON : '{}'}, ${context ? context as JSON : '{}'});
</script>