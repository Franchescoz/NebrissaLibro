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
import com.ejemplo.mongodb.DAO.historialDAO;
import com.ejemplo.mongodb.DAO.reseñaDAO;
import org.bson.Document;
import com.ejemplo.xml.generarXML;
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
            historialDAO historialDAO = new historialDAO();
            reseñaDAO reseñaDAO = new reseñaDAO();
            int num = -1, menuJDBC = -1 , menuHIBERNATE = -1;
            HibernateUtil.getSessionFactory();

            System.out.println("Hibernate conectado correctamente");

            boolean adminLogueado = false;
            boolean loginCorrecto = false;

            while (!loginCorrecto) {

                System.out.println("=== INICIO SESIÓN ===");

                System.out.println("Nombre usuario");
                String usuarioLogin = sc.nextLine();

                System.out.println("Password");
                String passwordLogin = sc.nextLine();

                if (usuarioDAO.existeUsuario(usuarioLogin, passwordLogin)) {

                    adminLogueado = usuarioDAO.iniciarSesion(usuarioLogin, passwordLogin);

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

            while (num != 0) {

                System.out.println("""              
                        1 - Gestion prestamos y usuarios
                        2 - Gestion clubes
                        3 - Gestion reseñas
                        4 - Informacion del libro
                        0 - Salir
                        """);

                num = sc.nextInt();
                sc.nextLine();

                switch (num) {
                    case 1:
                        menuJDBC = -1;
                        while (menuJDBC != 0) {
                            System.out.println("""
                                    Gestion de prestamos y usuarios
                                    1 - Insertar usuario
                                    2 - Hacer préstamo
                                    3 - Eliminar préstamo
                                    4 - Mostrar libros
                                    5 - Mostrar usuarios
                                    6 - Editar Usuario
                                    7 - Eliminar Usuario
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

                                    usuarioDAO.insertarUsuario(nombreusuario, email, password, false);
                                    historialDAO.guardarAccion(nombreusuario, "crear_usuario", "Se creó un usuario",
                                            new Document(
                                                    "detalle",
                                                    "Se ha creado el usuario " + nombreusuario
                                            )
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

                                    int idUsuario = usuarioDAO.obtenerIdUsuario(nombreusuario);
                                    int idLibro = libroDAO.obtenerIdLibro(nombrelibro);

                                    if (idUsuario != -1 && idLibro != -1) {
                                        prestamoDAO.hacerPrestamo(idUsuario, idLibro);
                                        historialDAO.guardarAccion(nombreusuario, "hacer_prestamo", "Usuario realizó un préstamo",
                                                new Document(
                                                        "detalle",
                                                        "El usuario " + nombreusuario + " ha pedido prestado el libro " + nombrelibro
                                                ).append("Estado","Prestado")
                                        );
                                    } else {
                                        System.out.println("Usuario o libro no encontrado");
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

                                    idUsuario = usuarioDAO.obtenerIdUsuario(nombreusuario);

                                    idLibro = libroDAO.obtenerIdLibro(nombrelibro);

                                    if (idUsuario != -1 && idLibro != -1) {
                                        prestamoDAO.devolverPrestamo(idUsuario, idLibro);

                                        historialDAO.guardarAccion(nombreusuario, "devolver_prestamo", "Usuario devolvió un préstamo",
                                                new Document(
                                                        "detalle",
                                                        "El usuario " + nombreusuario + " ha devuelto el libro " + nombrelibro
                                                ).append("Estado","Devuelto")
                                        );
                                    } else {
                                        System.out.println("Usuario o libro no encontrado");
                                    }
                                    break;
                                case 4:
                                    List<libro> libros = libroDAO.mostrarLibros();
                                    for (libro l : libros) {
                                        System.out.println(l.getNombre());
                                    }
                                    break;
                                case 5:
                                    if (!adminLogueado) {
                                        System.out.println("No tienes permisos");
                                        break;
                                    }
                                    usuarioDAO.mostrarUsuarios();
                                    break;
                                case 6:
                                    if (!adminLogueado) {
                                        System.out.println("No tienes permisos (solo ADMIN)");
                                        break;
                                    }

                                    System.out.println("Nombre del usuario a editar:");
                                    String nombreBuscar = sc.nextLine();

                                    int idUsuarioEditar = usuarioDAO.obtenerIdUsuario(nombreBuscar);

                                    if (idUsuarioEditar == -1) {
                                        System.out.println("Usuario no encontrado");
                                        break;
                                    }

                                    String nuevoNombre = null;
                                    String nuevoEmail = null;
                                    String nuevaPassword = null;

                                    int opcionEditar = -1;

                                    while (opcionEditar != 0) {
                                        System.out.println("""
                                                ¿Qué quieres modificar?
                                                1 - Nombre
                                                2 - Email
                                                3 - Password
                                                4 - Guardar cambios
                                                0 - Cancelar
                                                """);

                                        opcionEditar = sc.nextInt();
                                        sc.nextLine();

                                        switch (opcionEditar) {
                                            case 1:
                                                System.out.println("Nuevo nombre:");
                                                nuevoNombre = sc.nextLine();
                                                break;
                                            case 2:
                                                System.out.println("Nuevo email:");
                                                nuevoEmail = sc.nextLine();
                                                break;
                                            case 3:
                                                System.out.println("Nueva password:");
                                                nuevaPassword = sc.nextLine();
                                                break;
                                            case 4:
                                                usuarioDAO.editarUsuario(idUsuarioEditar, nuevoNombre != null  ? nuevoNombre : nombreBuscar, nuevoEmail, nuevaPassword);
                                                historialDAO.guardarAccion("ADMIN", "editar_usuario", "Admin editó un usuario",
                                                        new Document(
                                                                "detalle",
                                                                "Se editó el usuario " + nombreBuscar
                                                        )
                                                );
                                                opcionEditar = 0;
                                                break;

                                            case 0:
                                                System.out.println("Edición cancelada");
                                                break;
                                            default:
                                                System.out.println("Opción incorrecta");
                                        }
                                    }

                                    break;
                                case 7:
                                    if (!adminLogueado) {
                                        System.out.println("No tienes permisos (solo ADMIN)");
                                        break;
                                    }

                                    System.out.println("Nombre del usuario a eliminar:");
                                    String nombreEliminar = sc.nextLine();

                                    int idEliminar = usuarioDAO.obtenerIdUsuario(nombreEliminar);

                                    if (idEliminar == -1) {
                                        System.out.println("Usuario no encontrado");
                                        break;
                                    }
                                    usuarioDAO.eliminarUsuario(idEliminar);
                                    historialDAO.guardarAccion("ADMIN", "eliminar_usuario", "Admin eliminó usuario",
                                            new Document("detalle",
                                                    "Se eliminó el usuario " + nombreEliminar)
                                    );
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
                            System.out.println("Solo los administradores pueden acceder a gestion de clubes");
                            break;
                        }

                        menuHIBERNATE = -1;

                        while (menuHIBERNATE != 0) {

                            System.out.println("""
                                    Gestión de clubes
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

                                    System.out.println("Biblioteca");
                                    String biblioteca = sc.nextLine();

                                    clubDAO.crearClub(nombreClub, descripcion, biblioteca);
                                    historialDAO.guardarAccion("ADMIN", "crear_club", "Se creó un club",
                                            new Document(
                                                    "detalle",
                                                    "Se ha creado el club " + nombreClub
                                            )
                                    );
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
                                        historialDAO.guardarAccion("ADMIN", "añadir_libro_club", "Se añadió un libro al club",
                                                new Document(
                                                        "detalle",
                                                        "Se ha añadido el libro " + nombreLibro +
                                                                " al club " + nombreClubLibro
                                                )
                                        );
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
                                    historialDAO.guardarAccion("ADMIN", "quitar_libro_club", "Se quitó un libro del club",
                                            new Document(
                                                    "detalle",
                                                    "Se ha quitado el libro del club " +
                                                            nombreClubQuitarLibro
                                            )
                                    );
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
                                        clubDAO.asignarAdmin(idClubAdmin, usuario);
                                        historialDAO.guardarAccion("ADMIN", "asignar_admin", "Se asignó un admin al club",
                                                new Document(
                                                        "detalle",
                                                        "Se asignó el usuario " + nombreAdmin +
                                                                " como admin al club " + nombreClubAdmin
                                                )
                                        );
                                    } else {
                                        System.out.println("Usuario no encontrado");
                                    }
                                    break;
                                case 5:
                                    System.out.println("Nombre club");
                                    String nombreClubQuitarAdmin = sc.nextLine();

                                    Integer idClubQuitarAdmin = clubDAO.obtenerIdClubPorNombre(nombreClubQuitarAdmin);
                                    if (idClubQuitarAdmin == null) {
                                        System.out.println("Club no encontrado");
                                        break;
                                    }

                                    clubDAO.quitarAdmin(idClubQuitarAdmin);
                                    historialDAO.guardarAccion("ADMIN", "quitar_admin", "Se eliminó el admin del club",
                                            new Document(
                                                    "detalle",
                                                    "Se quitó el admin del club " +
                                                            nombreClubQuitarAdmin
                                            )
                                    );
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
                                    String nombreUsuarioAñadir = sc.nextLine();

                                    Usuario usuarioAñadir = clubDAO.obtenerUsuarioPorNombre(nombreUsuarioAñadir);
                                    if (usuarioAñadir != null) {
                                        clubDAO.añadirUsuario(idClubUsuario, usuarioAñadir);
                                        historialDAO.guardarAccion("ADMIN", "añadir_usuario_club", "Usuario añadido al club",
                                                new Document(
                                                        "detalle",
                                                        "Se añadió al usuario " +
                                                                nombreUsuarioAñadir +
                                                                " al club " + nombreClubUsuario
                                                )
                                        );
                                    } else {
                                        System.out.println("Usuario no encontrado");
                                    }
                                    break;
                                case 7:
                                    System.out.println("Nombre club");
                                    String nombreClubQuitar = sc.nextLine();

                                    Integer idClubQuitarUsuario = clubDAO.obtenerIdClubPorNombre(nombreClubQuitar);
                                    if (idClubQuitarUsuario == null) {
                                        System.out.println("Club no encontrado");
                                        break;
                                    }

                                    System.out.println("Nombre usuario");
                                    String nombreUsuarioQuitar = sc.nextLine();

                                    Usuario usuarioQuitar = clubDAO.obtenerUsuarioPorNombre(nombreUsuarioQuitar);
                                    if (usuarioQuitar != null) {
                                        clubDAO.quitarUsuario(idClubQuitarUsuario, usuarioQuitar);
                                        historialDAO.guardarAccion("ADMIN", "quitar_usuario_club", "Usuario eliminado del club",
                                                new Document(
                                                        "detalle",
                                                        "Se quitó al usuario " +
                                                                nombreUsuarioQuitar +
                                                                " del club " + nombreClubQuitar
                                                )
                                        );

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
                        break;
                    case 3:
                        int menuMongo = -1;
                        while (menuMongo != 0) {

                            System.out.println("""
                                    Gestion reseñas e historial                                 
                                    1 - Crear reseña
                                    2 - Ver reseñas libro
                                    3 - Añadir comentario a reseña
                                    4 - Ver historial acciones
                                    0 - Salir                                 
                                    """);

                            menuMongo = sc.nextInt();
                            sc.nextLine();

                            switch (menuMongo) {
                                case 1:
                                    System.out.println("Nombre usuario");
                                    String nombreUsuarioReseña = sc.nextLine();

                                    System.out.println("Nombre libro");
                                    String nombreLibroReseña = sc.nextLine();

                                    int idUsuarioReseña = usuarioDAO.obtenerIdUsuario(nombreUsuarioReseña);
                                    int idLibroReseña = libroDAO.obtenerIdLibro(nombreLibroReseña);

                                    if (idUsuarioReseña == -1 || idLibroReseña == -1) {
                                        System.out.println("Usuario o libro no encontrado");
                                        break;
                                    }
                                    System.out.println("Puntuacion");
                                    int puntuacion = sc.nextInt();
                                    sc.nextLine();

                                    System.out.println("Comentario");
                                    String comentario = sc.nextLine();

                                    reseñaDAO.crearReseña(nombreUsuarioReseña, nombreLibroReseña, puntuacion, comentario);
                                    historialDAO.guardarAccion(nombreUsuarioReseña, "crear_reseña", "Usuario añadió una reseña",
                                            new Document(
                                                    "detalle",
                                                    "El usuario " + nombreUsuarioReseña +
                                                            " hizo una reseña del libro " +
                                                            nombreLibroReseña
                                            )
                                    );
                                    break;
                                case 2:
                                    System.out.println("Nombre libro");
                                    String libroReseñas = sc.nextLine();

                                    reseñaDAO.mostrarReseñasLibro(libroReseñas);
                                    break;
                                case 3:
                                    System.out.println("Nombre usuario de la reseña");
                                    String usuarioReseña = sc.nextLine();

                                    System.out.println("Nombre libro de la reseña");
                                    String libroReseña = sc.nextLine();

                                    System.out.println("Nombre usuario que comenta");
                                    String nombreUsuarioComentario = sc.nextLine();

                                    int idUsuarioComentario = usuarioDAO.obtenerIdUsuario(nombreUsuarioComentario);
                                    if (idUsuarioComentario == -1) {
                                        System.out.println("Usuario no encontrado");
                                        break;
                                    }

                                    System.out.println("Comentario");
                                    String comentarioRespuesta = sc.nextLine();

                                    reseñaDAO.añadirComentario(usuarioReseña, libroReseña, nombreUsuarioComentario, comentarioRespuesta);

                                    historialDAO.guardarAccion(nombreUsuarioComentario, "comentario_reseña", "Usuario comentó una reseña",
                                            new Document(
                                                    "detalle",
                                                    "El usuario " + nombreUsuarioComentario +
                                                            " comentó una reseña del libro " + libroReseña
                                            )
                                    );
                                    break;
                                case 4:
                                    if (!adminLogueado) {
                                        System.out.println("No tienes permisos para ver el historial");
                                        break;
                                    }
                                    historialDAO.mostrarHistorial();
                                    break;
                                case 0:
                                    System.out.println("Saliendo");
                                    break;
                                default:
                                    System.out.println("Opcion incorrecta");
                            }
                        }
                        break;
                    case 0:
                        System.out.println("Programa finalizado");
                        break;
                    default:
                        System.out.println("Opcion incorrecta");
                        break;
                    case 4:
                        System.out.println("Nombre del libro para generar informe XML:");
                        String nombreLibroXML = sc.nextLine();

                        generarXML xml = new generarXML();
                        xml.generarInformeLibros(nombreLibroXML);

                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}