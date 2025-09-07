package com.springboot.registro_usuarios.repositories;

import com.springboot.registro_usuarios.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository

/*
La anotación @Repository se utiliza para indicar a Spring que una clase (no una interfaz)
es un "bean" de repositorio, lo que le permite a Spring detectar y manejar esa clase
como un componente de acceso a datos.

Sin embargo, en el caso de Spring Data JPA, la convención de extender JpaRepository
ya cumple esa función por mí. Spring es lo suficientemente inteligente como para escanear
y registrar todas las interfaces que extienden de sus interfaces base de repositorio,
sin necesidad de anotaciones adicionales.

Por lo tanto, añadir @Repository sería redundante, por ello lo comento.
 */
public interface UserRepository extends JpaRepository<User, Long> {/*
Esta línea es la clave.
Le estamos diciendo a Spring que nuestra interfaz UserRepository va a extender de JpaRepository.

JpaRepository es una interfaz de Spring Data JPA que ya viene con muchos métodos útiles
listos para usar, como save(), findById(), findAll(), y delete().

Los argumentos <User, Long> le indican a JpaRepository que trabajará con la entidad User
y que el tipo de dato de la clave primaria (id) es Long.
*/
    Optional<User> findByEmail(String email);/*
    Esta línea es un ejemplo del poder de Spring Data JPA.
    Simplemente declarando un método con una convención de nombres específica (findBy...),
    Spring es capaz de generar la consulta SQL necesaria para buscar un usuario por su email.

    findByEmail: El "findBy" indica una búsqueda, y "Email" es el nombre del atributo
    de nuestra clase User.
    Spring interpreta esto y crea la consulta SELECT * FROM users WHERE email = ?.

    Optional<User>: Este tipo de retorno es una buena práctica en Java.
    Un Optional es un contenedor que puede o no tener un valor.
    Si el email existe en la base de datos, el Optional contendrá un objeto User;
    de lo contrario, estará vacío. Esto nos ayuda a evitar los temidos NullPointerException.
     */
}
