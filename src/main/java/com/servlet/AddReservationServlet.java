package com.servlet;

import com.dao.ReservationDAO;
import com.model.Reservation;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

@WebServlet("/AddReservationServlet")
public class AddReservationServlet extends HttpServlet {

    private final ReservationDAO dao = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("nextId", String.format("%03d", dao.getNextReservationId()));
        req.getRequestDispatcher("reservationadd.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            String     name = req.getParameter("customerName").trim();
            String     room = req.getParameter("roomNumber").trim();
            Date       in   = Date.valueOf(req.getParameter("checkIn"));
            Date       out  = Date.valueOf(req.getParameter("checkOut"));
            BigDecimal amt  = new BigDecimal(req.getParameter("totalAmount"));

            // Block negative amount
            if (amt.compareTo(BigDecimal.ZERO) < 0) {
                req.setAttribute("message", "Total Amount should not be negative!");
                req.setAttribute("nextId", String.format("%03d", dao.getNextReservationId()));
                req.getRequestDispatcher("reservationadd.jsp").forward(req, res);
                return;
            }

            // ID = 0, MySQL will assign it automatically
            Reservation r = new Reservation(0, name, room, in, out, amt);
            boolean ok = dao.addReservation(r);

            req.setAttribute("message", ok ? "Reservation added successfully!" : "Failed to add reservation.");

        } catch (Exception e) {
            req.setAttribute("message", "Error: " + e.getMessage());
        }

        // Always load next ID after any action
        req.setAttribute("nextId", String.format("%03d", dao.getNextReservationId()));
        req.getRequestDispatcher("reservationadd.jsp").forward(req, res);
    }
}