<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>WCMS Template</title>
<style type="text/css">
table {width:70%}
tr,td,th {font-size:14px;padding:5px;}
select {padding:5px;width:300px;}
thead tr {background:#f6f6f6}
tfoot tr {background:#f6f6f6}
</style>
</head>
<body>

<nav name="Top">
	<a href="/xd">返回上级</a>
</nav>

<@InfoList var="result">
	<table border="1">
		<caption>信息列表标签Sample</caption>
		<thead>
			<tr>
				<td>信息Id</td>
				<td>信息标题</td>
				<td>发布日期</td>
			</tr>
		</thead>
		<tbody>
			<#list result.data as info>
			<tr>
				<td>${info.id!}</td>
				<td><a href="/info/${info.id!}">${info.title!}</a></td>
				<td>${info.publishedTime!}</td>
			</tr>
			</#list>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="3" align="center">
				${Paging(result)}
				
				每页显示：<mark>${result.pageSize!} </mark> 
				当前页：<mark>${result.currentPage!} </mark> 
				总记录数：<mark>${result.rowCount!} </mark> 
				总页数：<mark>${result.pageCount!}</mark> 
				</td>
			</tr>
		<tfoot>
	</table>
</@InfoList>

</body>
</html>
