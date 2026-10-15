// LISTA CIRCULAR: el ultimo nodo apunta de nuevo al primero.
// Aqui rota entre las parcelas: despues de la ultima vuelve a la primera.
public class ListaCircular {
    Nodo ultimo;     // ultimo.siguiente es el primero
    Nodo actual;     // parcela en turno

    void agregar(String dato) {
        Nodo nuevo = new Nodo(dato);
        if (ultimo == null) {
            nuevo.siguiente = nuevo;           // se apunta a si mismo
            ultimo = nuevo;
            actual = nuevo;
        } else {
            nuevo.siguiente = ultimo.siguiente; // el nuevo apunta al primero
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }
    }

    String actual() {
        return actual == null ? null : actual.dato;
    }

    // Pasa a la siguiente parcela
    String siguiente() {
        if (actual == null) return null;
        actual = actual.siguiente;
        return actual.dato;
    }
}
