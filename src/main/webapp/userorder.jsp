<%@page import="vo.OrderItem"%>
<%@page import="dao.OrderItemDao"%>
<%@page import="vo.Order"%>
<%@page import="dao.OrderDao"%>
<%@page import="java.util.List"%>
<%@page import="vo.Pagination"%>
<%@page import="vo.User"%>
<%@page import="dao.UserDao"%>
<%@page import="util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="../error/500.jsp"%>


<!-- 관리자만 접속할 수 있게 합니다. -->
<%
	//세션에 저장된 사용자정보를 조회한다.
	User logineduser = (User) session.getAttribute("LOGINED_USER");
	if (logineduser == null) {
		throw new RuntimeException("관리자 홈페이지에 접속하실 수 없습니다.");
	}
	if (!"admin@gmail.com".equals(logineduser.getEmail())) {
		throw new RuntimeException("관리자 홈페이지에 접속하실 수 없습니다.");
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>관리자페이지</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="shortcut icon"
	href="https://res.kurly.com/images/marketkurly/logo/favicon_v2.png"
	type="image/x-icon">
<link rel="stylesheet" href="../css/board.css">
<style>
#button {
	font-family: noto sans, malgun gothic, AppleGothic, dotum;
	line-height: 1;
	letter-spacing: -.05em;
	font-size: 12px;
	max-width: 100%;
}
</style>
</head>

<body class="board-list">
	<!-- header -->
	<div id="header">
		<%
		String menu = request.getParameter("menu");
		%>
		<nav class="navbar navbar-expand-lg bg-light  ">
			<div class="container">
				<a class="navbar-brand" href="../home.jsp"><img alt="마켓북스 로고"
					src="../images/marketbooks-logo.png"
					style="width: 40%; justify-content: center;"></a>
				<button class="navbar-toggler" type="button"
					data-bs-toggle="collapse" data-bs-target="#navbarNav"
					aria-controls="navbarNav" aria-expanded="false"
					aria-label="Toggle navigation">
					<span class="navbar-toggler-icon"></span>
				</button>
				<span class="navbar-text" style="font-size: 15px"><strong>관리자님
						환영합니다.</strong></span>
				<div class="">
					<div class="collapse navbar-collapse " id="navbarNav">
						<ul class="navbar-nav me-auto mb-2 mb-lg-0">
							<li class="nav-item"><a
								class="nav-link <%="admin".equals(menu) ? "active" : ""%>"
								aria-current="page" href="/marketbooks/admin/home.jsp">관리자홈</a>
							</li>
							<li class="nav-item"><a class="nav-link" aria-current="page"
								href="../logout.jsp">로그아웃</a></li>
						</ul>
					</div>
				</div>
			</div>
		</nav>
	</div>

	<div id="wrap">
		<div class="container">
			<div id="main">
				<div id="content">

					<div class="page_aticle">
						<div id="snb" class="snb_cc">
							<h2 class="tit_snb">관리자메뉴</h2>
							<div class="inner_snb">
								<ul class="list_menu">
									<li><a href="userlist.jsp">회원관리</a></li>
									<li><a href="booklist.jsp">도서관리</a></li>
									<li><a href="inquirylist.jsp">문의관리</a></li>
									<li><a href="orderlist.jsp">주문관리</a></li>
								</ul>
							</div>
						</div>
					
						<div class="page_section">
							<div class="head_aticle">
								<h2 class="tit">
									주문상세내역<span class="tit_sub">주문상세내역을 확인해보세요.</span>
								</h2>
							</div>
	
							<%
							// 요청 URL : http://localhost/marketbooks/admin/userorder.jsp?no=120003
							// 요청파라미터에서 게시글 번호를 조회한다.
							int orderNo = StringUtil.stringToInt(request.getParameter("no"));
	
							UserDao userDao = UserDao.getInstance();
							OrderDao orderDao = OrderDao.getInstance();
							OrderItemDao orderItemDao = OrderItemDao.getInstance();
	
							int totalRows = orderDao.getTotalRows();
	
							// orderNo로 order정보와 user정보 조회
							Order order = orderDao.getOrderByNo(orderNo);
	
							if (order == null) {
								throw new RuntimeException("주문정보가 존재하지 않습니다.");
							}
							%>
							<div style="height: 20px"></div>
	
							<table width="100%" align="center" cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<td>
											<div>
	
												<h2>[ <%=order.getUser().getName() %> ] 님의 주문상세내역</h2>
												<div style="height:50px"></div>
												<div class="xans-element- xans-myshop xans-myshop-couponserial ">
													<table width="100%" class="xans-board-listheader" cellpadding="0" cellspacing="0">
														<thead>
															<tr>
																<th>도서번호</th>
																<th>카테고리</th>
																<th>도서명</th>
																<th>수량</th>
																<th>가격</th>
															</tr>
														</thead>
														<tbody class="table-group-divider">
															<%
																List<OrderItem> orderList = orderItemDao.getOrderItemsByOrderNo(orderNo);
																for (OrderItem item : orderList) {
															%>
															<tr>
																<td><%=item.getBook().getNo() %></td>
																<td><%=item.getBook().getCategory().getName() %></td>
																<td><a href="../book/detail.jsp?bookNo=<%=item.getBook().getNo() %>"><%=item.getBook().getTitle() %></a></td>
																<td><%=item.getQuantity() %></td>
																<td><%=item.getPrice() %></td>
															</tr>
															<%
																}
															%>
														</tbody>
														
														<tfoot>
															<tr style="color:purple; font-size: 14px">
																<td><strong><%=order.getStatus() %></strong></td>
																<td><small>결제수단</small></td>
																<td><strong><%=order.getPayMethod() %></strong></td>
																<td><small>결제금액</small></td>
																<td><strong><%=order.getTotalPayPrice() %></strong></td>
															</tr>
														</tfoot>
													</table>
												</div>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
							
							
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript">
	
</script>
</body>
</html>
