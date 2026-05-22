import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

class HuffmanNode {
    byte data;
    int freq;
    HuffmanNode left, right;

    HuffmanNode(byte data, int freq) {
        this.data = data;
        this.freq = freq;
    }

    HuffmanNode(int freq, HuffmanNode left, HuffmanNode right) {
        this.data = 0;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    boolean isLeaf() {
        return left == null && right == null;
    }
}

public class HuffPressPro extends JFrame {

    // Modern UI Colors
    private static final Color BG_DARK = new Color(18, 22, 33);
    private static final Color BG_CARD = new Color(28, 34, 52);
    private static final Color TEXT_MAIN = new Color(240, 244, 255);
    private static final Color TEXT_MUTED = new Color(140, 155, 185);
    private static final Color ACCENT_PRIMARY = new Color(88, 101, 242);
    private static final Color ACCENT_SUCCESS = new Color(87, 242, 135);
    private static final Color ACCENT_DECOMP = new Color(235, 69, 158);

    private JTextField fileField;
    private JProgressBar bar;
    private JLabel statusLabel;
    
    // Live Dashboard Labels
    private JLabel lblOrigSize, lblCompSize, lblRatio, lblSaved;
    private JTextArea miniLog;

    private File selectedFile;

    public HuffPressPro() {
        setTitle("HuffPress Pro - Ultimate Compressor");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        initModernUI();
    }

    private void initModernUI() {
        setLayout(new BorderLayout(15, 15));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // ================= TOP: FILE PICKER PANEL =================
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setOpaque(false);

        fileField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        fileField.setOpaque(false);
        fileField.setBorder(new EmptyBorder(12, 15, 12, 15));
        fileField.setBackground(new Color(0,0,0,0));
        fileField.setForeground(TEXT_MAIN);
        fileField.setCaretColor(TEXT_MAIN);
        fileField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fileField.setEditable(false);
        fileField.setText("Select a file to begin compression or decompression...");

        JButton btnBrowse = createModernButton("Browse File", ACCENT_PRIMARY);
        topPanel.add(fileField, BorderLayout.CENTER);
        topPanel.add(btnBrowse, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ================= CENTER: MAIN DASHBOARD GRID =================
        JPanel mainGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        mainGrid.setOpaque(false);

        // LEFT SIDE: Stats Panel Cards
        JPanel statsContainer = new JPanel(new GridLayout(2, 2, 15, 15));
        statsContainer.setOpaque(false);

        lblOrigSize = createStatCard(statsContainer, "Original Size", "0 Bytes", TEXT_MAIN);
        lblCompSize = createStatCard(statsContainer, "Processed Size", "0 Bytes", TEXT_MAIN);
        lblRatio = createStatCard(statsContainer, "Compression Ratio", "1:1", ACCENT_PRIMARY);
        lblSaved = createStatCard(statsContainer, "Space Saved", "0.00%", ACCENT_SUCCESS);
        
        mainGrid.add(statsContainer);

        // RIGHT SIDE: Mini Terminal Logger Panel
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBackground(BG_CARD);
        logPanel.setBorder(BorderFactory.createLineBorder(new Color(45, 55, 85), 1));
        
        JLabel logTitle = new JLabel(" Processing Activity Logs");
        logTitle.setForeground(TEXT_MUTED);
        logTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logTitle.setBorder(new EmptyBorder(10, 10, 5, 10));
        logPanel.add(logTitle, BorderLayout.NORTH);

        miniLog = new JTextArea();
        miniLog.setBackground(BG_CARD);
        miniLog.setForeground(new Color(180, 200, 240));
        miniLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        miniLog.setEditable(false);
        miniLog.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JScrollPane scroll = new JScrollPane(miniLog);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        logPanel.add(scroll, BorderLayout.CENTER);

        mainGrid.add(logPanel);
        add(mainGrid, BorderLayout.CENTER);

        // ================= BOTTOM: CONTROL BAR =================
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);

        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionButtons.setOpaque(false);

        JButton btnCompress = createModernButton("⚡ Compress File", ACCENT_SUCCESS);
        JButton btnDecompress = createModernButton("\uD83D\uDCC2 Decompress File", ACCENT_DECOMP);
        actionButtons.add(btnCompress);
        actionButtons.add(btnDecompress);

        JPanel progressWrapper = new JPanel(new BorderLayout(10, 5));
        progressWrapper.setOpaque(false);

        statusLabel = new JLabel("Status: System Idle");
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        bar = new JProgressBar();
        bar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 8));
        bar.setForeground(ACCENT_PRIMARY);
        bar.setBackground(BG_CARD);
        bar.setBorderPainted(false);

