package com.ejemplo;

import com.ejemplo.DAO.usuarioDAO;
import com.ejemplo.DAO.libroDAO;
import com.ejemplo.DAO.prestamoDAO;
import com.ejemplo.model.libro;
import com.ejemplo.txt.ImportarTXT;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try (Connection con = com.ejemplo.util.DBUtil.getConnection();
             Statement st = con.createStatement()) {

            // CREAR TABLAS

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Usuario (
                    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    email VARCHAR(100) UNIQUE,
                    password VARCHAR(255),
                    admin BOOLEAN
                    )
                    """);

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Biblioteca (
                    id_biblioteca INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100),
                    direccion VARCHAR(255),
                    telefono_contacto VARCHAR(20)
                    )
                    """);

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Autor (
                    id_autor INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100),
                    bibliografia TEXT,
                    fecha_nacimiento DATE
                    )
                    """);

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Libro (
                    id_libro INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(150),
                    sinopsis TEXT,
                    ISBN VARCHAR(20),
                    tipo_libro VARCHAR(50),
                    fecha_publicacion DATE,
                    id_autor INT,
                    FOREIGN KEY (id_autor) REFERENCES Autor(id_autor)
                    )
                    """);

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Prestamo (
                    id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
                    id_usuario INT,
                    id_libro INT,
                    fecha_prestamo DATE,
                    estado ENUM('PRESTADO', 'DEVUELTO'),
                    fecha_devolucion DATE,
                    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
                    FOREIGN KEY (id_libro) REFERENCES Libro(id_libro)
                    )
                    """);

            System.out.println("Tablas creadas");

            // IMPORTAR TXT

            ImportarTXT txt = new ImportarTXT();
            txt.importarTodo("biblioteca.txt");



            usuarioDAO usuarioDAO = new usuarioDAO();
            libroDAO libroDAO = new libroDAO();
            prestamoDAO prestamoDAO = new prestamoDAO();

            int num = -1;

            while (num != 0) {

                System.out.println("""
                        
                        1 - Insertar usuario
                        2 - Hacer préstamo
                        3 - Eliminar préstamo
                        4 - Mostrar libros
                        0 - Salir
                        
                        """);

                num = sc.nextInt();
                sc.nextLine();

                switch (num) {



                    case 1:

                        System.out.println("Nombre usuario");
                        String nombreusuario = sc.nextLine();

                        System.out.println("Email");
                        String email = sc.nextLine();

                        System.out.println("Password");
                        String password = sc.nextLine();

                        usuarioDAO.insertarUsuario(
                                nombreusuario,
                                email,
                                password,
                                false
                        );

                        break;



                    case 2:

                        System.out.println("Nombre usuario");
                        nombreusuario = sc.nextLine();

                        System.out.println("Nombre libro");
                        String nombrelibro = sc.nextLine();

                        int idUsuario =
                                usuarioDAO.obtenerIdUsuario(nombreusuario);

                        int idLibro =
                                libroDAO.obtenerIdLibro(nombrelibro);

                        if (idUsuario != -1 && idLibro != -1) {

                            prestamoDAO.hacerPrestamo(
                                    idUsuario,
                                    idLibro
                            );

                        } else {

                            System.out.println(
                                    "Usuario o libro no encontrado"
                            );
                        }

                        break;



                    case 3:

                        System.out.println("Nombre usuario");
                        nombreusuario = sc.nextLine();

                        System.out.println("Nombre libro");
                        nombrelibro = sc.nextLine();

                        idUsuario =
                                usuarioDAO.obtenerIdUsuario(nombreusuario);

                        idLibro =
                                libroDAO.obtenerIdLibro(nombrelibro);

                        if (idUsuario != -1 && idLibro != -1) {

                            prestamoDAO.devolverPrestamo(
                                    idUsuario,
                                    idLibro
                            );

                        } else {

                            System.out.println(
                                    "Usuario o libro no encontrado"
                            );
                        }

                        break;



                    case 4:

                        List<libro> libros =
                                libroDAO.mostrarLibros();

                        for (libro l : libros) {

                            System.out.println(
                                    l.getNombre()
                            );
                        }

                        break;

                    case 0:

                        System.out.println("Saliendo");
                        break;

                    default:

                        System.out.println("Opción incorrecta");
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}