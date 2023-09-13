
<h1>频道详情</h1>

<@channel path="/首页/公司介绍" var="data">
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