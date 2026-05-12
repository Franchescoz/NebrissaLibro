
package com.example;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbUtil {


    // URL para base de datos en modo archivo (persistente en disco)
    public static final String JDBC_URL_FILE = "jdbc:h2:./data/tienda";


    // Usuario y contraseña por defecto de H2
    public static final String USER = "sa";
    public static final String PASSWORD = "";


    /**
     * Método para obtener una conexión a la base de datos H2
     * @return Connection activa
     * @throws SQLException si hay error de conexión
     */
    public static Connection getConnection() throws SQLException {
        // DriverManager devuelve un objeto Connection con la URL, usuario y contraseña
        return DriverManager.getConnection(JDBC_URL_FILE, USER, PASSWORD);
    }
}

