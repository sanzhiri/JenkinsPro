<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<h1>站点标签 Sample</h1>
<@site code="newstrength" var="site">
	<table border="1">
		<tr>
			<th>站点ID</th>
			<th>站点编码</th>
			<th>站点名称</th>
			<th>站点说明</th>
			<th>SEO 标题</th>
			<th>SEO 描述</th>
			<th>SEO 关键字</th>
		</tr>
		<tr>
			<td>${site.id!}</td>
			<td>${site.code!}</td>
			<td>${site.name!}</td>
			<td>${site.remark!}</td>
			<td>${site.seo.title!}</td>
			<td>${site.seo.description!}</td>
			<td>${site.seo.keyword!}</td>
		</tr>
	</table>
</@site>
</body>
</htlm>