package io.micro.starter

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.info.License

@QuarkusMain
class Application : QuarkusApplication {
    override fun run(vararg args: String): Int {
        println("Tokisaki 启动成功！")
        Quarkus.waitForExit()
        return 0
    }
}

fun main(args: Array<String>) {
    Quarkus.run(*args)
}

@OpenAPIDefinition(
    info = Info(
        title = "Tokisaki API",
        version = "1.0.0",
        contact = Contact(
            name = "Tokisaki API Support",
            url = "https://spcookie.github.io/",
            email = "spcokie@qq.com"
        ),
        license = License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"
        )
    )
)
class Api : jakarta.ws.rs.core.Application()