import org.mongodb.scala._
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import play.api.libs.json._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Mongo extends App {
  // MongoDB-Verbindungsdaten
  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017/")
  val database: MongoDatabase = mongoClient.getDatabase("news_crawler")
  val collection: MongoCollection[BsonDocument] = database.getCollection("tagesschau_crawler")

  // JSON-Dokument, das in die Collection geschrieben werden soll
  val jsVal = Crawler.getTagesschauNewsPageApi("https://tagesschau.de/api2/news/")


  // JsValue in ein JsArray umwandeln
  val jsonArray: JsResult[JsArray] = jsVal.validate[JsArray]

  // Überprüfen, ob die Validierung erfolgreich war
  jsonArray.fold(
    errors => {
      // Fehlerbehandlung, falls die Validierung fehlschlägt
      println(s"Fehler beim Parsen des JsValue: $errors")
    },
    jsArray => {
      // JsArray wurde erfolgreich extrahiert
      jsArray.value.foreach { jsEntry =>
        // Überprüfen, ob das Dokument bereits in der Collection vorhanden ist
        //Wandle den JsValue in ein BsonDoc um
        val bsonDocument: BsonDocument = BsonDocument.apply(Json.stringify(jsEntry))
        val title = bsonDocument.get("Title")
        val date = bsonDocument.get("Timestamp")
        val existingDocumentObservable = collection.find(
          and(
            equal("Title", title),
            equal("Timestamp", date)
          )
        ).limit(1)
        val existingDocument = Await.result(existingDocumentObservable.toFuture(), Duration.Inf)

        // falls das Dokument  noch nicht in der Collection vorhanden ist, füge es hinzu
        if (existingDocument.isEmpty) {
          //Schreibe den eintrag in die Datenbank
          val insertObservable = collection.insertOne(bsonDocument)
          Await.result(insertObservable.toFuture(), Duration.Inf)

          println("Document inserted successfully")
        } else {
          println("Document already in database")
        }

      }
    }
  )

  // Schließe die Verbindung zur MongoDB
  mongoClient.close()
}
