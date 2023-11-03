
import play.api.libs.json
import play.api.libs.json.{JsArray, Json, Writes}
import requests._

case class TagesschauData(title: String, date: String, text: String)

object Crawler {
  // Define an implicit Writes for TagesschauData
  implicit val tagesschauDataWrites: Writes[TagesschauData] = Json.writes[TagesschauData]

  def main(args: Array[String]): Unit = {
    // Send a GET request and parse the JSON response
    val response: requests.Response = requests.get("https://tagesschau.de/api2/news/")
    val jsonInput = Json.parse(response.text)

    // Extract the "news" array from the JSON
    val newsArray = (jsonInput \ "news").as[JsArray]

   if (newsArray.value.nonEmpty) {
      // Iterate over each news item and create JSON objects
      val newsObjects = newsArray.value.map { newsItem =>
        val title = (newsItem \ "title").as[String]
        val date = (newsItem \ "date").as[String]
        val text = (newsItem\ "firstSentence").as[String]
        TagesschauData(title, date, text)
      }

      // Convert the list of TagesschauData objects to a JSON array
      val jsonOutput = Json.toJson(newsObjects)

      // Convert the new JSON object to a string
      val outputJsonString = Json.stringify(jsonOutput)

      println(outputJsonString)
    } else {
      println("No news data found in the JSON response.")
    }

    //get relevant data from Json object

  }

  def extractText(string: String): String={
  "Hello World"
  }
}