buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id ("com.google.dagger.hilt.android") version "2.48" apply false

//    id("com.android.application") version "8.1.2" apply false
//    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
//    // when we use kotlin 1.9.0 then we have to use kas version between 1.9.0-1.13
//    id ("com.google.dagger.hilt.android") version "2.50" apply false
//    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
//    // if we have to use ksp with kotlin then we have to use kotlin version 1.9.0


}