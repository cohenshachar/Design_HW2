package il.ac.technion.cs.sd.lib

interface Storable {
    fun toStorageString(): String
}

interface UniquelyIdentifiedStorable :  Storable{

    fun toStorageStringWithoutGrade():String
    fun getIdBook(): String
    fun getIdReviewer(): String
    fun getGrade():Int




}