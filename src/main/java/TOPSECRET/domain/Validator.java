package TOPSECRET.domain;

public class Validator {

    public static boolean isNullOrBlank(String... strings) {

        // String... strings is a varargs (variable number of arguments).
        // It allows the method to accept zero or more String arguments.
        // Inside the method, the arguments are handled as a String[] array.


        boolean isInvalid = false;

        // Loop through each String in 'strings' one by one.
        for (String toValidate : strings) {
            if (toValidate == null || toValidate.isBlank()) {
                isInvalid = true;
            }
        }
        return isInvalid;
    }
}
