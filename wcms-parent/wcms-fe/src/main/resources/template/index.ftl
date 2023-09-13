<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>WCMS Template</title>
<style type="text/css">
table {width:70%}
tr,td,th {font-size:14px;padding:5px;}
select {padding:5px;width:300px;}
</style>
</head>
<body>

<nav name="Top">
	<a href="#SiteList">站点标签</a> |
	<a href="#Site">站点详情标签</a> |
	<a href="#Channel">栏目标签</a> |
	<a href="/info/47">信息详情标签</a> |
	<a href="/info/list/197">信息列表标签</a> |
	<a href="#Dic">数据字典标签</a>
</nav>

<h1>站点标签</h1>

<details id="SiteList">
	<summary>站点列表</summary>
	<p>
		<@SiteList code="xd" var="sites">
			<select>
				<#list sites as site>
				<option value ="${site.id!}">${site.name!}</option>
				</#list>
			</select>
		</@SiteList>
	</p>
</details>

<details id="Site">
	<summary>站点<mark>详情</mark></summary>
	<p>
		<@Site code="xd" var="site">
			<table border="1">
				<tr>
					<th>id</th>
					<th>code</th>
					<th>name</th>
					<th>remark</th>
					<th>directory</th>
				</tr>
				<tr>
					<td>${site.id!}</td>
					<td>${site.code!}</td>
					<td>${site.name!}</td>
					<td>${site.remark!}</td>
					<td>${site.directory!}</td>
				</tr>
			</table>
		</@Site>
	</p>
</details>

<details id="Info">
	<summary>信息<mark>详情</mark></summary>
	<p>
		<@Info infoId="23" var="info">
			<table border="1">
				<tr>
					<td>id</td>
					<td>${info.id!}</td>
				</tr>
				<tr>
					<td>title</td>
					<td>${info.title!}</td>
				</tr>
				<tr>
					<td>subTitle</td>
					<td>${info.subTitle!}</td>
				</tr>
				<tr>
					<td>summary</td>
					<td>${info.summary!}</td>
				</tr>
				<tr>
					<td>author</td>
					<td>${info.author!}</td>
				</tr>
				<tr>
					<td>source</td>
					<td>${info.source!}</td>
				</tr>
				<tr>
					<td>keyword</td>
					<td>${info.keyword!}</td>
				</tr>
				<tr>
					<td>attr</td>
					<td>
						<#list info.attr?keys as key> 
							${key} / ${info.attr[key]}
						</#list>
					</td>
				</tr>
				<tr>
					<td>createTime</td>
					<td>${info.createTime!}</td>
				</tr>
				<tr>
					<td>updateTime</td>
					<td>${info.updateTime!}</td>
				</tr>
				<tr>
					<td>publishedTime</td>
					<td>${info.publishedTime!}</td>
				</tr>
				<tr>
					<td>refFlag</td>
					<td>${info.refFlag!}</td>
				</tr>
				<tr>
					<td>content</td>
					<td>${info.content!}</td>
				</tr>
				<tr>
					<td>staticPath</td>
					<td>${info.staticPath!}</td>
				</tr>
				<tr>
					<td>attachments</td>
					<td>
					<#list info.attachments as attachment>
						${attachment.fileName!}
					</#list>
					</td>
				</tr>
				<tr>
					<td>files</td>
					<td>
					<#list info.files as file>
						${file.fileName!}
					</#list>
					</td>
				</tr>
			</table>
		</@Info>
	</p>
</details>

</body>
</html>
