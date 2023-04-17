package dao.student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public enum StudentDaoDB {
	instance;

	Connection con;
	Statement stmt;

	private StudentDaoDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/SOA_REST?useTimezone=true&serverTimezone=UTC";
			con = DriverManager.getConnection(url, "root", "");
			stmt = con.createStatement();
		} catch (Exception e) {
			System.out.println("Error: failed to connect to the database\n" + e.getMessage());
		}
	}

	public List<Student> getStudents() {
		List<Student> students = new ArrayList<Student>();
		try {
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM STUDENT");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String address = rs.getString("address");
				String course = rs.getString("course");
				students.add(new Student(id, name, address, course));
			}
		} catch (Exception e) {
			System.out.println("Error geting a list of students\n" + e.getMessage());
		}
		return students;
	}

	public Student getStudent(int id) {
		Student student = null;
		try {
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM STUDENT WHERE id = " + id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int studentId = rs.getInt("id");
				String name = rs.getString("name");
				String address = rs.getString("address");
				String course = rs.getString("course");
				student = new Student(id, name, address, course);
			}
		} catch (Exception e) {
			System.out.println("Error geting a student with id = " + id + "\n" + e.getMessage());
		}
		return student;
	}

	public int create(Student student) {
		int result = 0;
		try {
			PreparedStatement preparedStatement = con
					.prepareStatement("insert into student values(null,?,?,?);");
			preparedStatement.setString(1, student.getName() );
			preparedStatement.setString(2, student.getAddress());
			preparedStatement.setString(3, student.getCourse());
			int row = preparedStatement.executeUpdate();
			result = 1;
		} catch (Exception e) {
			System.out.println("Error creating a student\n" + e.getMessage());
		}
		return result;
	}
	
	public int editStudent(Student student) {
	    int result = 0;
	    try {
	        PreparedStatement preparedStatement = con
	                .prepareStatement("REPLACE INTO student (id, name, address, course) VALUES (?, ?, ?, ?);");
	        preparedStatement.setInt(1, student.getId());
	        preparedStatement.setString(2, student.getName());
	        preparedStatement.setString(3, student.getAddress());
	        preparedStatement.setString(4, student.getCourse());
	        int row = preparedStatement.executeUpdate();
	        result = 1;
	    } catch (Exception e) {
	        System.out.println("Error editing a student\n" + e.getMessage());
	    }
	    return result;
	}
	
	public int deleteStudent(int id) {
	    int result = 0;
	    try {
	        PreparedStatement preparedStatement = con
	                .prepareStatement("DELETE FROM student WHERE id = ?;");
	        preparedStatement.setInt(1, id);
	        int row = preparedStatement.executeUpdate();
	        result = 1;
	    } catch (Exception e) {
	        System.out.println("Error deleting a student\n" + e.getMessage());
	    }
	    return result;
	}
	
	public int deleteAllStudents() {
	    int result = 0;
	    try {
	        PreparedStatement preparedStatement = con
	                .prepareStatement("DELETE FROM student;");
	        int row = preparedStatement.executeUpdate();
	        result = 1;
	    } catch (Exception e) {
	        System.out.println("Error deleting all students\n" + e.getMessage());
	    }
	    return result;
	}




}
