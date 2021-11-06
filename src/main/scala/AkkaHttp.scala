import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.duration._
import com.typesafe.config._
import org.slf4j.{Logger, LoggerFactory}
import scala.util.{ Failure, Success }

object AkkaHttp {

  // Create logger
  val logger: Logger = LoggerFactory.getLogger(getClass)


  // AkkaHttp needs actor system and actor materializer. Foundation for AkkaHttp to run.
  // Actors are the unit of execution in Akka. The Actor model is an abstraction that makes it easier to write correct concurrent, parallel and distributed systems.
  implicit val system = ActorSystem() // AkkaSytem is a home to hierarchy of actors.
  implicit val materializer = ActorMaterializer() // Akka Streams. Powerful streaming based framework.
  import system.dispatcher //thread pool - to achieve concurrency
  logger.info("Created ActorSystem and ActorMaterializer instances.")

  // Load custom configurations
  val config = ConfigFactory.load()
  val inputDate = config.getString("parameters.inputDate") // Input date
  val inputTime = config.getString("parameters.inputTime") // Input time
  val deltaTime = config.getString("parameters.deltaTime") // Time difference between input time and initial time. Time difference between input time and end time.
  val lambdaUri = config.getString("parameters.uri") // URI for lambda function
  val pattern = config.getString("parameters.pattern") // Pattern to search
  logger.info(s"Loaded configuration with parameters: inputDate = $inputDate, inputTime = $inputTime, deltaTime = $deltaTime and with uri = $lambdaUri")

  val source =
    s"""
      |{
      |    "input_date": "$inputDate",
      |    "input_time": "$inputTime",
      |    "delta_time": $deltaTime,
      |    "pattern" : "$pattern"
      |}
      |""".stripMargin // JSON format data


  // HTTP request
  val request = HttpRequest(
    // Arguments
    method = HttpMethods.POST, // HTTP Method
    uri = lambdaUri, // Uniform Resource Identifier
    entity = HttpEntity(
      ContentTypes.`application/json`,  // Content type - as per the description specified in the API
      source
    )   // Payload (Data)
  )
  logger.info("Created HttpRequest instance.")


/*
  Obtains a Future containing the response. Unpacks the Future. Converts it into strict entity (whole content).
 */
  def sendRequest(): Future[String] = {
//  def sendRequest(): Unit = {
    // Create a future object containting the response. Future represents the result of an asynchronous operation
    val responseFuture : Future[HttpResponse] = Http().singleRequest(request)


    // Unpack the future. Convert into a strict entity. Entity contains the whole content of the response.
    val entityFuture : Future[HttpEntity.Strict] = responseFuture.flatMap(response => response.entity.toStrict(2.seconds))

    // Data is in sequence of bytes. Convert it into String.
    entityFuture.map(entity => entity.data.utf8String)
  }

 /*
  Main method. Execution begins here.
  */
  def main(args: Array[String]): Unit = {
    logger.info("Sending POST request.")
    sendRequest().foreach(println)
//    sendRequest()

  }
}

// Skeleton code from - https://dzone.com/articles/sending-http-requests-in-5-mins-with-scala-and-akk