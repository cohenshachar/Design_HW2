package il.ac.technion.cs.sd.lib

interface Storable<T>{
}

interface StorableReviews<T> : Storable<T> {
    fun getReviewersAvgScore() : Map<String , String>
    fun getReviewersReviews() : Map<String , List<String>>
    fun getBooksAvgScore() : Map<String , String>
    fun getBooksReviewerScore() : Map<String ,  List<Pair<String, String>>>
}
