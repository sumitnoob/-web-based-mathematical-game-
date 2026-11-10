<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="util.Html" %>
<%-- Math Lab: try the Java methods with your own values (Tasks 03, 04, 05 and 09) --%>
<% String pageTitle = "Math Lab"; String active = "lab"; %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<%
    String labType = (String) request.getAttribute("labType");
    String labResult = (String) request.getAttribute("labResult");
    String labError = (String) request.getAttribute("labError");
    String fnSel = request.getParameter("fn") == null ? "sin" : request.getParameter("fn");
%>

<section class="card">
  <h2>&#129514; Math Lab</h2>
  <p class="muted">Test the Java methods yourself. Try <b>7, 8, 1</b> for prime, <b>17 % 5</b> and divisor <b>0</b>
     for modulus, and <b>sin 30&deg;</b>, <b>cos 60&deg;</b>, <b>tan 45&deg;</b>, <b>tan 90&deg;</b> for trigonometry.</p>
</section>

<section class="card lab">
  <h3>Prime number &mdash; <code>isPrime(int)</code></h3>
  <form method="get" action="${pageContext.request.contextPath}/lab" class="row">
    <input type="hidden" name="type" value="prime">
    <input type="text" name="n" placeholder="Integer, e.g. 7" required
           value="<%= "prime".equals(labType) ? Html.esc(request.getParameter("n")) : "" %>">
    <button type="submit" class="btn small">Check</button>
  </form>
  <% if ("prime".equals(labType)) { %>
    <% if (labError != null) { %><div class="alert"><%= Html.esc(labError) %></div>
    <% } else { %><div class="out"><%= Html.esc(labResult) %></div><% } %>
  <% } %>
</section>

<section class="card lab">
  <h3>Modulus &mdash; <code>calculateMod(int a, int b)</code></h3>
  <form method="get" action="${pageContext.request.contextPath}/lab" class="row">
    <input type="hidden" name="type" value="mod">
    <input type="text" name="a" placeholder="a (dividend)" required
           value="<%= "mod".equals(labType) ? Html.esc(request.getParameter("a")) : "" %>">
    <span class="op">%</span>
    <input type="text" name="b" placeholder="b (divisor)" required
           value="<%= "mod".equals(labType) ? Html.esc(request.getParameter("b")) : "" %>">
    <button type="submit" class="btn small">Calculate</button>
  </form>
  <% if ("mod".equals(labType)) { %>
    <% if (labError != null) { %><div class="alert"><%= Html.esc(labError) %></div>
    <% } else { %><div class="out"><%= Html.esc(labResult) %></div><% } %>
  <% } %>
</section>

<section class="card lab">
  <h3>Trigonometry &mdash; <code>calculateSin / Cos / Tan(double degree)</code></h3>
  <form method="get" action="${pageContext.request.contextPath}/lab" class="row">
    <input type="hidden" name="type" value="trig">
    <select name="fn">
      <option value="sin" <%= "sin".equals(fnSel) ? "selected" : "" %>>sin</option>
      <option value="cos" <%= "cos".equals(fnSel) ? "selected" : "" %>>cos</option>
      <option value="tan" <%= "tan".equals(fnSel) ? "selected" : "" %>>tan</option>
    </select>
    <input type="text" name="deg" placeholder="Angle in degrees" required
           value="<%= "trig".equals(labType) ? Html.esc(request.getParameter("deg")) : "" %>">
    <button type="submit" class="btn small">Calculate</button>
  </form>
  <% if ("trig".equals(labType)) { %>
    <% if (labError != null) { %><div class="alert"><%= Html.esc(labError) %></div>
    <% } else { %><div class="out"><%= Html.esc(labResult) %></div><% } %>
  <% } %>
</section>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
