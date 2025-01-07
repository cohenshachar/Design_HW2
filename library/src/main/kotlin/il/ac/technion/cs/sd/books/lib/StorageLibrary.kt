package il.ac.technion.cs.sd.lib
import il.ac.technion.cs.sd.books.dummy.StorageDummy
import il.ac.technion.cs.sd.books.external.LineStorage
import il.ac.technion.cs.sd.books.external.LineStorageFactoyImpl
/**
 * Implement your library here. Feel free to change the class name,
 * but note that if you choose to change the class name,
 * you will need to update the import statements in GradesInitializer.kt
 * and in GradesReader.kt.
 */

class GenericGradeManager<T> {

    private val data_reviewers_books = mutableMapOf<String, MutableMap<String, T>>()
// Outer map: Reviewer ID -> Inner map
// Inner map: Book ID -> Grade

    private val data_books_reviewers = mutableMapOf<String, MutableMap<String, T>>()
// Outer map: Book ID -> Inner map
// Inner map: Reviewer ID -> Grade

    private  val avg_grades_of_reviewer = sortedMapOf<String,T>()
    private val avg_grades_of_book= sortedMapOf<String,T>()

    fun insert(idReviewer: String,idBook:String, grade: T)// INSERT OR UPDATE IF EXISTS
    {
        data_reviewers_books
            .computeIfAbsent(idReviewer) { mutableMapOf() }
            .put(idBook, grade)

        data_books_reviewers
            .computeIfAbsent(idBook) { mutableMapOf() }
            .put(idReviewer, grade)

    }
    fun getBooksByReviewer(reviewerId: String): Map<String, T>? {
        return data_reviewers_books[reviewerId]
    }
    fun getReviewersByBook(bookId: String): Map<String, T>? {
        return data_books_reviewers[bookId]
    }

    fun getGrade(idReviewer: String,idBook: String): T? {
        return getBooksByReviewer(idReviewer)?.get(idBook)
    }

    // Retrieve all data, sorted by id review and then idbooks
    fun getAllDataReviewers(): List<Pair<String, List<Pair<String, T>>>> {
        return data_reviewers_books.map { (reviewerId, books) ->
            reviewerId to books.map { (bookId, grade) -> bookId to grade }
        }
    }
    fun getAllDataBooks(): List<Pair<String, List<Pair<String, T>>>> {
        return data_books_reviewers.map { (bookId, reviewers) ->
            bookId to reviewers.map { (reviewerId, grade) -> reviewerId to grade }
        }
    }


