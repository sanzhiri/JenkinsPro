<!DOCTYPE html>
<html>
<head>
<meta charset=" utf-8">
<title>WCMS</title> 
</head>
<body>

<h1>频道首页</h1>
<@channel site="PC" code="Sample" var="channel">
	<table border="1">
		<tr>
			<th>频道ID</th>
			<th>父节点</th>
			<th>名称</th>
		</tr>
		<#list dics as dic>
		<tr>
			<td>${channel.id!}</td>
			<td>${channel.parentId!}</td>
			<td>${channel.name!}</td>
		</tr>
		</#list>
	</table>
</@channel>
</body>
</html>

