<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<% String pageTitle = "Oops"; String active = ""; %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<section class="card hero center">
  <h2>Oops &ndash; something went wrong</h2>
  <p class="muted">The page you asked for could not be shown. Please go back and try again.</p>
  <p><a class="btn" href="${pageContext.request.contextPath}/">Back to Home</a></p>
</section>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
