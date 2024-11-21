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
            isLoggedIn = administradorFachada.loginUsuario(correo, contrasena);
            if (isLoggedIn) {
                System.out.println("¡Bienvenido, Administrador!");
                isAdmin = true;
                break;
            }

            // Intentar login como cliente
            isLoggedIn = clienteFachada.loginUsuario(correo, contrasena);
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
        System.out.print("¿Qué tipo de usuario desea registrar? (1. Cliente, 2. Administrador): ");
        int tipo = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        if (tipo == 1) {
            System.out.println("\n=== Registrar Cliente ===");
            System.out.print("Ingrese ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Ingrese Nombres: ");
            String nombres = scanner.nextLine();
            System.out.print("Ingrese Apellidos: ");
            String apellidos = scanner.nextLine();
            System.out.print("Ingrese Tipo de Documento (Cédula/Pasaporte): ");
            String tipoDocumento = scanner.nextLine();
            System.out.print("Ingrese Número de Documento: ");
            String numeroDocumento = scanner.nextLine();
            System.out.print("Ingrese Correo: ");
            String correo = scanner.nextLine();
            System.out.print("Ingrese Contraseña: ");
            String contrasena = scanner.nextLine();
            System.out.print("Ingrese Domicilio: ");
            String domicilio = scanner.nextLine();
            System.out.print("Ingrese Estado (Activo/Inactivo): ");
            String estado = scanner.nextLine();
            System.out.print("Ingrese Número de Socio: ");
            int nroSocio = scanner.nextInt();
            System.out.print("Ingrese Total Anual de Cuotas: ");
            int totalAnualCuotas = scanner.nextInt();
            System.out.print("Ingrese Pago de Cuotas: ");
            int pagoCuotas = scanner.nextInt();
            System.out.print("¿Tiene dificultad auditiva? (true/false): ");
            boolean dificultadAuditiva = scanner.nextBoolean();
            System.out.print("¿Maneja lenguaje de señas? (true/false): ");
            boolean lenguajeSenas = scanner.nextBoolean();
            System.out.print("Ingrese ID de Perfil: ");
            int idPerfil = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

            clienteFachada.crearCliente(id, nombres, apellidos, tipoDocumento, numeroDocumento, domicilio, correo, contrasena,
                    estado, nroSocio, totalAnualCuotas, pagoCuotas, dificultadAuditiva, lenguajeSenas, idPerfil);
        } else if (tipo == 2) {
            System.out.println("\n=== Registrar Administrador ===");
            System.out.print("Ingrese ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Ingrese Nombres: ");
            String nombres = scanner.nextLine();
            System.out.print("Ingrese Apellidos: ");
            String apellidos = scanner.nextLine();
            System.out.print("Ingrese Tipo de Documento (Cédula/Pasaporte): ");
            String tipoDocumento = scanner.nextLine();
            System.out.print("Ingrese Número de Documento: ");
            String numeroDocumento = scanner.nextLine();
            System.out.print("Ingrese Correo: ");
            String correo = scanner.nextLine();
            System.out.print("Ingrese Contraseña: ");
            String contrasena = scanner.nextLine();
            System.out.print("Ingrese Domicilio: ");
            String domicilio = scanner.nextLine();
            System.out.print("Ingrese Estado (Activo/Inactivo): ");
            String estado = scanner.nextLine();

            administradorFachada.crearAdministrador(id, nombres, apellidos, tipoDocumento, numeroDocumento, domicilio,
                    correo, contrasena, estado);
        } else {
            System.out.println("Opción inválida.");
        }
    }

    private static void mostrarUsuarios(ClienteFachada clienteFachada, AdministradorFachada administradorFachada, Scanner scanner) {
        System.out.print("¿Qué tipo de usuarios desea mostrar? (1. Clientes, 2. Administradores): ");
        int tipo = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        if (tipo == 1) {
            clienteFachada.mostrarClientes();
        } else if (tipo == 2) {
            administradorFachada.mostrarAdministradores();
        } else {
            System.out.println("Opción inválida.");
        }
    }

    private static void modificarUsuario(ClienteFachada clienteFachada, AdministradorFachada administradorFachada, Scanner scanner) {
        System.out.print("¿Qué tipo de usuario desea modificar? (1. Cliente, 2. Administrador): ");
        int tipo = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        if (tipo == 1) {
            System.out.println("\n=== Modificar Cliente ===");
            // Solicitar datos para modificación de Cliente
        } else if (tipo == 2) {
            System.out.println("\n=== Modificar Administrador ===");
            // Solicitar datos para modificación de Administrador
        } else {
            System.out.println("Opción inválida.");
        }
    }

    private static void eliminarUsuario(ClienteFachada clienteFachada, AdministradorFachada administradorFachada, Scanner scanner) {
        System.out.print("¿Qué tipo de usuario desea eliminar? (1. Cliente, 2. Administrador): ");
        int tipo = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        if (tipo == 1) {
            System.out.println("\n=== Eliminar Cliente ===");
            System.out.print("Ingrese ID del Cliente: ");
            int id = scanner.nextInt();
            clienteFachada.eliminarCliente(id);
        } else if (tipo == 2) {
            System.out.println("\n=== Eliminar Administrador ===");
            System.out.print("Ingrese ID del Administrador: ");
            int id = scanner.nextInt();
            administradorFachada.eliminarAdministrador(id);
        } else {
            System.out.println("Opción inválida.");
        }
    }

    private static void modificarDatosPropios(ClienteFachada clienteFachada, Scanner scanner) {
        System.out.println("\n=== Modificar Datos Propios ===");
        // Solicitar datos para modificación
    }

    private static boolean validarCorreo(String correo) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(regex, correo);
    }


    private static void menuGestionPerfiles(PerfilFachada perfilFachada, Scanner scanner) {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Perfiles ===");
            System.out.println("1. Crear Perfil");
            System.out.println("2. Listar Perfiles");
            System.out.println("3. Modificar Perfil");
            System.out.println("4. Eliminar Perfil");
            System.out.println("5. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese Nombre del Perfil: ");
                    String nombrePerfil = scanner.nextLine();
                    System.out.print("Ingrese Descripción: ");
                    String descripcion = scanner.nextLine();
                    System.out.print("Ingrese Estado (Activo/Inactivo): ");
                    String estado = scanner.nextLine();
                    perfilFachada.crearPerfil(nombrePerfil, descripcion, estado);
                    break;
                case 2:
                    perfilFachada.listarPerfiles().forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("Ingrese ID del Perfil a Modificar: ");
                    int idModificar = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea
                    System.out.print("Ingrese Nuevo Nombre: ");
                    String nuevoNombre = scanner.nextLine();
                    System.out.print("Ingrese Nueva Descripción: ");
                    String nuevaDescripcion = scanner.nextLine();
                    System.out.print("Ingrese Nuevo Estado: ");
                    String nuevoEstado = scanner.nextLine();
                    perfilFachada.modificarPerfil(idModificar, nuevoNombre, nuevaDescripcion, nuevoEstado);
                    break;
                case 4:
                    System.out.print("Ingrese ID del Perfil a Eliminar: ");
                    int idEliminar = scanner.nextInt();
                    perfilFachada.eliminarPerfil(idEliminar);
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    private static void menuGestionFuncionalidades(FuncionalidadFachada funcionalidadFachada, Scanner scanner) {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Funcionalidades ===");
            System.out.println("1. Crear Funcionalidad");
            System.out.println("2. Listar Funcionalidades");
            System.out.println("3. Modificar Funcionalidad");
            System.out.println("4. Eliminar Funcionalidad");
            System.out.println("5. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese Nombre de la Funcionalidad: ");
                    String nombreFuncionalidad = scanner.nextLine();
                    System.out.print("Ingrese Descripción: ");
                    String descripcion = scanner.nextLine();
                    funcionalidadFachada.crearFuncionalidad(nombreFuncionalidad, descripcion);
                    break;
                case 2:
                    funcionalidadFachada.listarFuncionalidades().forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("Ingrese ID de la Funcionalidad a Modificar: ");
                    int idModificar = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea
                    System.out.print("Ingrese Nuevo Nombre: ");
                    String nuevoNombre = scanner.nextLine();
                    System.out.print("Ingrese Nueva Descripción: ");
                    String nuevaDescripcion = scanner.nextLine();
                    funcionalidadFachada.modificarFuncionalidad(idModificar, nuevoNombre, nuevaDescripcion);
                    break;
                case 4:
                    System.out.print("Ingrese ID de la Funcionalidad a Eliminar: ");
                    int idEliminar = scanner.nextInt();
                    funcionalidadFachada.eliminarFuncionalidad(idEliminar);
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }
    private static void menuGestionPagos(PagoFachada pagoFachada, Scanner scanner) {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Pagos ===");
            System.out.println("1. Registrar Pago");
            System.out.println("2. Modificar Pago");
            System.out.println("3. Listar Pagos por Cliente");
            System.out.println("4. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    System.out.println("\n=== Registrar Pago ===");
                    System.out.print("Ingrese ID de la Cuota: ");
                    int idCuota = scanner.nextInt();
                    System.out.print("Ingrese Número de Cuota: ");
                    int numeroCuota = scanner.nextInt();
                    System.out.print("Ingrese Valor de la Cuota: ");
                    double valorCuota = scanner.nextDouble();
                    scanner.nextLine(); // Consumir salto
                    System.out.print("Ingrese Fecha de la Cuota (YYYY-MM-DD): ");
                    String fechaCuotaStr = scanner.nextLine();
                    System.out.print("Ingrese ID del Cliente: ");
                    int idCliente = scanner.nextInt();

                    try {
                        Date fechaCuota = Date.valueOf(fechaCuotaStr);
                        pagoFachada.registrarPago(new Cuota(idCuota, numeroCuota, fechaCuota, valorCuota, clienteFachada.buscarCliente(idCliente)));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: Fecha inválida.");
                    }
                    break;

                case 2:
                    System.out.println("\n=== Modificar Pago ===");
                    System.out.print("Ingrese ID de la Cuota a Modificar: ");
                    int idCuotaModificar = scanner.nextInt();
                    System.out.print("Ingrese Nuevo Valor de la Cuota: ");
                    double nuevoValor = scanner.nextDouble();
                    scanner.nextLine(); // Consumir salto
                    System.out.print("Ingrese Nueva Fecha de la Cuota (YYYY-MM-DD): ");
                    String nuevaFechaStr = scanner.nextLine();

                    try {
                        Date nuevaFecha = Date.valueOf(nuevaFechaStr);
                        pagoFachada.modificarPago(idCuotaModificar, nuevoValor, nuevaFecha);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: Fecha inválida.");
                    }
                    break;

                case 3:
                    System.out.println("\n=== Listar Pagos por Cliente ===");
                    System.out.print("Ingrese ID del Cliente: ");
                    int idClientePagos = scanner.nextInt();
                    pagoFachada.listarPagosPorCliente(idClientePagos)
                            .forEach(System.out::println);
                    break;

                case 4:
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 4);
    }


}