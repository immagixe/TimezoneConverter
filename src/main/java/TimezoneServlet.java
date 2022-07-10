import org.timezoneconverter.TimeZoneConverter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TimezoneServlet", value = "/TimezoneServlet")
public class TimezoneServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String fromTimestamp = request.getParameter("timestamp");
        String format = request.getParameter("format");
        String fromCity = request.getParameter("from");
        String toCity = request.getParameter("to");

        TimeZoneConverter timeZoneConverter = new TimeZoneConverter(fromTimestamp, format, fromCity, toCity);
        String result = timeZoneConverter.convert();
        PrintWriter pw = response.getWriter();
        pw.println(result);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
