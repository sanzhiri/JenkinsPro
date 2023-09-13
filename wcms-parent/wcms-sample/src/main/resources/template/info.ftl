<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

<h1>信息详情标签</h1>

<@info id="9" var="result">
	<table border="1">
		<tr>
			<th>信息ID</th>
			<th>信息标题</th>
		</tr>
		<tr>
			<td>${result.id!}</td>
			<td>${result.title!}</td>
		</tr>
	</table>
</@info>

</body>
</htlm>