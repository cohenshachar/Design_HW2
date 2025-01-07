package il.ac.technion.cs.sd.books.app
import com.google.inject.Inject
import il.ac.technion.cs.sd.books.modules.Parser
import il.ac.technion.cs.sd.lib.Storable
import il.ac.technion.cs.sd.lib.StorageLibrary

interface BookScoreInitializer {
    /** Saves the XML data persistently, so that it could be queried using BookScoreReader */
    fun setup(xmlData: String)
}
class BookScoreInitializerImpl<T : Storable<T>> @Inject constructor(
    private val parser: Parser<T>,
    private val storageLib: StorageLibrary<T>
) : BookScoreInitializer {
    /** Saves the XML data persistently, so that it could be queried using BookScoreReader */
    override fun setup(xmlData: String) {
        val data: T = parser.parse(xmlData)
        println(data)
        //storageLib.saveRoot(root) // Save the unified reviewers
    }
}
