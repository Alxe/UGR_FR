# Fundamentos de Redes

## Práctica 1

**Autor:** Alejandro Núñez Pérez

### Cuestión 1

- **lo:** Red Local, con IPv4 `127.0.0.1`
- **enp0s3:** Red Ethernet (simulada por VirtualBox), con IPv4 `10.0.2.15`

### Cuestión 2

Al estar usando máquinas virtuales, para realizar un _ping_ he tenido que configurar una red interna nueva para la comunicación entre dos máquinas mediante `ifconfig <adapter> <address>`.

### Cuestión 3

Inicialmente, con los archivos de configuración dados por el guión, el servidor está desabilitado.

En el ordenador que actúa como servidor, modificamos el archivo ubicado en `/etc/xinetd.d/telnet` y modificamos `disable` a `no`

Una vez hecho esto, desde la máquina cliente podemos acceder con `telnet <address>` y autenticarnos con un usuario (no superusuario, root).

### Cuestión 4

Dentro de los archivos de configuración de _xinetd_, añadimos la clausula `only_from` y le asignamos la IP que queramos, la del cliente en este caso.

Para el registro, añadimos las cláusulas `log_on_success` y `log_on_failure`, para que añadan una noticia en el archivo de registro.

### Cuestión 5

Cambiamos la clausula `disable` a `no` en `etc/xinetd.d/vsftpd`, y en el archivo de configuración `/etc/vsftpd.conf` los correspondientes cambios a `listen`, `anonymous_enable` y `local_enable`

### Cuestión 6

Conectandonos con `ftp <address>` y usuario y contraseña de un usuario (no superusuario, root), y usando los comandos `get` de FTP, podemos ver que todo está bien configurado.

### Cuestión 7

Para esto, debemos modificar el archivo `/etc/vsftpd.conf` y añadir las clausulas `userlist_deny`, `userlist_enabled` y `userlist_file` a `NO`, `YES` y el archivo que usemos para listar los nombres de usuario que puedan acceder.

Del mismo modo, añadimos `write_enabled` a `YES` para permitir la subida de archivos.

### Cuestión 8

Modificando el archivo `/var/www/html/index.html`, y recargando forzosamente (para evitar tomar de cache) podemos ver reflejado los cambios en la página web.

Modificando `/etc/apache2/ports.conf` y añadiendo `Listen 8080`, conseguimos que también escuche por el puerto 8080.

Para restringir el acceso a una carpeta, he procedido a cambiar la directiva `AllowOverride` a `All` para todo `/var/www/`, cambiando los parametros desde el archivo `.htaccess` ubicado en `/var/www/html/restringida/`. Así, he podido sobreescribir las preferenias, para que el servidor solicite usuario y contraseña.