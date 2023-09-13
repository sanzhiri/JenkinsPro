<%@ page contentType="text/html;charset=GB2312" pageEncoding="GB2312" session="true"%>
<%request.setCharacterEncoding("GB2312");%>
<jsp:useBean id="eWebEditor" class="ewebeditor.admin.upload_jsp" scope="page"/>
<%
eWebEditor.Load(pageContext);
%>
