<r:script>
    <g:if test="${originalId}">analytics.alias('${newId}', '${originalId}');</g:if>
    <g:else>analytics.alias('${newId}');</g:else>
</r:script>