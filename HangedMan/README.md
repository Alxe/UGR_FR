# HangedMan (Ahorcado)

Pequeño proyecto con finalidad de diseñar un protocolo de comunicación, _Cliente-Servidor_ en este caso.

## Descripción

HangedMan es un juego simple, una copia del popular Ahorcado. La finalidad es, dada una palabra, adivinar cuál es antes de quedarte sin intentos y ser ahorcado, perdiendo la partida.

Para esto, se usa, a través del lenguaje y entorno Java, _sockets_ que se enlazan a un puerto del sistema y realizan la comunicación entre tres entes, **Juego**, **Cliente** y **Servidor**.

1. El **Juego** se encarga de la lógica de juego.
1. El **Cliente** gestiona la comunicación con el _Servidor_ y la representación local del _Juego_. 
1. El **Servidor** almacena y gestiona un conjunto de _Juegos_, identificados con una _ID de Sesión_, y responde a las peticiones del _Cliente_.

## Consideraciones

Este pequeño proyecto ha sido realizado en muy pocas horas, con la intención de demostrar una comunicación _Cliente-Servidor_. Esto quiere decir que, aunque sea funcional, tiene muchas lagunas:

- **La palabra es fija**: Lo apropiado sería seleccionar una palabra aleatoria de un diccionario, y gestionarlas por nivel de dificultad.
- **No existe comunicación UDP**: Aunque uno de los ejercicios de la práctica es demostrar la comunicación UDP, no la considero en este proyecto concreto.
- **Hay un exceso de abstracción y problemas de delegación de la responsabilidad**: Para agilizar las cosas, he implementado funcionalidad en el lugar equivocado, y para demostrar mejor la modularidad, he abstraido algunos conceptos de más.
- **El control de errores es nefasto**: Lo más adecuado sería generar códigos de error similares a los de HTTP.
