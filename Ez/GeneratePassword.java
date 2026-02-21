import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Password123";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Contraseña: " + rawPassword);
        System.out.println("Hash BCrypt: " + encodedPassword);
    }
}