    fun clear(){
        data_reviewers_books.clear()
        data_books_reviewers.clear()
        avg_grades_of_book.clear()
        avg_grades_of_reviewer.clear()
    }
}
class StorageLibrary<T> {
    companion object {
        fun storeUnique(items: List<UniquelyIdentifiedStorable>) {
            // openning the file to insert into ..?
            // reviewer first
            val factory= LineStorageFactoyImpl()

            val gradeStorage_reviewers_and_books = factory.open("reviewers_and_books")
            val gradeStorage_reviewer_first = factory.open("grades_that_reviewers_gave_books")

            items
                .groupBy { it.getIdReviewer() } // Associate by reviewer ID
                .toSortedMap(compareBy<String> { it } // Sort by reviewer ID
                    .thenBy { key -> items.find { it.getIdReviewer() == key }?.getIdBook() ?: 0 } // Then by book ID
                )
                .values
                .flatten() // Flattening the grouped items (since groupBy creates lists)
                .forEach { item -> gradeStorage_reviewer_first.appendLine(item.toStorageString()) // fun1
                     gradeStorage_reviewers_and_books.appendLine(item.toStorageStringWithoutGrade())/*fun3*/ }




            //fun5
            val reviewerAverages: Map<String, Double> = items

                .groupBy { it.getIdReviewer() } // Group items by reviewer ID
                .mapValues { (_, reviews) ->
                    reviews.map { it.getGrade().toDouble() }.average() // Calculate the average for each reviewer
                }
                .toSortedMap()


            //inserting averages
            val avgs_file_reviewers = factory.open("averages_of_reviewers")
            reviewerAverages.forEach { (reviewerId, average) ->
                avgs_file_reviewers.appendLine("$reviewerId:$average")
            }

            // averages for books// fun8
            val bookGradesAverages: Map<String, Double> = items
                .groupBy { it.getIdBook()} // Group items by reviewer ID
                .mapValues { (_, reviews) ->
                    reviews.map { it.getGrade().toDouble() }.average() // Calculate the average for each reviewer
                }
                .toSortedMap()

            val avgs_file_books= factory.open("averages_of_books")
            bookGradesAverages.forEach { (bookId, average) ->
                avgs_file_books.appendLine("$bookId:$average")
            }



            // books and their grades


            // Mapping for each book: reviewers and their grades
            val reviewers_of_a_book: MutableMap<String, MutableList<Pair<String, Int>>> = mutableMapOf()
            val books_of_reviewers: MutableMap<String, MutableList<Pair<String, Int>>> = mutableMapOf()
            items.forEach { item ->
                val bookId = item.getIdBook()
                val reviewerId = item.getIdReviewer()
                val grade = item.getGrade()

                // Add the reviewer and grade to the list for the book
                reviewers_of_a_book.computeIfAbsent(bookId) { mutableListOf() }
                    .add(Pair(reviewerId, grade))
                books_of_reviewers.computeIfAbsent(reviewerId){ mutableListOf() }
                    .add(Pair(bookId,grade))
            }

            val reviewers_of_a_book_with_grade = factory.open("reviewers_of_a_book_and_grades_in_one_line")
            val reviewers_of_a_book_with_grade_ids = factory.open("reviewers_of_a_book_and_grades_in_one_line_with_grades_ids")
            val reviewers_of_a_book_with_grade_ids2 = factory.open("reviewers_of_a_book_in_one_line_without_grades_ids")//fun 7
            val reviewers_of_a_book_=factory.open("reviewers_of_a_book_in_one_line_without_grades")//fun6

            reviewers_of_a_book.forEach { (bookId, reviewers) ->
                val formattedReviewers_with_grades = reviewers.joinToString(",") { (reviewer, grade) -> "$reviewer:$grade" }
                val formattedReviewers_without_grades = reviewers.joinToString(",") { (reviewer, grade) -> "$reviewer" }
                reviewers_of_a_book_with_grade_ids.appendLine("$bookId")
                reviewers_of_a_book_with_grade_ids2.appendLine(("$bookId"))
                reviewers_of_a_book_with_grade.appendLine("$formattedReviewers_with_grades")
                reviewers_of_a_book_.appendLine("$formattedReviewers_without_grades")

            }

        // Writing the data to a file

            val books_of_reviewer_with_grade = factory.open("books_of_a_reviewer_and_grades_in_one_line")
            val books_of_reviewer_with_grade_ids = factory.open("books_of_a_reviewer_and_grades_in_one_line_with_grades_ids")
            val books_of_reviewer_with_grade_ids2 = factory.open("books_of_a_reviewer_and_grades_in_one_line_without_grades_ids")//fun 4
            val books_of_reviewer_=factory.open("books_of_a_reviewer_in_one_line_without_grades")//fun2

            books_of_reviewers.forEach { (reviewerId, books) ->
                val formattedbooks_with_grades = books.joinToString(",") { (book, grade) -> "$book:$grade" }
                val formattedbooks_without_grades = books.joinToString(",") { (book, grade) -> "$book" }
                books_of_reviewer_with_grade_ids.appendLine("$reviewerId")
                books_of_reviewer_with_grade_ids2.appendLine(("$reviewerId"))
                books_of_reviewer_with_grade.appendLine("$formattedbooks_with_grades")
                books_of_reviewer_.appendLine("$formattedbooks_without_grades")


            }
            // 13 files -> ((13 * 12)/2 )*100 = 7800 -> 8 sec for creating files but this is in setup

            // in answering the queries will be given 1300 * 5 = 6500 -> 7 sec for openning ...

        }


        fun getLine(id:String, file: LineStorage, splits:Int): Int?{
            val dataOfId= binarySearchIterativeFromExternal(id,file,splits)
            return dataOfId?.first
        }

        /**
         * this function does binarysearch in linestorage
         * @param target - id of someone that we search in file
         * @return a pair <int, string> , first = num of line found the id in it and second=data of id that has been found
         */


        private fun binarySearchIterativeFromExternal(target: String, file: LineStorage, splits:Int): Pair<Int, String>? {
            var left = 0
            var right = file.numberOfLines() - 1

            while (left <= right) {
                val mid = left + (right - left) / 2
                val dataString = file.read(mid)
                val parts = dataString.split(",") // Split the string at the comma
                if (parts.size < splits) {
                    //TODO::EXCPETION ..?
                    // Handle error: dataString does not contain a comma
                    continue
                }
                val dataConverted = parts[0]
                when {
                    dataConverted == target -> return Pair(mid, dataString) // Target found
                    dataConverted < target -> left = mid + 1 // Move to the right half
                    else -> right = mid - 1 // Move to the left half
                }
            }
            return null // Target not found
        }


        fun retrieveGrade(idReviewer: String,idBook: String):String?{
            val dataFound=binarySearchIterativeFromExternal(id)
            return dataFound?.second
        }
        fun checkIfGaveGrade(idReviewer: String,idBook: String):Boolean{
        return false
        }
        fun getAllBooks(idReviewer: String):String?
        {
         return null
        }
        fun getAllBookWithGrades(idReviewer: String):String?{
            return null
        }
        fun getAllReviewers(idBook: String):String?{
            return null
        }

        fun getAllReviewersWithGrades(idBook: String):String?{
            return null
        }
        fun getAvgOfReviewer(idReviewer: String):String?{

            return null
        }
        fun getAvgOfBook(idBook: String):String?{
            return null
        }




        fun clearStorage() {
            StorageDummy.clear()
        }
    }

}
