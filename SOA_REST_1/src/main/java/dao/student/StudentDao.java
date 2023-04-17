package dao.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum StudentDao {
	
	instance;
	
	private Map<Integer, Student> studentsMap = new HashMap<Integer, Student>();

	private StudentDao() {
		Student stud1 = new Student();
		stud1.setId(1);
		stud1.setName("Joe Bloggs");
		stud1.setAddress("Athlone");
		stud1.setCourse("Software Engineering");
		studentsMap.put(1, stud1);
		
		Student stud2 = new Student();
		stud2.setId(2);
		stud2.setName("Jane Doe");
		stud2.setAddress("Mullingar");
		stud2.setCourse("Mechanical Engineering");
		studentsMap.put(2, stud2);
	}
	
	public List<Student> getStudents() {
        List<Student> students = new ArrayList<Student>();
        students.addAll(studentsMap.values());
        return students;
    }
	
	public Student getStudent(int id) {
        return studentsMap.get(id);
    }

    public void create(Student student) {
        studentsMap.put(student.getId(), student);
        System.out.println("Student created");
    }
    
    public void update(Student updatedStudent) {
        if (studentsMap.containsKey(updatedStudent.getId())) {
            studentsMap.put(updatedStudent.getId(), updatedStudent);
            System.out.println("Student updated");
        } else {
            throw new IllegalArgumentException("Student not found");
        }
    }


    public void delete(int id) {
        if (studentsMap.remove(id) != null) {
            System.out.println("Removed");
        } else {
            System.out.println("Not Removed");
        }
    }
    
    public void deleteAll() {
        studentsMap.clear();
        System.out.println("All students removed");
    }
    
    public int getNextId() {
        return Collections.max(studentsMap.keySet()) + 1;
    }

}