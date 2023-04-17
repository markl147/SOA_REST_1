package resource;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.student.Student;
import dao.student.StudentDaoDB;

@SuppressWarnings("unused")
@Path("/studentsDB")
public class StudentResourceDB {
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Student> getStudents() {
		System.out.println("Hello ... Testing");
		return StudentDaoDB.instance.getStudents();
	}
	
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("{studentId}")
	public Student getStudent(@PathParam("studentId") String id) {
		return StudentDaoDB.instance.getStudent(Integer.parseInt(id));
	}
	
	
	@POST
	@Produces (MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void postStudent( 
			@FormParam("name") String name, 
			@FormParam("address") String address,
			@FormParam("course") String course,
			@Context HttpServletResponse servletResponse) throws IOException {
		Student student = new Student(name, address, course);
		int result = StudentDaoDB.instance.create(student);
		
		if (result == 1) {
			servletResponse.sendRedirect("../../successfulAdd.html");
		}
		
	}


	    // 1. Edit a student's details or add a new student if the specified student does not exist
	    @PUT
	    @Path("/{id}")
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	    public Response editStudent(
	            @PathParam("id") int id,
	            @FormParam("name") String name,
	            @FormParam("address") String address,
	            @FormParam("course") String course) {

	        Student student = new Student(id, name, address, course);
	        int result = StudentDaoDB.instance.editStudent(student);

	        if (result == 1) {
	            return Response.status(Response.Status.OK).entity("Student edited or added").build();
	        } else {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error editing or adding student").build();
	        }
	    }

	    // 2. Delete one student specified by an id parameter
	    @DELETE
	    @Path("/{id}")
	    public Response deleteStudent(@PathParam("id") int id) {
	        int result = StudentDaoDB.instance.deleteStudent(id);

	        if (result == 1) {
	            return Response.status(Response.Status.OK).entity("Student deleted").build();
	        } else {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting student").build();
	        }
	    }

	    // 3. Delete all students
	    @DELETE
	    public Response deleteAllStudents() {
	        int result = StudentDaoDB.instance.deleteAllStudents();

	        if (result == 1) {
	            return Response.status(Response.Status.OK).entity("All students deleted").build();
	        } else {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting students").build();
	        }
	    }
	
	/*
	@PUT
	@Produces (MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("{studentId}")
	public void putStudent( @PathParam("studentId") String id,
			@FormParam("name") String name, 
			@FormParam("address") String address,
			@FormParam("course") String course,
			@Context HttpServletResponse servletResponse) throws IOException {
		Student student = new Student(Integer.parseInt(id), name, address, course);
		StudentDao.instance.edit( student, Integer.parseInt(id));
		servletResponse.sendRedirect("../../index.html");
	}
	
	@DELETE
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("{studentId}")
	public Response deleteStudent(@PathParam("studentId") String id,
			@Context HttpServletResponse servletResponse) throws IOException {
		StudentDao.instance.delete(Integer.parseInt(id));
		return Response.status(200).entity("Operation Successful").build();
	}
	
	@DELETE
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response deleteAllStudents( @Context HttpServletResponse servletResponse) throws IOException {
		StudentDao.instance.deleteAll();
		return Response.status(200).entity("Operation Successful").build();
	}
	
	@HEAD
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response doHead() {
		return Response.noContent()
				.header("Accept", "application_xml")
				.header("Accept", "application_json")
				.status(Response.Status.NO_CONTENT)
				.build();

	}
	
	@OPTIONS
	public Response doOptions() {
		return Response.noContent()
				.header("Allow",  "OPTIONS, HEAD, GET, POST, PUT, DELETE")
				.build();
		}
		*/
}
