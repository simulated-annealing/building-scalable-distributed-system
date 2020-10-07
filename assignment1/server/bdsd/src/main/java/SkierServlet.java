import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.alibaba.fastjson.JSONObject;

import java.util.regex.*;

public class SkierServlet extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String url = request.getPathInfo();
        if (!isUrlValid(url)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        // json validation
        try {
            BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            JSONObject.parseObject(responseStrBuilder.toString());
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing parameters");
            return;
        }

        if (!isUrlValid(urlPath)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("{}");// return a dummy json.
            res.getWriter().flush();
        }
    }

    private boolean isUrlValid(String url) {
        return true;
        /*
        // GET /skiers/{skierID}/vertical
        Pattern pat1 = Pattern.compile("\\/[0-9]+\\/vertical");
        // GET /skiers/{resortID}/days/{dayID}/skiers/{skierID}
        Pattern pat2 = Pattern.compile("\\/[A-Za-z0-9]+\\/days\\/[0-9]+\\/skiers\\/[0-9]+");
        // POST /skiers/liftrides
        Pattern pat3 = Pattern.compile("\\/liftrides");

        return pat1.matcher(url).matches() || pat2.matcher(url).matches() || pat3.matcher(url).matches();
         */
    }
}
