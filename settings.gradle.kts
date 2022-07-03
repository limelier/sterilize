rootProject.name = "sterilize"
pluginManagement {
    repositories {
        jcenter()
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }

    plugins {
        id("fabric-loom") version "0.12-SNAPSHOT"
        id("org.jetbrains.kotlin.jvm") version "1.7.0"
    }

}