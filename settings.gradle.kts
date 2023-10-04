pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DSA Visulizer"
include(":app")
include(":tree_visualization")
include(":common_ui")
include(":graph_visualization")
include(":linear_datastructure")
include(":graph_editor")
