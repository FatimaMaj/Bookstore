import java.math.BigDecimal;


public class BookQ extends Book {

	int quantity;
	public BookQ(String title, String author, BigDecimal price, int quantity) {
		super(title, author, price);
		
		this.quantity = quantity;
	}
	
	public BookQ(Book book, int quantity) {
		super(book.getTitle(), book.getAuthor(), book.getPrice());
		
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public boolean equal( Book book ){
		if ( book.getAuthor().equals(getAuthor()) &&
			 book.getPrice().equals(getPrice()) &&
			 book.getTitle().equals(getTitle()) )
			return true;
		else
			return false;
			 
	}
	
	public Book getBook()
	{
		return new Book(getTitle(), getAuthor(), getPrice());
	}
	
	public boolean equal( BookQ bookQ ){
		return equal( new Book(bookQ.getTitle(), bookQ.getAuthor() , bookQ.getPrice()) ) ;
			 
	}
	
	@Override
	public String toString() {
		return "Book [title=" + getTitle() + ", author=" + getAuthor() + ", price=" + getPrice() + ", quantity =" + getQuantity() + "]";
				//"Book_Q [quantity=" + quantity + "]";
	}

	

}
