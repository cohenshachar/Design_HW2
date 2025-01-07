plugins {
    id("buildlogic.kotlin-application-conventions")
}
dependencies {
    api(project(":library"))
    implementation("org.simpleframework:simple-xml:2.7.1")
}
