<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ include file="header.jsp"%>
<%@ include file="../adminPages/links.jsp"%>

<c:if test="${msg!=null }">
	<script>$.messager.alert('提示',"${msg}");</script>
</c:if>
	<!--      主体        -->
	

		
	<div class="search-area">
		<form action="<%=request.getContextPath() %>/topic" method="post">
			<div class="control-group">
				<input type="hidden" name="flag" value="search">
				<input type="text" class="search-field" placeholder="输入帖名" name="topicname" required="required"/>
				<input class="search-button" type="submit" value="搜索" >
			</div>
		</form>
	</div>
	<DIV class="t" >
		
		<!--       风云人物           -->
		<TABLE cellSpacing="0" cellPadding="0" style="width: 14%;float: left;overflow: hidden;" >		
			<TR  >
				<TH class="h" style="WIDTH: 100%; text-align: center;" colSpan="4" ><SPAN>风云人物</SPAN></TH>
			</TR>
<!--       表 头           -->
			<TR class="tr2" align="center" style="height: 50px;">
				<TD style="WIDTH: 5%" align="center">排行</TD>
				<TD style="WIDTH: 20%" align="center">作者</TD>
				<TD style="WIDTH: 10%" align="center">发帖数</TD>
			</TR>
<!--         主 题 列 表        -->
			<%int count=0; %>
			<c:forEach items="${personTop}" var="tp">
				<TR class="tr3" >
				<TD style="text-align: center;"><%=++count %></TD>
				<TD style="FONT-SIZE: 15px" align="center">
					<font><a href="<%=request.getContextPath()%>/topic?flag=personTopTopic&uid=${tp.uid}" >${tp.uname}</a></font>
				</TD>
				
				<td align="center">
				<c:if test="${tp.total==null}">0</c:if>
				<c:if test="${tp.total!=null}">${tp.total}</c:if>
				</td>
			</TR>
			</c:forEach>
			

		</TABLE>
	
	<!--       热帖           -->	
		<TABLE cellSpacing="0" cellPadding="0" style="width: 21%;float: right;overflow: hidden;table-layout: fixed;" >		
			<TR>
				<TH class="h" style="WIDTH: 100%;text-align: center;" colSpan="5"><SPAN>热帖排行</SPAN></TH>
			</TR>
<!--       表 头           -->
			<TR class="tr2" style="height: 50px;">
				<TD style="WIDTH: 5%" align="center">排行</TD>
				<TD style="WIDTH: 15%;" align="center">文章</TD>
				<TD style="WIDTH: 10%" align="center">板块</TD>
				<TD style="WIDTH: 10%" align="center">作者</TD>
				<TD style="WIDTH: 5%" align="center">回复</TD>
			</TR>
<!--         主 题 列 表        -->
			<%int count2=0; %>
			<c:forEach items="${pagebean}" var="tp">
				<TR class="tr3" style="overflow: hidden;">
				<TD style="text-align: center;"><%=++count2 %></TD>
				<TD style="FONT-SIZE: 15px;overflow: hidden;width: 30px;" >
					<A href="<%=request.getContextPath()%>/topic?flag=topicDetail&boardid=${tp.boardid }&topicid=${tp.topicid}&replyPages=1" >${tp.title}</A>
				</TD>
				<td align="center" style="overflow: hidden;">${tp.boardname}</td>
				<TD align="center">${tp.uname}</TD>
				<TD align="center">${tp.replycount}</TD>
			</TR>
			</c:forEach>
			

		</TABLE>
	<!--       主题           -->
		<TABLE cellSpacing="0" cellPadding="0" style="width: 65%;border-left: 1px solid #a6cbe7;border-right:1px solid #a6cbe7;margin-right: 0px; ">
			<TR class="tr2" align="center">
				<TD colSpan="2">论坛</TD>
				
				<TD style="WIDTH: 10%;">操作</TD>
				<TD style="WIDTH: 10%;">主题</TD>
				<TD style="WIDTH: 30%">最后发表</TD>
			</TR>
			<!--       主版块       -->

			<%--  循环整个Map<Integer, List<Board>> --%>
			<c:forEach items="${boardMap}" var="allBoards">

				<c:if test="${allBoards.key==0}">
				
					<%--  循环整4个Map中的List<Board> 取出大板块名--%>
					<c:forEach items="${allBoards.value}" var="boards">
						<TR class="tr3">
							<TD colspan="5">${boards.boardname}</TD>
						</TR>
						
						<%--  重新循环整个Map<Integer, List<Board>> 判断找出key值与大板块的boardid相同的list集合--%>
						<c:forEach items="${boardMap}" var="allBoards2">
							<c:if test="${allBoards2.key==boards.boardid}">
								
								<%--  循环找到对应key值的的List<Board>> --%>
								<c:forEach items="${allBoards2.value}" var="sonBoard">
								
									<c:if test="${ sonBoard!=null }">
									<!--       子版块       -->
									<TR class="tr3">
										<TD width="5%">&nbsp;</TD>
										<TH align="left"><IMG
											src="<%=request.getContextPath()%>/image/board.gif"> <A
											href="topic?pages=1&flag=topicList&boardid=${sonBoard.boardid }">${sonBoard.boardname}</A>
										</TH>
										<td align="center"> 
											<a href="topic?flag=topicHostList&boardid=${sonBoard.boardid }">查看板块热帖</a>
										</td>

										<TD align="center">${sonBoard.topicsum}</TD>
										<TH><SPAN> <A href="<%=request.getContextPath()%>/topic?flag=topicDetail&topicid=${sonBoard.recenttopicid }&boardid=${sonBoard.boardid}">${sonBoard.recenttopictitle}
											</A>
										</SPAN> <BR /> <SPAN>${sonBoard.recenttopicusername}</SPAN> 
										
										<c:if test="${sonBoard.recenttopicmodifytime!=null }">
											<SPAN class="gray">[ ${sonBoard.recenttopicmodifytime} ]</SPAN>
										</c:if>
										<c:if test="${sonBoard.recenttopicmodifytime==null }">
											<SPAN class="gray">暂无新帖发出</SPAN>
										</c:if>
										
										
										</TH>
									</TR>
									</c:if>

									
								</c:forEach>
	
							</c:if>

						</c:forEach>


					</c:forEach>
				</c:if>

			</c:forEach>

		</TABLE>
		
		
	
		
	</DIV>

 <%@ include file="bottom.jsp"%>