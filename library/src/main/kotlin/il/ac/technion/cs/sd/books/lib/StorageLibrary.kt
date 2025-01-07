package il.ac.technion.cs.sd.books.lib
import il.ac.technion.cs.sd.books.dummy.StorageDummyFiles
import il.ac.technion.cs.sd.lib.Storable

/**
 * Implement your library here. Feel free to change the class name,
 * but note that if you choose to change the class name,
 * you will need to update the import statements in GradesInitializer.kt
 * and in GradesReader.kt.
 */

interface StorageLibrary< T : Storable<T>>{
    fun store(itemsContainer : T)
}
class ReviewerStorageLibraryImpl<T : StorableReviewer<T>> : StorageLibrary<T> {
    overfun store(itemsContainer : T)
        // Implementation to store the items
        val gradeStorage_reviewers_and_books = LineStorageFactoy.open("reviewers_and_books")
        val gradeStorage_reviewer_first = LineStorageFactoy.open("grades_that_reviewers_gave_books")
        val avgs_file_reviewers_ids = LineStorageFactoy.open("averages_of_reviewers_ids")
        val avgs_file_reviewers = LineStorageFactoy.open("averages_of_reviewers")
        val avgs_file_books_ids = LineStorageFactoy.open("averages_of_books_ids")
        val avgs_file_books = LineStorageFactoy.open("averages_of_books")

        items
            .groupBy { it.getIdReviewer() } // Associate by reviewer ID
            .toSortedMap(compareBy<String> { it } // Sort by reviewer ID
                .thenBy { key -> items.find { it.getIdReviewer() == key }?.getIdBook() ?: 0 } // Then by book ID
            )
            .values
            .flatten() // Flattening the grouped items (since groupBy creates lists)
            .forEach { item -> gradeStorage_reviewer_first.appendLine(item.toStorageString()) // fun1
                gradeStorage_reviewers_and_books.appendLine(item.toStorageStringWithoutGrade())/*fun3*/ }


        val reviewerAverages: Map<String, Double> = items

            .groupBy { it.getIdReviewer() } // Group items by reviewer ID
            .mapValues { (_, reviews) ->
                reviews.map { it.getGrade().toDouble() }.average() // Calculate the average for each reviewer
            }
            .toSortedMap()
        reviewerAverages.forEach { (reviewerId, average) ->
            avgs_file_reviewers_ids.appendLine("$reviewerId")
            avgs_file_reviewers.appendLine("$average")
        }
        // averages for books// fun8
        val bookGradesAverages: Map<String, Double> = items
            .groupBy { it.getIdBook()} // Group items by reviewer ID
            .mapValues { (_, reviews) ->
                reviews.map { it.getGrade().toDouble() }.average() // Calculate the average for each reviewer
            }
            .toSortedMap()

        bookGradesAverages.forEach { (bookId, average) ->
            avgs_file_books_ids.appendLine("$bookId")
            avgs_file_books.appendLine("$average")
        }


    }
}

