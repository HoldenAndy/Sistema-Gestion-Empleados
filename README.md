<div class="container">
        <header>
            <h1>Sistema de Gestión de RRHH</h1>
            <p>Documentación Técnica del Sistema</p>
        </header>
        <div class="content">
            <div class="section">
                <h2>Descripción General del Proyecto</h2>
                <p>Este proyecto es una <strong>aplicación backend de gestión de recursos humanos</strong> desarrollada con Spring Boot. El sistema permite administrar empleados, asignar tareas, realizar seguimiento de su desempeño y gestionar la autenticación de usuarios mediante tokens JWT.</p>      
                <h3>Tipos de Usuarios</h3>
                <div class="user-types">
                    <div class="user-card">
                        <h4>Administradores (ADMIN)</h4>
                        <p>Tienen control total sobre empleados y tareas del sistema</p>
                    </div>
                    <div class="user-card">
                        <h4>Empleados (EMPLOYEE)</h4>
                        <p>Pueden ver y actualizar sus propias tareas asignadas</p>
                    </div>
                </div>
            </div>
            <div class="section">
                <h2>Tecnologías Utilizadas</h2>
                <div class="tech-grid">
                    <div class="tech-category">
                        <h4>Framework Principal</h4>
                        <ul class="tech-list">
                            <li><strong>Spring Boot:</strong> Framework base para la aplicación</li>
                            <li><strong>Spring Security:</strong> Gestión de seguridad y autenticación</li>
                            <li><strong>Spring Data JPA:</strong> Capa de persistencia y acceso a datos</li>
                        </ul>
                    </div>
                    <div class="tech-category">
                        <h4>Seguridad</h4>
                        <ul class="tech-list">
                            <li><strong>JWT:</strong> Autenticación stateless mediante tokens</li>
                            <li><strong>Auth0 JWT Library:</strong> Generación y validación de tokens</li>
                            <li><strong>BCrypt:</strong> Encriptación de contraseñas</li>
                        </ul>
                    </div>
                    <div class="tech-category">
                        <h4>Base de Datos</h4>
                        <ul class="tech-list">
                            <li><strong>JPA/Hibernate:</strong> ORM para mapeo objeto-relacional</li>
                            <li><strong>MySQL:</strong> Base de datos relacional</li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="section">
                <h2>Endpoints REST de la API</h2>
                <div class="endpoint-section">
                    <h3>AuthController (/api/auth)</h3>
                    <div class="endpoint">
                        <span class="method post">POST</span>
                        <span class="path">/login</span>
                        <p>Autentica usuario y devuelve token JWT más flag de primer login</p>
                    </div>
                    <div class="endpoint">
                        <span class="method get">GET</span>
                        <span class="path">/user/data</span>
                        <p>Endpoint de prueba para verificar autenticación</p>
                    </div>
                    <div class="endpoint">
                        <span class="method post">POST</span>
                        <span class="path">/change-password</span>
                        <span class="role-badge">Autenticado</span>
                        <p>Permite cambiar contraseña (requiere autenticación)</p>
                    </div>
                </div>
            <div class="section">
                <h2>Flujo de Funcionamiento</h2>
                <h3>Inicialización del Sistema</h3>
                <p>Al arrancar la aplicación, se ejecuta un CommandLineRunner que:</p>
                <ol class="flow-steps">
                    <li>Verifica si existe el usuario administrador</li>
                    <li>Si no existe, crea un usuario admin con credenciales predefinidas</li>
                    <li>Crea un empleado asociado al usuario admin</li>
                </ol>
                <div class="credentials">
                    <strong>Credenciales por defecto:</strong> admin@empresa.com / admin123
                </div>
            </div>
                <div class="endpoint-section">
                    <h3>EmployeeController (/api/employees)</h3>
                    <div class="endpoint">
                        <span class="method get">GET</span>
                        <span class="path">/</span>
                        <span class="role-badge">ADMIN</span>
                        <p>Lista todos los empleados</p>
                    </div>
                    <div class="endpoint">
                        <span class="method post">POST</span>
                        <span class="path">/</span>
                        <span class="role-badge">ADMIN</span>
                        <p>Crea nuevo empleado con su usuario asociado</p>
                    </div>
                    <div class="endpoint">
                        <span class="method put">PUT</span>
                        <span class="path">/{id}</span>
                        <span class="role-badge">ADMIN</span>
                        <p>Actualiza información del empleado</p>
                    </div>
                    <div class="endpoint">
                        <span class="method delete">DELETE</span>
                        <span class="path">/{id}</span>
                        <span class="role-badge">ADMIN</span>
                        <p>Elimina empleado y su usuario</p>
                    </div>
                </div>
                <div class="endpoint-section">
                    <h3>TaskController (/api/tasks)</h3>
                    <div class="endpoint">
                        <span class="method post">POST</span>
                        <span class="path">/assign</span>
                        <span class="role-badge">ADMIN</span>
                        <p>Asigna nueva tarea a un empleado</p>
                    </div>
                    <div class="endpoint">
                        <span class="method put">PUT</span>
                        <span class="path">/update-status/{taskId}</span>
                        <span class="role-badge">ADMIN/EMPLOYEE</span>
                        <p>Actualiza estado de tarea</p>
                    </div>
                    <div class="endpoint">
                        <span class="method get">GET</span>
                        <span class="path">/employee/{employeeId}</span>
                        <span class="role-badge">ADMIN/EMPLOYEE</span>
                        <p>Obtiene todas las tareas de un empleado</p>
                    </div>
                </div>
            </div>
            <div class="section">
                <h2>Flujo de Autenticación</h2>
                <ol class="flow-steps">
                    <li>Usuario envía credenciales a <code>/api/auth/login</code></li>
                    <li>AuthService valida email y contraseña</li>
                    <li>Si son válidos, JwtService genera un token JWT</li>
                    <li>Se devuelve el token y el flag firstLogin</li>
                    <li>Cliente incluye el token en header "Authorization: Bearer {token}"</li>
                    <li>JwtAuthFilter intercepta, valida el token y establece autenticación</li>
                    <li>Spring Security verifica permisos según rol</li>
                </ol>
            </div>
            <div class="section">
                <h2>Flujo de Primer Login</h2>
                <ol class="flow-steps">
                    <li>Usuario nuevo inicia sesión y recibe <code>firstLogin: true</code></li>
                    <li>Frontend debe redirigir a cambio de contraseña obligatorio</li>
                    <li>Usuario llama a <code>/api/auth/change-password</code></li>
                    <li>Sistema actualiza contraseña y cambia <code>firstLogin</code> a false</li>
                    <li>En siguientes logins, puede acceder normalmente</li>
                </ol>
            </div>
            <div class="section">
                <h2>Flujo de Gestión de Tareas</h2>
                <ol class="flow-steps">
                    <li>Admin asigna tarea con estado "PENDING"</li>
                    <li>Empleado consulta sus tareas</li>
                    <li>Al completar, empleado actualiza estado a "COMPLETED"</li>
                    <li>Sistema automáticamente incrementa performanceScore del empleado en 10 puntos</li>
                    <li>Este sistema de puntuación permite evaluar el desempeño del empleado</li>
                </ol>
            </div>
            <div class="section">
                <h2>Casos de Uso Principales</h2>
                <div class="use-case-grid">
                    <div class="use-case-card">
                        <h4>Para Administradores</h4>
                        <ul>
                            <li>Gestionar empleados (crear, actualizar, eliminar)</li>
                            <li>Asignar tareas a empleados</li>
                            <li>Ver estado de todas las tareas</li>
                            <li>Actualizar estado de tareas</li>
                            <li>Monitorear desempeño mediante performanceScore</li>
                        </ul>
                    </div>
                    <div class="use-case-card">
                        <h4>Para Empleados</h4>
                        <ul>
                            <li>Cambiar contraseña en primer login</li>
                            <li>Ver tareas asignadas</li>
                            <li>Actualizar estado de sus tareas</li>
                            <li>Acumular puntos de desempeño al completar tareas</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
