package com.ejemplo.txt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class ImportarTXT {

    public void importarTodo(String rutaArchivo) {

        try (Connection con = com.example.Pruebas.DBUtil.getConnection()) {

            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
                 PreparedStatement psBiblio = con.prepareStatement("INSERT INTO Biblioteca (nombre, dirección, telefono_contacto) VALUES (?, ?, ?)");
                 PreparedStatement psAutor = con.prepareStatement("INSERT INTO Autor (nombre, bibliografía, fecha_nacimiento) VALUES (?, ?, ?)");
                 PreparedStatement psLibro = con.prepareStatement("INSERT INTO Libro (nombre, sinopsis, ISBN, tipo_libro, fecha_publicacion, id_autor) VALUES (?, ?, ?, ?, ?, ?)");
                 PreparedStatement psBuscarAutor = con.prepareStatement("SELECT id_autor FROM Autor WHERE nombre = ?")) {

                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.trim().isEmpty()) continue;String[] datos = linea.split(";");
                    for (String dato : datos) {
                        System.out.println(dato);
                    }
                    if (datos.length == 3) {
                        if (datos[0].startsWith("B")) {
                            psBiblio.setString(1, datos[0]);
                            psBiblio.setString(2, datos[1]);
                            psBiblio.setString(3, datos[2]);
                            psBiblio.executeUpdate();
                        }else {
                            Date fecha = Date.valueOf(datos[2]);
                            psAutor.setString(1, datos[0]);
                            psAutor.setString(2, datos[1]);
                            psAutor.setDate(3, fecha);
                            psAutor.executeUpdate();
                        }

                    } else if (datos.length == 6) {

                        psBuscarAutor.setString(1, datos[5]);
                        ResultSet rs = psBuscarAutor.executeQuery();

                        if (rs.next()) {
                            int idAutor = rs.getInt("id_autor");

                            psLibro.setString(1, datos[0]);
                            psLibro.setString(2, datos[1]);
                            psLibro.setString(3, datos[2]);
                            psLibro.setString(4, datos[3]);
                            psLibro.setDate(5, Date.valueOf(datos[4]));
                            psLibro.setInt(6, idAutor);
                            psLibro.executeUpdate();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Proceso de importación desde archivo único finalizado.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}