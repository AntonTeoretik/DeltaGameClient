plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "com.delta"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    val ktxVersion="1.11.0-rc5"
    val ashleyVersion="1.7.4"
    val box2dlightsVersion="1.5"
    val aiVersion="1.8.2"
    val controllerScene2DVersion="2.3.0"
    val dialogsVersion="1.3.0"
    val digitalVersion="0.1.8"
    val anim8Version="0.3.12"
    val gdxVersion="1.11.0"


    dependencies {
        api("com.badlogicgames.ashley:ashley:$ashleyVersion")
        api("com.badlogicgames.box2dlights:box2dlights:$box2dlightsVersion")
        api("com.badlogicgames.gdx:gdx-ai:$aiVersion")
        api("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
        api("com.badlogicgames.gdx:gdx:$gdxVersion")
        api("com.github.tommyettinger:anim8-gdx:$anim8Version")
        api("com.github.tommyettinger:digital:$digitalVersion")
        api("de.golfgl.gdxcontrollerutils:gdx-controllerutils-scene2d:$controllerScene2DVersion")
        api("de.tomgrill.gdxdialogs:gdx-dialogs-core:$dialogsVersion")
        api("io.github.libktx:ktx-app:$ktxVersion")
        api("io.github.libktx:ktx-assets:$ktxVersion")
        api("io.github.libktx:ktx-graphics:$ktxVersion")

        implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
        implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
        implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}