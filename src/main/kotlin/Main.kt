import com.delta.AppConfig
import com.delta.Application

fun main(args: Array<String>) {
    val serverAddress = "http://192.168.178.48"
    //val playersName = args[0]

    val app = Application(AppConfig("A", serverAddress))
    app.start()
}