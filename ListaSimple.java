// LISTA SIMPLE: cada nodo apunta solo al siguiente.
// Aqui guarda el historial de mezclas, una tras otra.
public class ListaSimple {
    Nodo cabeza;     // primer nodo
    int tamano = 0;

    // Agrega al final de la lista
    void agregar(String dato) {
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {   // recorre hasta el ultimo
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamano++;
    }

    // Quita el ultimo nodo (se usa al deshacer)
    void eliminarUltimo() {
        if (cabeza == null) return;
        if (cabeza.siguiente == null) {
            cabeza = null;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = null;
        }
        tamano--;
    }

    // Devuelve todo el contenido en un texto, una mezcla por linea
    String texto() {
        String resultado = "";
        Nodo actual = cabeza;
        int numero = 1;
        while (actual != null) {
            resultado = resultado + numero + ". " + actual.dato + "\n";
            actual = actual.siguiente;
            numero++;
        }
        return resultado;
    }
}
