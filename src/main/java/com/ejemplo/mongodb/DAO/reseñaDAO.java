package com.ejemplo.mongodb.DAO;

import com.ejemplo.mongodb.MongoDBUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class reseñaDAO {

    private final MongoCollection<Document> collection;

    public reseñaDAO() {

        MongoDatabase db =
                MongoDBUtil.getDatabase();

        collection =
                db.getCollection("reseñas");
    }

    public void crearReseña(String usuario, String libro, int puntuacion, String comentario) {

        Document reseña = new Document();
        reseña.append("usuario", usuario);
        reseña.append("libro", libro);
        reseña.append("puntuacion", puntuacion);
        reseña.append("comentario", comentario);
        reseña.append("fecha_reseña", LocalDate.now().toString());
        reseña.append("respuestas", new ArrayList<>());
        collection.insertOne(reseña);

        System.out.println("Reseña creada");
    }



    public void mostrarReseñasLibro(String libro) {

        List<Document> reseñas = collection.find(new Document("libro", libro)).into(new ArrayList<>());

        if (reseñas.isEmpty()) {

            System.out.println("No hay resultados o has escrito mal el nombre");

            return;
        }

        for (Document doc : reseñas) {

            System.out.println("---------------");

            System.out.println("ID reseña: " + doc.getObjectId("_id"));

            System.out.println("Usuario: " + doc.getString("usuario"));

            System.out.println("Puntuacion: " + doc.getInteger("puntuacion"));

            System.out.println("Comentario: " + doc.getString("comentario"));

            System.out.println("Fecha: " + doc.getString("fecha_reseña"));

            System.out.println("Respuestas:");

            List<Document> respuestas = (List<Document>) doc.get("respuestas");

            if (respuestas != null && !respuestas.isEmpty()) {

                for (Document respuesta : respuestas) {

                    System.out.println("-------------------");

                    System.out.println("Usuario respuesta: " + respuesta.getString("usuario"));

                    System.out.println("Comentario respuesta: " + respuesta.getString("comentario"));

                    System.out.println("Fecha respuesta: " + respuesta.getString("fecha"));
                }

            } else {

                System.out.println("No hay respuestas");
            }
        }
    }

    public void añadirComentario(String usuarioReseña, String libro, String usuario, String comentario) {

        Document respuesta = new Document();

        respuesta.append("usuario", usuario);

        respuesta.append("comentario", comentario);

        respuesta.append("fecha", LocalDate.now().toString());

        Document reseña = collection.find(
                new Document("usuario", usuarioReseña)
                        .append("libro", libro)
        ).first();

        if (reseña == null) {
            System.out.println("Reseña no encontrada");
            return;
        }

        collection.updateOne(
                new Document("_id", reseña.getObjectId("_id")),
                new Document("$push",
                        new Document("respuestas", respuesta)
                )
        );

        System.out.println("Comentario añadido");
    }

}