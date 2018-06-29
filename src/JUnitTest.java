import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;


public class JUnitTest {
	static Bookstore test;
	static ArrayList<BookQ> booksQ;
	static Book[] allBooks;
	
	@BeforeClass
	// loading all the required variables and arrays
	public static void setUpBeforeClass() throws Exception {
		test = new Bookstore();
		booksQ = Bookstore.booksQInStore;
		allBooks = new Book[booksQ.size()];
		for (int i=0; i<booksQ.size(); i++)
			allBooks[i] = booksQ.get(i).getBook();
	}
	
	@Test
	// LIST all the books
	public void list() {

		Book[] results = test.list("");
		System.out.println(results[0].toString());
		System.out.println(allBooks[0].toString());
		String resultsString = convertBooksArrayToString(results);
		String allBooksString = convertBooksArrayToString(allBooks);

		assertEquals(allBooksString, resultsString);
	}
	
	@Test
	// LIST books with keywords 'author'
	public void searchByAuthor() {
		Book[] results = test.list("author");
		Book[] expecteds = new Book[]{allBooks[2], allBooks[3]};
		
		String resultsString = convertBooksArrayToString( results );
		String expected = convertBooksArrayToString (expecteds);
		assertEquals(expected, resultsString);
	}
	
	@Test
	// LIST books with keywords 'title'
	public void searchByTitle() {
		Book[] results = test.list("Random");
		Book[] expecteds = new Book[]{allBooks[4], allBooks[5]};
		
		String resultsString = convertBooksArrayToString( results );
		String expected = convertBooksArrayToString (expecteds);
		assertEquals(expected, resultsString);
	}
	
	@Test
	// ADD a book to basket
	public void addToBasket() {
		
		// Book exists and it's added
		boolean result = test.add(booksQ.get(0), 1);
		assertEquals(true, result);
		
		// Not enough in stock, so it should not add
		result = test.add(booksQ.get(0), 25);
		assertEquals(false, result);
	}
	
	@Test
	// DELETE a book from basket
	public void deleteFromBasket() {
		
		// First adding a few books
		boolean result = test.add(booksQ.get(0), 5);
		assertEquals(true, result);
		
		// and now removing some
		result = test.removeFromBasket(booksQ.get(0), 2);
		assertEquals(true, result);
		
		// and then removing all
		result = test.removeFromBasket(booksQ.get(0), 3);
		assertEquals(true, result);
		
		// try to remove what doesn't exist in the basket and the result should be False
		result = test.removeFromBasket(booksQ.get(0), 1);
		assertEquals(false, result);
	}
	
	
	
	@Test
	// BUY multiple books using buy method
	public void buyMultipleBooks() {
		
		// Add to valid books to basket
		int[] results = test.buy(new Book[]{booksQ.get(0), booksQ.get(1)});
		int[] expected = new int[]{0,0};
		assertArrayEquals(expected, results);
		
		// Add one valid book, one out of stock book 
		int[] results2 = test.buy(new Book[]{booksQ.get(0), booksQ.get(6)});
		int[] expected2 = new int[]{0,1};
		assertArrayEquals(expected2, results2);
		
		// Add one valid book, one out of stock book, and one book that doesn't exist
		int[] results3 = test.buy(new Book[]{booksQ.get(0), booksQ.get(6), 
								  new Book("My golden Book", "Smart ass", new BigDecimal("999.59"))});
		int[] expected3 = new int[]{0,1,2};
		assertArrayEquals(expected3, results3);
		
	}
	
	
	
	
	public String convertBooksArrayToString(Book[] books)
	{
		String results = "";
		for (int i = 0; i < books.length; i++)
			results+= books[i].toString();
		
		return results;
	}
	

	
	

}
