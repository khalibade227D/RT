pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        dependencyResolutionManagement {
            repositories {
                google()
                mavenCentral()
                maven(url = "https://jitpack.io") // ✅ JitPack repo
            }
        }

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {url = uri ("https://jitpack.io" )}
    }
}

rootProject.name = "RT"
include(":app")
