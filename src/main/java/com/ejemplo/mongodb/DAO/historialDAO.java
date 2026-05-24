package com.ejemplo.mongodb.DAO;

import com.ejemplo.mongodb.MongoDBUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;

public class historialDAO {

    private final MongoCollection<Document> collection;

    public historialDAO() {
        MongoDatabase db = MongoDBUtil.getDatabase();
        collection = db.getCollection("historial_acciones");
    }

    public void guardarAccion(String nombreUsuario, String accion, String descripcion, Document detalles) {

        Document historial = new Document();
        historial.append("nombre_usuario", nombreUsuario);
        historial.append("accion", accion);
        historial.append("descripcion", descripcion);
        historial.append("fecha", LocalDate.now().toString());
        historial.append("detalles", detalles);

        collection.insertOne(historial);
    }

    public void mostrarHistorial() {

        for (Document doc : collection.find()) {

            System.out.println("---------------");

            System.out.println("Usuario: " + doc.getString("nombre_usuario"));

            System.out.println("Accion: " + doc.getString("accion"));

            System.out.println("Descripcion: " + doc.getString("descripcion"));

            System.out.println("Fecha: " + doc.getString("fecha"));

            Document detalles = (Document) doc.get("detalles");
            System.out.println("DetalleS: ");
            if (detalles != null) {
                System.out.println("Detalle: " + detalles.getString("detalle"));
            if (detalles.getString("Estado")!=null){
                    System.out.println("Estado: " + detalles.getString("Estado"));
                }
            } else {
                System.out.println("Detalles: null");
            }
        }
        System.out.println("---------------\n");
    }
}