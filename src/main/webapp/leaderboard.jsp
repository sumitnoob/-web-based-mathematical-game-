<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.GameScore, util.Html" %>
<%-- Screen 4: Leaderboard - top 10 scores --%>
<% String pageTitle = "Leaderboard"; String active = "board"; %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<%
    @SuppressWarnings("unchecked")
    List<GameScore> scores = (List<GameScore>) request.getAttribute("scores");
    String error = (String) request.getAttribute("error");
%>

<section class="card board">
  <h2>&#127942; Top 10 Scores</h2>
  <p class="muted">Highest score first. When scores are equal, the earlier game ranks higher.</p>

  <% if (error != null) { %>
    <div class="alert"><%= Html.esc(error) %></div>
  <% } else if (scores == null || scores.isEmpty()) { %>
    <p class="empty">No games played yet &ndash; be the first on the board!</p>
  <% } else { %>
  <div class="tablewrap">
    <table>
      <thead>
        <tr><th>Rank</th><th>Player</th><th>Score</th><th>Difficulty</th><th>Date / Time</th></tr>
      </thead>
      <tbody>
      <% int rank = 1; for (GameScore s : scores) { %>
        <tr class="<%= rank <= 3 ? "top" + rank : "" %>">
          <td><%= rank == 1 ? "&#129351;" : rank == 2 ? "&#129352;" : rank == 3 ? "&#129353;" : String.valueOf(rank) %></td>
          <td><%= Html.esc(s.getPlayerName()) %></td>
          <td><b><%= s.getScore() %></b> / 10</td>
          <td><span class="level"><%= Html.esc(s.getDifficulty()) %></span></td>
          <td><%= Html.esc(s.getPlayedAtText()) %></td>
        </tr>
      <% rank++; } %>
      </tbody>
    </table>
  </div>
  <% } %>

  <p class="center" style="margin-bottom:0"><a class="btn" href="${pageContext.request.contextPath}/">Play the Game</a></p>
</section>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
