package il.ac.technion.cs.sd.books.app
import com.google.inject.Inject
import il.ac.technion.cs.sd.books.lib.StorageLibrary
import il.ac.technion.cs.sd.books.lib.StorageLibraryImpl
import il.ac.technion.cs.sd.books.modules.Parser
import il.ac.technion.cs.sd.lib.Storable

/**
 * This class will only be instantiated by kotlin-guice after
 * BookScoreInitializer.setup() has been called
 */
interface BookScoreReader {

    /**
     * Returns true iff a reviewer with ID reviewerId has reviewed the book
     * with ID bookId. If either the reviewer or the book does not exist,
     * returns false
     */
    fun gaveReview(reviewerId: String, bookId: String): Boolean

    /**
     * Returns the reviewer's review score for the book, if the reviewer
     * reviewed the book. If either the reviewer or the book does not exist,
     * or the reviewer did not review the book, returns null.
     */
    fun getScore(reviewerId: String, bookId: String): Int?

    /**
     * Returns a sorted (!) list of all the books the reviewer reviewed.
     * If the reviewer does not exist returns an empty list.
     */
    fun getReviewedBooks(reviewerId: String): List<String>

    /**
     * Returns the scores of all the books the reviewer reviewed, as a map from
     * the book ID to its score. If the reviewer does not exist returns an
     * empty map.
     */
    fun getAllReviewsByReviewer(reviewerId: String): Map<String, Int>

    /**
     * Returns the average score the reviewer gave, across all books reviewed
     * by him. If the reviewer does not exist, returns null.
     */
    fun getAverageScoreForReviewer(reviewerId: String): Double?

    /**
     * Returns a sorted (!) list of all the reviewers that reviewed the book.
     * If the book does not exist, returns an empty list.
     */
    fun getReviewers(bookId: String): List<String>

    /**
     * Returns the scores of all the reviewers who reviewed the book, as a map
     * from reviewer ID to their review score. If the book does not exist,
     * returns an empty map.
     */
    fun getReviewsForBook(bookId: String): Map<String, Int>

    /**
     * Returns the average review score of the book. If the book does not
     * exist, returns null.
     */
    fun getAverageScoreForBook(bookId: String): Double?

}

class BookScoreReaderImpl<T : Storable<T>> @Inject constructor(
    private val storageLib: StorageLibrary<T>
) : BookScoreReader {
    override fun gaveReview(reviewerId: String, bookId: String): Boolean {
        return storageLib.hasReviewedBook(reviewerId, bookId)
    }

    override fun getScore(reviewerId: String, bookId: String): Int? {
        val grade = storageLib.getBookReviewScoreBy(reviewerId, bookId)
        return grade?.toIntOrNull()
    }

    override fun getReviewedBooks(reviewerId: String): List<String> {
        val books = storageLib.getAllBooksReviewedBy(reviewerId)
        return books?.split(",")?.filter { it.isNotEmpty() }?.sorted() ?: emptyList()
    }

    override fun getAllReviewsByReviewer(reviewerId: String): Map<String, Int> {
        val booksWithGrades = storageLib.getAllBooksReviewedByWithScore(reviewerId)
        return booksWithGrades
            ?.split(",")
            ?.mapNotNull {
                val parts = it.split(":")
                if (parts.size == 2) {
                    val bookId = parts[0]
                    val grade = parts[1].toIntOrNull()
                    if (grade != null) bookId to grade else null
                } else null
            }
            ?.toMap() ?: emptyMap()

    }

    override fun getAverageScoreForReviewer(reviewerId: String): Double? {
        val avg = storageLib.getReviewerAvgScore(reviewerId)
        return avg?.toDoubleOrNull()
    }

    override fun getReviewers(bookId: String): List<String> {
        val reviewers = storageLib.getAllReviewersOfBook(bookId)
        return reviewers?.split(",")?.filter { it.isNotEmpty() }?.sorted() ?: emptyList()
    }

    override fun getReviewsForBook(bookId: String): Map<String, Int> {
        val reviewersWithGrades = storageLib.getAllReviewersOfBookWithScores(bookId)
        return reviewersWithGrades
            ?.split(",")
            ?.mapNotNull {
                val parts = it.split(":")
                if (parts.size == 2) {
                    val reviewerId = parts[0]
                    val grade = parts[1].toIntOrNull()
                    if (grade != null) reviewerId to grade else null
                } else null
            }
            ?.toMap() ?: emptyMap()
    }

    override fun getAverageScoreForBook(bookId: String): Double? {
        val avg = storageLib.getBookAvgScore(bookId)
        return avg?.toDoubleOrNull()
    }
}
