rootProject.name = "GShelter"

val REPLACE_WITH_LOCAL = false

if (REPLACE_WITH_LOCAL) {
    includeBuild("../geshikt/geshikt") {
        dependencySubstitution {
            // substitute remote dependency with local module
            substitute(module("io.github.sikrinick:geshikt"))
                .using(project(":"))
        }
    }
}
