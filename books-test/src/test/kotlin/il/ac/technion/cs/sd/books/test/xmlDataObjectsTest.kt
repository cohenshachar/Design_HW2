package il.ac.technion.cs.sd.books.test

import il.ac.technion.cs.sd.books.app.BookScoreReader
import il.ac.technion.cs.sd.books.modules.*
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.simpleframework.xml.core.Persister
import java.io.FileNotFoundException

internal class XmlDataStructsTest {

    private lateinit var parser: Parser<XmlRoot>

    private fun getFileContent(fileName: String): String {
        val fileContents: String =
            javaClass.getResource(fileName)?.readText() ?: throw FileNotFoundException("Could not open file $fileName")
        return fileContents
    }

    @BeforeEach
    fun setUp() {
        parser = XmlParser()
    }

    @Test
    fun testReviewSerialization() {
        val xmlString = getFileContent("small.xml")
        val parsedRoot = parser.parse(xmlString)
        parsedRoot.reviewers.forEach { println(it) }
    }
}
//
//    @Test
//    fun testReviewerSerialization() {
//        val review1 = Review(id = "123", score = 5)
//        val review2 = Review(id = "456", score = 8)
//        val reviewer = Reviewer(id = "789", reviews = mutableListOf(review1, review2))
//        val serializer = Persister()
//        val xml = serializer.write(reviewer)
//        val deserializedReviewer = serializer.read(Reviewer::class.java, xml)
//
//        assertEquals(reviewer.id, deserializedReviewer.id)
//        assertEquals(reviewer.reviews.size, deserializedReviewer.reviews.size)
//        assertEquals(reviewer.reviews[0].id, deserializedReviewer.reviews[0].id)
//        assertEquals(reviewer.reviews[0].score, deserializedReviewer.reviews[0].score)
//        assertEquals(reviewer.reviews[1].id, deserializedReviewer.reviews[1].id)
//        assertEquals(reviewer.reviews[1].score, deserializedReviewer.reviews[1].score)
//    }
//
//    @Test
//    fun testXmlRootSerialization() {
//        val review1 = Review(id = "123", score = 5)
//        val review2 = Review(id = "456", score = 8)
//        val reviewer = Reviewer(id = "789", reviews = mutableListOf(review1, review2))
//        val book = Book(id = "123", avgScore = 6.5, reviews = mutableMapOf("789" to review1))
//        val xmlRoot = XmlRoot(reviewers = mutableListOf(reviewer), books = mutableListOf(book))
//        val serializer = Persister()
//        val xml = serializer.write(xmlRoot)
//        val deserializedXmlRoot = serializer.read(XmlRoot::class.java, xml)
//
//        assertEquals(xmlRoot.reviewers.size, deserializedXmlRoot.reviewers.size)
//        assertEquals(xmlRoot.books.size, deserializedXmlRoot.books.size)
//        assertEquals(xmlRoot.reviewers[0].id, deserializedXmlRoot.reviewers[0].id)
//        assertEquals(xmlRoot.reviewers[0].reviews.size, deserializedXmlRoot.reviewers[0].reviews.size)
//        assertEquals(xmlRoot.books[0].id, deserializedXmlRoot.books[0].id)
//        assertEquals(xmlRoot.books[0].avgScore, deserializedXmlRoot.books[0].avgScore)
//        assertEquals(xmlRoot.books[0].reviews.size, deserializedXmlRoot.books[0].reviews.size)
//    }
//}
