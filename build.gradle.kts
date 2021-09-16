import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.spongepowered.gradle.plugin") version "2.0.0"
}

project.group = "dev.divinegenesis"

repositories {
    mavenCentral()
}

sponge {
    apiVersion("8.0.0-SNAPSHOT")
    license("CHANGEME")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("soulbound") {
        displayName("Soulbound")
        entrypoint("dev.divinegenesis.soulbound.Soulbound")
        description("Bind items to users soul")
        version("0.3.0")
        links {
            homepage("https://github.com/DivineGenesis/BetterSoulBinding")
            source("https://github.com/DivineGenesis/BetterSoulBinding")
            issues("https://github.com/DivineGenesis/BetterSoulBinding/issues")
        }
        contributor("DrZodd") {
            description("Dev")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
        dependency("spotlin") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
            version("0.3.0")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.spongepowered:configurate-extra-kotlin:4.1.1")
}

tasks {
    val java = "15" //16 bugs out
    compileKotlin {
        kotlinOptions { jvmTarget = java }
        sourceCompatibility = java
    }
}
