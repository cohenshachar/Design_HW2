package il.ac.technion.cs.sd.lib

interface Storable<T>{
    fun toStorageString(): String
}

interface UniquelyIdentifiedStorable<T> :  Storable<T>{

    fun toStorageStringWithoutGrade():String
    fun getIdBook(): String
    fun getIdReviewer(): String
    fun getGrade():Int
}