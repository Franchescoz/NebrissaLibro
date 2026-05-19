package com.ejemplo.txt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

import com.ejemplo.model.autor;
import com.ejemplo.model.biblioteca;
import com.ejemplo.model.libro;

public class ImportarTXT {

    private List<biblioteca> bibliotecas = new ArrayList<>();
    private List<autor> autores = new ArrayList<>();
    private List<libro> libros = new ArrayList<>();

    public void importarTodo(String rutaArchivo) {

        try (Connection con = com.ejemplo.DBUtil.getConnection()) {

            try (
                    BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));


                    PreparedStatement psBiblio = con.prepareStatement(
                            "INSERT INTO Biblioteca (nombre, dirección, telefono_contacto) VALUES (?, ?, ?)");

                    PreparedStatement psAutor = con.prepareStatement(
                            "INSERT INTO Autor (nombre, bibliografía, fecha_nacimiento) VALUES (?, ?, ?)");

                    PreparedStatement psLibro = con.prepareStatement(
                            "INSERT INTO Libro (nombre, sinopsis, ISBN, tipo_libro, fecha_publicacion, id_autor) VALUES (?, ?, ?, ?, ?, ?)");


                    PreparedStatement psExisteBiblio = con.prepareStatement(
                            "SELECT id_biblioteca FROM Biblioteca WHERE nombre = ?");

                    PreparedStatement psExisteAutor = con.prepareStatement(
                            "SELECT id_autor FROM Autor WHERE nombre = ?");

                    PreparedStatement psExisteLibro = con.prepareStatement(
                            "SELECT id_libro FROM Libro WHERE ISBN = ?")

            ) {

                String linea;

                while ((linea = br.readLine()) != null) {

                    if (linea.trim().isEmpty()) {
                        continue;
                    }

                    String[] datos = linea.split(";");


                    if (datos.length == 3) {


                        if (datos[0].startsWith("B")) {


                            psExisteBiblio.setString(1, datos[0]);
                            ResultSet rsBiblio = psExisteBiblio.executeQuery();

                            if (!rsBiblio.next()) {

                                psBiblio.setString(1, datos[0]);
                                psBiblio.setString(2, datos[1]);
                                psBiblio.setString(3, datos[2]);

                                psBiblio.executeUpdate();

                                biblioteca b = new biblioteca(
                                        datos[0],
                                        datos[1],
                                        datos[2]
                                );

                                bibliotecas.add(b);

                                System.out.println("Biblioteca insertada: " + datos[0]);

                            } else {
                                System.out.println("Biblioteca repetida: " + datos[0]);
                            }

                        }


                        else {

                            psExisteAutor.setString(1, datos[0]);
                            ResultSet rsAutor = psExisteAutor.executeQuery();

                            if (!rsAutor.next()) {

                                Date fecha = Date.valueOf(datos[2]);

                                psAutor.setString(1, datos[0]);
                                psAutor.setString(2, datos[1]);
                                psAutor.setDate(3, fecha);

                                psAutor.executeUpdate();

                                autor a = new autor(
                                        datos[0],
                                        datos[1],
                                        fecha
                                );

                                autores.add(a);

                                System.out.println("Autor insertado: " + datos[0]);

                            } else {
                                System.out.println("Autor repetido: " + datos[0]);
                            }
                        }

                    }


                    else if (datos.length == 6) {


                        psExisteLibro.setString(1, datos[2]);

                        ResultSet rsLibro = psExisteLibro.executeQuery();

                        if (!rsLibro.next()) {


                            psExisteAutor.setString(1, datos[5]);

                            ResultSet rsAutor = psExisteAutor.executeQuery();

                            if (rsAutor.next()) {

                                int idAutor = rsAutor.getInt("id_autor");

                                psLibro.setString(1, datos[0]);
                                psLibro.setString(2, datos[1]);
                                psLibro.setString(3, datos[2]);
                                psLibro.setString(4, datos[3]);
                                psLibro.setDate(5, Date.valueOf(datos[4]));
                                psLibro.setInt(6, idAutor);

                                psLibro.executeUpdate();

                                libro l = new libro(
                                        datos[0],
                                        datos[1],
                                        datos[2],
                                        datos[3],
                                        Date.valueOf(datos[4]),
                                        idAutor
                                );

                                libros.add(l);

                                System.out.println("Libro insertado: " + datos[0]);
                            }

                        } else {
                            System.out.println("Libro repetido ISBN: " + datos[2]);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Proceso de importación finalizado.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<biblioteca> getBibliotecas() {
        return bibliotecas;
    }

    public List<autor> getAutores() {
        return autores;
    }

    public List<libro> getLibros() {
        return libros;
    }
}