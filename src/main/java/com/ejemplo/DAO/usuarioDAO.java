package com.ejemplo.DAO;

import com.ejemplo.DBUtil;

import java.sql.*;

public class usuarioDAO {


    public void insertarUsuario(String nombre,
                                String email,
                                String password,
                                boolean admin) {

        String sql = """
                INSERT INTO Usuario
                (nombre, email, password, admin)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setBoolean(4, admin);

            ps.executeUpdate();

            System.out.println("Usuario insertado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int obtenerIdUsuario(String nombre) {

        String sql = "SELECT id_usuario FROM Usuario WHERE nombre=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("id_usuario");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }





    public void eliminarUsuario(int idUsuario) {

        String sql = "DELETE FROM Usuario WHERE id_usuario=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            ps.executeUpdate();

            System.out.println("Usuario eliminado");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}