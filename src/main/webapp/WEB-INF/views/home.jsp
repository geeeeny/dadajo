<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<!-- Popper JS -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<c:set value="${pageContext.request.contextPath}/" var="root"
	scope="request" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.js">
	
</script>	
<!-- jQuery -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="<c:url value="/resources/js/monitoring.js"/>"></script>

<head>
<script>
	$(function() {
		$('#monitoring').monitoring();
	});
</script>



</head>
<title>Home</title>

<body>
	<div class="container">
		<h1>Gateway</h1>
		<hr>
		<div id="monitoring"></div>
	</div>
</body>