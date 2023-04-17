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
import dao.student.StudentDao;

@Path("/students")
public class StudentResource {
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<Student> getstudents() {

        return StudentDao.instance.getStudents();
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("{studentId}")
    public Student getStudent(@PathParam("studentId") String id) {
        System.out.println("Hello.. Testing");
        return StudentDao.instance.getStudent(Integer.parseInt(id));
    }
    
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void postStudent(
//    		@FormParam("id") int id, 
    		@FormParam("name") String name,
            @FormParam("address") String address, 
			@FormParam("course") String course,
            @Context HttpServletResponse servletResponse) throws IOException {

    	int newId = StudentDao.instance.getNextId();
        Student student = new Student(newId, name, address, course);
//        student.setId(id);
//        student.setName(name);
//        student.setAddress(address);
//        student.setCourse(course);
        StudentDao.instance.create(student);
        servletResponse.sendRedirect("../../successfulAdd.html"); //postman
        //servletResponse.sendRedirect("../successfulAdd.html"); //form

    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void updateStudent(
        @PathParam("id") int id,
        @FormParam("name") String name,
        @FormParam("address") String address,
        @FormParam("course") String course,
        @Context HttpServletResponse servletResponse) throws IOException {

        Student student = StudentDao.instance.getStudent(id);

        if (student == null) {
            servletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        student.setName(name);
        student.setAddress(address);
        student.setCourse(course);
        StudentDao.instance.update(student);

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
    public Response deleteAll(){
        StudentDao.instance.deleteAll();
        return Response.status(200).entity("All students deleted").build();
    }
    
    @HEAD
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response doHead() {
    	return Response
    			.noContent()
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
}