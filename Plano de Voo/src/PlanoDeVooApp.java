import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;

public class PlanoDeVooApp {

    // --- Paleta de cores ---
    private static final Color AZUL_ESCURO   = new Color(0x0C, 0x44, 0x7C);
    private static final Color AZUL_MEDIO    = new Color(0x18, 0x5F, 0xA5);
    private static final Color AZUL_CLARO    = new Color(0xE6, 0xF1, 0xFB);
    private static final Color FUNDO         = new Color(0xF8, 0xF9, 0xFB);
    private static final Color SUPERFICIE    = Color.WHITE;
    private static final Color BORDA         = new Color(0xD8, 0xDC, 0xE3);
    private static final Color TEXTO_PRIMARIO= new Color(0x1A, 0x1F, 0x2E);
    private static final Color TEXTO_SEC     = new Color(0x5A, 0x65, 0x7A);
    private static final Color TEXTO_HINT    = new Color(0xA0, 0xAA, 0xBB);
    private static final Color VERDE_OK      = new Color(0x1A, 0x7A, 0x4A);
    private static final Color VERMELHO_ERR  = new Color(0xA3, 0x2D, 0x2D);

    // --- Campos do formulário ---
    private static JTextField  identaeronaveField;
    private static JCheckBox[] regrasField;
    private static JComboBox<String> tipoDeVooField;
    private static JTextField  numaeronaveField;
    private static JTextField  tipoaeronaveField;
    private static JComboBox<String> catetField;
    private static JComboBox<String> equipamentoField;
    private static JComboBox<String> equipamentoVField;
    private static JTextField  aerodromoptField;
    private static JTextField  horaeobtField;
    private static JTextField  velocidadeField;
    private static JTextField  nivelDeVooField;
    private static JTextField  rotaField;
    private static JTextField  aerodromodestinoField;
    private static JTextField  duracaototalvooField;
    private static JTextField  aerodromoalternativaField;
    private static JTextArea   observacoesField;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(PlanoDeVooApp::criarJanela);
    }

    private static void criarJanela() {
        JFrame frame = new JFrame("Plano de Voo — ICAO FPL");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(780, 600));
        frame.setLocationRelativeTo(null);

        // Painel principal com scroll
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(FUNDO);

        root.add(criarHeader(), BorderLayout.NORTH);

        JPanel corpo = new JPanel();
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setBackground(FUNDO);
        corpo.setBorder(new EmptyBorder(20, 24, 10, 24));

        corpo.add(criarSecao("1", "Identificação da Aeronave", criarPainelIdentificacao()));
        corpo.add(Box.createVerticalStrut(14));
        corpo.add(criarSecao("2", "Classificação e Equipamentos", criarPainelClassificacao()));
        corpo.add(Box.createVerticalStrut(14));
        corpo.add(criarSecao("3", "Partida e Rota", criarPainelRota()));
        corpo.add(Box.createVerticalStrut(14));
        corpo.add(criarSecao("4", "Destino e Alternativa", criarPainelDestino()));
        corpo.add(Box.createVerticalStrut(20));

        JScrollPane scroll = new JScrollPane(corpo);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(FUNDO);
        scroll.getViewport().setBackground(FUNDO);

        root.add(scroll, BorderLayout.CENTER);
        root.add(criarFooter(frame), BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.pack();
        frame.setSize(820, 780);
        frame.setVisible(true);
    }

    // ── Header ──────────────────────────────────────────────────────────────
    private static JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_ESCURO);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Linha decorativa sutil
                g2.setColor(new Color(0xFF, 0xFF, 0xFF, 18));
                g2.setStroke(new BasicStroke(1f));
                for (int i = -40; i < getWidth() + 40; i += 60) {
                    g2.drawLine(i, getHeight(), i + 80, 0);
                }
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(0, 74));
        header.setBorder(new EmptyBorder(0, 24, 0, 24));

        // Ícone avião
        JLabel iconLabel = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xFF, 0xFF, 0xFF, 40));
                g2.fillOval(0, 0, 44, 44);
                g2.setColor(new Color(0xFF, 0xFF, 0xFF, 80));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(0, 0, 44, 44);
                // Avião simples
                g2.setColor(Color.WHITE);
                int[] xp = {22, 14, 30};
                int[] yp = {8,  36, 36};
                g2.fillPolygon(xp, yp, 3);
                g2.fillRect(16, 26, 12, 4);
                g2.dispose();
            }
        };
        iconLabel.setPreferredSize(new Dimension(48, 48));

        JPanel titulos = new JPanel();
        titulos.setLayout(new BoxLayout(titulos, BoxLayout.Y_AXIS));
        titulos.setOpaque(false);
        titulos.setBorder(new EmptyBorder(0, 14, 0, 0));

        JLabel titulo = new JLabel("PLANO DE VOO");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("FLIGHT PLAN SUBMISSION FORM  ·  ICAO FPL");
        subtitulo.setFont(new Font("Monospaced", Font.PLAIN, 10));
        subtitulo.setForeground(new Color(0xFF, 0xFF, 0xFF, 130));
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        titulos.add(titulo);
        titulos.add(Box.createVerticalStrut(3));
        titulos.add(subtitulo);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(iconLabel);
        left.add(titulos);

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        badges.setOpaque(false);
        badges.add(criarBadge("FPL"));
        badges.add(criarBadge("ICAO"));

        header.add(left, BorderLayout.WEST);
        header.add(badges, BorderLayout.EAST);
        return header;
    }

    private static JLabel criarBadge(String texto) {
        JLabel b = new JLabel(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xFF, 0xFF, 0xFF, 28));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(new Color(0xFF, 0xFF, 0xFF, 60));
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setText(texto);
        b.setFont(new Font("Monospaced", Font.BOLD, 10));
        b.setForeground(new Color(0xFF, 0xFF, 0xFF, 160));
        b.setBorder(new EmptyBorder(4, 10, 4, 10));
        b.setOpaque(false);
        return b;
    }

    // ── Seção com título numerado ────────────────────────────────────────────
    private static JPanel criarSecao(String num, String titulo, JPanel conteudo) {
        JPanel secao = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SUPERFICIE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BORDA);
                g2.setStroke(new BasicStroke(0.8f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        secao.setOpaque(false);
        secao.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Cabeçalho da seção
        JPanel cabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        cabecalho.setBackground(new Color(0xF2, 0xF5, 0xFA));
        cabecalho.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDA),
            new EmptyBorder(8, 14, 8, 14)
        ));

        // Número da seção (círculo)
        JLabel numLabel = new JLabel(num) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_ESCURO);
                g2.fillOval(0, 0, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        numLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        numLabel.setForeground(Color.WHITE);
        numLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numLabel.setPreferredSize(new Dimension(20, 20));
        numLabel.setOpaque(false);

        JLabel tituloLabel = new JLabel(titulo.toUpperCase());
        tituloLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        tituloLabel.setForeground(TEXTO_SEC);

        cabecalho.add(numLabel);
        cabecalho.add(tituloLabel);

        secao.add(cabecalho, BorderLayout.NORTH);
        conteudo.setBorder(new EmptyBorder(14, 16, 16, 16));
        secao.add(conteudo, BorderLayout.CENTER);
        return secao;
    }

    // ── Painéis de conteúdo ──────────────────────────────────────────────────
    private static JPanel criarPainelIdentificacao() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(SUPERFICIE);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 10, 10);

        // Linha 1: Identificação | Número
        c.gridx = 0; c.gridy = 0; c.weightx = 0.5;
        p.add(criarCampo("Identificação da Aeronave *", identaeronaveField = criarTextField("ex: PT-ABC", true)), c);
        c.gridx = 1; c.insets = new Insets(0, 0, 10, 0);
        p.add(criarCampo("Número da Aeronave *", numaeronaveField = criarTextField("ex: 737", true)), c);

        // Linha 2: Tipo | Regras
        c.gridx = 0; c.gridy = 1; c.insets = new Insets(0, 0, 0, 10);
        p.add(criarCampo("Tipo de Aeronave *", tipoaeronaveField = criarTextField("ex: B737", true)), c);
        c.gridx = 1; c.insets = new Insets(0, 0, 0, 0);
        p.add(criarCampoRegras(), c);

        return p;
    }

    private static JPanel criarPainelClassificacao() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(SUPERFICIE);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0 / 3.0;

        // Linha 1: Tipo de Voo | Esteira | Equipamento
        c.gridx = 0; c.gridy = 0; c.insets = new Insets(0, 0, 10, 10);
        tipoDeVooField = criarComboBox(new String[]{"G","S","N","M","X"});
        p.add(criarCampo("Tipo de Voo *", tipoDeVooField), c);

        c.gridx = 1;
        catetField = criarComboBox(new String[]{"L","M","H","J"});
        p.add(criarCampo("Esteira de Turbulência *", catetField), c);

        c.gridx = 2; c.insets = new Insets(0, 0, 10, 0);
        equipamentoField = criarComboBox(new String[]{"N","S","A","B","C","D","E1","E2","E3","F","G","H","I"});
        p.add(criarCampo("Equipamento *", equipamentoField), c);

        // Linha 2: Equip. Vigilância (largura total)
        c.gridx = 0; c.gridy = 1; c.gridwidth = 3; c.weightx = 1.0;
        c.insets = new Insets(0, 0, 0, 0);
        equipamentoVField = criarComboBox(new String[]{"N","S","A","B","C","D","E1","E2","E3","F","G","H","I"});
        p.add(criarCampo("Equipamento de Vigilância *", equipamentoVField), c);

        return p;
    }

    private static JPanel criarPainelRota() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(SUPERFICIE);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        // Linha 1: Aeródromo Partida | Hora EOBT
        c.gridx = 0; c.gridy = 0; c.weightx = 0.5; c.insets = new Insets(0, 0, 10, 10);
        p.add(criarCampo("Aeródromo de Partida *", aerodromoptField = criarTextField("ICAO ex: SBSP", true)), c);
        c.gridx = 1; c.insets = new Insets(0, 0, 10, 0);
        p.add(criarCampo("Hora EOBT *", horaeobtField = criarTextField("HHMM ex: 1430", true)), c);

        // Linha 2: Velocidade | Nível
        c.gridx = 0; c.gridy = 1; c.insets = new Insets(0, 0, 10, 10);
        p.add(criarCampo("Velocidade de Cruzeiro *", velocidadeField = criarTextField("ex: N0450", true)), c);
        c.gridx = 1; c.insets = new Insets(0, 0, 10, 0);
        p.add(criarCampo("Nível de Cruzeiro *", nivelDeVooField = criarTextField("ex: F350", true)), c);

        // Linha 3: Rota (largura total)
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.weightx = 1.0;
        c.insets = new Insets(0, 0, 0, 0);
        p.add(criarCampo("Rota *", rotaField = criarTextField("ex: EFICO UL306 GINKO", true)), c);

        return p;
    }

    private static JPanel criarPainelDestino() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(SUPERFICIE);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0 / 3.0;

        // Linha 1: Destino | Duração | Alternativa
        c.gridx = 0; c.gridy = 0; c.insets = new Insets(0, 0, 10, 10);
        p.add(criarCampo("Aeródromo de Destino *", aerodromodestinoField = criarTextField("ICAO ex: SBRJ", true)), c);
        c.gridx = 1;
        p.add(criarCampo("Duração Prevista *", duracaototalvooField = criarTextField("HHMM ex: 0145", true)), c);
        c.gridx = 2; c.insets = new Insets(0, 0, 10, 0);
        p.add(criarCampo("Aeródromo Alternativa *", aerodromoalternativaField = criarTextField("ICAO ex: SBGL", true)), c);

        // Linha 2: Observações (largura total)
        observacoesField = new JTextArea(3, 20);
        observacoesField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        observacoesField.setForeground(TEXTO_PRIMARIO);
        observacoesField.setBackground(new Color(0xF8, 0xF9, 0xFB));
        observacoesField.setLineWrap(true);
        observacoesField.setWrapStyleWord(true);
        observacoesField.setBorder(new CompoundBorder(
            new LineBorder(BORDA, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        JScrollPane sp = new JScrollPane(observacoesField);
        sp.setBorder(null);

        c.gridx = 0; c.gridy = 1; c.gridwidth = 3; c.weightx = 1.0;
        c.insets = new Insets(0, 0, 0, 0);
        p.add(criarCampo("Observações", sp), c);

        return p;
    }

    // ── Componentes auxiliares ───────────────────────────────────────────────
    private static JPanel criarCampo(String label, JComponent campo) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(SUPERFICIE);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(TEXTO_SEC);

        p.add(lbl, BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }

    private static JPanel criarCampoRegras() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 4));
        wrapper.setBackground(SUPERFICIE);

        JLabel lbl = new JLabel("Regras *");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(TEXTO_SEC);
        wrapper.add(lbl, BorderLayout.NORTH);

        String[] opcoes = {"I", "V", "Y", "Z"};
        regrasField = new JCheckBox[opcoes.length];
        JPanel cbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        cbPanel.setBackground(SUPERFICIE);

        for (int i = 0; i < opcoes.length; i++) {
            JCheckBox cb = new JCheckBox(opcoes[i]) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color bg = isSelected() ? AZUL_CLARO : new Color(0xF8, 0xF9, 0xFB);
                    Color border = isSelected() ? AZUL_MEDIO : BORDA;
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g2.setColor(border);
                    g2.setStroke(new BasicStroke(isSelected() ? 1.5f : 0.8f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            cb.setFont(new Font("Monospaced", Font.BOLD, 13));
            cb.setForeground(isSelected(cb) ? AZUL_ESCURO : TEXTO_PRIMARIO);
            cb.setOpaque(false);
            cb.setContentAreaFilled(false);
            cb.setBorderPainted(false);
            cb.setFocusPainted(false);
            cb.setBorder(new EmptyBorder(5, 12, 5, 12));
            cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cb.addItemListener(e -> cb.repaint());
            regrasField[i] = cb;
            cbPanel.add(cb);
        }

        wrapper.add(cbPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private static boolean isSelected(JCheckBox cb) { return false; }

    private static JTextField criarTextField(String placeholder, boolean mono) {
        JTextField tf = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.setColor(TEXTO_HINT);
                    g3.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    FontMetrics fm = g3.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g3.drawString(placeholder, getInsets().left + 2, y);
                    g3.dispose();
                }
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isFocusOwner() ? AZUL_MEDIO : BORDA);
                g2.setStroke(new BasicStroke(isFocusOwner() ? 1.5f : 0.8f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.dispose();
            }
        };
        tf.setFont(new Font(mono ? "Monospaced" : "SansSerif", Font.PLAIN, 13));
        tf.setForeground(TEXTO_PRIMARIO);
        tf.setBackground(new Color(0xF8, 0xF9, 0xFB));
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(7, 10, 7, 10));
        tf.setPreferredSize(new Dimension(0, 36));
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { tf.repaint(); }
            public void focusLost(FocusEvent e) { tf.repaint(); }
        });
        return tf;
    }

    private static JComboBox<String> criarComboBox(String[] opcoes) {
        String[] withEmpty = new String[opcoes.length + 1];
        withEmpty[0] = "— selecione";
        System.arraycopy(opcoes, 0, withEmpty, 1, opcoes.length);

        JComboBox<String> cb = new JComboBox<>(withEmpty);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cb.setForeground(TEXTO_PRIMARIO);
        cb.setBackground(new Color(0xF8, 0xF9, 0xFB));
        cb.setPreferredSize(new Dimension(0, 36));
        cb.setBorder(new LineBorder(BORDA, 1, true));
        return cb;
    }

    // ── Footer ───────────────────────────────────────────────────────────────
    private static JPanel criarFooter(JFrame frame) {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(SUPERFICIE);
        footer.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDA),
            new EmptyBorder(14, 24, 14, 24)
        ));

        JLabel obrig = new JLabel("* Campos obrigatórios");
        obrig.setFont(new Font("SansSerif", Font.PLAIN, 11));
        obrig.setForeground(TEXTO_HINT);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setOpaque(false);

        JButton cancelar = criarBotao("Cancelar", false);
        cancelar.addActionListener(e -> frame.dispose());

        JButton enviar = criarBotao("Enviar Plano de Voo", true);
        enviar.addActionListener(e -> enviarPlano());

        botoes.add(cancelar);
        botoes.add(enviar);

        footer.add(obrig, BorderLayout.WEST);
        footer.add(botoes, BorderLayout.EAST);
        return footer;
    }

    private static JButton criarBotao(String texto, boolean primario) {
        JButton btn = new JButton(texto) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (primario) {
                    g2.setColor(hovered ? AZUL_MEDIO : AZUL_ESCURO);
                } else {
                    g2.setColor(hovered ? new Color(0xEE, 0xF1, 0xF6) : SUPERFICIE);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {
                if (!primario) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(BORDA);
                    g2.setStroke(new BasicStroke(0.8f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    g2.dispose();
                }
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(primario ? Color.WHITE : TEXTO_PRIMARIO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 20, 9, 20));
        return btn;
    }

    // ── Validação ────────────────────────────────────────────────────────────
    private static boolean validarEntrada(String texto, String tipo) {
        switch (tipo) {
            case "letras":  return texto.matches("[a-zA-Z]+");
            case "numeros": return texto.matches("\\d+");
            case "horario": return texto.matches("\\d{4}");
            default:        return true;
        }
    }

    private static boolean validarFormulario() {
        if (identaeronaveField.getText().trim().isEmpty()) {
            erro("Identificação da Aeronave deve ser preenchida!"); return false;
        }
        boolean regraSelecionada = false;
        for (JCheckBox cb : regrasField) if (cb.isSelected()) { regraSelecionada = true; break; }
        if (!regraSelecionada) { erro("Ao menos uma Regra deve ser selecionada!"); return false; }
        if (tipoDeVooField.getSelectedIndex() == 0) { erro("Tipo de Voo deve ser selecionado!"); return false; }
        if (numaeronaveField.getText().trim().isEmpty()) { erro("Número da Aeronave deve ser preenchido!"); return false; }
        if (!validarEntrada(numaeronaveField.getText().trim(), "numeros")) { erro("Número da Aeronave deve conter apenas números."); return false; }
        if (tipoaeronaveField.getText().trim().isEmpty()) { erro("Tipo de Aeronave deve ser preenchido!"); return false; }
        if (catetField.getSelectedIndex() == 0) { erro("Categoria de Esteira de Turbulência deve ser selecionada!"); return false; }
        if (equipamentoField.getSelectedIndex() == 0) { erro("Equipamento deve ser selecionado!"); return false; }
        if (equipamentoVField.getSelectedIndex() == 0) { erro("Equipamento de Vigilância deve ser selecionado!"); return false; }
        if (aerodromoptField.getText().trim().isEmpty()) { erro("Aeródromo de Partida deve ser preenchido!"); return false; }
        if (!validarEntrada(horaeobtField.getText().trim(), "horario")) { erro("Hora EOBT deve estar no formato HHMM (ex: 1430)!"); return false; }
        if (velocidadeField.getText().trim().isEmpty()) { erro("Velocidade de Cruzeiro deve ser preenchida!"); return false; }
        if (nivelDeVooField.getText().trim().isEmpty()) { erro("Nível de Cruzeiro deve ser preenchido!"); return false; }
        if (rotaField.getText().trim().isEmpty()) { erro("Rota deve ser preenchida!"); return false; }
        if (aerodromodestinoField.getText().trim().isEmpty()) { erro("Aeródromo de Destino deve ser preenchido!"); return false; }
        if (!validarEntrada(aerodromodestinoField.getText().trim(), "letras")) { erro("Aeródromo de Destino deve conter apenas letras."); return false; }
        if (duracaototalvooField.getText().trim().isEmpty()) { erro("Duração Prevista do Voo deve ser preenchida!"); return false; }
        if (!validarEntrada(duracaototalvooField.getText().trim(), "numeros")) { erro("Duração do Voo deve conter apenas números."); return false; }
        if (aerodromoalternativaField.getText().trim().isEmpty()) { erro("Aeródromo de Alternativa deve ser preenchido!"); return false; }
        if (!validarEntrada(aerodromoalternativaField.getText().trim(), "letras")) { erro("Aeródromo de Alternativa deve conter apenas letras."); return false; }
        return true;
    }

    private static void erro(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
    }

    // ── Envio ────────────────────────────────────────────────────────────────
    private static void enviarPlano() {
        if (!validarFormulario()) return;

        StringBuilder regras = new StringBuilder();
        for (JCheckBox cb : regrasField) if (cb.isSelected()) regras.append(cb.getText()).append(", ");
        String regrasStr = regras.toString().replaceAll(", $", "");

        // --- CONFIGURAÇÃO NEON (POSTGRESQL) ---
        String dbUrl = "jdbc:postgresql://ep-purple-resonance-ac9ld1p7-pooler.sa-east-1.aws.neon.tech/neondb?sslmode=require";
        String user = "neondb_owner";
        String pass = "npg_i9PkZ2R0czgl";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, pass)) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO plano_voo (identificacao_aeronave, regras, tipo_voo, " +
                "numero_aeronave, tipo_aeronave, catet, equipamento, equipamento_vigilancia, " +
                "aerodromo_pt, hora_eob, velocidade, nivel_voo, rota, aerodromo_destino, " +
                "duracao_total_voo, aerodromo_alternativa, observacoes) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
            );
            ps.setString(1,  identaeronaveField.getText().trim());
            ps.setString(2,  regrasStr);
            ps.setString(3,  (String) tipoDeVooField.getSelectedItem());
            ps.setString(4,  numaeronaveField.getText().trim());
            ps.setString(5,  tipoaeronaveField.getText().trim());
            ps.setString(6,  (String) catetField.getSelectedItem());
            ps.setString(7,  (String) equipamentoField.getSelectedItem());
            ps.setString(8,  (String) equipamentoVField.getSelectedItem());
            ps.setString(9,  aerodromoptField.getText().trim());
            ps.setString(10, horaeobtField.getText().trim());
            ps.setString(11, velocidadeField.getText().trim());
            ps.setString(12, nivelDeVooField.getText().trim());
            ps.setString(13, rotaField.getText().trim());
            ps.setString(14, aerodromodestinoField.getText().trim());
            ps.setString(15, duracaototalvooField.getText().trim());
            ps.setString(16, aerodromoalternativaField.getText().trim());
            ps.setString(17, observacoesField.getText().trim());
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Plano de voo enviado com sucesso para o Neon!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao Neon:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void limparCampos() {
        identaeronaveField.setText("");
        numaeronaveField.setText("");
        tipoaeronaveField.setText("");
        aerodromoptField.setText("");
        horaeobtField.setText("");
        velocidadeField.setText("");
        nivelDeVooField.setText("");
        rotaField.setText("");
        aerodromodestinoField.setText("");
        duracaototalvooField.setText("");
        aerodromoalternativaField.setText("");
        observacoesField.setText("");
        for (JCheckBox cb : regrasField) cb.setSelected(false);
        tipoDeVooField.setSelectedIndex(0);
        catetField.setSelectedIndex(0);
        equipamentoField.setSelectedIndex(0);
        equipamentoVField.setSelectedIndex(0);
    }
}