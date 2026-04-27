package com.servlet;

import com.dao.ReservationDAO;
import com.model.Reservation;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {

    private final ReservationDAO dao = new ReservationDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Read directly from request parameters
        String reportType = req.getParameter("reportType");
        String fromStr    = req.getParameter("fromDate");
        String toStr      = req.getParameter("toDate");

        req.setAttribute("reportType", reportType);

        try {
            if (reportType == null || reportType.isEmpty()) {
                req.setAttribute("error", "Please select a report type.");

            } else if (reportType.equals("dateRange")) {
                if (fromStr == null || fromStr.isEmpty() || toStr == null || toStr.isEmpty()) {
                    req.setAttribute("error", "Please select both From and To dates.");
                } else {
                    Date from = Date.valueOf(fromStr);
                    Date to   = Date.valueOf(toStr);
                    List<Reservation> results = dao.getReservationsByDateRange(from, to);
                    req.setAttribute("reservations", results);
                    req.setAttribute("fromDate", fromStr);
                    req.setAttribute("toDate",   toStr);
                }

            } else if (reportType.equals("mostBooked")) {
                List<Map<String, Object>> rooms = dao.getMostBookedRooms();
                req.setAttribute("roomStats", rooms);

            } else if (reportType.equals("revenue")) {
                if (fromStr == null || fromStr.isEmpty() || toStr == null || toStr.isEmpty()) {
                    req.setAttribute("error", "Please select both From and To dates.");
                } else {
                    Date from = Date.valueOf(fromStr);
                    Date to   = Date.valueOf(toStr);
                    BigDecimal revenue = dao.getTotalRevenue(from, to);
                    req.setAttribute("revenue",  revenue);
                    req.setAttribute("fromDate", fromStr);
                    req.setAttribute("toDate",   toStr);
                }

            } else {
                req.setAttribute("error", "Unknown report type: " + reportType);
            }

        } catch (Exception e) {
            req.setAttribute("error", "Error generating report: " + e.getMessage());
            e.printStackTrace();
        }

        req.getRequestDispatcher("report_result.jsp").forward(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendRedirect(req.getContextPath() + "/report_form.jsp");
    }
}