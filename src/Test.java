public class Test {

    public static String encrypt(final String text, final int n) {
        // Your code here
        if (text==null) return null;
        if(text.isEmpty() || n<=0) {return text;}
        else {
            String s = "";
            String end = "";
            int len = text.length();
            for (int i = 0; i < len; i++) {
                if (i % 2 == 1) {
                    s = s + text.toCharArray()[i];
                } else {
                    end = end + text.toCharArray()[i];
                }
            }
            return encrypt(s+end, n-1);
        }
    }

    public static String decrypt(final String encryptedText, final int n) {
        // Your code here
        if (encryptedText==null) return null;
        if(encryptedText.isEmpty() || n<=0) {return encryptedText;}
        else {
            int len = encryptedText.length();
            char[] c = new char[len];
            int counter = 1;
            for (int i = 0; i < len/2; i++) {
                c[counter] = encryptedText.toCharArray()[i];
                counter ++;
                counter ++;
            }
            counter = 0;
            for (int i = len/2; i < len; i++) {
                c[counter] = encryptedText.toCharArray()[i];
                counter ++;
                counter ++;
            }

            return decrypt(new String(c), n-1);
        }
    }


}