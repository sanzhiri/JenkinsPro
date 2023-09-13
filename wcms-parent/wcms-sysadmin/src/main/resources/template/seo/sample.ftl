<!DOCTYPE html>
<html>
<head>
<meta charset=" utf-8">
<title>WCMS</title> 
</head>
<body>

<h1>获取信息SEO设置</h1>
<@seo type="SEO_INFO" key="2" var="data">
	<table border="1">
		<tr>
			<th>标题</th>
			<th>关键字</th>
			<th>描述</th>
		</tr>
		<tr>
			<td>${data.title!}</td>
			<td>${data.keyword!}</td>
			<td>${data.description!}</td>
		</tr>
	</table>
</@seo>

<h1>获取站点SEO设置</h1>
<@seo type="SEO_SITE" key="2" var="data">
	<table border="1">
		<tr>
			<th>标题</th>
			<th>关键字</th>
			<th>描述</th>
		</tr>
		<tr>
			<td>${data.title!}</td>
			<td>${data.keyword!}</td>
			<td>${data.description!}</td>
		</tr>
	</table>
</@seo>

<h1>获取栏目SEO设置</h1>
<@seo type="SEO_CATEGORY" key="2" var="data">
	<table border="1">
		<tr>
			<th>标题</th>
			<th>关键字</th>
			<th>描述</th>
		</tr>
		<tr>
			<td>${data.title!}</td>
			<td>${data.keyword!}</td>
			<td>${data.description!}</td>
		</tr>
	</table>
</@seo>

</body>
</html>

