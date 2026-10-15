// COLA (FIFO): el primero que entra es el primero que sale.
// Aqui son los lotes de abono por procesar.
public class Cola {
    Nodo frente;   // el que sale primero
    Nodo fin;      // el ultimo que entro
    int tamano = 0;

    void enqueue(String dato) {
        Nodo nuevo = new Nodo(dato);
        if (fin == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamano++;
    }

    String dequeue() {
        if (frente == null) return null;   // cola vacia
        String dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamano--;
        return dato;
    }
}
