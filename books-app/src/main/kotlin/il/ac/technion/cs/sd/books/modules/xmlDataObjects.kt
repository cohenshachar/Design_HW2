package il.ac.technion.cs.sd.books.modules

import il.ac.technion.cs.sd.lib.Storable
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.core.Commit
import org.simpleframework.xml.core.Persister


interface Parser<T : Storable<T>> {
    fun parse(xml: String): T
}
class XmlParser : Parser<xmlRoot> {
    override fun parse(xml: String): xmlRoot {
        val serializer = Persister()
        return serializer.read(xmlRoot::class.java, xml)
    }
}
@Root(name = "Root")
data class xmlRoot(
    @field:ElementList(name = "Reviewer", inline = true)
    var reviewers: MutableList<Reviewer> = mutableListOf()
) : Storable<xmlRoot> {
    @Commit
    fun afterDeserialization() {
        unifyReviews()
    }
    private fun unifyReviews() {
        val reviewerMap = mutableMapOf<String, Reviewer>()
        reviewers.asReversed().asSequence().forEach { reviewer ->
            val uniqueReviewer = reviewerMap.getOrPut(reviewer.id) { Reviewer(reviewer.id, reviewer.reviews.toMutableList()) }
            uniqueReviewer.reviews.addAll(reviewer.reviews.asSequence().filter { newReview -> uniqueReviewer.reviews.none { it.id == newReview.id } })
        }
        reviewers = reviewerMap.values.toMutableList()
    }

    override fun toStorageString(): String {
        TODO("Not yet implemented")
    }
}

@Root(name = "Reviewer")
data class Reviewer(
    @field:Element(name = "Id", required = false)
    var id: String = "",

    @field:ElementList(name = "Review", inline = true)
    var reviews: MutableList<Review> = mutableListOf()
)

@Root(name = "Review")
data class Review(
    @field:Element(name = "Id")
    var id: String = "",

    @field:Element(name = "Score")
    var score: Int = 0
)
