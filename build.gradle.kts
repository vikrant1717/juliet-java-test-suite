plugins {
    // Existing plugins
    `java-library`
    `maven-publish`
    with(Plugins.Shadow) { id(id) version (version) }

    // SonarQube plugin added here
    id("org.sonarqube") version "7.0.1.6134"
}

group = "com.github.Lipen"

// SonarQube configuration for the root project
sonar {
    properties {
        property("sonar.projectKey", "vikrant1717_juliet-java-test-suite")
        property("sonar.organization", "vikrant1717")
    }
}

subprojects {
    group = "${rootProject.group}.${rootProject.name}"
    version = rootProject.version
}

allprojects {
    // Existing project configuration
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    
    // Note: The SonarQube plugin is already applied to all projects via the top-level 'plugins' block
    // in the root build file when using the new plugin syntax, so no 'apply' is needed here.

    repositories {
        mavenCentral()
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))

        withSourcesJar()
        // withJavadocJar()
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
        repositories {
            maven(url = uri(layout.buildDirectory.dir("repository")))
        }
    }
}

dependencies {
    for (p in subprojects) {
        implementation(p)
    }
}

tasks.wrapper {
    gradleVersion = "8.3"
    distributionType = Wrapper.DistributionType.ALL
}
