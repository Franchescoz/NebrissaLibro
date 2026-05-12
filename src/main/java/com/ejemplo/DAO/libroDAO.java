package com.ejemplo.DAO;

import com.ejemplo.model.libro;
import com.ejemplo.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class libroDAO {


    public void insertarLibro(libro l) {

        String sql = """
                INSERT INTO Libro
                (nombre, sinopsis, ISBN, tipo_libro, fecha_publicacion, id_autor)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, l.getNombre());
            ps.setString(2, l.getSinopsis());
            ps.setString(3, l.getISBN());
            ps.setString(4, l.getTipo_libro());
            ps.setDate(5, l.getFecha_publicacion());
            ps.setInt(6, l.getId_autor());

            ps.executeUpdate();

            System.out.println("Libro insertado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int obtenerIdLibro(String nombre) {

        String sql = "SELECT id_libro FROM Libro WHERE nombre=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("id_libro");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }


    public List<libro> mostrarLibros() {

        List<libro> lista = new ArrayList<>();

        String sql = "SELECT * FROM Libro";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                libro l = new libro(
                        rs.getString("nombre"),
                        rs.getString("sinopsis"),
                        rs.getString("ISBN"),
                        rs.getString("tipo_libro"),
                        rs.getDate("fecha_publicacion"),
                        rs.getInt("id_autor")
                );

                lista.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public void eliminarLibro(int idLibro) {

        String sql = "DELETE FROM Libro WHERE id_libro=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);

            ps.executeUpdate();

            System.out.println("Libro eliminado");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}