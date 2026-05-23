package com.ejemplo;

import com.ejemplo.DAO.usuarioDAO;
import com.ejemplo.DAO.libroDAO;
import com.ejemplo.DAO.prestamoDAO;
import com.ejemplo.hibernate.DAO.ClubDAO;
import com.ejemplo.hibernate.model.Biblioteca;
import com.ejemplo.hibernate.model.Libro;
import com.ejemplo.hibernate.model.Usuario;
import com.ejemplo.hibernate.util.HibernateUtil;
import com.ejemplo.model.libro;
import com.ejemplo.txt.ImportarTXT;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try (Connection con = com.ejemplo.DBUtil.getConnection();
             Statement st = con.createStatement()) {



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
            ImportarTXT txt = new ImportarTXT();
            txt.importarTodo("biblioteca.txt");
            usuarioDAO usuarioDAO = new usuarioDAO();
            libroDAO libroDAO = new libroDAO();
            prestamoDAO prestamoDAO = new prestamoDAO();
            ClubDAO clubDAO = new ClubDAO();
            int num =-1, menuJDBC=-1, menuHIBERNATE=-1;
            HibernateUtil.getSessionFactory();
            System.out.println("Hibernate conectado correctamente");
            boolean adminLogueado = false;
            boolean loginCorrecto = false;

            while (!loginCorrecto) {

                System.out.println("=== INICIO SESION ===");

                System.out.println("Nombre usuario");
                String usuarioLogin = sc.nextLine();

                System.out.println("Password");
                String passwordLogin = sc.nextLine();

                if (usuarioDAO.existeUsuario(usuarioLogin, passwordLogin)) {

                    adminLogueado =
                            usuarioDAO.iniciarSesion(usuarioLogin, passwordLogin);

                    loginCorrecto = true;

                    if (adminLogueado) {

                        System.out.println("Sesion iniciada como ADMIN");

                    } else {

                        System.out.println("Sesion iniciada como USUARIO");
                    }

                } else {

                    System.out.println("Usuario o password incorrectos");
                }
            }

            while(num!=0){
                System.out.println("""
                        
                        1 - Gestion prestamos y usuarios
                        2 - Gestion clubes
                        3 - 
                        4 - 
                        0 - Salir
                        
                        """);

                num = sc.nextInt();
                sc.nextLine();
                switch (num){

                    case 1:
                        menuJDBC = -1;
                        while (menuJDBC != 0) {
                            System.out.println("""
                        Gestion de prestamos y Usuarios \n
                        1 - Insertar usuario
                        2 - Hacer préstamo
                        3 - Eliminar préstamo
                        4 - Mostrar libros
                        5 - Mostrar usuarios
                        0 - Salir
                        
                        """);

                            menuJDBC = sc.nextInt();
                            sc.nextLine();

                            switch (menuJDBC) {
                                case 1:
                                    if (!adminLogueado) {

                                        System.out.println("No tienes permisos");

                                        break;
                                    }

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
                                    if (!adminLogueado) {

                                        System.out.println("No tienes permisos");

                                        break;
                                    }
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
                                    if (!adminLogueado) {

                                        System.out.println("No tienes permisos");

                                        break;
                                    }
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
                                case 5:
                                    if (!adminLogueado) {

                                        System.out.println("No tienes permisos");

                                        break;
                                    }
                                    usuarioDAO.mostrarUsuarios();

                                    break;
                                case 0:

                                    System.out.println("Saliendo");
                                    break;

                                default:

                                    System.out.println("Opción incorrecta");
                            }
                        }
                        break;
                    case 2:
                        if (!adminLogueado) {

                            System.out.println(
                                    "Solo los administradores pueden acceder a gestion de clubes"
                            );

                            break;
                        }
                        menuHIBERNATE= -1;
                        while (menuHIBERNATE != 0) {

                            System.out.println("""
                                                Gestión de clubes\n
                                                1 - Crear club
                                                2 - Añadir libro a club
                                                3 - Quitar libro de club
                                                4 - Añadir admin a club
                                                5 - Quitar admin de club
                                                6 - Añadir integrantes
                                                7 - Quitar integrantes
                                                8 - Ver datos club
                                                0 - Salir
                                                
                                                """);

                            menuHIBERNATE = sc.nextInt();
                            sc.nextLine();

                            switch (menuHIBERNATE) {

                                case 1:

                                    System.out.println("Nombre club");
                                    String nombreClub = sc.nextLine();

                                    System.out.println("Descripcion");
                                    String descripcion = sc.nextLine();

                                    Biblioteca biblioteca = new Biblioteca();
                                    biblioteca.setNombre("Biblioteca principal");

                                    clubDAO.crearClub(nombreClub, descripcion, biblioteca);

                                    break;
                                case 2:

                                    System.out.println("Nombre club");
                                    String nombreClubLibro = sc.nextLine();

                                    Integer idClubLibro = clubDAO.obtenerIdClubPorNombre(nombreClubLibro);

                                    if (idClubLibro == null) {

                                        System.out.println("Club no encontrado");
                                        break;
                                    }

                                    System.out.println("Nombre libro");
                                    String nombreLibro = sc.nextLine();

                                    Libro libroHibernate = clubDAO.obtenerLibroPorNombre(nombreLibro);

                                    if (libroHibernate != null) {

                                        clubDAO.asignarLibro(idClubLibro, libroHibernate);

                                    } else {

                                        System.out.println("Libro no encontrado");
                                    }

                                    break;

                                case 3:

                                    System.out.println("Nombre club");
                                    String nombreClubQuitarLibro = sc.nextLine();

                                    Integer idClubQuitarLibro = clubDAO.obtenerIdClubPorNombre(nombreClubQuitarLibro);

                                    if (idClubQuitarLibro == null) {

                                        System.out.println("Club no encontrado");
                                        break;
                                    }

                                    clubDAO.quitarLibro(idClubQuitarLibro);

                                    break;

                                case 4:

                                    System.out.println("Nombre club");
                                    String nombreClubAdmin = sc.nextLine();

                                    Integer idClubAdmin = clubDAO.obtenerIdClubPorNombre(nombreClubAdmin);
                                    if (idClubAdmin == null) {

                                        System.out.println("Club no encontrado");
                                        break;
                                    }

                                    System.out.println("Nombre admin");
                                    String nombreAdmin = sc.nextLine();

                                    Usuario usuario = clubDAO.obtenerUsuarioPorNombre(nombreAdmin);

                                    if (usuario != null) {

                                        clubDAO.asignarAdmin(
                                                idClubAdmin, usuario);
                                    } else {

                                        System.out.println("Usuario no encontrado");
                                    }

                                    break;

                                case 5:

                                    System.out.println("Nombre club");
                                    String nombreClubQuitarAdmin = sc.nextLine();

                                    Integer idClubQuitarAdmin =
                                            clubDAO.obtenerIdClubPorNombre(nombreClubQuitarAdmin);

                                    if (idClubQuitarAdmin == null) {

                                        System.out.println("Club no encontrado");
                                        break;
                                    }

                                    clubDAO.quitarAdmin(idClubQuitarAdmin);

                                    break;

                                case 6:

                                    System.out.println("Nombre club");
                                    String nombreClubUsuario = sc.nextLine();

                                    Integer idClubUsuario = clubDAO.obtenerIdClubPorNombre(nombreClubUsuario);

                                    if (idClubUsuario == null) {

                                        System.out.println("Club no encontrado");
                                        break;
                                    }

                                    System.out.println("Nombre usuario");
                                    String nombreUsuarioAdd = sc.nextLine();

                                    Usuario usuarioAdd = clubDAO.obtenerUsuarioPorNombre(nombreUsuarioAdd);

                                    if (usuarioAdd != null) {

                                        clubDAO.añadirUsuario(idClubUsuario, usuarioAdd);

                                    } else {

                                        System.out.println("Usuario no encontrado");
                                    }

                                    break;

                                case 7:

                                    System.out.println("Nombre club");
                                    String nombreClubRemove = sc.nextLine();

                                    Integer idClubQuitarUsuario = clubDAO.obtenerIdClubPorNombre(nombreClubRemove);

                                    if (idClubQuitarUsuario == null) {

                                        System.out.println("Club no encontrado");
                                        break;
                                    }

                                    System.out.println("Nombre usuario");
                                    String nombreUsuarioRemove = sc.nextLine();

                                    Usuario usuarioRemove = clubDAO.obtenerUsuarioPorNombre(nombreUsuarioRemove);

                                    if (usuarioRemove != null) {

                                        clubDAO.quitarUsuario(idClubQuitarUsuario, usuarioRemove);

                                    } else {

                                        System.out.println("Usuario no encontrado");
                                    }

                                    break;
                                case 8:

                                    System.out.println("Nombre club");
                                    String nombreClubDatos = sc.nextLine();

                                    clubDAO.mostrarDatosClub(nombreClubDatos);

                                    break;
                                case 0:

                                    System.out.println("Saliendo");
                                    break;

                                default:

                                    System.out.println("Opcion incorrecta");



                            }
                        }

                }
            }




        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}