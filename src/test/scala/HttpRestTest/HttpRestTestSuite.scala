package HttpRestTest

import org.scalatest.funspec.AnyFunSpec
import com.typesafe.config.ConfigFactory


class HttpRestTestSuite extends AnyFunSpec {
  val config = ConfigFactory.load()

  // Configuration file test
  describe("Config file") {
    it("should be present") {
      assert(!config.isEmpty)
    }
  }

  // Configurations test
  describe("Config parameters"){
    it("should have input date for the POST request") {
      assert(! config.getString("parameters.inputDate").isEmpty)
    }
    it("should have input time for the POST request") {
      assert(! config.getString("parameters.inputTime").isEmpty)
    }
    it("should have delta time for the POST request") {
      assert(! config.getString("parameters.deltaTime").isEmpty)
    }
    it("should have uri for the POST request") {
      assert(! config.getString("parameters.uri").isEmpty)
    }
  }


  // Test GET method with sample data
    describe("GET request Test 1") {
      it("should contain the known number of log messages.") {
        // Load custom configurations
        val inputDate = "2021-11-01"
        val inputTime = "19:41:12.024"
        val deltaTime = 1
        val uri = s"https://3ekrhlf0g0.execute-api.us-east-2.amazonaws.com/default/lambda_grpc?input_date=$inputDate&input_time=$inputTime&delta_time=$deltaTime"

        // Creates Source from file with given file
        val responseBody = scala.io.Source.fromURL(uri).mkString

        assert(responseBody == "154")
      }
    }

  // Test GET method with sample data
  describe("GET request Test 2") {
    it("should contain the known number of log messages.") {
      // Load custom configurations
      val inputDate = "2021-11-01"
      val inputTime = "19:41:12.024"
      val deltaTime = 0
      val uri = s"https://3ekrhlf0g0.execute-api.us-east-2.amazonaws.com/default/lambda_grpc?input_date=$inputDate&input_time=$inputTime&delta_time=$deltaTime"

      // Creates Source from file with given file
      val responseBody = scala.io.Source.fromURL(uri).mkString

      assert(responseBody == "9")
    }
  }
}
