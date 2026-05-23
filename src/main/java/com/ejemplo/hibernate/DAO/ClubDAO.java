package com.ejemplo.hibernate.DAO;

import com.ejemplo.hibernate.model.Biblioteca;
import com.ejemplo.hibernate.model.Club;
import com.ejemplo.hibernate.model.Libro;
import com.ejemplo.hibernate.model.Usuario;
import com.ejemplo.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClubDAO {

    public void crearClub(String nombre,String descripcion,Biblioteca biblioteca) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Long existe = session.createQuery(
                    "SELECT COUNT(c) FROM Club c WHERE c.nombre = :nombre",
                    Long.class
            ).setParameter("nombre",nombre).uniqueResult();

            if (existe != null && existe > 0) {

                System.out.println("Ya existe un club con ese nombre");

                tx.commit();

                return;
            }

            Biblioteca bibliotecaPersistida = session.merge(biblioteca);

            Club club = new Club();

            club.setNombre(nombre);
            club.setDescripcion(descripcion);
            club.setFechaFundacion(java.time.LocalDate.now());
            club.setIdbiblioteca(bibliotecaPersistida);
            club.setNumIntegrantes(0);

            session.persist(club);

            tx.commit();

            System.out.println("Club creado");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void asignarLibro(int idClub,Libro libro) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Club club = session.get(Club.class,idClub);

            if (club != null) {

                Libro libroPersistido = session.merge(libro);

                club.setLibroActual(libroPersistido);

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

            Club club = session.get(Club.class,idClub);

            if (club != null) {

                club.setLibroActual(null);

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

    public void asignarAdmin(int idClub,Usuario usuario) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Club club = session.get(Club.class,idClub);

            if (club != null) {

                Usuario usuarioPersistido = session.merge(usuario);

                club.setUsuarioAdmin(usuarioPersistido);

                if (!club.getUsuarios().contains(usuarioPersistido)) {

                    club.getUsuarios().add(usuarioPersistido);
                }

                club.setNumIntegrantes(club.getUsuarios().size());

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

            Club club = session.get(Club.class,idClub);

            if (club != null) {

                club.setUsuarioAdmin(null);

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

    public void añadirUsuario(int idClub,Usuario usuario) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Club club = session.get(Club.class,idClub);

            if (club != null) {

                Usuario usuarioPersistido = session.merge(usuario);

                club.getUsuarios().add(usuarioPersistido);

                club.setNumIntegrantes(club.getUsuarios().size());

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

            Club club = session.get(Club.class,idClub);

            if (club != null) {

                Usuario usuarioPersistido = session.merge(usuario);

                club.getUsuarios().remove(usuarioPersistido);

                club.setNumIntegrantes(club.getUsuarios().size());

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
    public void mostrarDatosClub(String nombreClub) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Club club = session.createQuery(
                            "FROM Club WHERE nombre = :nombre",
                            Club.class
                    ).setParameter("nombre",nombreClub)
                    .setMaxResults(1)
                    .uniqueResult();

            if (club != null) {
                System.out.println("Datos del club");
                System.out.println("Nombre: " + club.getNombre());

                System.out.println("Descripcion: " + club.getDescripcion());

                System.out.println("Fecha fundacion: " + club.getFechaFundacion());

                System.out.println("Numero integrantes: " + club.getNumIntegrantes());

                if (club.getUsuarioAdmin() != null) {

                    System.out.println(
                            "Admin: " +
                                    club.getUsuarioAdmin().getNombre()
                    );

                } else {

                    System.out.println("Admin: Sin admin");
                }

                if (club.getLibroActual() != null) {

                    System.out.println(
                            "Libro actual: " +
                                    club.getLibroActual().getNombre()
                    );

                } else {

                    System.out.println("Libro actual: Sin libro");
                }

                System.out.println("Integrantes:");

                for (Usuario u : club.getUsuarios()) {

                    System.out.println("-"+u.getNombre());
                }

            } else {

                System.out.println("Club no encontrado");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Usuario obtenerUsuarioPorNombre(String nombre) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("FROM Usuario WHERE nombre = :nombre",Usuario.class).setParameter("nombre",nombre).uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    public Libro obtenerLibroPorNombre(String nombre) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("FROM Libro WHERE nombre = :nombre",Libro.class).setParameter("nombre",nombre).uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    public Integer obtenerIdClubPorNombre(String nombre) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Club club = session.createQuery(
                    "FROM Club WHERE nombre = :nombre",
                    Club.class
            ).setParameter("nombre", nombre).uniqueResult();

            if (club != null) {

                return club.getId();
            }

            return null;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}