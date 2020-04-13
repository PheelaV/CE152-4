package lab4;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
    private String password;

    public Password(String password) {
        this.password = password;
    }

    public Password() {
    }

    void setPassword(String password){
        this.password = password;
    }

    void showPassword(){
        System.out.println(this.password);
    }

    String getMD5(){

        return getMD5(this.password);
    }

    public static String getMD5(String password){
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            for (byte x:bytes){
                sb.append(String.format("%02x", x));
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
