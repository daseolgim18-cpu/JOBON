<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<section class="page-heading">
  <div>
    <h1>${pageTitle}</h1>
    <p>${pageDescription}</p>
  </div>
  <c:if test="${not empty pageActionUrl}">
    <a class="jobon-btn jobon-btn--primary" href="${ctx}${pageActionUrl}">${pageActionLabel}</a>
  </c:if>
</section>
