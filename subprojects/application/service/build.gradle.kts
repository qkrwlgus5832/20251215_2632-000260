dependencies {
    api(project(":domain"))
    api(project(":client:notificationSender"))
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