class XmlRootStorageLibraryImpl1 : StorageLibrary<StorableReviewer<*>> {
    override fun store(items: List<UniquelyIdentifiedStorable>) {
        // openning the file to insert into ..?
        // reviewer first


        val gradeStorage_reviewers_and_books = LineStorageFactoy.open("reviewers_and_books")
        val gradeStorage_reviewer_first = LineStorageFactoyImpl.open("grades_that_reviewers_gave_books")

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
        val avgs_file_reviewers_ids = LineStorageFactoyImpl.open("averages_of_reviewers_ids")
        val avgs_file_reviewers = LineStorageFactoyImpl.open("averages_of_reviewers")
        reviewerAverages.forEach { (reviewerId, average) ->
            avgs_file_reviewers_ids.appendLine("$reviewerId")
            avgs_file_reviewers.appendLine("$average")
        }

        // averages for books// fun8
        val bookGradesAverages: Map<String, Double> = items
            .groupBy { it.getIdBook()} // Group items by reviewer ID
            .mapValues { (_, reviews) ->
                reviews.map { it.getGrade().toDouble() }.average() // Calculate the average for each reviewer
            }
            .toSortedMap()

        val avgs_file_books_ids= LineStorageFactoyImpl.open("averages_of_books_ids")
        val avgs_file_books= LineStorageFactoyImpl.open("averages_of_books")
        bookGradesAverages.forEach { (bookId, average) ->
            avgs_file_books_ids.appendLine("$bookId")
            avgs_file_books.appendLine("$average")
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

        val reviewers_of_a_book_with_grade = LineStorageFactoyImpl.open("reviewers_of_a_book_and_grades_in_one_line")
        val reviewers_of_a_book_with_grade_ids = LineStorageFactoyImpl.open("reviewers_of_a_book_and_grades_in_one_line_with_grades_ids")
        val reviewers_of_a_book_with_grade_ids2 = LineStorageFactoyImpl.open("reviewers_of_a_book_in_one_line_without_grades_ids")//fun 7
        val reviewers_of_a_book_= LineStorageFactoyImpl.open("reviewers_of_a_book_in_one_line_without_grades")//fun6

        reviewers_of_a_book.forEach { (bookId, reviewers) ->
            val formattedReviewers_with_grades = reviewers.joinToString(",") { (reviewer, grade) -> "$reviewer:$grade" }
            val formattedReviewers_without_grades = reviewers.joinToString(",") { (reviewer, grade) -> "$reviewer" }
            reviewers_of_a_book_with_grade_ids.appendLine("$bookId")
            reviewers_of_a_book_with_grade_ids2.appendLine(("$bookId"))
            reviewers_of_a_book_with_grade.appendLine("$formattedReviewers_with_grades")
            reviewers_of_a_book_.appendLine("$formattedReviewers_without_grades")

        }

        // Writing the data to a file

        val books_of_reviewer_with_grade = LineStorageFactoyImpl.open("books_of_a_reviewer_and_grades_in_one_line")
        val books_of_reviewer_with_grade_ids = LineStorageFactoyImpl.open("books_of_a_reviewer_and_grades_in_one_line_with_grades_ids")
        val books_of_reviewer_with_grade_ids2 = LineStorageFactoyImpl.open("books_of_a_reviewer_and_grades_in_one_line_without_grades_ids")//fun 4
        val books_of_reviewer_= LineStorageFactoyImpl.open("books_of_a_reviewer_in_one_line_without_grades")//fun2

        books_of_reviewers.forEach { (reviewerId, books) ->
            val formattedbooks_with_grades = books.joinToString(",") { (book, grade) -> "$book:$grade" }
            val formattedbooks_without_grades = books.joinToString(",") { (book, grade) -> "$book" }
            books_of_reviewer_with_grade_ids.appendLine("$reviewerId")
            books_of_reviewer_with_grade_ids2.appendLine(("$reviewerId"))
            books_of_reviewer_with_grade.appendLine("$formattedbooks_with_grades")
            books_of_reviewer_.appendLine("$formattedbooks_without_grades")


        }

// TODO:: IF IDS CAN CONTAIN "," :: i checked piazza and they answered alphanumeric chars
        fun getLine(id:String,file: LineStorage): Int?{
            val dataOfId= binarySearchIterativeFromExternal(id,file,true)
            return dataOfId?.first
        }

        /**
         * this function does binarysearch in linestorage
         * @param target - id of someone that we search in file
         * @return a pair <int, string> , first = num of line found the id in it and second=data of id that has been found
         */


        private fun binarySearchIterativeFromExternal(target: String, file: LineStorage, check_only_first_part:Boolean): Pair<Int, String>? {
            var left = 0
            var right = file.numberOfLines() - 1

            while (left <= right) {
                val mid = left + (right - left) / 2
                val dataString = file.read(mid)
                val parts = dataString.split(",") // Split the string at the comma
                /*if (parts.size < splits) {
                    //TODO::EXCPETION ..?
                    // Handle error: dataString does not contain a comma
                    continue
                }*/

                //TODO:: WHAT  HAPPENS IN THE CASE OF TWO BOOKS FOR ONE REVIEWER ..?

                val dataConverted = if (check_only_first_part ) {
                    parts[0]
                } else {
                    parts[0] + ',' + parts[1]
                }

                when {
                    dataConverted == target -> return Pair(mid, dataString) // Target found
                    dataConverted < target -> left = mid + 1 // Move to the right half
                    else -> right = mid - 1 // Move to the left half
                }
            }
            return null // Target not found
        }


        fun retrieveGrade(idReviewer: String,idBook: String):String?{
            val gradeStorage_reviewer_first = LineStorageFactoyImpl.open("grades_that_reviewers_gave_books")
            val dataFound=binarySearchIterativeFromExternal(idReviewer+','+idBook,gradeStorage_reviewer_first,false)
            return dataFound?.second
        }
        fun checkIfGaveGrade(idReviewer: String,idBook: String):Boolean {
            val gradeStorage_reviewer_first = LineStorageFactoyImpl.open("reviewers_and_books")
            val dataFound = binarySearchIterativeFromExternal(idReviewer + ',' + idBook, gradeStorage_reviewer_first,false)
            return if (dataFound == null) {
                false
            } else {
                // Add your logic here if dataFound is not null
                true
            }
        }
        fun getAllBooks(idReviewer: String): String? {
            val books_of_reviewer_with_grade_ids2 = LineStorageFactoyImpl.open("books_of_a_reviewer_and_grades_in_one_line_without_grades_ids") // fun 4
            val books_of_reviewer_ = LineStorageFactoyImpl.open("books_of_a_reviewer_in_one_line_without_grades") // fun 2
            val line = getLine(idReviewer, books_of_reviewer_with_grade_ids2)
            return line?.let { books_of_reviewer_.read(it) }
        }

        fun getAllBooksWithGrades(idReviewer: String): String? {
            val books_of_reviewer_with_grade = LineStorageFactoyImpl.open("books_of_a_reviewer_and_grades_in_one_line")
            val books_of_reviewer_with_grade_ids = LineStorageFactoyImpl.open("books_of_a_reviewer_and_grades_in_one_line_with_grades_ids")
            val line = getLine(idReviewer, books_of_reviewer_with_grade_ids)

            return line?.let { books_of_reviewer_with_grade.read(it) }

        }

        fun getAllReviewers(idBook: String):String?{

            val reviewers_of_a_book_with_grade_ids2 = LineStorageFactoyImpl.open("reviewers_of_a_book_in_one_line_without_grades_ids")//fun 7
            val reviewers_of_a_book_= LineStorageFactoyImpl.open("reviewers_of_a_book_in_one_line_without_grades")//fun6
            val line = getLine(idBook, reviewers_of_a_book_with_grade_ids2)
            return line?.let { reviewers_of_a_book_.read(it) }


        }

        fun getAllReviewersWithGrades(idBook: String):String?{
            val reviewers_of_a_book_with_grade = LineStorageFactoyImpl.open("reviewers_of_a_book_and_grades_in_one_line")
            val reviewers_of_a_book_with_grade_ids = LineStorageFactoyImpl.open("reviewers_of_a_book_and_grades_in_one_line_with_grades_ids")
            val line = getLine(idBook, reviewers_of_a_book_with_grade_ids)
            return line?.let { reviewers_of_a_book_with_grade.read(it) }


        }
        fun getAvgOfReviewer(idReviewer: String):String?{
            val file2openids= LineStorageFactoyImpl.open("averages_of_reviewers_ids")
            val line = getLine(idReviewer, file2openids)
            val file2open = LineStorageFactoyImpl.open("averages_of_reviewers")
            return line?.let { file2open.read(it) }


        }
        fun getAvgOfBook(idBook: String):String?{
            val file2openids= LineStorageFactoyImpl.open("averages_of_books_ids")
            val line = getLine(idBook, file2openids)
            val file2open = LineStorageFactoyImpl.open("averages_of_books")
            return line?.let { file2open.read(it) }
           // val dataFound=binarySearchIterativeFromExternal(idBook,file2open,true)
            //return (dataFound?.second)?.split(",")?.getOrNull(1)

        }




        fun clearStorage() {

            StorageDummyFiles.clearAll()

        }
    }

}
