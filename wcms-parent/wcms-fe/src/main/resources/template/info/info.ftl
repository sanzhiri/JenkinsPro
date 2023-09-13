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
    <link rel="stylesheet" href="/static/css/newstrength.css" />
    <link rel="stylesheet" href="/static/css/style.css"/>
    <script src="/static/js/jquery.min.js"></script>
    <script src="/static/js/dropdown-menu.js"></script>
</head>
<body>

<nav name="Top">
	<a href="../sample">返回上级</a>
</nav>

<@Info var="info">
	<table border="1">
		<caption>信息详情标签Sample</caption>
		<thead>
			<tr>
				<td>属性字段</td>
				<td>属性字段说明</td>
				<td>数据值</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>id</td>
				<td>信息id</td>
				<td>${info.id!}</td>
			</tr>
			<tr>
				<td>title</td>
				<td>信息标题</td>
				<td>${info.title!}</td>
			</tr>
			<tr>
				<td>subTitle</td>
				<td>信息副标题</td>
				<td>${info.subTitle!}</td>
			</tr>
			<tr>
				<td>summary</td>
				<td>信息摘要</td>
				<td>${info.summary!}</td>
			</tr>
			<tr>
				<td>author</td>
				<td>信息作者</td>
				<td>${info.author!}</td>
			</tr>
			<tr>
				<td>source</td>
				<td>信息来源</td>
				<td>${info.source!}</td>
			</tr>
			<tr>
				<td>keyword</td>
				<td>信息关键字</td>
				<td>${info.keyword!}</td>
			</tr>
			<#if info.attr?exists>
			<tr>
				<td>attr</td>
				<td>信息扩展字段</td>
				<td>
					<#list info.attr?keys as key> 
						${key} / ${info.attr[key]!}
					</#list>
				</td>
			</tr>
			</#if>
			<tr>
				<td>createTime</td>
				<td>信息创建时间</td>
				<td>${info.createTime!}</td>
			</tr>
			<tr>
				<td>updateTime</td>
				<td>信息更新时间</td>
				<td>${info.updateTime!}</td>
			</tr>
			<tr>
				<td>publishedTime</td>
				<td>信息发布时间</td>
				<td>${info.publishedTime!}</td>
			</tr>
			<tr>
				<td>refFlag</td>
				<td>是否引用信息</td>
				<td>${info.refFlag!}</td>
			</tr>
			<tr>
				<td>content</td>
				<td>信息正文</td>
				<td>${info.content!}</td>
			</tr>
			<tr>
				<td>staticPath</td>
				<td>信息静态地址</td>
				<td>${info.staticPath!}</td>
			</tr>
			<tr>
				<td>files</td>
				<td>信息相关附件</td>
				<td>
				<#list info.files as file>
					${file.fileName!}
				</#list>
				</td>
			</tr>
			<tr>
				<td>images</td>
				<td>信息相关图片</td>
				<td>
				<#list info.images as image>
					${image.fileName!}
				</#list>
				</td>
			</tr>
		</tbody>
	</table>
</@Info>

</body>
</html>
