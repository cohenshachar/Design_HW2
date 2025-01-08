package il.ac.technion.cs.sd.books.app

import dev.misfitlabs.kotlinguice4.KotlinModule
import com.google.inject.TypeLiteral
import il.ac.technion.cs.sd.books.external.LineStorageModule
import il.ac.technion.cs.sd.books.modules.*
import il.ac.technion.cs.sd.lib.StorableReviews


class BookScoreModule : KotlinModule() {
    override fun configure() {
        install(XmlModule())
        install(LineStorageModule())
        bind(BookScoreInitializer::class.java).to(object : TypeLiteral<BookScoreInitializerImpl<StorableReviews>>() {})
        bind(BookScoreReader::class.java).to(object : TypeLiteral<BookScoreReaderImpl<StorableReviews>>() {})
    }
}

