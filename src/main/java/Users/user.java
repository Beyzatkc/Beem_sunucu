package Users;

import jakarta.persistence.*;

import java.net.URL;
import java.time.LocalDateTime;

@Entity
@Table(name="kisi")
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false,unique = true)
    private String Username;

    @Column(nullable = false,unique = true)
    private String Email;

    @Column(nullable = false)
    private String Password;

    @Column(nullable = false)
    private String Name;

    @Column(nullable = false,unique = true)
    private String Surname;

    @Column(nullable = true)
    private String Profile;

    @Column(nullable = false)
    private LocalDateTime Date;

    @Column(nullable = true)
    private String Biography;
}
