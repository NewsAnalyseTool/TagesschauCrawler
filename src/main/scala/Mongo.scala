import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import play.api.libs.json.{JsObject, JsValue}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Mongo extends App {
  // MongoDB-Verbindungsdaten
  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017/")
  val database: MongoDatabase = mongoClient.getDatabase("news_crawler")
  val collection: MongoCollection[JsValue] = database.getCollection("tagesschau_crawler")

  // JSON-Dokument, das in die Collection geschrieben werden soll
  val jsVal = Crawler.getTagesschauNewsPageApi("https://tagesschau.de/api2/news/")


  // Überprüfen, ob das Dokument bereits in der Collection vorhanden ist
  val title: String = jsVal.result("title").toString()
  val date: String =  jsVal.result("date").toString()

  val existingDocumentObservable = collection.find(
    and(
      equal("title", title),
      equal("date",date),
    )
  ).limit(1)

  val existingDocument = Await.result(existingDocumentObservable.toFuture(), Duration.Inf)

  if (existingDocument.isEmpty) {
    // Das Dokument ist noch nicht in der Collection vorhanden, füge es hinzu
    val insertObservable = collection.insertOne(jsVal)

    Await.result(insertObservable.toFuture(), Duration.Inf)

    println("Document inserted successfully")
    println(jsVal)
  }else{
    println("Document already in database")
  }

  // Schließe die Verbindung zur MongoDB
  mongoClient.close()
}
