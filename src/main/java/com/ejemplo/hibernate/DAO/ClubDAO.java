package com.ejemplo.hibernate.DAO;

import com.ejemplo.hibernate.model.Biblioteca;
import com.ejemplo.hibernate.model.Club;
import com.ejemplo.hibernate.model.Libro;
import com.ejemplo.hibernate.model.Usuario;
import com.ejemplo.hibernate.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClubDAO {

    public void crearClub(String nombre, int numIntegrantes, String descripcion, Biblioteca biblioteca) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Club club = new Club();

            club.setNombre(nombre);
            club.setNumIntegrantes(numIntegrantes);
            club.setDescripcion(descripcion);
            club.setFechaFundacion(java.time.LocalDate.now());
            club.setIdbiblioteca(biblioteca);

            Usuario usuarioVacio = new Usuario();
            usuarioVacio.setNombre("SIN ADMIN");

            Libro libroVacio = new Libro();
            libroVacio.setNombre("SIN LIBRO");

            session.persist(usuarioVacio);
            session.persist(libroVacio);

            club.setUsuarioAdmin(usuarioVacio);
            club.setLibroActual(libroVacio);

            session.persist(club);

            tx.commit();

            System.out.println("Club creado");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void asignarLibro(
            int idClub,
            Libro libro
    ) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Club club = session.get(Club.class, idClub);

            if (club != null) {

                club.setLibroActual(libro);

                session.merge(club);

                System.out.println("Libro añadido al club");

            } else {

                System.out.println("Club no encontrado");
            }

            tx.commit();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void quitarLibro(int idClub) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Club club = session.get(Club.class, idClub);

            if (club != null) {

                Libro libroVacio = new Libro();

                libroVacio.setNombre("SIN LIBRO");

                session.persist(libroVacio);

                club.setLibroActual(libroVacio);

                session.merge(club);

                System.out.println("Libro eliminado del club");

            } else {

                System.out.println("Club no encontrado");
            }

            tx.commit();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void asignarAdmin(
            int idClub,
            Usuario usuario
    ) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Club club = session.get(Club.class, idClub);

            if (club != null) {

                club.setUsuarioAdmin(usuario);

                session.merge(club);

                System.out.println("Admin añadido al club");

            } else {

                System.out.println("Club no encontrado");
            }

            tx.commit();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void quitarAdmin(int idClub) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Club club = session.get(Club.class, idClub);

            if (club != null) {

                Usuario usuarioVacio = new Usuario();

                usuarioVacio.setNombre("SIN ADMIN");

                session.persist(usuarioVacio);

                club.setUsuarioAdmin(usuarioVacio);

                session.merge(club);

                System.out.println("Admin eliminado del club");

            } else {

                System.out.println("Club no encontrado");
            }

            tx.commit();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public void añadirUsuario(int idClub, Usuario usuario) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Club club = session.get(Club.class, idClub);

            if (club != null) {

                club.getUsuarios().add(usuario);

                session.merge(club);

                System.out.println("Usuario añadido al club");

            } else {

                System.out.println("Club no encontrado");
            }

            tx.commit();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void quitarUsuario(int idClub,Usuario usuario) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Club club = session.get(Club.class, idClub);

            if (club != null) {

                club.getUsuarios().remove(usuario);

                session.merge(club);

                System.out.println("Usuario eliminado del club");

            } else {

                System.out.println("Club no encontrado");
            }

            tx.commit();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public Usuario obtenerUsuarioPorNombre(String nombre) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "FROM Usuario u WHERE u.nombre = :nombre",
                            Usuario.class
                    )
                    .setParameter("nombre", nombre)
                    .uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}