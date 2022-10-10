package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import helper.DaoHelper;
import util.ConnectionUtil;
import vo.Book;
import vo.Category;

public class BookDao {

	private static BookDao instance = new BookDao();

	private BookDao() {
	}

	public static BookDao getInstance() {
		return instance;
	}

	private DaoHelper helper = DaoHelper.getInstance();
	
	public List<Book> getNewBooks(int beginIndex, int endIndex) throws SQLException {
		String sql = "select B.book_no, B.category_no, C.category_name, B.book_title, B.book_author, B.book_discount_price, B.book_price, B.book_created_date "
				   + "from (select book_no, category_no, book_title, book_author, book_discount_price, book_price, book_created_date, "
				   + "             row_number() over (order by book_no desc) row_number " 
				   + "      from hta_books "
				   + "      where book_deleted = 'N') B, hta_book_categories C "
				   + "where B.row_number >= ? and B.row_number <= ? " 
				   + "and B.category_no = C.category_no "
				   + "and to_char(book_created_date, 'YYYYMMDD')>'20220301' "
				   + "order by book_created_date desc ";

		return helper.selectList(sql, rs -> {
			Book book = new Book();
			book.setNo(rs.getInt("book_no"));

			Category category = new Category();
			category.setNo(rs.getInt("category_no"));
			category.setName(rs.getString("category_name"));

			book.setCategoryNo(rs.getInt("category_no"));
			book.setTitle(rs.getString("book_title"));
			book.setAuthor(rs.getString("book_author"));
			book.setPrice(rs.getInt("book_price"));
			book.setDiscountPrice(rs.getInt("book_discount_price"));
			book.setCreatedDate(rs.getDate("book_created_date"));

			return book;
		}, beginIndex, endIndex);
	}
	
	public List<Book> getBooks(int beginIndex, int endIndex) throws SQLException {
		String sql = "select B.book_no, B.category_no, C.category_name, B.book_title, B.book_author, B.book_publisher, B.book_discount_price, B.book_price, B.book_created_date, B.book_stock "
				+ "from (select book_no, category_no, book_title, book_author, book_publisher, book_discount_price, book_price, book_created_date, book_stock, "
				+ "             row_number() over (order by book_no desc) row_number " + "      from hta_books "
				+ "      where book_deleted = 'N') B, hta_book_categories C "
				+ "where B.row_number >= ? and B.row_number <= ? " + "and B.category_no = C.category_no "
				+ "order by B.book_no desc ";

		return helper.selectList(sql, rs -> {
			Book book = new Book();
			book.setNo(rs.getInt("book_no"));

			Category category = new Category();
			category.setNo(rs.getInt("category_no"));
			category.setName(rs.getString("category_name"));
			book.setCategory(category);

			book.setCategoryNo(rs.getInt("category_no"));
			book.setTitle(rs.getString("book_title"));
			book.setAuthor(rs.getString("book_author"));
			book.setPublisher(rs.getString("book_publisher"));
			book.setPrice(rs.getInt("book_price"));
			book.setDiscountPrice(rs.getInt("book_discount_price"));
			book.setCreatedDate(rs.getDate("book_created_date"));
			book.setStock(rs.getInt("book_stock"));
			
			return book;
		}, beginIndex, endIndex);
	}
	
	public List<Book> getBooks(int beginIndex, int endIndex, String keyword) throws SQLException {
		String sql = "select B.book_no, B.category_no, C.category_name, B.book_title, B.book_author, B.book_discount_price, B.book_price, B.book_created_date "
				   + "from (select book_no, category_no, book_title, book_author, book_discount_price, book_price, book_created_date, "
				   + "             row_number() over (order by book_no desc) row_number " 
				   + "      from hta_books "
				   + "      where book_deleted = 'N' and book_title like '%' || ? || '%') B, hta_book_categories C "
				   + "where B.row_number >= ? and B.row_number <= ? " 
				   + "and B.category_no = C.category_no "
				   + "order by B.book_no asc ";

		return helper.selectList(sql, rs -> {
			Book book = new Book();
			book.setNo(rs.getInt("book_no"));

			Category category = new Category();
			category.setNo(rs.getInt("category_no"));
			category.setName(rs.getString("category_name"));

			book.setCategoryNo(rs.getInt("category_no"));
			book.setTitle(rs.getString("book_title"));
			book.setAuthor(rs.getString("book_author"));
			book.setPrice(rs.getInt("book_price"));
			book.setDiscountPrice(rs.getInt("book_discount_price"));
			book.setCreatedDate(rs.getDate("book_created_date"));

			return book;
		}, keyword, beginIndex, endIndex);
	}
	
