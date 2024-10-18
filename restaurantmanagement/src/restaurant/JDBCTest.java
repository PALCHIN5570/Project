package restaurant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JDBCTest {
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println();
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "Palchin@5570");
			System.out.println("Connection Done");
			PreparedStatement ps = conn.prepareStatement("insert into hm(id,name) values(?,?)");
			ps.setInt(1, 88585);
			ps.setString(2, "jai");
			int i = ps.executeUpdate();
			System.out.println(i);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
