<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="util.MultipartRequest"%>
<%@page import="vo.Book"%>
<%@page import="dao.BookDao"%>
<%@page import="util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="../error/500.jsp" %>
    
<%
	// multipart/form-data 요청을 처리하는 MultipartRequest 객체 생성하기
	/* MultipartRequest mr = new MultipartRequest(request, "C:\\eclipse\\workspace-web\\atteched-file"); */

	// 요청파라미터값을 조회한다.
	int bookNo = StringUtil.stringToInt(request.getParameter("bookNo"));
	int currentPage = StringUtil.stringToInt(request.getParameter("page"), 1);
	// 도서 번호에 해당하는 도서 정보를 조회한다.
 	Book book = BookDao.getInstance().getBookByNo(bookNo);
	// 도서 정보가 없으면 에러메시지 출력
	if(book == null) {
		throw new RuntimeException("도서 정보가 존재하지 않습니다.");
	} 
	
	// 요청파라미터에서 수정된 정보를 조회한다.
	
	/* 파일 전송이 없기 때문에 MultipartRequest를 쓰지 않는다.
	int categoryNo = StringUtil.stringToInt(mr.getParameter("categoryNo"));
	String title = mr.getParameter("title");
	String author = mr.getParameter("author");
	String publisher = mr.getParameter("publisher");
	int price = StringUtil.stringToInt(mr.getParameter("price"));
	int discountPrice = StringUtil.stringToInt(mr.getParameter("discount-price"));
	int stock = StringUtil.stringToInt(mr.getParameter("stock"));
	String discription = mr.getParameter("discription");
	String createdDate = mr.getParameter("createdDate"); */
	
	
	int categoryNo = StringUtil.stringToInt(request.getParameter("categoryNo"));
	String title = request.getParameter("title");
	String author = request.getParameter("author");
	String publisher = request.getParameter("publisher");
	int price = StringUtil.stringToInt(request.getParameter("price"));
	int discountPrice = StringUtil.stringToInt(request.getParameter("discount-price"));
	int stock = StringUtil.stringToInt(request.getParameter("stock"));
	String discription = request.getParameter("discription");
	String createdDate = request.getParameter("createdDate");
	
	// String 타입의 출간일 Date로 변환하기
	DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
	Date alt_createdDate = date.parse(createdDate);
	
	// book객체를 새로 생성해서 도서 정보를 저장한다.
	book.setCategoryNo(categoryNo);
	book.setTitle(title);
	book.setAuthor(author);
	book.setPublisher(publisher);
	book.setPrice(price);
	book.setDiscountPrice(discountPrice);
	book.setStock(stock);
	book.setDescription(discription);
	book.setCreatedDate(alt_createdDate);
	
	// 도서 정보를 데이터베이스에 갱신시킨다.
	BookDao.getInstance().updateBook(book);
	
	// 재요청 URL을 응답으로 보낸다.
	response.sendRedirect("booklist.jsp");
	
%>
