package il.ac.technion.cs.sd.lib

interface Storable<T>{

}

interface StorableReviews<T> : Storable<T> {
    fun getReviewersAvgScore() : Map<String , String>//v
    fun getReviewersReviews() : Map<String , List<String>>//
    fun getBooksAvgScore() : Map<String , String>// v
    fun getBooksReviewerScore() : Map<String ,  List<Pair<String, String>>> // v
}
