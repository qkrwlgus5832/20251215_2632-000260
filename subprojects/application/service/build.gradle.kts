dependencies {
    api(project(":domain"))
    api(project(":client:notification-sender"))
    implementation(project(":infra"))
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}