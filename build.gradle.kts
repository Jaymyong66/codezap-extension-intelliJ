plugins {
    java
    id("org.jetbrains.intellij") version "1.0"
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
}

intellij {
    version.set("2021.1")
    plugins.set(listOf("java"))
}

tasks {
    patchPluginXml {
        changeNotes.set("Initial version.")
    }

    runIde {
        ideDir.set(file("/Applications/IntelliJ IDEA.app/Contents"))
    }
}
