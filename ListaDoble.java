// LISTA DOBLE: cada nodo apunta al siguiente Y al anterior.
// Aqui guarda el historial de pH, para ir hacia atras y hacia adelante.
public class ListaDoble {
    Nodo cabeza;
    Nodo cola;       // ultimo nodo
    Nodo actual;     // medicion que se esta mirando
    int tamano = 0;

    void agregar(String dato) {
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;   // el ultimo apunta al nuevo
            nuevo.anterior = cola;    // el nuevo apunta de vuelta al ultimo
            cola = nuevo;
        }
        actual = cola;                // queda en la medicion mas reciente
        tamano++;
    }

    // Retrocede una medicion (si puede) y devuelve el pH actual
    String anterior() {
        if (actual != null && actual.anterior != null) {
            actual = actual.anterior;
        }
        return actual == null ? null : actual.dato;
    }

    // Avanza una medicion (si puede) y devuelve el pH actual
    String siguiente() {
        if (actual != null && actual.siguiente != null) {
            actual = actual.siguiente;
        }
        return actual == null ? null : actual.dato;
    }

    // Todas las mediciones separadas por flechas
    String texto() {
        String resultado = "";
        Nodo n = cabeza;
        while (n != null) {
            resultado = resultado + n.dato;
            if (n.siguiente != null) resultado = resultado + " <-> ";
            n = n.siguiente;
        }
        return resultado;
    }
}
