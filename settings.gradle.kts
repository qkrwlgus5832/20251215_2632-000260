rootProject.name = "notifiaction-front-server"

include(
    "domain",
    "application:service",
    "application:notificationKafkaConsumer",
    "application:scheduler",
    "ui:api",
    "infra",
    "client:notificationSender"
)

makeProjectDir(rootProject, "subprojects")

fun makeProjectDir(project: ProjectDescriptor, group: String) {
    project.children.forEach {
        println("$group -> ${it.name}")

        it.projectDir = file("$group/${it.name}")
        makeProjectDir(it, "$group/${it.name}")
    }
}