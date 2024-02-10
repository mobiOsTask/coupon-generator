package org.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Prasad
 * Date: 1/6/2020
 * Time: 3:00 PM
 */
public class MobileNoValidator {

    /**
     * @param selectedNumber The mobile number to be get valid mobile number
     * @return return mobile number (94*********)
     */
    public static String getValidNumber(String selectedNumber) {
        String validNumber = "";
        if (selectedNumber != null) {
            selectedNumber = selectedNumber.trim();

            if (selectedNumber.length() == 11) {
                validNumber = selectedNumber;
            } else if (selectedNumber.length() == 10) {

                selectedNumber = selectedNumber.replaceFirst("0", "94");
                validNumber = selectedNumber;

            } else if (selectedNumber.length() == 9) {
                selectedNumber = "94" + selectedNumber;
                validNumber = selectedNumber;

            } else if (selectedNumber.length() == 12) {
                selectedNumber = selectedNumber.replaceFirst("\\+", "");
                validNumber = selectedNumber;

            } else if (selectedNumber.length() == 13) {
                selectedNumber = selectedNumber.replaceFirst("0094", "94");
                validNumber = selectedNumber;
            }
        }

        return validNumber;
    }

    /**
     * @param number The mobile number
     * @return return operator Name
     */
    public static String getOperator(String number) {
        String substring = number.substring(2, 4);
        switch (substring) {
            case "75":
                return "AIRTEL";
            case "70":
                return "MOBITEL";
            case "71":
                return "MOBITEL";
            case "72":
                return "ETISALAT";
            case "78":
                return "HUTCH";
            case "77":
                return "DIALOG";
            case "74":
                return "DIALOG";
            case "76":
                return "DIALOG";
            default:
                return "OTHER";
        }
    }

    /**
     * @param number The mobile number
     * @return return operator code (two digits)
     */
    public static String getOperatorCode(String number) {
        return number.substring(2, 4);
    }

    /**
     * @param number The mobile number to be matched
     * @return return boolean result is valid mobile number
     */
    public static Boolean checkValidNumber(String number) {

        if (number != null) {
            Pattern p = Pattern.compile("^(?:0|94|\\+94)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|912)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$");
            Matcher matcher = p.matcher(number);
            return (matcher.find() && matcher.group().equals(number));
        }
        return false;

    }

}
