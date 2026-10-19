import java.awt.*;
import javax.swing.*;

public final class Ventana extends JFrame {

    // ---------- DATOS (arreglos) ----------
    // Matriz 2D: fila = nutriente (N, P, K), columna = fertilizante (Urea, DAP, KCl)
    double[][] A = {
        { 0.46, 0.18, 0.00 },   // Nitrogeno (N)
        { 0.00, 0.46, 0.00 },   // Fosforo (P)
        { 0.00, 0.00, 0.60 }    // Potasio (K)
    };
    String[] nombresFert = { "Urea", "DAP", "KCl" };

    ListaSimple mezclas = new ListaSimple();     // historial de mezclas
    ListaDoble historialPH = new ListaDoble();   // historial de pH
    ListaCircular parcelas = new ListaCircular();// rotacion de parcelas
    Pila pilaUndo = new Pila();                  // para deshacer
    Cola colaLotes = new Cola();                 // lotes por procesar
    int numeroLote = 0;

    // ---------- COMPONENTES DE LA VENTANA ----------
    JLabel lblParcela = new JLabel();
    JTextField txtDefN = new JTextField("120");
    JTextField txtDefP = new JTextField("46");
    JTextField txtDefK = new JTextField("90");
    JTextField txtUrea = new JTextField("200");
    JTextField txtDAP = new JTextField("100");
    JTextField txtKCl = new JTextField("150");
    JTextField txtPH = new JTextField();
    JLabel lblPH = new JLabel("pH: sin registros");
    JLabel lblCola = new JLabel("Lotes en cola: 0");
    JTextArea areaResultado = new JTextArea();
    JTextArea areaHistorial = new JTextArea();

