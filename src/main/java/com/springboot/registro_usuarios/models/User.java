package com.springboot.registro_usuarios.models;

import jakarta.persistence.*;

/*
JPA significa Java Persistence API,
una especificación estándar para el mapeo de objetos relacionales (ORM) en Java
que simplifica la persistencia de datos entre objetos Java y bases de datos relacionales.
 */

@Entity /*
Esta notación sirve para indicarle a JPA que la clase es una entidad
y que debe ser mapeada a una base de datos.
 */
@Table(name = "users") /*
Opcional, pero muy buena práctica.
Le dice a JPA que el nombre que debe tener la tabla en la base de datos.
*/
public class User {

    @Id/*
    Le dice a JPA que este atributo (id) es la clave primaria de la tabla.
    */
    @GeneratedValue(strategy = GenerationType.IDENTITY)/*
    Esta anotación le dice a la base de datos que genere automáticamente el valor del ID.
    La estrategia IDENTITY es la más común para esto.
    */
    private Long id;

    @Column(unique = true, nullable = false)/*
    Esta anotación permite definir las propiedades de una columna.
    La propiedad unique en true es para indicar que el atributo debe ser único,
    y el nullable es para indicar que el atributo no puede ser null, debe contener datos.
    */
    private String email;

    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
