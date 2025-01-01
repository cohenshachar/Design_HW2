plugins {
    id("buildlogic.kotlin-common-conventions")
}

dependencies {
    testImplementation(project(":books-app"))
    testImplementation(project(":library"))
    testImplementation("il.ac.technion.cs.sd:books-external:1.0")
}