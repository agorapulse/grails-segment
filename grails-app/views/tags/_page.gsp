<%@ page import="grails.converters.JSON" expressionCodec="raw" %>
<script type="text/javascript">
    <g:if test="${category && name}">analytics.page('${category}', '${name}', ${properties ? properties as JSON : '{}'}, ${options ? options as JSON : '{}'});</g:if>
    <g:elseif test="${name}">analytics.page('${name}', ${properties ? properties as JSON : '{}'}, ${options ? options as JSON : '{}'});</g:elseif>
    <g:else>analytics.page();</g:else>
</script>t>