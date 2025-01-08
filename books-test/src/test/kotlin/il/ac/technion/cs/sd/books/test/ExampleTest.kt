package il.ac.technion.cs.sd.books.test

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions

import java.io.FileNotFoundException

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance


import il.ac.technion.cs.sd.books.app.BookScoreReader
import il.ac.technion.cs.sd.books.app.BookScoreInitializer
import il.ac.technion.cs.sd.books.app.BookScoreModule
import il.ac.technion.cs.sd.books.external.LineStorageModule
import il.ac.technion.cs.sd.books.lib.StorageLibraryModule


class ExampleTest {

    private fun getScoresReader(fileName: String): BookScoreReader {
        val fileContents: String =
            javaClass.getResource(fileName)?.readText() ?:
            throw FileNotFoundException("Could not open file $fileName")

        val injector = Guice.createInjector( BookScoreModule(), LineStorageModule())
        injector.getInstance<BookScoreInitializer>().setup(fileContents)
        return injector.getInstance<BookScoreReader>()
    }

    @Test
    fun `simple example test`() {
        val reader = getScoresReader("small.xml")
        val startTime = System.nanoTime()

        Assertions.assertEquals(listOf("Boobar", "Foobar", "Moobar"), reader.getReviewedBooks("123"))
        Assertions.assertEquals(6.0, reader.getAverageScoreForReviewer("123"))
        Assertions.assertEquals(10.0, reader.getAverageScoreForBook("Foobar"))
        Assertions.assertEquals(true, reader.gaveReview("123","Foobar"))
        Assertions.assertEquals(false, reader.gaveReview("123","Foobac"))
        Assertions.assertEquals(false, reader.gaveReview("129","Foobar"))
        Assertions.assertEquals(false, reader.gaveReview("129","Foobac"))
        val endTime = System.nanoTime()
        val durationInSeconds = (endTime - startTime) / 1_000_000_000.0
        println("Execution Time: $durationInSeconds seconds")
    }
    @Test
    fun `take last`(){
        val reader = getScoresReader("take_last.xml")
        val startTime = System.nanoTime()
        Assertions.assertEquals(listOf("Loobar"), reader.getReviewedBooks("123"))
        Assertions.assertEquals(8.0, reader.getAverageScoreForReviewer("123"))
        Assertions.assertEquals(8, reader.getScore("123","Loobar"))
        val endTime = System.nanoTime()
        val durationInSeconds = (endTime - startTime) / 1_000_000_000.0
        println("Execution Time: $durationInSeconds seconds")

    }

    @Test
    fun `zero in the start`() {
        val reader = getScoresReader("zero_in_the_start.xml")
        val startTime = System.nanoTime()
        Assertions.assertEquals(true, reader.gaveReview("0123","Foobar"))
        Assertions.assertEquals(false, reader.gaveReview("123","Foobar"))
        Assertions.assertEquals(null, reader.getScore("123","Foobar"))
        Assertions.assertEquals(10, reader.getScore("0123","Foobar"))


        Assertions.assertEquals(true, reader.gaveReview("007","Zoobar"))
        Assertions.assertEquals(false, reader.gaveReview("7","Zoobar"))
        Assertions.assertEquals(null, reader.getScore("7","Zoobar"))
        Assertions.assertEquals(10, reader.getScore("007","Zoobar"))
        val endTime = System.nanoTime()
        val durationInSeconds = (endTime - startTime) / 1_000_000_000.0
        println("Execution Time: $durationInSeconds seconds")
    }

    @Test
    fun `not exists reviewer`() {
        val reader = getScoresReader("zero_in_the_start.xml")
        val startTime = System.nanoTime()
        Assertions.assertEquals(false, reader.gaveReview("123","Foobar"))
        Assertions.assertEquals(null, reader.getAverageScoreForBook("123"))

        Assertions.assertEquals(null, reader.getAverageScoreForReviewer("123"))
        Assertions.assertEquals(null, reader.getScore("123","Foobar"))
        val emptylist: List<String> = emptyList()
        Assertions.assertEquals(emptylist, reader.getReviewedBooks("123"))
        val emptyMap: Map<String, Int> = emptyMap()
        Assertions.assertEquals(emptyMap, reader.getAllReviewsByReviewer("123"))
        val endTime = System.nanoTime()
        val durationInSeconds = (endTime - startTime) / 1_000_000_000.0
        println("Execution Time: $durationInSeconds seconds")








    }

