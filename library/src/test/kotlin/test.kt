package il.ac.technion.cs.sd.lib

import il.ac.technion.cs.sd.books.external.LineStorageFactory
import il.ac.technion.cs.sd.books.lib.StorageLibrary
import org.junit.jupiter.api.*
import il.ac.technion.cs.sd.books.modules.XmlRoot

import com.google.inject.Inject
import com.google.inject.Guice
import com.google.inject.AbstractModule
import dev.misfitlabs.kotlinguice4.KotlinModule
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach



// added these
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
// fun getReviewersReviews() : Map<String , List<String>>

class MockItem(private val id: String, private val data: String, private val line: Int) : Storable<MockItem> {
    fun getId(): String = id
    fun toStorageString(): String = "$id,$data,$line"
}

class StorageLibraryTest {

    @Inject lateinit var storageLibrary: StorageLibrary<MockItem>
    @Inject lateinit var lineStorageFactory: LineStorageFactory
    @Inject lateinit var mockItem: MockItem
    private lateinit var mockStorageLibrary: StorageLibrary<MockItem>

    @BeforeEach
    fun setUp() {
        // Initialize Guice Injector

        val injector = Guice.createInjector(TestModule())
        injector.injectMembers(this)  // Inject dependencies
        mockStorageLibrary = mockk(relaxed = true)  // Mock the StorageLibrary

        // we must clear storage ..? TODO::
    }

    @Test
    fun storeUniqueShouldAddItemsToTheStorage() {
        every { mockStorageLibrary.store(mockItem) } answers { }

        mockStorageLibrary.store(mockItem)

        verify { mockStorageLibrary.store(mockItem) }
    }

    @Test
    fun getLineShouldReturnTheCorrectLineNumberForAGivenId() {
        every { mockStorageLibrary.store((mockItem)) } answers { }
        every { mockStorageLibrary.getLine("1") } returns 1

        mockStorageLibrary.store(listOf(mockItem))
        val line = mockStorageLibrary.getLine("1")
        assertNotNull(line)
        assertEquals(1, line)
    }

    @Test
    fun retrieveByIdShouldReturnNullIfItemIsNotFound() {

        every { mockStorageLibrary.getBookReviewScoreBy("21","FOO")} returns null
            //  every { mockStorageLibrary.retrieveById("zxc21zxcsda") } returns null

       // val result = mockStorageLibrary.retrieveById("21")
       // val result1 = mockStorageLibrary.retrieveById("zxc21zxcsda")


    }

    @Test
    fun clearStorageShouldRemoveAllItemsFromStorage() {
        every { mockStorageLibrary.clearStorage() } answers { }

        mockStorageLibrary.clearStorage()

        verify { mockStorageLibrary.clearStorage() }
    }


}


class TestModule : KotlinModule() {
    override fun configure() {
        bind(StorageLibrary::class.java).toInstance(mockk(relaxed = true))
        bind(LineStorageFactory::class.java).toInstance(mockk(relaxed = true))
        bind(MockItem::class.java).toInstance(MockItem("1", "Dddd", 10)) // Bind MockItem to its instance
    }
}

/*
class GenericGradeManagerTest {

    @Test
    fun testInsertNewEntry() {
        val manager = GenericGradeManager<String>()
        manager.insert("1", "A")
        assertEquals("A", manager.getGrade("1"))
    }

    @Test
    fun testInsertUpdateExistingEntry() {
        val manager = GenericGradeManager<String>()
        manager.insert("1", "A")
        manager.insert("1", "B")
        assertEquals("B", manager.getGrade("1"))
    }

    @Test
    fun testGetGradeForNonExistingId() {
        val manager = GenericGradeManager<String>()
        assertNull(manager.getGrade("999"))
    }

    @Test
    fun testGetAllDataEmpty() {
        val manager = GenericGradeManager<String>()
        val allData = manager.getAllData()
        assertTrue(allData.isEmpty())
    }

    @Test
    fun testGetAllDataSorted() {
        val manager = GenericGradeManager<String>()
        manager.insert("2", "B")
        manager.insert("1", "A")
        manager.insert("3", "C")

        val expected = listOf(
            "1" to "A",
            "2" to "B",
            "3" to "C"
        )

        assertEquals(expected, manager.getAllData())
    }

    @Test
    fun testInsertWithDifferentGenericTypes() {
        val intManager = GenericGradeManager<Int>()
        intManager.insert("1", 100)
        assertEquals(100, intManager.getGrade("1"))

        val doubleManager = GenericGradeManager<Double>()
        doubleManager.insert("1", 99.9)
        assertEquals(99.9, doubleManager.getGrade("1"))

        val stringManager = GenericGradeManager<String>()
        stringManager.insert("1", "Excellent")
        assertEquals("Excellent", stringManager.getGrade("1"))
    }
}*/
