<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.GameState, util.Html" %>
<%-- Screen 2: Game page - four questions --%>
<% String pageTitle = "Game"; String active = "game"; %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<%
    GameState g = (GameState) request.getAttribute("game");
    @SuppressWarnings("unchecked")
    List<String> errors = (List<String>) request.getAttribute("errors");
    String[] entered = (String[]) request.getAttribute("entered");
    if (entered == null) { entered = new String[] {"", "", "", ""}; }
%>

<p class="player">Player: <b><%= Html.esc(g.getPlayer()) %></b> &middot; Difficulty: <span class="level">NORMAL</span></p>

<% if (errors != null && !errors.isEmpty()) { %>
  <div class="alert">
    <b>Please fix these answers:</b>
    <ul>
      <% for (String e : errors) { %><li><%= Html.esc(e) %></li><% } %>
    </ul>
  </div>
<% } %>

<form method="post" action="${pageContext.request.contextPath}/game" autocomplete="off">
  <input type="hidden" name="action" value="submit">

  <% for (int i = 0; i < 4; i++) { %>
  <section class="card q">
    <div class="qhead">
      <span>Question <%= i + 1 %> &middot; <%= g.getArea(i) %></span>
      <span class="tag"><%= g.getMaxMarks(i) %> marks</span>
    </div>
    <div class="display">
      <div class="expr"><%= Html.esc(g.getExpression(i)) %> =</div>
      <div class="big">?</div>
    </div>
    <label for="q<%= i + 1 %>"><%= Html.esc(g.getQuestionText(i)) %></label>
    <p class="hint"><%= Html.esc(g.getHint(i)) %></p>
    <input type="text" inputmode="<%= i < 2 ? "numeric" : "decimal" %>" id="q<%= i + 1 %>" name="q<%= i + 1 %>"
           placeholder="Your answer" required value="<%= Html.esc(entered[i]) %>">
  </section>
  <% } %>

  <div class="center">
    <button type="submit" class="btn">Submit Answers</button>
  </div>
</form>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
