package com.ejemplo.DAO;

import com.ejemplo.DBUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class prestamoDAO {


    public void hacerPrestamo(int idUsuario, int idLibro) {

        String sql = """
                INSERT INTO Prestamo
                (id_usuario, id_libro, fecha_prestamo, estado, fecha_devolucion)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idLibro);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setString(4, "PRESTADO");
            ps.setDate(5, Date.valueOf(LocalDate.now().plusDays(30)));

            ps.executeUpdate();

            System.out.println("Prestamo realizado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void devolverPrestamo(int idUsuario, int idLibro) {

        String sql = """
                DELETE FROM Prestamo
                WHERE id_usuario=? AND id_libro=?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idLibro);

            int filas = ps.executeUpdate();

            if (filas > 0) {

                System.out.println("Prestamo eliminado");

            } else {

                System.out.println("No existe ese prestamo");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}