// App.java
package Elevador;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Elevador();
        });
    }
}