	public List<Book> getBooksByCategory(int beginIndex, int endIndex, String categoryName) throws SQLException {
		String sql = "select B.book_no, B.category_no, B.book_title, B.book_author, B.book_discount_price, B.book_price, B.book_created_date, C.category_name "
				   + "from (select book_no, category_no, book_title, book_author, book_discount_price, book_price, book_created_date, "
				   + "             row_number() over (order by book_no desc) row_number "
			       + "      from hta_books "
				   + "      where book_deleted = 'N' and category_no = (select category_no "
				   + "                                                  from hta_book_categories "
				   + "                                                  where category_name like '%' || ? || '%')) B, hta_book_categories C "
				   + "where B.row_number >= ? and B.row_number <= ? "
				   + "and B.category_no = C.category_no "
				   + "order by B.book_no asc ";

		return helper.selectList(sql, rs -> {
			Book book = new Book();
			book.setNo(rs.getInt("book_no"));

			Category category = new Category();
			category.setNo(rs.getInt("category_no"));
			category.setName(categoryName);

			book.setCategoryNo(rs.getInt("category_no"));
			book.setTitle(rs.getString("book_title"));
			book.setAuthor(rs.getString("book_author"));
			book.setPrice(rs.getInt("book_price"));
			book.setDiscountPrice(rs.getInt("book_discount_price"));
			book.setCreatedDate(rs.getDate("book_created_date"));

			return book;
		}, categoryName, beginIndex, endIndex);
	}
	
	public List<Book> getBooksOrderByDiscountPrice(int beginIndex, int endIndex) throws SQLException {
		String sql = "select B.book_no, B.category_no, C.category_name, B.book_title, B.book_author, B.book_discount_price, B.book_price, B.book_created_date "
				   + "from (select book_no, category_no, book_title, book_author, book_discount_price, book_price, book_created_date, "
				   + "             row_number() over (order by book_no desc) row_number " 
				   + "      from hta_books "
				   + "      where book_deleted = 'N') B, hta_book_categories C "
				   + "where B.row_number >= ? and B.row_number <= ? " 
				   + "and B.category_no = C.category_no "
				   + "order by B.Book_discount_price asc ";

		return helper.selectList(sql, rs -> {
			Book book = new Book();
			book.setNo(rs.getInt("book_no"));

			Category category = new Category();
			category.setNo(rs.getInt("category_no"));
			category.setName(rs.getString("category_name"));

			book.setCategoryNo(rs.getInt("category_no"));
			book.setTitle(rs.getString("book_title"));
			book.setAuthor(rs.getString("book_author"));
			book.setPrice(rs.getInt("book_price"));
			book.setDiscountPrice(rs.getInt("book_discount_price"));
			book.setCreatedDate(rs.getDate("book_created_date"));

			return book;
		}, beginIndex, endIndex);
	}

	public void insertbook(Book book) throws SQLException {
		String sql = "insert into hta_books "
				+ "(book_no, category_no, book_title, book_author, book_publisher, book_description, book_price, "
				+ "book_discount_price, book_on_sell, book_stock, book_created_date, book_updated_date, book_deleted) "
				+ "values " + "(sample_books_seq.nextval, ?, ?, ?, ?, ?, ?, ?, default, ?, ?, ?, default)";

		helper.insert(sql, book.getCategoryNo(), book.getTitle(), book.getAuthor(), book.getPublisher(),
				book.getDescription(), book.getPrice(), book.getDiscountPrice(), book.getStock(),
				book.getCreatedDate(), book.getUpdatedDate());
	}
	
	public void updateBook(Book book) throws SQLException {
		String sql = "update hta_books "
					+ "set "
					+ "		category_no = ?, "
					+ "		book_title = ?, "
					+ "		book_author = ?, "
					+ "		book_publisher= ?, "
					+ "		book_description = ?, "
					+ "		book_price = ?,	"
					+ "		book_discount_price = ?, "
					+ "		book_on_sell = ?, "
					+ "		book_stock = ?, "
					+ "		book_updated_date = sysdate, "
					+ "		book_deleted = ? "
					+ "where book_no = ? ";
		
		helper.update(sql, book.getCategoryNo(), book.getTitle(), book.getAuthor(), book.getPublisher(), 
				           book.getDescription(), book.getPrice(), book.getDiscountPrice(), book.getOnSell(), book.getStock(), book.getDeleted(), book.getNo());
	}
	
