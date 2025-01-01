package il.ac.technion.cs.sd.lib
import il.ac.technion.cs.sd.dummy.StorageDummy
import il.ac.technion.cs.sd.grades.external.LineStorage

/**
 * Implement your library here. Feel free to change the class name,
 * but note that if you choose to change the class name,
 * you will need to update the import statements in GradesInitializer.kt
 * and in GradesReader.kt.
 */

class GenericGradeManager<T> {
    private val data = sortedMapOf<String, T>() // Map of id to grade
    fun insert(id: String, grade: T)// INSERT OR UPDATE IF EXISTS
    {
        data[id] = grade
    }
    fun getGrade(id: String): T? {
        return data[id]
    }

    // Retrieve all data, sorted by id
    fun getAllData(): List<Pair<String, T>> {
        return data.entries.map { it.key to it.value }
    }
    fun clear(){
        data.clear()
    }
}
class StorageLibrary {
    companion object {
        private val mappedData=GenericGradeManager<UniquelyIdentifiedStorable>() // no need for this
        private var numLines=0
        fun storeUnique(items: List<UniquelyIdentifiedStorable>) {
            numLines=items.size
            items.forEach{item->
                val id=item.getId()
                mappedData.insert(id,item)
            }
            val itemsSorted= mappedData.getAllData()
            itemsSorted.forEach { item ->
                val id = item.first
                val data = item.second
                LineStorage.appendLine(data.toStorageString()) // Add item as a string to LineStorage
            }
        }


        fun getLine(id:String): Int?{
            val dataOfId= binarySearchIterativeFromExternal(id)
            return dataOfId?.first
        }

        /**
         * this function does binarysearch in linestorage
         * @param target - id of someone that we search in file
         * @return a pair <int, string> , first = num of line found the id in it and second=data of id that has been found
         */
//        private fun  binarySearchIterativeFromExternal( target: String): Pair<Int,String>? {
//            var left = 0
//            var right=LineStorage.numberOfLines()-1
//
//            while (left <= right) {
//                val mid = left + (right - left) / 2
//                val dataString=LineStorage.read(mid)
//                val parts = dataString.split(",") // Split the string at the comma
//                val dataConverted = parts[0]
//                when {
//                    dataConverted  == target -> return Pair<Int,String>(mid,dataString) // Target found
//                    dataConverted  < target -> left = mid + 1 // Move to the right half
//                    else -> right = mid - 1 // Move to the left half
//                }
//            }
//            return null // Target not found
//        }
        private fun binarySearchIterativeFromExternal(target: String): Pair<Int, String>? {
            var left = 0
            var right = LineStorage.numberOfLines() - 1

            while (left <= right) {
                val mid = left + (right - left) / 2
                val dataString = LineStorage.read(mid)
                val parts = dataString.split(",") // Split the string at the comma
                if (parts.size < 2) {
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


        fun retrieveById(id: String):String?{
            val dataFound=binarySearchIterativeFromExternal(id)
            return dataFound?.second
        }

        fun clearStorage() {
            StorageDummy.clear()
            mappedData.clear()
        }
    }
}
