<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Html" %>
<%-- Screen 1: Welcome page --%>
<% String pageTitle = "Welcome"; String active = "home"; %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<% String error = (String) request.getAttribute("error"); %>
<% if (error != null) { %>
  <div class="alert"><%= Html.esc(error) %></div>
<% } %>

<section class="card hero center">
  <h2>Welcome, challenger!</h2>
  <p class="muted">Four mathematical challenges. Every answer is calculated by Java methods,
     and your score goes onto the leaderboard.</p>

  <form method="post" action="${pageContext.request.contextPath}/game" class="start">
    <input type="hidden" name="action" value="start">
    <label for="player">Player name</label>
    <input type="text" id="player" name="player" maxlength="50" placeholder="Enter your name"
           required autofocus value="<%= Html.esc(request.getParameter("player")) %>">
    <button type="submit" class="btn">Start Game &rarr;</button>
  </form>
</section>

<section class="grid3">
  <div class="card mini">
    <span class="tag">2 marks</span>
    <h3>Prime Number</h3>
    <p class="muted">Is the number prime? Answer <b>1</b> for prime, <b>0</b> for not prime.</p>
    <span class="level">NORMAL</span>
  </div>
  <div class="card mini">
    <span class="tag">2 marks</span>
    <h3>Modulus</h3>
    <p class="muted">Find the remainder of an integer division.</p>
    <span class="level">NORMAL</span>
  </div>
  <div class="card mini">
    <span class="tag">3 + 3 marks</span>
    <h3>Trigonometry</h3>
    <p class="muted">Sine, cosine or tangent of an angle in degrees (2 decimals).</p>
    <span class="level">NORMAL</span>
  </div>
</section>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
