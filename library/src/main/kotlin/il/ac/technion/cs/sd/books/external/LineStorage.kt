package il.ac.technion.cs.sd.books.external
import dev.misfitlabs.kotlinguice4.KotlinModule
import com.google.inject.Inject
import com.google.inject.Singleton
import il.ac.technion.cs.sd.books.dummy.StorageDummyFiles
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

// LineStorage Interfaces and Implementations
interface LineStorageFactory {
    fun open(filename: String): LineStorage
}

interface LineStorage {
    fun appendLine(line : String)
    fun read(lineNumber: Int): String
    fun numberOfLines(): Int
}


object LineStorageFactoryImpl : LineStorageFactory {
    private val filesMap = mutableMapOf<String, LineStorage>()

    override fun open(filename: String): LineStorage {
        if (filesMap.size > 13) throw IllegalStateException("Too many files opened.")
        Thread.sleep(filesMap.size * 100L)
        return filesMap.getOrPut(filename) { LineStorageImpl(filename) }
    }
}
/*
@Singleton
class LineStorageFactoryImpl @Inject constructor() : LineStorageFactory {
    private val filesMap: MutableMap<String, LineStorage> = mutableMapOf()

    override fun open(filename: String): LineStorage {
        if (filesMap.size > 13) {
            throw IllegalStateException("Too many files opened...check implementation")
        }
        val delay = filesMap.size * 100 // 100ms per file
        Thread.sleep(delay.toLong())
        return filesMap.getOrPut(filename) { LineStorageImpl(filename) }
    }
}

*/
class LineStorageImpl(private val filename: String) : LineStorage {
    override fun appendLine(line: String) {
        StorageDummyFiles.getOrCreateStorage(filename).append(line)
    }

    override fun read(lineNumber: Int): String {
        val result = StorageDummyFiles.getOrCreateStorage(filename).get(lineNumber) ?: ""
        Thread.sleep(result.length.toLong())
        return result
    }

    override fun numberOfLines(): Int {
        Thread.sleep(100)
        return StorageDummyFiles.getOrCreateStorage(filename).size()
    }
}

// Guice Module
class LineStorageModule : KotlinModule() {
    override fun configure() {
        bind(LineStorageFactory::class.java).toInstance(LineStorageFactoryImpl)

    }
}




// for testing .......
/*
//
class TestLineStorageFactory : LineStorageFactory {
    private val files = mutableMapOf<String, LineStorage>()

    override fun open(filename: String): LineStorage {
        return files.getOrPut(filename) {
            object : LineStorage {
                private val lines = mutableListOf<String>()
                override fun appendLine(line: String) { lines.add(line) }
                override fun read(lineNumber: Int): String = lines.getOrElse(lineNumber) { "" }
                override fun numberOfLines(): Int = lines.size
            }
        }
    }
}






class TestLineStorageModule : AbstractModule() {
    override fun configure() {
        bind(LineStorageFactory::class.java).to(TestLineStorageFactory::class.java)
    }
}
*/