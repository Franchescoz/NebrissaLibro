package com.ejemplo.xml;

import com.ejemplo.DBUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.sql.*;

public class generarXML {

    public void generarInformeLibros(String nombreLibroFiltro) {

        try (Connection con = DBUtil.getConnection()) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("informe_libros");
            doc.appendChild(root);

            String sql = """
                SELECT 
                    l.id_libro,
                    l.nombre AS libro_nombre,
                    l.sinopsis,
                    l.ISBN,
                    a.nombre AS autor_nombre,
                    a.bibliografía,
                    a.fecha_nacimiento
                FROM Libro l
                JOIN Autor a ON l.id_autor = a.id_autor
                WHERE l.nombre = ?
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreLibroFiltro);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int idLibro = rs.getInt("id_libro");
                int prestamos = contarPrestamos(con, idLibro);

                Element libro = doc.createElement("libro");

                libro.appendChild(crearNodo(doc, "nombre", rs.getString("libro_nombre")));
                libro.appendChild(crearNodo(doc, "sinopsis", rs.getString("sinopsis")));
                libro.appendChild(crearNodo(doc, "ISBN", rs.getString("ISBN")));
                libro.appendChild(crearNodo(doc, "numero_prestamos", String.valueOf(prestamos)));

                Element autor = doc.createElement("autor");

                autor.appendChild(crearNodo(doc, "nombre", rs.getString("autor_nombre")));
                autor.appendChild(crearNodo(doc, "bibliografía", rs.getString("bibliografía")));
                autor.appendChild(crearNodo(doc, "fecha_nacimiento", String.valueOf(rs.getDate("fecha_nacimiento"))));

                libro.appendChild(autor);
                root.appendChild(libro);
            }


            String fileName = "INFOR - " + nombreLibroFiltro + ".xml";

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
            System.out.println("XML generado correctamente: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Element crearNodo(Document doc, String nombre, String valor) {
        Element nodo = doc.createElement(nombre);
        nodo.appendChild(doc.createTextNode(valor != null ? valor : ""));
        return nodo;
    }
    private int contarPrestamos(Connection con, int idLibro) throws SQLException {

        String sql = """
            SELECT COUNT(*) AS total
            FROM Prestamo
            WHERE id_libro = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
}