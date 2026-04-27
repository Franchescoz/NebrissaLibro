package com.ejemplo;

import com.ejemplo.txt.ImportarTXT;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
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
            int num = -1;
            String nombreusuario="", email="", password="";
            String nombrelibro="";
            Boolean estado = true;


            while(num!=0) {
                System.out.println("Elige entre estas opciones\n1-insertar un usuario\n2-Hacer prestamo\n3-deshacer un prestamo\n4-Mostrar libros");
                num=sc.nextInt();

                switch(num) {
                    case 1:
                        System.out.println("Dame tu nombre de usuario");
                        nombreusuario=sc.next();
                        System.out.println("Dame tu email ");
                        email=sc.next();
                        System.out.println("Dame tu password");
                        password=sc.next();
                        PreparedStatement ps = con.prepareStatement("INSERT INTO Usuario (nombre, email, password) VALUES (?, ?, ?)");
                        ps.setString(1, nombreusuario);
                        ps.setString(2, email);
                        ps.setString(3, password);

                        ps.executeUpdate();
                        break;
                    case 2:
                        System.out.println("Dame el nombre del libro que buscas");
                        nombrelibro=sc.next();
                        System.out.println("Dame tu nombre de usuario");
                        nombreusuario=sc.next();
                        PreparedStatement ps3 = con.prepareStatement("SELECT id_usuario FROM usuario WHERE nombre=? ");
                        ps3.setString(1, nombreusuario);
                        PreparedStatement pslibro = con.prepareStatement("SELECT id_libro FROM Libro WHERE nombre=? ");
                        pslibro.setString(1, nombrelibro);
                        ResultSet rslibro = pslibro.executeQuery();
                        ResultSet rs2= ps3.executeQuery();
                        PreparedStatement prestamo= con.prepareStatement("INSERT INTO Prestamo(id_usuario,id_libro,fecha_prestamo,estado,fecha_devolucion) VALUES (?,?,?,?,?)");
                        prestamo.setInt(1,rs2.getInt("id_usuario"));
                        prestamo.setInt(2,rslibro.getInt("id_libro"));
                        prestamo.setDate(3, Date.valueOf(LocalDate.now()));
                        prestamo.setBoolean(4,estado);
                        prestamo.setDate(5,Date.valueOf(LocalDate.now().plusDays(30)));
                        prestamo.executeUpdate();

                    case 3:
                        System.out.println("Dame el nombre del libro que buscas");
                        nombrelibro=sc.next();
                        System.out.println("Dame tu nombre de usuario");
                        nombreusuario=sc.next();
                        PreparedStatement psusuario3 = con.prepareStatement("SELECT id_usuario FROM usuario WHERE nombre=? ");
                        psusuario3.setString(1, nombreusuario);
                        PreparedStatement pslibro2 = con.prepareStatement("SELECT id_libro FROM Libro WHERE nombre=? ");
                        pslibro2.setString(1, nombrelibro);
                        ResultSet rslibro2 = pslibro2.executeQuery();
                        ResultSet rsusuario2= psusuario3.executeQuery();
                        PreparedStatement eliminarprestamo = con.prepareStatement("DELETE FROM Prestamo WHERE id_libro=? and id_usuario=?");
                        eliminarprestamo.setInt(1, rslibro2.getInt("id_libro"));
                        eliminarprestamo.setInt(2, rslibro2.getInt("id_usuario"));
                        eliminarprestamo.executeUpdate();
                    case 4:
                        System.out.println("Estos son los libros");
                        PreparedStatement libro = con.prepareStatement("SELECT * FROM libro ");

                        ResultSet rs = libro.executeQuery();

                        while  (rs.next()) {
                            System.out.println(rs.getString("nombre")+"---");


                        }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}