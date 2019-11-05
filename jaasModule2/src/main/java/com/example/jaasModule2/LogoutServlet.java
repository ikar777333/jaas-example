package com.example.jaasModule2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "logoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request,
		HttpServletResponse response) 
			throws ServletException, IOException {
		
		// Invalidate current HTTP session.
		// Will call LoginModule logout() method
		request.getSession().invalidate();
		
		response.sendRedirect(request.getContextPath() + "/admin/admin.jsp");
		
	}

}
