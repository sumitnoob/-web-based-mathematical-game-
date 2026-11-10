<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.GameState, util.Html" %>
<%-- Screen 3: Result page --%>
<% String pageTitle = "Result"; String active = "game"; %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<% GameState g = (GameState) request.getAttribute("game"); %>

<section class="card hero center">
  <h2>Result for <%= Html.esc(g.getPlayer()) %></h2>
  <div class="score"><%= g.getTotalScore() %><span> / ${maxScore}</span></div>
  <p class="muted">Difficulty: <span class="level">NORMAL</span> &middot;
     Saved to the database on <%= Html.esc(g.getPlayedAtText()) %></p>
</section>

<% for (int i = 0; i < 4; i++) { %>
<section class="card q">
  <div class="qhead">
    <span>Question <%= i + 1 %> &middot; <%= g.getArea(i) %></span>
    <span class="tag <%= g.getCorrect()[i] ? "ok" : "bad" %>"><%= g.getEarnedMarks(i) %> / <%= g.getMaxMarks(i) %> marks</span>
  </div>
  <div class="display">
    <div class="expr"><%= Html.esc(g.getExpression(i)) %> =</div>
    <div class="big"><%= Html.esc(g.getExpected()[i]) %></div>
  </div>
  <p class="hint">Calculated by <code><%= Html.esc(g.getMethodName(i)) %></code></p>
  <p>Your answer: <b><%= Html.esc(g.getAnswers()[i]) %></b> &nbsp;
     <% if (g.getCorrect()[i]) { %><span class="okt">&#10004; Correct</span>
     <% } else { %><span class="badt">&#10008; Not correct &ndash; 0 marks</span><% } %></p>
</section>
<% } %>

<div class="center actions">
  <form method="post" action="${pageContext.request.contextPath}/game" class="inline">
    <input type="hidden" name="action" value="start">
    <input type="hidden" name="player" value="<%= Html.esc(g.getPlayer()) %>">
    <button type="submit" class="btn">Play Again</button>
  </form>
  <a class="btn alt" href="${pageContext.request.contextPath}/leaderboard">View Leaderboard</a>
  <a class="btn alt" href="${pageContext.request.contextPath}/">Home</a>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
