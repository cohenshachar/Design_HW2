plugins {
    id("buildlogic.kotlin-common-conventions")
}

dependencies {
    testImplementation(project(":books-app"))
    //testImplementation(project(":library"))
    testImplementation("il.ac.technion.cs.sd:books-external:1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.mockito:mockito-core:3.9.0")
    testImplementation("org.mockito:mockito-junit-jupiter:3.9.0")
    implementation("org.simpleframework:simple-xml:2.7.1")

}