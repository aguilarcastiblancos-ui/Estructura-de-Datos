// PILA (LIFO): lo ultimo que entra es lo primero que sale.
// Aqui sirve para deshacer la ultima mezcla.
public class Pila {
    Nodo tope;
    int tamano = 0;

    void push(String dato) {
        Nodo nuevo = new Nodo(dato);
        nuevo.siguiente = tope;   // el nuevo queda encima
        tope = nuevo;
        tamano++;
    }

    String pop() {
        if (tope == null) return null;   // pila vacia
        String dato = tope.dato;
        tope = tope.siguiente;
        tamano--;
        return dato;
    }

    boolean estaVacia() {
        return tope == null;
    }
}
