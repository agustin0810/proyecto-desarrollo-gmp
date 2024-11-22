import Config.PostgresException;
import Dominio.Cliente;
import Dominio.Cuota;
import Fachada.*;

import java.sql.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    private static ClienteFachada clienteFachada = new ClienteFachada();
    private static AdministradorFachada administradorFachada = new AdministradorFachada();
    private static FuncionalidadFachada funcionalidadFachada = new FuncionalidadFachada();
    private static PerfilFachada perfilFachada = new PerfilFachada();
    private static PagoFachada pagoFachada = new PagoFachada();
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean isLoggedIn = false;
        boolean isAdmin = false; // true = administrador, false = cliente
        int opcion;

        // Login único para administradores y clientes
        while (!isLoggedIn) {
            System.out.print("\n=== Sistema de Gestión ASUR ===\nIngrese Correo: ");
            String correo = scanner.nextLine();
            System.out.print("Ingrese Contraseña: ");
            String contrasena = scanner.nextLine();

            // Intentar login como administrador
            try {
                isLoggedIn = administradorFachada.loginUsuario(correo, contrasena);
            } catch (PostgresException e) {
                System.out.println(e.getMessage());
            }
            if (isLoggedIn) {
                System.out.println("¡Bienvenido, Administrador!");
                isAdmin = true;
                break;
            }

            // Intentar login como cliente
            try {
                isLoggedIn = clienteFachada.loginUsuario(correo, contrasena);
            } catch (PostgresException e) {
                System.out.println(e.getMessage());
            }
            if (isLoggedIn) {
                System.out.println("¡Bienvenido, Cliente!");
                isAdmin = false;
            }

            if (!isLoggedIn) {
                System.out.println("Credenciales inválidas. Intente nuevamente.");
            }
        }

        // Menú principal
        do {
            System.out.println("\n=== Menú de Gestión ===");
            System.out.println("1. Registrar Usuario");
            System.out.println("2. Mostrar Usuarios");
            System.out.println("3. Modificar Usuario");
            System.out.println("4. Eliminar Usuario");
            if (!isAdmin) {
                System.out.println("5. Modificar Datos Propios");
            }
            System.out.println("6. Gestión de Pagos");
            System.out.println("7. Gestión de Perfiles");
            System.out.println("8. Gestión de Funcionalidades");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    if (isAdmin) {
                        registrarUsuario(clienteFachada, administradorFachada, scanner);
                    } else {
                        System.out.println("Acceso denegado. Solo administradores pueden registrar usuarios.");
                    }
                    break;
                case 2:
                    if (isAdmin) {
                        mostrarUsuarios(clienteFachada, administradorFachada, scanner);
                    } else {
                        System.out.println("Acceso denegado. Solo administradores pueden mostrar usuarios.");
                    }
                    break;
                case 3:
                    if (isAdmin) {
                        modificarUsuario(clienteFachada, administradorFachada, scanner);
                    } else {
                        System.out.println("Acceso denegado. Solo administradores pueden modificar usuarios.");
                    }
                    break;
                case 4:
                    if (isAdmin) {
                        eliminarUsuario(clienteFachada, administradorFachada, scanner);
                    } else {
                        System.out.println("Acceso denegado. Solo administradores pueden eliminar usuarios.");
                    }
                    break;
                case 5:
                    if (!isAdmin) {
                        modificarDatosPropios(clienteFachada, scanner);
                    } else {
                        System.out.println("Acceso denegado. Solo los clientes pueden modificar sus propios datos.");
                    }
                    break;
                case 6:
                    if (isAdmin) {
                        menuGestionPagos(pagoFachada, scanner);
                    } else {
                        System.out.println("Acceso denegado. Solo administradores pueden gestionar pagos.");
                    }
                    break;
                case 7:
                    if (isAdmin) {
                        menuGestionPerfiles(perfilFachada, scanner);
                    } else {
                        System.out.println("Acceso denegado. Solo administradores pueden gestionar pagos.");
                    }
                    break;
                case 8:
                    if (isAdmin) {
                        menuGestionFuncionalidades(funcionalidadFachada, scanner);
                    } else {
                        System.out.println("Acceso denegado. Solo administradores pueden gestionar pagos.");
                    }
                    break;
                case 9:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 9);

        scanner.close();
    }

    private static void registrarUsuario(ClienteFachada clienteFachada, AdministradorFachada administradorFachada, Scanner scanner) {
        try {
            System.out.print("¿Qué tipo de usuario desea registrar? (1. Cliente, 2. Administrador): ");
            int tipo = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (tipo != 1 && tipo != 2) {
                System.out.println("Opción inválida. Por favor, seleccione 1 para Cliente o 2 para Administrador.");
                return;
            }

            System.out.println(tipo == 1 ? "\n=== Registrar Cliente ===" : "\n=== Registrar Administrador ===");
            int id;
            do {
                System.out.print("Ingrese ID (número entero positivo): ");
                while (!scanner.hasNextInt()) {
                    System.out.println("ID inválido. Debe ser un número entero positivo.");
                    scanner.next(); // Limpiar entrada no válida
                }
                id = scanner.nextInt();
                scanner.nextLine();
                if (id <= 0) {
                    System.out.println("El ID debe ser un número entero positivo.");
                }
            } while (id <= 0);

            System.out.print("Ingrese Nombres: ");
            String nombres = scanner.nextLine().trim();
            if (nombres.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
                return;
            }

            System.out.print("Ingrese Apellidos: ");
            String apellidos = scanner.nextLine().trim();
            if (apellidos.isEmpty()) {
                System.out.println("El apellido no puede estar vacío.");
                return;
            }

            System.out.print("Ingrese Tipo de Documento (Cédula/Pasaporte): ");
            String tipoDocumento = scanner.nextLine().trim();
            if (!tipoDocumento.equalsIgnoreCase("Cédula") && !tipoDocumento.equalsIgnoreCase("Pasaporte")) {
                System.out.println("El tipo de documento debe ser 'Cédula' o 'Pasaporte'.");
                return;
            }

            System.out.print("Ingrese Número de Documento: ");
            String numeroDocumento = scanner.nextLine().trim();
            if (numeroDocumento.isEmpty()) {
                System.out.println("El número de documento no puede estar vacío.");
                return;
            }

            // Validar correo
            String correo;
            do {
                System.out.print("Ingrese Correo: ");
                correo = scanner.nextLine().trim();
                if (!validarCorreo(correo)) {
                    System.out.println("Correo inválido. Por favor, ingrese un correo válido.");
                }
            } while (!validarCorreo(correo));

            System.out.print("Ingrese Contraseña: ");
            String contrasena = scanner.nextLine().trim();
            if (contrasena.isEmpty()) {
                System.out.println("La contraseña no puede estar vacía.");
                return;
            }

            System.out.print("Ingrese Domicilio: ");
            String domicilio = scanner.nextLine().trim();

            System.out.print("Ingrese Estado (Activo/Inactivo): ");
            String estado = scanner.nextLine().trim();
            if (!estado.equalsIgnoreCase("Activo") && !estado.equalsIgnoreCase("Inactivo")) {
                System.out.println("El estado debe ser 'Activo' o 'Inactivo'.");
                return;
            }

            if (tipo == 1) { // Registro de cliente
                System.out.print("Ingrese Número de Socio: ");
                int nroSocio = scanner.nextInt();

                System.out.print("Ingrese Total Anual Cuotas: ");
                int totalAnualCuotas = scanner.nextInt();

                System.out.print("Ingrese Pago de Cuotas: ");
                int pagoCuotas = scanner.nextInt();

                System.out.print("¿Dificultad Auditiva? (true/false): ");
                boolean dificultadAuditiva = scanner.nextBoolean();

                System.out.print("¿Maneja Lenguaje de Señas? (true/false): ");
                boolean lenguajeSenas = scanner.nextBoolean();

                System.out.print("Ingrese ID Perfil: ");
                int idPerfil = scanner.nextInt();

                try {
                    clienteFachada.crearCliente(id, nombres, apellidos, tipoDocumento, numeroDocumento, domicilio, correo,
                            contrasena, estado, nroSocio, totalAnualCuotas, pagoCuotas, dificultadAuditiva, lenguajeSenas, idPerfil);
                    System.out.println("Cliente registrado exitosamente.");
                } catch (PostgresException e) {
                    System.out.println("Error al registrar cliente: " + e.getMessage());
                }
            } else if (tipo == 2) { // Registro de administrador
                try {
                    administradorFachada.crearAdministrador(id, nombres, apellidos, tipoDocumento, numeroDocumento,
                            domicilio, correo, contrasena, estado);
                    System.out.println("Administrador registrado exitosamente.");
                } catch (PostgresException e) {
                    System.out.println("Error al registrar administrador: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private static void mostrarUsuarios(ClienteFachada clienteFachada, AdministradorFachada administradorFachada, Scanner scanner) {
        try {
            System.out.print("¿Qué tipo de usuarios desea mostrar? (1. Clientes, 2. Administradores): ");

            // Validar la entrada del usuario
            int tipo = -1;
            do {
                if (scanner.hasNextInt()) {
                    tipo = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea
                    if (tipo != 1 && tipo != 2) {
                        System.out.println("Opción inválida. Debe seleccionar 1 para Clientes o 2 para Administradores.");
                    }
                } else {
                    System.out.println("Entrada inválida. Por favor, ingrese un número (1 o 2).");
                    scanner.next(); // Limpiar la entrada no válida
                }
            } while (tipo != 1 && tipo != 2);

            // Mostrar clientes
            if (tipo == 1) {
                try {
                    clienteFachada.mostrarClientes();
                    System.out.println("Lista de clientes mostrada exitosamente.");
                } catch (PostgresException e) {
                    System.out.println("Error al mostrar clientes: " + e.getMessage());
                }
            }
            // Mostrar administradores
            else if (tipo == 2) {
                try {
                    administradorFachada.mostrarAdministradores();
                    System.out.println("Lista de administradores mostrada exitosamente.");
                } catch (PostgresException e) {
                    System.out.println("Error al mostrar administradores: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private static void modificarUsuario(ClienteFachada clienteFachada, AdministradorFachada administradorFachada, Scanner scanner) {
        try {
            System.out.print("¿Qué tipo de usuario desea modificar? (1. Cliente, 2. Administrador): ");

            // Validar la entrada del tipo de usuario
            int tipo = -1;
            do {
                if (scanner.hasNextInt()) {
                    tipo = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea
                    if (tipo != 1 && tipo != 2) {
                        System.out.println("Opción inválida. Debe seleccionar 1 para Cliente o 2 para Administrador.");
                    }
                } else {
                    System.out.println("Entrada inválida. Por favor, ingrese un número (1 o 2).");
                    scanner.next(); // Limpiar la entrada no válida
                }
            } while (tipo != 1 && tipo != 2);

            if (tipo == 1) {
                System.out.println("\n=== Modificar Cliente ===");
                System.out.print("Ingrese ID del Cliente: ");
                int id = scanner.nextInt();
                scanner.nextLine(); // Consumir salto de línea

                // Solicitar los nuevos datos
                System.out.print("Ingrese Nuevos Nombres (dejar en blanco para mantener): ");
                String nombres = scanner.nextLine();
                System.out.print("Ingrese Nuevos Apellidos (dejar en blanco para mantener): ");
                String apellidos = scanner.nextLine();
                System.out.print("Ingrese Nuevo Domicilio (dejar en blanco para mantener): ");
                String domicilio = scanner.nextLine();
                System.out.print("Ingrese Nuevo Estado (Activo/Inactivo): ");
                String estado = scanner.nextLine();

                // Validar el estado
                if (!estado.equalsIgnoreCase("Activo") && !estado.equalsIgnoreCase("Inactivo")) {
                    throw new IllegalArgumentException("El estado debe ser 'Activo' o 'Inactivo'.");
                }

                System.out.print("Ingrese Nuevo Total Anual Cuotas: ");
                int totalAnualCuotas = scanner.nextInt();
                System.out.print("Ingrese Nuevo Pago de Cuotas: ");
                int pagoCuotas = scanner.nextInt();
                System.out.print("¿Tiene Dificultad Auditiva? (true/false): ");
                boolean dificultadAuditiva = scanner.nextBoolean();
                System.out.print("¿Maneja Lenguaje de Señas? (true/false): ");
                boolean lenguajeSenas = scanner.nextBoolean();
                System.out.print("Ingrese Nuevo ID de Perfil: ");
                int idPerfil = scanner.nextInt();

                // Intentar la modificación
                try {
                    clienteFachada.modificarCliente(
                            id,
                            nombres.isEmpty() ? null : nombres,
                            apellidos.isEmpty() ? null : apellidos,
                            domicilio.isEmpty() ? null : domicilio,
                            estado,
                            totalAnualCuotas,
                            pagoCuotas,
                            dificultadAuditiva,
                            lenguajeSenas,
                            idPerfil
                    );
                    System.out.println("Cliente modificado exitosamente.");
                } catch (PostgresException e) {
                    System.out.println("Error al modificar el cliente: " + e.getMessage());
                }
            } else if (tipo == 2) {
                System.out.println("\n=== Modificar Administrador ===");
                System.out.print("Ingrese ID del Administrador: ");
                int id = scanner.nextInt();
                scanner.nextLine(); // Consumir salto de línea

                // Solicitar los nuevos datos
                System.out.print("Ingrese Nuevos Nombres (dejar en blanco para mantener): ");
                String nombres = scanner.nextLine();
                System.out.print("Ingrese Nuevos Apellidos (dejar en blanco para mantener): ");
                String apellidos = scanner.nextLine();
                System.out.print("Ingrese Nuevo Domicilio (dejar en blanco para mantener): ");
                String domicilio = scanner.nextLine();
                System.out.print("Ingrese Nuevo Estado (Activo/Inactivo): ");
                String estado = scanner.nextLine();

                // Validar el estado
                if (!estado.equalsIgnoreCase("Activo") && !estado.equalsIgnoreCase("Inactivo")) {
                    throw new IllegalArgumentException("El estado debe ser 'Activo' o 'Inactivo'.");
                }

                // Intentar la modificación
                try {
                    administradorFachada.modificarAdministrador(
                            id,
                            nombres.isEmpty() ? null : nombres,
                            apellidos.isEmpty() ? null : apellidos,
                            domicilio.isEmpty() ? null : domicilio,
                            estado
                    );
                    System.out.println("Administrador modificado exitosamente.");
                } catch (PostgresException e) {
                    System.out.println("Error al modificar el administrador: " + e.getMessage());
                }
            } else {
                System.out.println("Opción inválida.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error en los datos ingresados: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private static void eliminarUsuario(ClienteFachada clienteFachada, AdministradorFachada administradorFachada, Scanner scanner) {
        try {
            System.out.print("¿Qué tipo de usuario desea eliminar? (1. Cliente, 2. Administrador): ");

            // Validar la entrada del tipo de usuario
            int tipo = -1;
            do {
                if (scanner.hasNextInt()) {
                    tipo = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea
                    if (tipo != 1 && tipo != 2) {
                        System.out.println("Opción inválida. Debe seleccionar 1 para Cliente o 2 para Administrador.");
                    }
                } else {
                    System.out.println("Entrada inválida. Por favor, ingrese un número (1 o 2).");
                    scanner.next(); // Limpiar la entrada no válida
                }
            } while (tipo != 1 && tipo != 2);

            if (tipo == 1) {
                System.out.println("\n=== Eliminar Cliente ===");
                System.out.print("Ingrese ID del Cliente: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("ID inválido. Por favor, ingrese un número.");
                    scanner.next(); // Limpiar la entrada no válida
                    return;
                }
                int id = scanner.nextInt();

                try {
                    clienteFachada.eliminarCliente(id);
                    System.out.println("Cliente eliminado exitosamente.");
                } catch (PostgresException e) {
                    System.out.println("Error al eliminar el cliente: " + e.getMessage());
                }
            } else if (tipo == 2) {
                System.out.println("\n=== Eliminar Administrador ===");
                System.out.print("Ingrese ID del Administrador: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("ID inválido. Por favor, ingrese un número.");
                    scanner.next(); // Limpiar la entrada no válida
                    return;
                }
                int id = scanner.nextInt();

                try {
                    administradorFachada.eliminarAdministrador(id);
                    System.out.println("Administrador eliminado exitosamente.");
                } catch (PostgresException e) {
                    System.out.println("Error al eliminar el administrador: " + e.getMessage());
                }
            } else {
                System.out.println("Opción inválida.");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private static void modificarDatosPropios(ClienteFachada clienteFachada, Scanner scanner) {
        System.out.println("\n=== Modificar Datos Propios ===");
    }

    private static boolean validarCorreo(String correo) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(regex, correo);
    }


    private static void menuGestionPerfiles(PerfilFachada perfilFachada, Scanner scanner) {
        try {
            int opcion;
            do {
                System.out.println("\n=== Gestión de Perfiles ===");
                System.out.println("1. Crear Perfil");
                System.out.println("2. Listar Perfiles");
                System.out.println("3. Modificar Perfil");
                System.out.println("4. Eliminar Perfil");
                System.out.println("5. Volver al Menú Principal");
                System.out.print("Seleccione una opción: ");

                // Validar entrada de opción
                while (!scanner.hasNextInt()) {
                    System.out.println("Entrada inválida. Por favor, ingrese un número del 1 al 5.");
                    scanner.next(); // Limpiar entrada no válida
                }
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1: // Crear Perfil
                        try {
                            System.out.print("Ingrese Nombre del Perfil: ");
                            String nombrePerfil = scanner.nextLine();
                            if (nombrePerfil.trim().isEmpty()) {
                                System.out.println("El nombre del perfil no puede estar vacío.");
                                break;
                            }

                            System.out.print("Ingrese Descripción: ");
                            String descripcion = scanner.nextLine();
                            if (descripcion.trim().isEmpty()) {
                                System.out.println("La descripción no puede estar vacía.");
                                break;
                            }

                            System.out.print("Ingrese Estado (Activo/Inactivo): ");
                            String estado = scanner.nextLine();
                            if (!estado.equalsIgnoreCase("Activo") && !estado.equalsIgnoreCase("Inactivo")) {
                                System.out.println("Estado inválido. Solo se permite 'Activo' o 'Inactivo'.");
                                break;
                            }

                            perfilFachada.crearPerfil(nombrePerfil, estado, descripcion);
                            System.out.println("Perfil creado exitosamente.");
                        } catch (PostgresException e) {
                            System.out.println("Error al crear el perfil: " + e.getMessage());
                        }
                        break;

                    case 2: // Listar Perfiles
                        try {
                            perfilFachada.listarPerfiles().forEach(System.out::println);
                        } catch (PostgresException e) {
                            System.out.println("Error al listar los perfiles: " + e.getMessage());
                        }
                        break;

                    case 3: // Modificar Perfil
                        try {
                            System.out.print("Ingrese ID del Perfil a Modificar: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("ID inválido. Debe ser un número entero.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            int idModificar = scanner.nextInt();
                            scanner.nextLine(); // Consumir el salto de línea

                            System.out.print("Ingrese Nuevo Nombre: ");
                            String nuevoNombre = scanner.nextLine();
                            if (nuevoNombre.trim().isEmpty()) {
                                System.out.println("El nombre del perfil no puede estar vacío.");
                                break;
                            }

                            System.out.print("Ingrese Nueva Descripción: ");
                            String nuevaDescripcion = scanner.nextLine();
                            if (nuevaDescripcion.trim().isEmpty()) {
                                System.out.println("La descripción no puede estar vacía.");
                                break;
                            }

                            System.out.print("Ingrese Nuevo Estado: ");
                            String nuevoEstado = scanner.nextLine();
                            if (!nuevoEstado.equalsIgnoreCase("Activo") && !nuevoEstado.equalsIgnoreCase("Inactivo")) {
                                System.out.println("Estado inválido. Solo se permite 'Activo' o 'Inactivo'.");
                                break;
                            }

                            perfilFachada.modificarPerfil(idModificar, nuevoNombre, nuevoEstado, nuevaDescripcion);
                            System.out.println("Perfil modificado exitosamente.");
                        } catch (PostgresException e) {
                            System.out.println("Error al modificar el perfil: " + e.getMessage());
                        }
                        break;

                    case 4: // Eliminar Perfil
                        try {
                            System.out.print("Ingrese ID del Perfil a Eliminar: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("ID inválido. Debe ser un número entero.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            int idEliminar = scanner.nextInt();
                            scanner.nextLine(); // Consumir el salto de línea

                            perfilFachada.eliminarPerfil(idEliminar);
                            System.out.println("Perfil eliminado exitosamente.");
                        } catch (PostgresException e) {
                            System.out.println("Error al eliminar el perfil: " + e.getMessage());
                        }
                        break;

                    case 5: // Volver al menú principal
                        System.out.println("Volviendo al menú principal...");
                        break;

                    default: // Opción no válida
                        System.out.println("Opción inválida. Por favor, seleccione una opción entre 1 y 5.");
                }
            } while (opcion != 5);
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private static void menuGestionFuncionalidades(FuncionalidadFachada funcionalidadFachada, Scanner scanner) {
        try {
            int opcion;
            do {
                System.out.println("\n=== Gestión de Funcionalidades ===");
                System.out.println("1. Crear Funcionalidad");
                System.out.println("2. Listar Funcionalidades");
                System.out.println("3. Modificar Funcionalidad");
                System.out.println("4. Eliminar Funcionalidad");
                System.out.println("5. Volver al Menú Principal");
                System.out.print("Seleccione una opción: ");

                // Validar entrada numérica
                while (!scanner.hasNextInt()) {
                    System.out.println("Entrada inválida. Por favor, ingrese un número del 1 al 5.");
                    scanner.next(); // Limpiar entrada no válida
                }
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1: // Crear Funcionalidad
                        try {
                            System.out.print("Ingrese Nombre de la Funcionalidad: ");
                            String nombreFuncionalidad = scanner.nextLine();
                            if (nombreFuncionalidad.trim().isEmpty()) {
                                System.out.println("El nombre de la funcionalidad no puede estar vacío.");
                                break;
                            }

                            System.out.print("Ingrese Descripción: ");
                            String descripcion = scanner.nextLine();
                            if (descripcion.trim().isEmpty()) {
                                System.out.println("La descripción no puede estar vacía.");
                                break;
                            }

                            funcionalidadFachada.crearFuncionalidad(nombreFuncionalidad, descripcion);
                            System.out.println("Funcionalidad creada exitosamente.");
                        } catch (PostgresException e) {
                            System.out.println("Error al crear la funcionalidad: " + e.getMessage());
                        }
                        break;

                    case 2: // Listar Funcionalidades
                        try {
                            funcionalidadFachada.listarFuncionalidades().forEach(System.out::println);
                        } catch (PostgresException e) {
                            System.out.println("Error al listar las funcionalidades: " + e.getMessage());
                        }
                        break;

                    case 3: // Modificar Funcionalidad
                        try {
                            System.out.print("Ingrese ID de la Funcionalidad a Modificar: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("ID inválido. Debe ser un número entero.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            int idModificar = scanner.nextInt();
                            scanner.nextLine(); // Consumir el salto de línea

                            System.out.print("Ingrese Nuevo Nombre: ");
                            String nuevoNombre = scanner.nextLine();
                            if (nuevoNombre.trim().isEmpty()) {
                                System.out.println("El nombre de la funcionalidad no puede estar vacío.");
                                break;
                            }

                            System.out.print("Ingrese Nueva Descripción: ");
                            String nuevaDescripcion = scanner.nextLine();
                            if (nuevaDescripcion.trim().isEmpty()) {
                                System.out.println("La descripción no puede estar vacía.");
                                break;
                            }

                            funcionalidadFachada.modificarFuncionalidad(idModificar, nuevoNombre, nuevaDescripcion);
                            System.out.println("Funcionalidad modificada exitosamente.");
                        } catch (PostgresException e) {
                            System.out.println("Error al modificar la funcionalidad: " + e.getMessage());
                        }
                        break;

                    case 4: // Eliminar Funcionalidad
                        try {
                            System.out.print("Ingrese ID de la Funcionalidad a Eliminar: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("ID inválido. Debe ser un número entero.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            int idEliminar = scanner.nextInt();
                            scanner.nextLine(); // Consumir el salto de línea

                            funcionalidadFachada.eliminarFuncionalidad(idEliminar);
                            System.out.println("Funcionalidad eliminada exitosamente.");
                        } catch (PostgresException e) {
                            System.out.println("Error al eliminar la funcionalidad: " + e.getMessage());
                        }
                        break;

                    case 5: // Volver al menú principal
                        System.out.println("Volviendo al menú principal...");
                        break;

                    default: // Opción no válida
                        System.out.println("Opción inválida. Por favor, seleccione una opción entre 1 y 5.");
                }
            } while (opcion != 5);
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }
    private static void menuGestionPagos(PagoFachada pagoFachada, Scanner scanner) {
        try {
            int opcion;
            do {
                System.out.println("\n=== Gestión de Pagos ===");
                System.out.println("1. Registrar Pago");
                System.out.println("2. Modificar Pago");
                System.out.println("3. Listar Pagos por Cliente");
                System.out.println("4. Volver al Menú Principal");
                System.out.print("Seleccione una opción: ");

                // Validar entrada numérica para opción
                while (!scanner.hasNextInt()) {
                    System.out.println("Entrada inválida. Por favor, ingrese un número entre 1 y 4.");
                    scanner.next(); // Limpiar entrada no válida
                }
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1: // Registrar Pago
                        try {
                            System.out.println("\n=== Registrar Pago ===");

                            System.out.print("Ingrese ID de la Cuota: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("ID de Cuota inválido. Debe ser un número entero.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            int idCuota = scanner.nextInt();

                            System.out.print("Ingrese Número de Cuota: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Número de Cuota inválido. Debe ser un número entero.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            int numeroCuota = scanner.nextInt();

                            System.out.print("Ingrese Valor de la Cuota: ");
                            if (!scanner.hasNextDouble()) {
                                System.out.println("Valor de la Cuota inválido. Debe ser un número.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            double valorCuota = scanner.nextDouble();
                            scanner.nextLine(); // Consumir salto

                            System.out.print("Ingrese Fecha de la Cuota (YYYY-MM-DD): ");
                            String fechaCuotaStr = scanner.nextLine();
                            Date fechaCuota;
                            try {
                                fechaCuota = Date.valueOf(fechaCuotaStr);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Fecha inválida. Debe estar en el formato YYYY-MM-DD.");
                                break;
                            }

                            System.out.print("Ingrese ID del Cliente: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("ID del Cliente inválido. Debe ser un número entero.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            int idCliente = scanner.nextInt();

                            pagoFachada.registrarPago(new Cuota(idCuota, numeroCuota, fechaCuota, valorCuota, clienteFachada.buscarCliente(idCliente)));
                            System.out.println("Pago registrado exitosamente.");
                        } catch (PostgresException e) {
                            System.out.println("Error al registrar el pago: " + e.getMessage());
                        }
                        break;

                    case 2: // Modificar Pago
                        try {
                            System.out.println("\n=== Modificar Pago ===");

                            System.out.print("Ingrese ID de la Cuota a Modificar: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("ID de Cuota inválido. Debe ser un número entero.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            int idCuotaModificar = scanner.nextInt();

                            System.out.print("Ingrese Nuevo Valor de la Cuota: ");
                            if (!scanner.hasNextDouble()) {
                                System.out.println("Nuevo Valor de la Cuota inválido. Debe ser un número.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            double nuevoValor = scanner.nextDouble();
                            scanner.nextLine(); // Consumir salto

                            System.out.print("Ingrese Nueva Fecha de la Cuota (YYYY-MM-DD): ");
                            String nuevaFechaStr = scanner.nextLine();
                            Date nuevaFecha;
                            try {
                                nuevaFecha = Date.valueOf(nuevaFechaStr);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Fecha inválida. Debe estar en el formato YYYY-MM-DD.");
                                break;
                            }

                            pagoFachada.modificarPago(idCuotaModificar, nuevoValor, nuevaFecha);
                            System.out.println("Pago modificado exitosamente.");
                        } catch (PostgresException e) {
                            System.out.println("Error al modificar el pago: " + e.getMessage());
                        }
                        break;

                    case 3: // Listar Pagos por Cliente
                        try {
                            System.out.println("\n=== Listar Pagos por Cliente ===");

                            System.out.print("Ingrese ID del Cliente: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("ID del Cliente inválido. Debe ser un número entero.");
                                scanner.next(); // Limpiar entrada no válida
                                break;
                            }
                            int idClientePagos = scanner.nextInt();

                            pagoFachada.listarPagosPorCliente(idClientePagos)
                                    .forEach(System.out::println);
                        } catch (PostgresException e) {
                            System.out.println("Error al listar los pagos: " + e.getMessage());
                        }
                        break;

                    case 4: // Volver al Menú Principal
                        System.out.println("Volviendo al menú principal...");
                        break;

                    default: // Entrada no válida
                        System.out.println("Opción inválida. Por favor, seleccione una opción entre 1 y 4.");
                }
            } while (opcion != 4);
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }


}