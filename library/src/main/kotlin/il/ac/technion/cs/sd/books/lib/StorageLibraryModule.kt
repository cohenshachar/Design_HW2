package il.ac.technion.cs.sd.books.lib

import com.google.inject.Singleton
import com.google.inject.TypeLiteral
import dev.misfitlabs.kotlinguice4.KotlinModule
import il.ac.technion.cs.sd.books.external.LineStorageFactory
import il.ac.technion.cs.sd.books.external.LineStorageFactoryImpl
import il.ac.technion.cs.sd.lib.StorableReviews

class StorageLibraryModule : KotlinModule() {
    override fun configure() {
        bind(object : TypeLiteral<StorageLibrary<StorableReviews>>() {}).to(object : TypeLiteral<StorageLibraryImpl<StorableReviews>>() {})
        bind(LineStorageFactory::class.java).toInstance(LineStorageFactoryImpl)
    }
}