// Un nodo guarda un texto y los enlaces a otros nodos.
public class Nodo {
    String dato;
    Nodo siguiente;   // apunta al nodo que sigue
    Nodo anterior;    // apunta al nodo de atras (solo lo usa la lista doble)

    Nodo(String dato) {
        this.dato = dato;
    }
}
