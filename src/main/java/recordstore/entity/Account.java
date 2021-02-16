package recordstore.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import recordstore.validation.ValidEmail;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "accounts", schema = "recordstore")
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_name")
    @NotBlank(message = "Field is mandatory")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Field is mandatory")
    private String password;

    @Column(name = "email")
    @ValidEmail
    @NotBlank(message = "Field is mandatory")
    private String email;

    @Column(name = "enabled")
    private boolean enabled = false;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = Collections.singleton(new Role(1L, "ROLE_USER"));

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (getId() != account.getId()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}