    public Ventana() {
        super("Optimizador de fertilizacion - Estructura de Datos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Parcelas (lista circular)
        parcelas.agregar("Parcela A");
        parcelas.agregar("Parcela B");
        parcelas.agregar("Parcela C");
        lblParcela.setText("Parcela actual: " + parcelas.actual());

        // --- Arriba: muestra la matriz A ---
        JTextArea areaMatriz = new JTextArea(mostrarMatriz());
        areaMatriz.setEditable(false);
        areaMatriz.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaMatriz.setBorder(BorderFactory.createTitledBorder("Matriz A (fraccion de N, P, K en cada fertilizante)"));
        add(areaMatriz, BorderLayout.NORTH);

        // --- Izquierda: datos y botones ---
        JPanel izquierda = new JPanel(new GridLayout(0, 1, 4, 4));
        izquierda.setBorder(BorderFactory.createTitledBorder("Datos (kg)"));
        izquierda.add(lblParcela);
        izquierda.add(fila("Deficit N:", txtDefN));
        izquierda.add(fila("Deficit P:", txtDefP));
        izquierda.add(fila("Deficit K:", txtDefK));
        izquierda.add(fila("kg de Urea:", txtUrea));
        izquierda.add(fila("kg de DAP:", txtDAP));
        izquierda.add(fila("kg de KCl:", txtKCl));
        izquierda.add(fila("pH:", txtPH));

        JButton btnMultiplicar = new JButton("Multiplicar (A x kg)");
        JButton btnDeshacer = new JButton("Deshacer ultima mezcla");
        JButton btnParcela = new JButton("Siguiente parcela");
        JButton btnRegistrarPH = new JButton("Registrar pH");
        JButton btnPHAnt = new JButton("< pH anterior");
        JButton btnPHSig = new JButton("pH siguiente >");
        JButton btnLote = new JButton("Procesar siguiente lote");

        izquierda.add(btnMultiplicar);
        izquierda.add(btnDeshacer);
        izquierda.add(btnParcela);
        izquierda.add(btnRegistrarPH);
        izquierda.add(btnPHAnt);
        izquierda.add(btnPHSig);
        izquierda.add(btnLote);
        add(izquierda, BorderLayout.WEST);

        // --- Centro: resultados e historial ---
        areaResultado.setEditable(false);
        areaHistorial.setEditable(false);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaHistorial.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane spRes = new JScrollPane(areaResultado);
        spRes.setBorder(BorderFactory.createTitledBorder("Resultado"));
        JScrollPane spHis = new JScrollPane(areaHistorial);
        spHis.setBorder(BorderFactory.createTitledBorder("Historial de mezclas"));

        JPanel abajoCentro = new JPanel(new GridLayout(2, 1, 4, 4));
        abajoCentro.add(spRes);
        abajoCentro.add(spHis);

        JPanel estado = new JPanel(new GridLayout(2, 1));
        estado.add(lblPH);
        estado.add(lblCola);

        add(abajoCentro, BorderLayout.CENTER);
        add(estado, BorderLayout.SOUTH);

        // --- Que hace cada boton ---
        btnMultiplicar.addActionListener(e -> multiplicar());
        btnDeshacer.addActionListener(e -> deshacer());
        btnParcela.addActionListener(e -> siguienteParcela());
        btnRegistrarPH.addActionListener(e -> registrarPH());
        btnPHAnt.addActionListener(e -> mostrarPH(historialPH.anterior()));
        btnPHSig.addActionListener(e -> mostrarPH(historialPH.siguiente()));
        btnLote.addActionListener(e -> procesarLote());

        setSize(850, 620);
        setLocationRelativeTo(null);
    }

    // ---------- FUNCIONES ----------

    // Multiplica la matriz A por el vector de kg y compara con el deficit
    void multiplicar() {
        try {
            // Vectores (arreglos 1D)
            double[] kg = { leer(txtUrea), leer(txtDAP), leer(txtKCl) };
            double[] deficit = { leer(txtDefN), leer(txtDefP), leer(txtDefK) };
            double[] aportado = new double[3];
            double[] falta = new double[3];

            // Multiplicacion matriz x vector:
            // aportado[i] = A[i][0]*kg[0] + A[i][1]*kg[1] + A[i][2]*kg[2]
            for (int i = 0; i < 3; i++) {
                aportado[i] = 0;
                for (int j = 0; j < 3; j++) {
                    aportado[i] = aportado[i] + A[i][j] * kg[j];
                }
                falta[i] = deficit[i] - aportado[i];
                if (falta[i] < 0) falta[i] = 0;   // si sobra, no falta nada
            }

            String texto = "Parcela: " + parcelas.actual() + "\n\n";
            texto = texto + String.format("Aporta:  N=%.1f  P=%.1f  K=%.1f kg%n", aportado[0], aportado[1], aportado[2]);
            texto = texto + String.format("Deficit: N=%.1f  P=%.1f  K=%.1f kg%n", deficit[0], deficit[1], deficit[2]);
            texto = texto + String.format("Falta:   N=%.1f  P=%.1f  K=%.1f kg%n", falta[0], falta[1], falta[2]);
            boolean cubierto = falta[0] == 0 && falta[1] == 0 && falta[2] == 0;
            texto = texto + (cubierto ? "\nEl deficit queda cubierto." : "\nTodavia falta cubrir el deficit.");
            areaResultado.setText(texto);

            // Guardar la mezcla en las estructuras
            String mezcla = parcelas.actual() + " | Urea " + kg[0] + ", DAP " + kg[1] + ", KCl " + kg[2]
                    + " | falta N " + String.format("%.1f", falta[0])
                    + ", P " + String.format("%.1f", falta[1])
                    + ", K " + String.format("%.1f", falta[2]);
            mezclas.agregar(mezcla);   // lista simple
            pilaUndo.push(mezcla);     // pila (para deshacer)
            numeroLote++;
            colaLotes.enqueue("Lote " + numeroLote + " -> " + mezcla);   // cola
            actualizar();
        } catch (NumberFormatException ex) {
            aviso("Escribe solo numeros (por ejemplo 100 o 46.5).");
        }
    }

    // Deshace la ultima mezcla usando la pila
    void deshacer() {
        String mezcla = pilaUndo.pop();
        if (mezcla == null) {
            areaResultado.setText("No hay mezclas para deshacer.");
        } else {
            mezclas.eliminarUltimo();
            areaResultado.setText("Se deshizo la mezcla:\n" + mezcla);
        }
        actualizar();
    }

    // Pasa a la siguiente parcela (lista circular)
    void siguienteParcela() {
        String p = parcelas.siguiente();
        lblParcela.setText("Parcela actual: " + p);
        areaResultado.setText("Parcela en turno: " + p);
    }

    // Guarda un pH en la lista doble
    void registrarPH() {
        try {
            double ph = leer(txtPH);
            if (ph < 0 || ph > 14) {
                aviso("El pH debe estar entre 0 y 14.");
                return;
            }
            historialPH.agregar("" + ph);
            txtPH.setText("");
            actualizar();
        } catch (NumberFormatException ex) {
            aviso("Escribe un pH valido (por ejemplo 6.5).");
        }
    }

    // Muestra el pH al moverse atras o adelante en la lista doble
    void mostrarPH(String valor) {
        if (valor == null) {
            aviso("Aun no hay mediciones de pH.");
        } else {
            lblPH.setText("pH actual: " + valor + "     Historial: " + historialPH.texto());
        }
    }

    // Saca el primer lote de la cola (FIFO)
    void procesarLote() {
        String lote = colaLotes.dequeue();
        areaResultado.setText(lote == null ? "No hay lotes pendientes." : "Lote procesado:\n" + lote);
        actualizar();
    }

    // ---------- AYUDAS ----------

    // Actualiza el historial y las etiquetas
    void actualizar() {
        areaHistorial.setText(mezclas.tamano == 0 ? "(sin mezclas)" : mezclas.texto());
        lblCola.setText("Lotes en cola: " + colaLotes.tamano);
        if (historialPH.tamano > 0) {
            lblPH.setText("pH actual: " + historialPH.actual.dato + "     Historial: " + historialPH.texto());
        }
    }

    // Lee un numero de un cuadro de texto (acepta coma o punto)
    double leer(JTextField campo) {
        return Double.parseDouble(campo.getText().trim().replace(',', '.'));
    }

    void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    // Convierte la matriz A en texto para mostrarla
    String mostrarMatriz() {
        String[] nutrientes = { "Nitrogeno (N)", "Fosforo (P)  ", "Potasio (K)  " };
        String t = String.format("%-15s", "");
        for (int j = 0; j < 3; j++) t = t + String.format("%-8s", nombresFert[j]);
        t = t + "\n";
        for (int i = 0; i < 3; i++) {
            t = t + String.format("%-15s", nutrientes[i]);
            for (int j = 0; j < 3; j++) t = t + String.format("%-8.2f", A[i][j]);
            if (i < 2) t = t + "\n";
        }
        return t;
    }

    // Una fila con una etiqueta y un cuadro de texto
    JPanel fila(String texto, JTextField campo) {
        JPanel p = new JPanel(new GridLayout(1, 2, 4, 0));
        p.add(new JLabel(texto));
        p.add(campo);
        return p;
    }

    // ---------- PROGRAMA PRINCIPAL ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ventana().setVisible(true));
    }
}
