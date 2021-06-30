package com.sessions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LibraryTest {

    Library library;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("At BeforeAll");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("At AfterAll");
    }

    @BeforeEach
    public void setup() {
        library = new Library();
        System.out.println("At BeforeEach");
    }

    @AfterEach
    public void AfterEach() {
        System.out.println("At AfterEach");
    }

    @Test
    public void the_default_number_ofBooks_should_be_one() {

        Library library = new Library();

        assertEquals(1, library.getBooks().size());
        assertEquals(library.getBooks().get(0).getName(), "The God Of Small Things");
    }

    @Test
    public void adding_the_catalog_should_increase_the_size_of_book_and_newly_created_books_Id_should_be_2() {

        Library library = new Library();
        Book newlyCreatedBook = library.addToCatalogue("1984", "George Orwell", 32, 2.0);

        int totalBooks = library.getBooks().size();
        List<Book> availableBooks = library.getBooks();

        assertEquals(2, newlyCreatedBook.getId());
        assertThat(totalBooks, equalTo(2));
        assertThat(availableBooks, hasItem(newlyCreatedBook));

    }

    @Test
    public void findBookByName_called_with_bookName_available_in_library_should_return_book_object() {
        Library library = new Library();
        Book book = library.findBookByName("The God Of Small Things");
        assertNotNull(book);
    }

    @Test
    public void findBookByName_called_with_non_existent_bookName_should_return_null() {
        Library library = new Library();
        Book book = library.findBookByName("InValid Name");
        assertNull(book);
    }

    @Test
    public void calculateBookRent_should_return_2_dollar_if_the_number_of_days_is_4() {

        RentedBook rentedBook = Mockito.mock(RentedBook.class);
        LocalDate fourDaysBeforeToday = LocalDate.now().minusDays(4);
        Mockito.when(rentedBook.getRentedDate()).thenReturn(fourDaysBeforeToday);
        Double calculatedPrice = library.calculateBookRent(rentedBook);
        assertThat(calculatedPrice, equalTo(2.0));
        Mockito.verify(rentedBook, Mockito.times(2)).getRentedDate();
    }

    @Test
    public void calculateBookRent_should_return_6_dollar_if_the_number_of_days_is_6() {

        RentedBook rentedBook = Mockito.mock(RentedBook.class);
        LocalDate sixDaysBeforeToday = LocalDate.now().minusDays(6);
        Mockito.when(rentedBook.getRentedDate()).thenReturn(sixDaysBeforeToday);
        Double calculatedPrice = library.calculateBookRent(rentedBook);
        assertThat(calculatedPrice, equalTo(6.0));
        Mockito.verify(rentedBook, Mockito.times(2)).getRentedDate();
    }

    @Test
    public void when_returning_book_receipt_should_be_return() {
        RentedBook rentedBook = library.rent("The God Of Small Things");
        long numberOfBooksInLibraryBeforeReturn = library.getBooks().size();

        LocalDate FiveDaysBeforeToday = LocalDate.now().minusDays(5);
        rentedBook.setRentedDate(FiveDaysBeforeToday);
        Double amountGiven = 3.0;


        Receipt receipt = library.returnBook(rentedBook, amountGiven);
        assertNotNull(receipt);
        assertThat(receipt.bookName, equalTo("The God Of Small Things"));
        assertThat(receipt.receiptDate, equalTo(LocalDate.now()));
        assertThat(receipt.amountGiven, equalTo(amountGiven));
        assertThat(receipt.actualAmount, greaterThan(0.0));
        Double expectedBalanceToBeReturned = receipt.actualAmount - receipt.amountGiven;
        assertThat(receipt.balanceToBeReturn, equalTo(expectedBalanceToBeReturned));

        long numberOfBooksInLibraryAfterReturned = library.getBooks().size();
        assertThat(numberOfBooksInLibraryAfterReturned, equalTo(numberOfBooksInLibraryBeforeReturn + 1));
    }

    @Test
    public void when_amount_provided_isLower_then_actual_amount_an_exception_to_be_thrown() {

        RentedBook rentedBook = library.rent("The God Of Small Things");


        LocalDate FiveDaysBeforeToday = LocalDate.now().minusDays(5);
        rentedBook.setRentedDate(FiveDaysBeforeToday);
        Double amountGiven = 1.0;

        assertThrows(LowerAmountException.class, () -> {
            library.returnBook(rentedBook, amountGiven);

        });
    }
}
