<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>WCMS Template</title>
<style>
	body {font-size:14px;}
	table{border-collapse: collapse;border:1px solid #000}
	th,td {border:1px solid #000;padding:10px;text-align:center}
</style>
</head>
<body>
<h3>
	通过数据字典标签获取字典信息
</h3>

<@DicList key="DIC_FIELD_EXT_SEX" var="dics">
<table>
	<caption>字典列表</caption>
	<tr>
		<th>id</th>
		<th>name</th>
		<th>code</th>
		<th>value</th>
		<th>remark</th>
		<th>ext1</th>
		<th>ext2</th>
		<th>ext3</th>
	</tr>
	<#list dics as dic>
	<tr>
		<td>${dic.dicId!}</td>
		<td>${dic.dicName!}</td>
		<td>${dic.dicCode!}</td>
		<td>${dic.dicValue!}</td>
		<td>${dic.remark!}</td>
		<td>${dic.ext1!}</td>
		<td>${dic.ext2!}</td>
		<td>${dic.ext3!}</td>
	</tr>
	</#list>
</table>
</@DicList>
<@Dic key="DIC_FIELD_EXT_SEX" code="DIC_FIELD_EXT_SEX_2" var="dic">
<table>
	<caption>字典详情</caption>
	<tr>
		<th>id</th>
		<th>name</th>
		<th>code</th>
		<th>value</th>
		<th>remark</th>
		<th>ext1</th>
		<th>ext2</th>
		<th>ext3</th>
	</tr>
	<tr>
		<td>${dic.dicId!}</td>
		<td>${dic.dicName!}</td>
		<td>${dic.dicCode!}</td>
		<td>${dic.dicValue!}</td>
		<td>${dic.remark!}</td>
		<td>${dic.ext1!}</td>
		<td>${dic.ext2!}</td>
		<td>${dic.ext3!}</td>
	</tr>
</table>
</@Dic>
</body>
</html>


