import com.typesafe.config._
import org.slf4j.{Logger, LoggerFactory}

object HttpGet {
  // Create logger
  val logger: Logger = LoggerFactory.getLogger(getClass)

    // Load custom configurations
    val config = ConfigFactory.load()
    val inputDate = config.getString("parameters.inputDate") // Input date
    val inputTime = config.getString("parameters.inputTime") // Input time
    val deltaTime = config.getString("parameters.deltaTime") // Time difference between input time and initial time. Time difference between input time and end time.
    val lambdaUri = config.getString("parameters.uri") // URI for lambda function
    val pattern = config.getString("parameters.pattern") // Pattern to search

  logger.info(s"Loaded configuration with parameters: inputDate = $inputDate, inputTime = $inputTime, deltaTime = $deltaTime and with uri = $lambdaUri")


  def main(args: Array[String]): Unit = {
    // URI for GET Request
    val uri = s"$lambdaUri?input_date=$inputDate&input_time=$inputTime&delta_time=$deltaTime"

    logger.info("Sending GET request.")
    // Creates Source from file with given file
    val responseBody = scala.io.Source.fromURL(uri).mkString

    // Print response
    println(responseBody)

  }


}

