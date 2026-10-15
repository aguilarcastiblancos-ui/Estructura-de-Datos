// Prueba por consola de las estructuras (sin ventana).
public class Prueba {
    public static void main(String[] args) {
        ListaSimple ls = new ListaSimple();
        ls.agregar("Mezcla 1"); ls.agregar("Mezcla 2"); ls.agregar("Mezcla 3");
        ls.eliminarUltimo();
        System.out.println("Lista simple:\n" + ls.texto());

        ListaDoble ld = new ListaDoble();
        ld.agregar("6.2"); ld.agregar("6.5"); ld.agregar("6.8");
        System.out.println("Lista doble: " + ld.texto());
        System.out.println("anterior: " + ld.anterior() + " | anterior: " + ld.anterior() + " | siguiente: " + ld.siguiente());

        ListaCircular lc = new ListaCircular();
        lc.agregar("Parcela A"); lc.agregar("Parcela B"); lc.agregar("Parcela C");
        System.out.println("Circular: " + lc.actual() + ", " + lc.siguiente() + ", " + lc.siguiente() + ", " + lc.siguiente());

        Pila pila = new Pila();
        pila.push("Mezcla 1"); pila.push("Mezcla 2");
        System.out.println("Pila pop: " + pila.pop());

        Cola cola = new Cola();
        cola.enqueue("Lote 1"); cola.enqueue("Lote 2");
        System.out.println("Cola dequeue: " + cola.dequeue());
    }
}
