package hotel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Validator {

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) return false;
        // 10 digits starting with 7, 8, or 9
        String phoneRegex = "^[789]\\d{9}$";
        Pattern pat = Pattern.compile(phoneRegex);
        return pat.matcher(phone).matches();
    }

    public static boolean isValidIdProof(String idProof) {
        if (idProof == null || idProof.isEmpty()) return false;
        // Aadhar pattern: 12 digits
        String aadharRegex = "^\\d{12}$";
        // PAN pattern: 5 letters, 4 digits, 1 letter
        String panRegex = "^[A-Za-z]{5}\\d{4}[A-Za-z]{1}$";
        
        return idProof.matches(aadharRegex) || idProof.matches(panRegex);
    }

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || !dateStr.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static java.sql.Date parseSqlDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
            Date d = sdf.parse(dateStr);
            return new java.sql.Date(d.getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isValidRating(double rating) {
        return rating >= 0.0 && rating <= 5.0;
    }

    public static boolean isValidSalary(double salary) {
        return salary >= 0.0;
    }

    public static boolean isBeforeOrEqual(String dateBeforeStr, String dateAfterStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            Date d1 = sdf.parse(dateBeforeStr);
            Date d2 = sdf.parse(dateAfterStr);
            return !d1.after(d2); // True if d1 is before or equal to d2
        } catch (ParseException e) {
            return false;
        }
    }
}