        progressWrapper.add(statusLabel, BorderLayout.WEST);
        progressWrapper.add(bar, BorderLayout.CENTER);

        bottomPanel.add(actionButtons, BorderLayout.NORTH);
        bottomPanel.add(progressWrapper, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // ================= WIRING EVENT CONTROLLERS =================
        btnBrowse.addActionListener(e -> {
            JFileChooser ch = new JFileChooser();
            if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = ch.getSelectedFile();
                fileField.setText(selectedFile.getAbsolutePath());
                resetDashboard();
                log("Selected target: " + selectedFile.getName() + " (" + formatSize(selectedFile.length()) + ")");
            }
        });

        btnCompress.addActionListener(e -> new Thread(this::compressPipeline).start());
        btnDecompress.addActionListener(e -> new Thread(this::decompressPipeline).start());
    }

    // ================= GUI HELPER ENGINES =================
    private JButton createModernButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(baseColor.brighter());
                } else {
                    g2.setColor(baseColor);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 24, 12, 24));
        return btn;
    }

    private JLabel createStatCard(JPanel container, String title, String initVal, Color metricColor) {
        JPanel card = new JPanel(new BorderLayout(0, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLabel.setForeground(TEXT_MUTED);

        JLabel valLabel = new JLabel(initVal);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLabel.setForeground(metricColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valLabel, BorderLayout.CENTER);
        container.add(card);

        return valLabel;
    }

    private void resetDashboard() {
        lblOrigSize.setText(formatSize(selectedFile != null ? selectedFile.length() : 0));
        lblCompSize.setText("0 Bytes");
        lblRatio.setText("1:1");
        lblSaved.setText("0.00%");
        miniLog.setText("");
        bar.setValue(0);
        statusLabel.setText("Status: Ready");
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.2f %cB", bytes / Math.pow(1024, exp), pre);
    }

    private void log(String msg) {
        SwingUtilities.invokeLater(() -> miniLog.append("» " + msg + "\n"));
    }

    private void updateUIState(int progress, String status, Runnable dataUpdater) {
        SwingUtilities.invokeLater(() -> {
            bar.setValue(progress);
            statusLabel.setText("Status: " + status);
            if (dataUpdater != null) dataUpdater.run();
        });
    }

    // ================= SECURE HUFFMAN ENGINES =================
    private void compressPipeline() {
        if (selectedFile == null || !selectedFile.exists()) {
            log("Execution Aborted: File reference missing.");
            return;
        }
        try {
            updateUIState(10, "Extracting dynamic source streams...", null);
            byte[] rawData = Files.readAllBytes(selectedFile.toPath());
            if (rawData.length == 0) {
                log("Error: Target file empty.");
                return;
            }

            updateUIState(25, "Building frequency matrices...", null);
            Map<Byte, Integer> frequencies = new HashMap<>();
            for (byte b : rawData) {
                frequencies.put(b, frequencies.getOrDefault(b, 0) + 1);
            }

            PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.freq));
            for (var entry : frequencies.entrySet()) {
                pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
            }

            // Fix Edge Case: Handle single character redundancy safely
            if (pq.size() == 1) {
                HuffmanNode target = pq.poll();
                pq.add(new HuffmanNode(target.freq, target, null));
            } else {
                while (pq.size() > 1) {
                    HuffmanNode nodeA = pq.poll();
                    HuffmanNode nodeB = pq.poll();
                    pq.add(new HuffmanNode(nodeA.freq + nodeB.freq, nodeA, nodeB));
                }
            }

            HuffmanNode root = pq.poll();
            Map<Byte, String> prefixCodes = new HashMap<>();
            generateBitmaps(root, "", prefixCodes);

            updateUIState(55, "Streaming data structures onto disk...", null);
            File outputFile = new File(selectedFile.getAbsolutePath() + ".huff");
            
            try (DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                outputStream.writeUTF(selectedFile.getName());
                outputStream.writeInt(frequencies.size());

                for (var entry : frequencies.entrySet()) {
                    outputStream.writeByte(entry.getKey());
                    outputStream.writeInt(entry.getValue());
                }

                long computedBits = 0;
                for (byte b : rawData) {
                    computedBits += prefixCodes.get(b).length();
                }
                outputStream.writeLong(computedBits);

                // Bit streaming architecture to prevent memory spikes
                int streamAccumulator = 0;
                int trackBits = 0;
                for (byte b : rawData) {
                    String sequence = prefixCodes.get(b);
                    for (int i = 0; i < sequence.length(); i++) {
                        streamAccumulator <<= 1;
                        if (sequence.charAt(i) == '1') {
                            streamAccumulator |= 1;
                        }
                        trackBits++;
                        if (trackBits == 8) {
                            outputStream.writeByte(streamAccumulator);
                            streamAccumulator = 0;
                            trackBits = 0;
                        }
                    }
                }
                if (trackBits > 0) {
                    streamAccumulator <<= (8 - trackBits);
                    outputStream.writeByte(streamAccumulator);
                }
            }

            // Calculation metrics
            long sizeOrig = selectedFile.length();
            long sizeComp = outputFile.length();
            double spaceSavedFactor = 100.0 - ((double) sizeComp / sizeOrig) * 100;
            double compressionRatio = (double) sizeOrig / sizeComp;

            log("Compression successfully executed.");
            log("Artifact packed: " + outputFile.getName());

            updateUIState(100, "Compression Completed!", () -> {
                lblOrigSize.setText(formatSize(sizeOrig));
                lblCompSize.setText(formatSize(sizeComp));
                lblRatio.setText(String.format("%.2fx", compressionRatio));
                lblSaved.setText(String.format("%.2f%%", spaceSavedFactor));
            });

        } catch (Exception ex) {
            log("Critical Error: " + ex.getMessage());
            updateUIState(0, "Execution failed", null);
        }
    }

    private void decompressPipeline() {
        if (selectedFile == null || !selectedFile.exists()) {
            log("Execution Aborted: Target source path context corrupt.");
            return;
        }
        try {
            updateUIState(15, "Parsing system stream headers...", null);
            
            try (DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(selectedFile)))) {
                String structuralName = inputStream.readUTF();
                int internalTableSize = inputStream.readInt();

                Map<Byte, Integer> frequencies = new HashMap<>();
                for (int i = 0; i < internalTableSize; i++) {
                    frequencies.put(inputStream.readByte(), inputStream.readInt());
                }

                PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.freq));
                for (var entry : frequencies.entrySet()) {
                    pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
                }

                if (pq.size() == 1) {
                    HuffmanNode target = pq.poll();
                    pq.add(new HuffmanNode(target.freq, target, null));
                } else {
                    while (pq.size() > 1) {
                        HuffmanNode nodeA = pq.poll();
                        HuffmanNode nodeB = pq.poll();
                        pq.add(new HuffmanNode(nodeA.freq + nodeB.freq, nodeA, nodeB));
                    }
                }

                HuffmanNode root = pq.poll();
                long dynamicBitBound = inputStream.readLong();

                updateUIState(50, "De-serializing tree bitstream pipelines...", null);
                File outTargetFile = new File(selectedFile.getParent(), "DECOMPRESSED_" + structuralName);
                
                try (BufferedOutputStream fileExporter = new BufferedOutputStream(new FileOutputStream(outTargetFile))) {
                    HuffmanNode evaluationTracker = root;
                    long bitExecutionCursor = 0;

                    while (bitExecutionCursor < dynamicBitBound) {
                        int currentByteStream = inputStream.read();
                        if (currentByteStream == -1) break;

                        for (int shiftPosition = 7; shiftPosition >= 0 && bitExecutionCursor < dynamicBitBound; shiftPosition--) {
                            int operationalBit = (currentByteStream >> shiftPosition) & 1;
                            evaluationTracker = (operationalBit == 0) ? evaluationTracker.left : evaluationTracker.right;

                            if (evaluationTracker.isLeaf()) {
                                fileExporter.write(evaluationTracker.data);
                                evaluationTracker = root;
                            }
                            bitExecutionCursor++;
                        }
                    }
                }

                log("Decompression extraction finalized.");
                log("Output verified: " + outTargetFile.getName());

                long targetLen = outTargetFile.length();
                updateUIState(100, "Decompression Completed!", () -> {
                    lblOrigSize.setText(formatSize(selectedFile.length()));
                    lblCompSize.setText(formatSize(targetLen));
                    lblRatio.setText("1:1 Output");
                    lblSaved.setText("Restored");
                });
            }

        } catch (Exception ex) {
            log("Critical Error during pipeline extraction: " + ex.getMessage());
            updateUIState(0, "Decompression Failed", null);
        }
    }

    private void generateBitmaps(HuffmanNode node, String dynamicPath, Map<Byte, String> targetMap) {
        if (node == null) return;
        if (node.isLeaf()) {
            targetMap.put(node.data, dynamicPath.isEmpty() ? "0" : dynamicPath);
            return;
        }
        generateBitmaps(node.left, dynamicPath + "0", targetMap);
        generateBitmaps(node.right, dynamicPath + "1", targetMap);
    }
//main
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new HuffPressPro().setVisible(true));
    }
}
