package com.ejemplo;

import com.ejemplo.txt.ImportarTXT;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        String dbName = "NebrissaBiblioteca";

        try (Connection con = com.example.Pruebas.DBUtil.getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            st.execute("USE " + dbName);

            st.executeUpdate("CREATE TABLE IF NOT EXISTS Usuario (" +
                    "id_usuario INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE, " +
                    "password VARCHAR(255), " +
                    "admin BOOLEAN)");
            System.out.println("Tabla Usuario creada");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Biblioteca (" +
                    "id_biblioteca INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100), " +
                    "dirección VARCHAR(255), " +
                    "telefono_contacto VARCHAR(20))");
            System.out.println("Tabla Biblioteca creada");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Autor (" +
                    "id_autor INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100), " +
                    "bibliografía TEXT, " +
                    "fecha_nacimiento DATE)");
            System.out.println("Tabla Autor creada");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Libro (" +
                    "id_libro INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(150), " +
                    "sinopsis TEXT, " +
                    "ISBN VARCHAR(20), " +
                    "tipo_libro VARCHAR(50), " +
                    "fecha_publicacion DATE, " +
                    "id_autor INT, " +
                    "FOREIGN KEY (id_autor) REFERENCES Autor(id_autor))");
            System.out.println("Tabla Libro creada");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Prestamo (" +
                    "id_prestamo INT AUTO_INCREMENT PRIMARY KEY, " +
                    "id_usuario INT, " +
                    "id_libro INT, " +
                    "fecha_prestamo DATE, " +
                    "estado ENUM('PRESTADO', 'DEVUELTO'), " +
                    "fecha_devolucion DATE, " +
                    "FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario), " +
                    "FOREIGN KEY (id_libro) REFERENCES Libro(id_libro))");

            System.out.println("Tabla Prestamo creada");
            ImportarTXT txt = new ImportarTXT();
            txt.importarTodo("biblioteca.txt");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}