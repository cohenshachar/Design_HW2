package il.ac.technion.cs.sd.books.test

import com.google.inject.*
import il.ac.technion.cs.sd.books.app.BookScoreModule
import il.ac.technion.cs.sd.books.lib.StorageLibraryModule
import il.ac.technion.cs.sd.books.modules.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException

internal class XmlDataStructsTest {

    private lateinit var injector: Injector
    private lateinit var parser: Parser<XmlRoot>

    private fun getFileContent(fileName: String): String {
        val fileContents: String =
            javaClass.getResource(fileName)?.readText() ?: throw FileNotFoundException("Could not open file $fileName")
        return fileContents
    }

    @BeforeEach
    fun setUp() {
        injector = Guice.createInjector(StorageLibraryModule(), BookScoreModule())
        val parserKey = Key.get(object : TypeLiteral<Parser<XmlRoot>>() {})
        parser = injector.getInstance(parserKey)
    }

    @Test
    fun testSerialization() {
        val xmlString = getFileContent("small.xml")
        val parsedRoot = parser.parse(xmlString)
        println(parsedRoot.toString())
    }

    @Test
    fun testTakeLast() {
        val xmlString = getFileContent("take_last.xml")
        val parsedRoot = parser.parse(xmlString)
        assertNotNull(parsedRoot)
        assertEquals(1, parsedRoot.reviewers.size)
        assertEquals(1, parsedRoot.books.size)
        assertEquals(1, parsedRoot.reviewers[0].reviews.size)
        assertEquals(8, parsedRoot.reviewers[0].reviews[0].score)
        assertEquals(8.0, parsedRoot.reviewers[0].avgScore)
        assertEquals("123", parsedRoot.reviewers[0].id)
        assertEquals(8.0, parsedRoot.books[0].avgScore)
        assertEquals(1, parsedRoot.books[0].reviews.size)
        assertEquals("Loobar", parsedRoot.books[0].id)
    }

    @Test
    fun testMultipleReviewersOneBook() {
        val xmlString = getFileContent("multiple_reviewers_one_book.xml")
        val parsedRoot = parser.parse(xmlString)
        assertNotNull(parsedRoot)
        assertEquals(8, parsedRoot.reviewers.size)
        assertEquals(1, parsedRoot.books.size)
        assertEquals(1, parsedRoot.reviewers[0].reviews.size)
        assertEquals(8, parsedRoot.reviewers[0].reviews[0].score)
        assertEquals(7, parsedRoot.reviewers[1].reviews[0].score)
        assertEquals(6, parsedRoot.reviewers[2].reviews[0].score)
        assertEquals(5, parsedRoot.reviewers[3].reviews[0].score)
        assertEquals(4, parsedRoot.reviewers[4].reviews[0].score)
        assertEquals(3, parsedRoot.reviewers[5].reviews[0].score)
        assertEquals(2, parsedRoot.reviewers[6].reviews[0].score)
        assertEquals(1, parsedRoot.reviewers[7].reviews[0].score)
        assertEquals("1238", parsedRoot.reviewers[0].id)
        assertEquals("1237", parsedRoot.reviewers[1].id)
        assertEquals("1236", parsedRoot.reviewers[2].id)
        assertEquals("1235", parsedRoot.reviewers[3].id)
        assertEquals("1234", parsedRoot.reviewers[4].id)
        assertEquals("1233", parsedRoot.reviewers[5].id)
        assertEquals("1232", parsedRoot.reviewers[6].id)
        assertEquals("1231", parsedRoot.reviewers[7].id)
        assertEquals(8.0, parsedRoot.reviewers[0].avgScore)
        assertEquals((8+7.0+6.0+5.0+4.0+3.0+2.0+1.0)/8, parsedRoot.books[0].avgScore)
        assertEquals(8, parsedRoot.books[0].reviews.size)
    }

    @Test
    fun testRepeatedReviewMultipleBooks() {
        val xmlString = getFileContent("repeatedBook_in_multiple_register.xml")
        val parsedRoot = parser.parse(xmlString)
        assertNotNull(parsedRoot)
        assertEquals(1, parsedRoot.reviewers.size)
        assertEquals(5, parsedRoot.books.size)
        assertEquals("Loobar", parsedRoot.books[0].id)
        assertEquals(8.0, parsedRoot.books[0].avgScore)
        assertEquals(1, parsedRoot.books[0].reviews.size)
        assertEquals("boobar", parsedRoot.books[1].id)
        assertEquals(6.0, parsedRoot.books[1].avgScore)
        assertEquals(1, parsedRoot.books[1].reviews.size)
        assertEquals("xoobar", parsedRoot.books[2].id)
        assertEquals(5.0, parsedRoot.books[2].avgScore)
        assertEquals(1, parsedRoot.books[2].reviews.size)
        assertEquals("doobar", parsedRoot.books[3].id)
        assertEquals(2.0, parsedRoot.books[3].avgScore)
        assertEquals(1, parsedRoot.books[3].reviews.size)
        assertEquals("shmoobar", parsedRoot.books[4].id)
        assertEquals(1.0, parsedRoot.books[4].avgScore)
        assertEquals(1, parsedRoot.books[4].reviews.size)
    }

    @Test
    fun testRepeatedBooksInOneReview() {
        val xmlString = getFileContent("repeatedBook_in_one_register.xml")
        val parsedRoot = parser.parse(xmlString)
        assertNotNull(parsedRoot)
        assertEquals(1, parsedRoot.reviewers.size)
        assertEquals(3, parsedRoot.books.size)
        assertEquals("Foobar", parsedRoot.books[0].id)
        assertEquals(9.0, parsedRoot.books[0].avgScore)
        assertEquals(1, parsedRoot.books[0].reviews.size)
        assertEquals("Doobar", parsedRoot.books[1].id)
        assertEquals(8.0, parsedRoot.books[1].avgScore)
        assertEquals(1, parsedRoot.books[1].reviews.size)
        assertEquals("Loobar", parsedRoot.books[2].id)
        assertEquals(6.0, parsedRoot.books[2].avgScore)
        assertEquals(1, parsedRoot.books[2].reviews.size)
    }

    @Test
    fun testEmpty() {
        val xmlString = getFileContent("empty.xml")
        val parsedRoot = parser.parse(xmlString)
        assertNotNull(parsedRoot)
        assertEquals(0, parsedRoot.reviewers.size)
        assertEquals(0, parsedRoot.books.size)
    }

    @Test
    fun testJibrish() {
        val xmlString = getFileContent("jibrish.xml")
        val parsedRoot = parser.parse(xmlString)
        assertNotNull(parsedRoot)
        assertEquals(0, parsedRoot.reviewers.size)
        assertEquals(0, parsedRoot.books.size)
    }
}
