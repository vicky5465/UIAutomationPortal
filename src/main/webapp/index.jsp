<%--this is the comment of jsp --%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath(); //to get project name
/*
http://localhost:8081/AgentAppWebTest/
*/
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!--this is the comment of html -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>">
</head>
<body>
<h2>Hello World!</h2>
</body>
</html>
