<r:script>
    <g:if test="${url}">analytics.pageview('${url}');</g:if>
    <g:else>analytics.pageview();</g:else>
</r:script>