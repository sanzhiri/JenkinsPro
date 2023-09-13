<!DOCTYPE html>
<html>
<head>
<meta charset=" utf-8">
<title>WCMS</title> 
</head>
<body>
<h1>字典列表</h1>
<@dictionary key="SYS_PARAM" var="dics">
	<table border="1">
		<tr>
			<th>字典ID</th>
			<th>字典名称</th>
			<th>字典编码</th>
			<th>字典值</th>
			<th>字典备注</th>
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
</@dictionary>

<h1>字典具体值</h1>
<@dictionary key="SYS_PARAM" code="JWT_EXPIRE_MINUTE" var="dic">
	<table border="1">
		<tr>
			<th>字典ID</th>
			<th>字典名称</th>
			<th>字典编码</th>
			<th>字典值</th>
			<th>字典备注</th>
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
</@dictionary>

</body>
</html>

