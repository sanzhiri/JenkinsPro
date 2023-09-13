<!DOCTYPE html> 
<html lang="en"> 
<head> 
	<title>SpringBoot + Freemarker</title> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
</head> 
<body> 
	<h1>Hello world,</h1>
	<p>当前时间：${.now?string("yyyy-MM-dd HH:mm:ss.sss")}</p>
	<h1 style="color: red">${message}</h1>
	
<h1>频道详情</h1>

<@channel id="10" var="result">
	<#assign data=result.data>
		<table>
			<tr>
				<td>ID：</td>
				<td>${data.id}</td>
			</tr>
			<tr>
				<td>名称：</td>
				<td>${data.name}</td>
			</tr>
			<tr>
				<td>摘要：</td>
				<td>${data.summary}</td>
			</tr>
			<tr>
				<td>状态：</td>
				<td>${data.status!}</td>
			</tr>
		</table>	
</@channel>	
</body> 
</html>