package Elevador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Elevador extends JFrame {
    private JButton[] botoesChamar;
    private JButton[] botoesAndar;
    private JTextArea display;
    private ElevadorPanel[] elevadores;

    private int[] posicaoElevadores;
    private int[] direcaoElevadores;

    public Elevador() {
        super("Sistema de Elevadores");

        // Inicializando variáveis
        botoesChamar = new JButton[2];
        botoesAndar = new JButton[6];
        display = new JTextArea();
        elevadores = new ElevadorPanel[2];
        posicaoElevadores = new int[] { 0, 0 }; // Posição inicial dos elevadores (Térreo)
        direcaoElevadores = new int[] { 0, 0 }; // 0 para parado, 1 para subindo, -1 para descendo

        // Configurando a interface gráfica
        JPanel panel = new JPanel(new BorderLayout());

        // Painel para os botões
        JPanel botoesPanel = new JPanel(new GridLayout(6, 2));
        for (int i = 0; i < 6; i++) {
            final int andar = i;
            botoesAndar[i] = new JButton(String.valueOf(i));
            botoesAndar[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int andarSolicitado = Integer.parseInt(((JButton) e.getSource()).getText());
                    moverElevador(0, andarSolicitado);
                }
            });
            botoesPanel.add(botoesAndar[i]);

            botoesChamar[i % 2] = new JButton("Chamar");
            botoesChamar[i % 2].setBackground(new Color(224, 202, 0)); // Cor verde mais vibrante
            botoesChamar[i % 2].setForeground(Color.BLACK); // Texto em branco para maior contraste
            botoesChamar[i % 2].setFont(new Font("Arial", Font.PLAIN, 12)); // Fonte diferente
            botoesChamar[i % 2].setFocusPainted(false); // Remove a borda ao clicar
            botoesChamar[i % 2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int elevadorMaisProximo = obterElevadorMaisProximo(andar);
                    moverElevador(elevadorMaisProximo, andar);
                }
            });

            botoesPanel.add(botoesChamar[i % 2]);
        }

        // Painel para os elevadores
        JPanel elevadoresPanel = new JPanel(new GridLayout(1, 2));
        for (int i = 0; i < 2; i++) {
            elevadores[i] = new ElevadorPanel(i + 1); // Passando o número do elevador
            elevadoresPanel.add(elevadores[i]);
        }

        // Adicionando os painéis ao painel principal
        panel.add(botoesPanel, BorderLayout.EAST);
        panel.add(elevadoresPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(display), BorderLayout.CENTER);

        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setVisible(true);
    }

    // Retorna o índice do elevador mais próximo do andar especificado
    private int obterElevadorMaisProximo(int andar) {
        int elevadorMaisProximo = 0;
        int distanciaMinima = Math.abs(posicaoElevadores[0] - andar);
        for (int i = 1; i < posicaoElevadores.length; i++) {
            int distancia = Math.abs(posicaoElevadores[i] - andar);
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                elevadorMaisProximo = i;
            }
        }
        return elevadorMaisProximo;
    }

    // Move o elevador para o andar especificado
    private void moverElevador(final int indiceElevador, final int andar) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int andarAtual = posicaoElevadores[indiceElevador];
                int andarDestino = andar;
                direcaoElevadores[indiceElevador] = andarAtual < andarDestino ? 1 : -1;

                display.append("Elevador " + (indiceElevador + 1) + " está se movendo...\n");
                while (andarAtual != andarDestino) {
                    try {
                        Thread.sleep(2000); // Delay de 2 segundos por andar
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    andarAtual += direcaoElevadores[indiceElevador];
                    posicaoElevadores[indiceElevador] = andarAtual;
                    elevadores[indiceElevador].setAndarAtual(andarAtual);
                    display.setText(display.getText() + "Elevador " + (indiceElevador + 1) + " chegou ao "
                            + (andarAtual >= 0 ? "andar " + andarAtual : "subsolo") + "\n");
                }
                display.append("Bem-vindo! Por favor, entre no Elevador " + (indiceElevador + 1) + "\n");
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Elevador();
            }
        });
    }
}

class ElevadorPanel extends JPanel {
    private int andarAtual;
    private int numeroElevador;

    public ElevadorPanel(int numeroElevador) {
        this.numeroElevador = numeroElevador;
        setPreferredSize(new Dimension(100, 300));
        setBackground(new Color(192, 192, 192)); // Cor cinza para o elevador
    }

    public void setAndarAtual(int andarAtual) {
        this.andarAtual = andarAtual;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Cenário de céu como fundo
        g2d.setColor(new Color(135, 206, 250)); // Azul claro
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Adicione cores mais atraentes
        g2d.setColor(new Color(255, 223, 186)); // Cor do Elevador
        g2d.fillRoundRect(20, 50, 60, 200, 10, 10); // Elevador com borda arredondada
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(20, 50, 60, 200, 10, 10); // Contorno do Elevador

        // Adicione um gradiente para tornar mais atraente
        GradientPaint gradient = new GradientPaint(20, 50, Color.GRAY, 80, 250, new Color(169, 169, 169));
        g2d.setPaint(gradient);
        g2d.fillRect(20, 50, 60, 200);

        // Adicione um texto estilizado
        g2d.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD, 12);
        g2d.setFont(font);
        g2d.drawString("Elevador " + numeroElevador, 25, 30); // Texto "Elevador"
        g2d.drawString("Andar: " + andarAtual, 25, 270); // Texto "Andar"
    }
}
