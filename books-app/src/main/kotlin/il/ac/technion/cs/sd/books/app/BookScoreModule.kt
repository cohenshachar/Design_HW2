package il.ac.technion.cs.sd.books.app

import dev.misfitlabs.kotlinguice4.KotlinModule
import com.google.inject.Singleton
import com.google.inject.TypeLiteral
import il.ac.technion.cs.sd.books.modules.Parser
import il.ac.technion.cs.sd.books.modules.XmlParser
import il.ac.technion.cs.sd.books.modules.XmlRoot

class BookScoreModule : KotlinModule() {
    override fun configure() {
        bind(BookScoreInitializer::class.java).to(BookScoreInitializerImpl::class.java)
        bind(BookScoreReader::class.java).to(BookScoreReaderImpl::class.java)
        bind(object : TypeLiteral<Parser<XmlRoot>>() {}).to(XmlParser::class.java).`in`(Singleton::class.java)
        //  bind(object : TypeLiteral<StorageLibrary<xmlRoot>>() {}).to(StorageLibraryImpl::class)
    }
}