    @Test
    fun `zero reviews for reviewers`() {
        val reader = getScoresReader("zero_reviews_for_reviewers.xml")
        val startTime = System.nanoTime()
        val emptylist: List<String> = emptyList()
        Assertions.assertEquals(emptylist, reader.getReviewedBooks("123"))
        Assertions.assertEquals(null,reader.getAverageScoreForReviewer("123"))
        Assertions.assertEquals(null,reader.getAverageScoreForReviewer("123000"))
        val emptyMap: Map<String, Int> = emptyMap()
        Assertions.assertEquals(emptyMap,reader.getReviewsForBook("ss"))
        val endTime = System.nanoTime()
        val durationInSeconds = (endTime - startTime) / 1_000_000_000.0
        println("Execution Time: $durationInSeconds seconds")

    }

    @Test
    fun `multiple_books_ine_reviewer`() {
        val reader = getScoresReader("multiple_books_one_reviewer.xml")
        val startTime = System.nanoTime()
        Assertions.assertEquals(listOf( "Loobar","boobar", "shmoobar","xoobar"), reader.getReviewedBooks("1"))
        Assertions.assertEquals(listOf("Loobar","boobar", "doobar"), reader.getReviewedBooks("2"))
        Assertions.assertEquals(listOf("Loobar", "boobar","xoobar"), reader.getReviewedBooks("3"))
        val endTime = System.nanoTime()
        val durationInSeconds = (endTime - startTime) / 1_000_000_000.0
        println("Execution Time: $durationInSeconds seconds")



    }
    @Test
    fun `multiple_reviewers_for_a_book`() {
        val reader = getScoresReader("multiple_reviewers_one_book.xml")
        val startTime = System.nanoTime()
        Assertions.assertEquals(listOf( "1231","1232", "1233","1234","1235","1236","1237","1238"), reader.getReviewers("Loobar"))
        val endTime = System.nanoTime()
        val durationInSeconds = (endTime - startTime) / 1_000_000_000.0
        println("Execution Time: $durationInSeconds seconds")


    }
    @Test
    fun `take last2`(){
        val reader = getScoresReader("last_review_is_the_selected.xml")
        val startTime = System.nanoTime()
        Assertions.assertEquals(null, reader.getAverageScoreForBook("123"))
        Assertions.assertEquals(5.04, reader.getAverageScoreForBook("Book123"))
        Assertions.assertEquals(9, reader.getScore("1001","Book123"))
        val endTime = System.nanoTime()
        val durationInSeconds = (endTime - startTime) / 1_000_000_000.0
        println("Execution Time: $durationInSeconds seconds")

    }

    @Test
    fun `one_hundred_reviewers_for_one_book_with_gradeii`(){
        val reader = getScoresReader("one_hundred_reviewers_for_one_book_with_grade5.xml")
        val startTime = System.nanoTime()

        Assertions.assertEquals(5.0, reader.getAverageScoreForBook("Book123"))
        println(";;;;;;;;;;;;;;;;;;;")
        print(reader.getReviewsForBook("Book123"))

        println()
        val endTime = System.nanoTime()
        val durationInSeconds = (endTime - startTime) / 1_000_000_000.0
        println("Execution Time: $durationInSeconds seconds")

    }

   /* @Test
    fun `estimating time`()
    {
        val booksList = (99901..100000).map { "Book$it" }.sorted()
        val reader = getScoresReader("very_big.xml")
        Assertions.assertEquals(booksList, reader.getReviewedBooks("100000"))


    }*/




}