<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Add Reservation</title>
  <style>
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body { font-family: 'Segoe UI', sans-serif; background: #f0f4f8; color: #333; }
    header { background: #1a3c5e; color: #fff; padding: 18px 32px; display: flex; align-items: center; gap: 16px; }
    header a { color: #fff; text-decoration: none; font-size: 13px; opacity: .75; }
    header h2 { font-size: 20px; font-weight: 600; }
    .container { max-width: 520px; margin: 40px auto; background: #fff; border-radius: 10px; padding: 32px; box-shadow: 0 2px 10px rgba(0,0,0,.08); }
    label { display: block; font-size: 13px; font-weight: 600; margin-bottom: 4px; margin-top: 16px; }
    input { width: 100%; padding: 10px 12px; border: 1px solid #cdd5df; border-radius: 6px; font-size: 14px; }
    input:focus { outline: none; border-color: #1a3c5e; }
    input[readonly] { background: #f1f5f9; color: #1a3c5e; font-weight: 700; cursor: not-allowed; border-color: #e2e8f0; letter-spacing: 2px; }
    .hint { font-size: 11px; color: #888; margin-top: 3px; }
    button { margin-top: 24px; width: 100%; padding: 12px; background: #1a3c5e; color: #fff; border: none; border-radius: 6px; font-size: 15px; cursor: pointer; }
    button:hover { background: #14304d; }
    .msg-ok  { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; padding: 12px; border-radius: 6px; margin-bottom: 16px; }
    .msg-err { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; padding: 12px; border-radius: 6px; margin-bottom: 16px; }
    .amt-error-box { display: none; background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; padding: 10px 14px; border-radius: 6px; margin-top: 8px; font-size: 13px; font-weight: 600; }
    input.field-err { border-color: #c0392b; background: #fff5f5; }
  </style>
  <script>
    function validateForm() {
      var amtInput = document.getElementById('totalAmount');
      var amt = parseFloat(amtInput.value);
      var errorBox = document.getElementById('amtErrorBox');
      if (isNaN(amt) || amt < 0) {
        amtInput.classList.add('field-err');
        amtInput.focus();
        errorBox.style.display = 'block';
        return false;
      }
      errorBox.style.display = 'none';
      amtInput.classList.remove('field-err');
      return true;
    }
    function clearAmtError() {
      var amt = parseFloat(document.getElementById('totalAmount').value);
      if (!isNaN(amt) && amt >= 0) {
        document.getElementById('totalAmount').classList.remove('field-err');
        document.getElementById('amtErrorBox').style.display = 'none';
      }
    }
  </script>
</head>
<body>
  <header>
    <a href="<%= request.getContextPath() %>/index.jsp">&#8592; Home</a>
    <h2>Add Reservation</h2>
  </header>
  <div class="container">

    <%
      String msg = (String) request.getAttribute("message");
      if (msg != null) {
          boolean ok = msg.contains("successfully");
    %>
      <div class="<%= ok ? "msg-ok" : "msg-err" %>"><%= msg %></div>
    <% } %>

    <form method="post"
          action="<%= request.getContextPath() %>/AddReservationServlet"
          onsubmit="return validateForm()">

      <label>Reservation ID</label>
      <input type="text"
             value="<%= request.getAttribute("nextId") != null ? request.getAttribute("nextId") : "001" %>"
             readonly>
      <p class="hint">&#128274; Auto-generated — starts from 001, increments by 1 each booking</p>

      <label>Customer Name</label>
      <input type="text" name="customerName" required placeholder="Full name">

      <label>Room Number</label>
      <input type="text" name="roomNumber" required placeholder="e.g. 204">

      <label>Check-In Date</label>
      <input type="date" name="checkIn" required>

      <label>Check-Out Date</label>
      <input type="date" name="checkOut" required>

      <label>Total Amount (&#8377;)</label>
      <input type="number"
             step="0.01"
             min="0"
             name="totalAmount"
             id="totalAmount"
             required
             placeholder="e.g. 3500.00"
             oninput="clearAmtError()">
      <div class="amt-error-box" id="amtErrorBox">
        &#10060; Total Amount should not be negative!
      </div>

      <button type="submit">Book Reservation</button>
    </form>
  </div>
</body>
</html>