	public Book getBookByNo(int bookNo) throws SQLException {
		String sql = "select book_no, category_no, book_title, book_author, book_publisher, book_description, book_price, "
					+ "book_discount_price, book_on_sell, book_stock, book_created_date, book_updated_date, book_deleted "
					+ "from hta_books "
					+ "where book_no = ? "
					+ "and book_on_sell = 'Y' "
					+ "and book_deleted = 'N' ";
		
		return helper.selectOne(sql, rs -> {
			Book book = new Book();
			book.setNo(bookNo);
			book.setCategoryNo(rs.getInt("category_no"));
			book.setTitle(rs.getString("book_title"));
			book.setAuthor(rs.getString("book_author"));
			book.setPublisher(rs.getString("book_publisher"));
			book.setDescription(rs.getString("book_description"));
			book.setPrice(rs.getInt("book_price"));
			book.setDiscountPrice(rs.getInt("book_discount_price"));
			book.setOnSell(rs.getString("book_on_sell"));
			book.setStock(rs.getInt("book_stock"));
			book.setCreatedDate(rs.getDate("book_created_date"));
			book.setUpdatedDate(rs.getDate("book_updated_date"));
			book.setDeleted(rs.getString("book_deleted"));
			
			return book;
		}, bookNo);
	}

	public int getTotalRows() throws SQLException {
		String sql = "select count(*) cnt " + "from hta_books "
				   + "where book_deleted = 'N'" ;
		
		return helper.selectOne(sql, rs -> {
			return rs.getInt("cnt");
		});
	}
	
	public int getTotalRowsByCategoryName(String categoryName) throws SQLException {
		String sql = "select count(*) cnt " + "from hta_books " 
				   + "where book_deleted = 'N' "
				   + "and category_no = (select category_no "
				   + "					 from hta_book_categories "
				   + "					 where category_name like '%' || ? || '%') ";
		
		return helper.selectOne(sql, rs -> {
			return rs.getInt("cnt");
		}, categoryName);
	}

	public int getTotalRowsByKeyword(String keyword) throws SQLException {
		String sql = "select count(*) cnt " + "from hta_books "
				   + "where book_deleted = 'N' and book_title like '%' || ? || '%' ";

		return helper.selectOne(sql, rs -> {
			return rs.getInt("cnt");
		}, keyword);
	}
	
	/**
	 * 최근 등록한 도서정보 객체 3개를 반환합니다.
	 * @return 최근 도서정보 객체 3개
	 * @throws SQLException
	 */
	public List<Book> getRecentBooks() throws SQLException {
		String sql = "select B.book_no, B.category_no, C.category_name, B.book_title, B.book_author, B.book_publisher, B.book_discount_price, B.book_price, B.book_created_date, B.book_stock "
				   + "from (select book_no, category_no, book_title, book_author, book_publisher, book_discount_price, book_price, book_created_date, book_stock, "
				   + "             row_number() over (order by book_no desc) row_number " + "      from hta_books "
				   + "      where book_deleted = 'N') B, hta_book_categories C "
				   + "where B.row_number >= ? and B.row_number <= ? " + "and B.category_no = C.category_no "
				   + "order by book_no desc ";
		
		List<Book> recentBook = new ArrayList<>();
		
		Connection connection = ConnectionUtil.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, 1);
		pstmt.setInt(2, 3);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			Book book = new Book();
			book.setNo(rs.getInt("book_no"));

			Category category = new Category();
			category.setNo(rs.getInt("category_no"));
			category.setName(rs.getString("category_name"));

			book.setCategoryNo(rs.getInt("category_no"));
			book.setTitle(rs.getString("book_title"));
			book.setAuthor(rs.getString("book_author"));
			book.setPrice(rs.getInt("book_price"));
			book.setDiscountPrice(rs.getInt("book_discount_price"));
			book.setCreatedDate(rs.getDate("book_created_date"));
			book.setStock(rs.getInt("book_stock"));
			
			recentBook.add(book);
		}
		
		rs.close();
		connection.close();
		pstmt.close();
		
		return recentBook;
		
		}
		
}
