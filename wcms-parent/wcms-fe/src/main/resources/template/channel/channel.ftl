<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>WCMS Template</title>
    <style type="text/css">
        table {
            width: 70%
        }

        tr, td, th {
            font-size: 14px;
            padding: 5px;
        }

        select {
            padding: 5px;
            width: 300px;
        }
    </style>
</head>
    <body>
        <table border="1">
            <thead>
                <tr>
                    <td>栏目名称</td>
                    <td>首页链接</td>
                    <td>列表页链接</td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <@Channel code="." var="channel">
                    <td>${channel.name!}</td>
                    <td>${channel.homeUrl!}</td>
                    <td>${channel.listUrl!}</td>
                    </@Channel>
                </tr>
            </tbody>
        </table>
    </body>
</html>