package com.uniovi;

import java.text.ParseException;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class MongoDBInsertion {
	private MongoClient mongoClient; 
	private MongoDatabase mongodb; 

	public void connectDatabase() {
		try {
			setMongoClient(new MongoClient(new MongoClientURI(
					"mongodb://uo257947:uo257947@sdi-actividad2-606-608-shard-00-00-na9yb.mongodb.net:27017,sdi-actividad2-606-608-shard-00-01-na9yb.mongodb.net:27017,sdi-actividad2-606-608-shard-00-02-na9yb.mongodb.net:27017/test?ssl=true&replicaSet=sdi-actividad2-606-608-shard-0&authSource=admin&retryWrites=true")));
			setMongodb(getMongoClient().getDatabase("test"));
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	
	public void insertOneDataTest() throws ParseException {
		try {
			MongoCollection<Document> col = getMongodb().getCollection("usuarios");
			col.insertOne(new Document().append("nombre", "admin").append("apellido", "admin")
					.append("email", "admin@email.com")
					.append("password", "ebd5359e500475700c6cc3dd4af89cfd0569aa31724a1bf10ed1e3019dcfdb11")
					.append("rol", "admin").append("dinero", 100));
			col.insertOne(new Document().append("nombre", "Fernando").append("apellido", "Ruiz Berciano")
					.append("email", "ruizbolyt@gmail.com")
					.append("password", "6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a")
					.append("rol", "usuario").append("dinero", 100));
			col.insertOne(new Document().append("nombre", "prueba1").append("apellido", "prueba1")
					.append("email", "prueba1@prueba1.com")
					.append("password", "57420b1f0b1e2d07e407a04ff8bbc205a57b3055b34ed94658c04ed38f62daa7")
					.append("rol", "usuario").append("dinero", 100));
			col.insertOne(new Document().append("nombre", "Prueba2").append("apellido", "prueba2")
					.append("email", "prueba2@prueba2.com")
					.append("password", "6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4")
					.append("rol", "usuario").append("dinero", 100));
			col.insertOne(new Document().append("nombre", "Javier").append("apellido", "Ruiz Berciano")
					.append("email", "prueba@prueba.com")
					.append("password", "6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a")
					.append("rol", "usuario").append("dinero", 100));
			col = getMongodb().getCollection("ofertas");			
			col.insertOne(new Document().append("nombre", "Bicicleta").append("detalle", "De carretera")
						.append("precio", "100")
						.append("autor", "ruizbolyt@gmail.com")
						.append("fecha", "2019-05-03T09:41:32.048+00:00"));
			
			col.insertOne(new Document().append("nombre", "pruebaruiz").append("detalle", "prueba hecha por ruiz")
					.append("precio", "8.89")
					.append("autor", "ruizbolyt@gmail.com")
					.append("fecha", "2019-05-02T09:41:32.048+00:00"));
			
			col.insertOne(new Document().append("nombre", "Prueba").append("detalle", "Esto es una prueba")
					.append("precio", "2.5")
					.append("autor", "prueba1@prueba1.com")
					.append("fecha", "2019-05-01T09:41:32.048+00:00"));
			
			col.insertOne(new Document().append("nombre", "Pablo").append("detalle", "Esto es una prueba")
					.append("precio", "10")
					.append("autor", "prueba1@prueba1.com")
					.append("fecha", "2019-05-06T09:41:32.048+00:00"));
			
			col.insertOne(new Document().append("nombre", "Prueba2").append("detalle", "Esto es una prueba2")
					.append("precio", "3.5")
					.append("autor", "prueba2@prueba2.com")
					.append("fecha", "2019-05-05T09:41:32.048+00:00"));
		} catch (Exception ex) {
			System.out.print(ex.toString());
		}

	}

	public void removeDataTest() {
		getMongodb().getCollection("ofertas").drop();
		getMongodb().getCollection("usuarios").drop();
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public MongoDatabase getMongodb() {
		return mongodb;
	}

	public void setMongodb(MongoDatabase mongodb) {
		this.mongodb = mongodb;
	}

	
	public void insertData() throws ParseException {
		MongoDBInsertion javaMongodbInsertData = new MongoDBInsertion();
		System.out.println("Conectando a la base");
		javaMongodbInsertData.connectDatabase();
		System.out.println("Eliminando la base");
		javaMongodbInsertData.removeDataTest();
		System.out.println("Insertando en la base");
		javaMongodbInsertData.insertOneDataTest();
	}
}