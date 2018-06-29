
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
//import java.text.NumberFormat;

public class Bookstore implements BookList
{
	// Using this formatter for prices
	DecimalFormat formatter = new DecimalFormat("#,###.00");
	
	
	static ArrayList<BookQ> booksQInStore = new ArrayList<BookQ>(); // stores all books in this array list
	static ArrayList<BookQ> basket = new ArrayList<BookQ>(); // stores items added by user
	static String line = null; // to read from the input file 

	// constructor which is used to load the input file (bookstoredata.txt)
	public Bookstore(){
		String fileName = "bookstoredata.txt";

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	
            	String[] fields = line.split(";"); // get each item in a row individually
            	
            	// Convert the price into BigDeciaml and also parse the quantity into integer
            	BookQ bookQ = new BookQ( fields[0], fields[1], new BigDecimal(fields[2].replace(",", "")), Integer.parseInt(fields[3]) );
            	booksQInStore.add(bookQ);
            }   

            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println( "Error reading file '" + fileName + "'");                  
        }
	}
	
	// get user input and interact with them on the command line
	public static void main(String[] args)
	{
		Bookstore bookstore = new Bookstore();
		
        Scanner input = new Scanner(System.in);
        boolean done = false;
        
        String commands = "\nPlease type one of the following commands:\n\n" +
				"\t[L]: To list all the books or [$ L author] or [$ L title] \n" +
				"\t[B]: Buy a book [$ B ID (quantity)] \n" +
				"\t[K]: Show basket  \n" +
				"\t[D]: Delete a book from Basket [$ D BookID in the basket (quantity)] \n" +
				"\t[C]: Show commands \n" + 
				"\t[X]: Exit \n";
        
        System.out.println( commands );
        
        while ( !done )
        {
	        System.out.print("\n$ ");
	        line = input.nextLine();
	        
	        String[] c = line.split(" "); // collect the command
	        
	        if ( c.length == 1 ) { // commands with ONE arguments
	        	switch ( c[0].toLowerCase() ) {
	        		case "l": 
	        			bookstore.list("");
	        			break;
	        		case "k":
	        			bookstore.showBasket();
	        			break;
	        		case "c":
	        			System.out.println(commands);
	        			break;
	        		case "x":
	        			System.out.println("Thank you and bye!");
	        			done = true;
	        			System.exit(0);
	        		default: 
	        			System.out.println("Invalid command");
	        			break;
	        	}
	        }
	        else if (c.length == 2 ) { // commands with TWO arguments
	        	switch ( c[0].toLowerCase() ) {
	    		case "l": 
	    			bookstore.list(c[1].trim());
	    			break;
	    		case "b": 
	    			int bookID = Integer.parseInt(c[1].trim());
	    			if (bookID < booksQInStore.size())
	    				bookstore.add(booksQInStore.get(bookID), 1);
	    			else
	    				System.out.println("DOES_NOT_EXIST(2)");
	    			
	    			break;
	    		case "d": 
	    			bookstore.removeFromBasket(basket.get( Integer.parseInt(c[1].trim()) ).getBook(), 1);
	    			break;
	    		default: 
	    			System.out.println("Invalid command");
	    			break;
	        	}
	        }
	        else if (c.length == 3 ) { // commands with THREE arguments
	        	switch ( c[0].toLowerCase() ) {
	        	case "l": 
	    			bookstore.list(c[1].trim());
	    			break;
	    		case "b": 
	    			
	    			int bookID = Integer.parseInt(c[1].trim());
	    			int quantity = Integer.parseInt(c[2].trim());
	    			if (bookID < booksQInStore.size() && quantity > 0)
	    				bookstore.add(booksQInStore.get(bookID), quantity);
	    			else
	    				System.out.println("DOES_NOT_EXIST(2)");
	    			
	    			break;
	    		case "d": 
	    			bookstore.removeFromBasket(basket.get( Integer.parseInt(c[1].trim()) ), Integer.parseInt(c[2].trim()));
	    			break;
	    		default: 
	    			System.out.println("Invalid command");
	    			break;
	        	}
	        }
	        else if (c.length > 3 ) { 
	        	
	        	switch ( c[0].toLowerCase() ) {
	        	case "l": 
	    			bookstore.list(c[1].trim());
	    			break;
	        	default: 
	    			System.out.println("Invalid command");
	    			break;
	        	}
	        }
        }
        input.close();
        
        System.exit(0);
        

	}
	
	
	@Override
	public Book[] list(String searchString) {
	
		ArrayList<Book> books = new ArrayList<Book>(); // Store books here and then add to Book array
		System.out.println("\nList of books: \n");
		
		// List all the books and highlight the ones out of stock
		if ( searchString.trim().length() == 0 ){
			
			for ( int i = 0; i < booksQInStore.size(); i++ ) { 
				
				BookQ bookQ = booksQInStore.get(i);
				books.add(bookQ.getBook());
				
				System.out.println( "["+i+"]" + ": " + bookQ.getTitle() + ", " + 
									bookQ.getAuthor() + ", " + formatter.format(bookQ.getPrice()) + " kr \tStock: " + bookQ.getQuantity());
			}
		}
		else { // Go through all the books and check
			for ( int i = 0; i < booksQInStore.size(); i++ ) {
				BookQ bookQ = booksQInStore.get(i);
				boolean found = false;
				
				if ( bookQ.getTitle().toLowerCase().contains(searchString.toLowerCase()) ||
					 bookQ.getAuthor().toLowerCase().contains(searchString.toLowerCase())	) {
					found = true;
				}
				if (found){
					books.add(bookQ.getBook());
					
					System.out.println( "["+i+"]" + ": " + bookQ.getTitle() + ", " + 
									bookQ.getAuthor() + ", " + formatter.format(bookQ.getPrice()) + " kr \tStock: " + bookQ.getQuantity());
				}
				
			}
		}
		
		// create an empty array wit the rigth size to store the books
		Book[] returnedBooks = new Book[books.size()];
		for (int i = 0; i < books.size(); i++)
			returnedBooks[i] = books.get(i); 
		
		return returnedBooks;
	}
	
	// Display what the user has added to the basket
	public void showBasket(){
		
		double sum = 0;
		String itemsAdded = "";
		
		for ( int i = 0; i < basket.size(); i++ ) { 
			
			BookQ bookQ = basket.get(i);
			sum+= bookQ.getPrice().doubleValue(); // calculate the price of each book
			
			itemsAdded += "["+i+"]" + ": " + bookQ.getTitle() + ", " + 
								bookQ.getAuthor() + ", " + formatter.format(bookQ.getPrice()) + " kr, \tQuantity: " + bookQ.getQuantity() + "\n";
		}
		
		if (basket.size() == 0)
		{
			System.out.println("Your basket is empty!");
		}
		else {
			// to show the total price and using the Swedish Locale
			// Locale swedishLocale = new Locale("sv", "SE");
	        // NumberFormat format = NumberFormat.getCurrencyInstance(swedishLocale); 
	        // String currency = format.format(sum);

	        //At the end used the one below as the number were showing as 1 003 534 534,343.87
	        
			System.out.println("\nList of books in your basket: \n\n" + itemsAdded + "\nTotal price: " + formatter.format(sum) + " kr");
		}
		
		
	}

	// ADD an item to basket
	// the commented part was for adding a book to the list of bookstore
	@Override
	public boolean add(Book book, int quantity) {
		
			// check if the book can be found and also if it's in stock
			boolean found = false;
			boolean inStock = false; 
			for ( int j = 0; j < booksQInStore.size(); j++ ){
				BookQ bookQ = booksQInStore.get(j);
				
				// If found go inside this if statement
				if ( bookQ.equal(book) ){
					found = true;
					if ( bookQ.getQuantity() >= quantity ){
						inStock = true;
						addToBasket(bookQ, quantity); // if in stock, add to basket
						
						// reducing the stock value
						bookQ.setQuantity(bookQ.getQuantity() - quantity);
						booksQInStore.set(j, bookQ);
					}
				}
			}
			
			// message to user 
			if ( !found ) {
				System.out.println( "DOES_NOT_EXIST(2)" );
				return false; 
			}
			else if ( !inStock ) {
				System.out.println( "NOT_IN_STOCK(1)" );
				return false; 
			}
			else {
				System.out.println("Book/s added to basket!");
				return true; // OK(0)
			}

	
		
//		// for adding to the bookstore
//		for ( int i = 0; i < quantity; i++ ) {
//			BookQ bookQ = new BookQ(book, quantity);
//			//basket.add(bookQ);
//			booksQInStore.add(bookQ);
//			System.out.println( "Added: " + book.toString() );
//		}
		
	}

	@Override
	public int[] buy(Book[] books) {

		int[] status = new int[books.length]; 
		// Go through each book to be bought and add if it can be found and not out of stock
		for ( int i = 0; i < books.length; i++ ){

			boolean found = false;
			boolean inStock = false; 
			for ( int j = 0; j < booksQInStore.size(); j++ ){
				BookQ bookQ = booksQInStore.get(j);
				if ( bookQ.equal(books[i]) ){
					found = true;
					if ( bookQ.getQuantity() > 0 ){
						inStock = true;
						addToBasket(bookQ, 1);
					}
				}
			}
			
			// message to user for each book
			if ( !found )
				status[i] = 2; // DOES_NOT_EXIST(2)
			else if ( !inStock )
				status[i] = 1; // NOT_IN_STOCK(1)
			else
				status[i] = 0; // OK(0)
		}
		
		return status;
	}
	
	public boolean removeFromBasket(Book book, int quantity){
		boolean found = false; // check if the item exists in the basket, 
		// in which case, reduce the quantity by one (if the quantity > int quantity) or delete the item (if quantity = 1)
		// and then add the quantity to the bookstore
		
		for ( int i = 0; i < basket.size(); i++ ){
			
			BookQ inBasket = basket.get(i);
			if ( inBasket.equal(book) ){
				found = true;
				int diff = inBasket.getQuantity() - quantity;
				
				
				if (diff < 1){ // this means all the copies of the book should be removed from basket
					basket.remove(i);
					
					// find the item in the store to update the stock there
					for ( int j = 0; j < booksQInStore.size(); j++ ){
						BookQ bookQ = booksQInStore.get(j);
						if ( bookQ.equal(inBasket) )
						{
							bookQ.setQuantity( bookQ.getQuantity() + 1);
							booksQInStore.set(j, bookQ);
							System.out.println("Item removed!");
							break;
						}
					}
						
				}
				else { // removing some but keeping some copies of the book in the basket 
					inBasket.setQuantity( diff );
					basket.set(i, inBasket);
					
					for ( int j = 0; j < booksQInStore.size(); j++ ){
						BookQ bookQ = booksQInStore.get(j);
						if ( bookQ.equal(inBasket) )
						{
							bookQ.setQuantity( bookQ.getQuantity() + quantity);
							booksQInStore.set(j, bookQ);
							System.out.println( "Items removed!" );
							break;
						}
					}
				}
			}
		}
		
		if ( !found ) {
			System.out.println("Book not found!");
			return false;
		}
		
		return true;
	}
	
	
	public void addToBasket(Book book, int quantity){
		
		boolean found = false; // check if the item exists, in which case, add to the quantity
		for ( int i = 0; i < basket.size(); i++ ){
			
			BookQ inBasket = basket.get(i);
			if ( inBasket.equal(book) ){
				found = true;
				inBasket.setQuantity( inBasket.getQuantity()+quantity );
				basket.set(i, inBasket);
			}
		}
		
		if ( !found )
			basket.add(new BookQ(book,quantity));
		
	}
	
	public void addToBasket(Book book){
		addToBasket(book,1);
	}
	
	
	
}
