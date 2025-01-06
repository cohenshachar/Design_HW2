package il.ac.technion.cs.sd.grades.external
import il.ac.technion.cs.sd.dummy.StorageDummy
import il.ac.technion.cs.sd.dummy.StorageDummyFiles
import kotlin.system.measureTimeMillis

/**
 * This package and class override the external library
 * which was automatically imported to the project (you can view it under
 * the 'External Libraries' directory). This is NOT good practice but
 * was required for this assignment. In the following assignments
 * we will be using a different mechanism to achieve this behavior.
 *
 * You should implement this class for testing your library implementation.
 * Make sure it enforces the timing restrictions specified in the assignment PDF.
 * Note that your implementation will be overridden by staff code.
 * IMPORTANT: 1) DO NOT in any way alter the API of this class.
 *            2) DO NOT create any files under the package defined at the top of this file.
 */

interface LineStorageFactory {
    fun open(filename: String): LineStorage
}

class LineStorageFactoyImpl:LineStorageFactory{

    private val filesMap: MutableMap<String,LineStorage> = mutableMapOf()

    override fun open(filename:String):LineStorage{
        if(filesMap.size > 12)//TODO::EXCEPTION ..?
        val towait=(filesMap.size)*100// 100 for every file we created before
        Thread.sleep(towait.toLong())
        return filesMap.getOrPut(filename) { LineStorageImp(filename) }

    }

}

// Interface for line storage operations
interface LineStorage {
    fun appendLine(line: String)
    fun read(lineNumber: Int): String
    fun numberOfLines(): Int
}



class LineStorageImp(private val filename: String):LineStorage {
    // TODO:: IN THE PDF ITS CALLED INTERFACE INESTORAGE ... WE DID NOT FIND IT IN THE FILES

        /** Appends a line to the END of the file */
        override  fun appendLine(line: String) {

            StorageDummyFiles.getOrCreateStorage(filename).append(line)
        }

        /** Returns the line at index lineNumber (0-indexed) */
        override   fun read(lineNumber: Int): String {

            var toRtn=""
            val elapsed = measureTimeMillis {
                  toRtn =  StorageDummyFiles.getOrCreateStorage(filename).get(lineNumber) ?: ""
                val towait=(toRtn.length)
            //    println("Executing some task...")
                Thread.sleep(towait.toLong()) // Example task that takes time
            }
          //  println("took..." + elapsed)
            return toRtn
        }

        /** Returns the total number of lines in the file */
        override   fun numberOfLines(): Int {
            val elapsed = measureTimeMillis {

             //   println("Executing some task...")
                Thread.sleep(100) // Example task that takes time
            }

        //    println("Done!after "+elapsed+"millis")
            return StorageDummyFiles.getOrCreateStorage(filename).size()
        }

}
