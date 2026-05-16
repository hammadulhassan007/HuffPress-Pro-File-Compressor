import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.util.*;

// --- Core Huffman Tree Node Structure ---
class HuffmanNode implements Serializable {
    private static final long serialVersionUID = 1L;
    char data;
    int frequency;
    HuffmanNode left, right;

    public HuffmanNode(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.data = '\0';
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}

// --- Final Premium HuffPress Pro Configuration ---
public class HuffPressPro extends JFrame {
    private JTextField fileTextField;
    private JTextArea logTextArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private File selectedFile;

    // Premium Color Matrix Palette
    private final Color BACKGROUND_COLOR = new Color(24, 28, 31);      // Deep Carbon Black
    private final Color PANEL_COLOR = new Color(33, 37, 41);           // Matte Slate Gray
    private final Color TEXT_COLOR = new Color(248, 249, 250);         // Arctic White
    private final Color ACCENT_GREEN = new Color(40, 167, 69);         // Cyber Neon Green
    private final Color ACCENT_BLUE = new Color(0, 123, 255);          // Electric Blue
    private final Color COMPONENT_BORDER = new Color(52, 58, 64);      // Subtle Divider Color

    public HuffPressPro() {
        super("HuffPress Pro - Ultimate Compression Engine");
        initGUI();
    }

    private void initGUI() {
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(15, 15));

        // --- Top Frame: File Core Connector ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(PANEL_COLOR);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COMPONENT_BORDER, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel selectLabel = new JLabel("Source Target: ");
        selectLabel.setForeground(TEXT_COLOR);
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        fileTextField = new JTextField();
        fileTextField.setEditable(false);
        fileTextField.setBackground(BACKGROUND_COLOR);
        fileTextField.setForeground(new Color(173, 181, 189));
        fileTextField.setCaretColor(TEXT_COLOR);
        fileTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COMPONENT_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        fileTextField.setFont(new Font("Consolas", Font.PLAIN, 12));

        JButton browseButton = createStyledButton("Browse Core", new Color(108, 117, 125));
        
        topPanel.add(selectLabel, BorderLayout.WEST);
        topPanel.add(fileTextField, BorderLayout.CENTER);
        topPanel.add(browseButton, BorderLayout.EAST);

        // --- Center Frame: High Tech Activity Monitor & Status Layer ---
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(BACKGROUND_COLOR);

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setBackground(new Color(15, 17, 19)); 
        logTextArea.setForeground(new Color(57, 255, 20)); // Hacker green console lines
        logTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        logTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COMPONENT_BORDER, 1), 
                "Algorithmic Performance Monitor"
        );
        titledBorder.setTitleColor(TEXT_COLOR);
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
        scrollPane.setBorder(titledBorder);

        // New Element: Cyber Progress Monitor Panel
        JPanel statusPanel = new JPanel(new BorderLayout(10, 5));
        statusPanel.setBackground(PANEL_COLOR);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        statusLabel = new JLabel("System Idle... Awaiting Payload Execution Data.");
        statusLabel.setForeground(new Color(206, 212, 218));
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setBackground(BACKGROUND_COLOR);
        progressBar.setForeground(ACCENT_BLUE);
        progressBar.setBorder(BorderFactory.createLineBorder(COMPONENT_BORDER, 1));
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 11));

        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(progressBar, BorderLayout.CENTER);

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(statusPanel, BorderLayout.SOUTH);

        // --- Bottom Frame: Action Execution Command Hub ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        bottomPanel.setBackground(PANEL_COLOR);
        bottomPanel.setBorder(BorderFactory.createLineBorder(COMPONENT_BORDER, 1));
        
        JButton compressButton = createStyledButton("EXECUTE COMPRESSION", ACCENT_GREEN);
        JButton decompressButton = createStyledButton("EXECUTE DECOMPRESSION", ACCENT_BLUE);
        
        bottomPanel.add(compressButton);
        bottomPanel.add(decompressButton);

        // Attach elements to Window core
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- UI Functional Interactions & Threads Processing ---
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                fileTextField.setText(selectedFile.getAbsolutePath());
                progressBar.setValue(0);
                progressBar.setIndeterminate(false);
                statusLabel.setText("File Vector Armed: ready for runtime compilation.");
                log("[SYSTEM] Active Pointer Loaded: " + selectedFile.getName());
            }
        });

        compressButton.addActionListener(e -> {
            if (selectedFile == null || !selectedFile.exists()) {
                JOptionPane.showMessageDialog(this, "Operation Fatal: File pointer missing.", "Runtime Abort", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Trigger multi-threading so GUI progress animation doesn't freeze
            new Thread(this::executeCompression).start();
        });

        decompressButton.addActionListener(e -> {
            if (selectedFile == null || !selectedFile.exists()) {
                JOptionPane.showMessageDialog(this, "Operation Fatal: File pointer missing.", "Runtime Abort", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new Thread(this::executeDecompression).start();
        });
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateProgress(int value, String status, boolean indeterminate, Color barColor) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setIndeterminate(indeterminate);
            progressBar.setValue(value);
            progressBar.setForeground(barColor);
            statusLabel.setText(status);
        });
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> logTextArea.append(message + "\n"));
    }

    // --- Core Back-End Compression Engine ---
    private void executeCompression() {
        try {
            updateProgress(15, "Initializing Data Vectors...", false, ACCENT_GREEN);
            log("\n=======================================================");
            log("[RUNNING] INITIALIZING HUFFMAN GREEDY EXTRACTION ENGINE...");
            log("=======================================================");
            long startTime = System.currentTimeMillis();

            StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                int ch;
                while ((ch = reader.read()) != -1) {
                    contentBuilder.append((char) ch);
                }
            }
            String content = contentBuilder.toString();
            if (content.isEmpty()) {
                log("[ABORTED] Zero payload capacity detected.");
                updateProgress(0, "Process Failed: Empty File Structure.", false, Color.RED);
                return;
            }

            updateProgress(35, "Compiling Token Frequencies...", false, ACCENT_GREEN);
            Map<Character, Integer> frequencies = new HashMap<>();
            for (char c : content.toCharArray()) {
                frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
            }

            updateProgress(55, "Allocating Structural Min-Heap...", false, ACCENT_GREEN);
            PriorityQueue<HuffmanNode> minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.frequency));
            for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
                minHeap.add(new HuffmanNode(entry.getKey(), entry.getValue()));
            }

            updateProgress(70, "Synthesizing Dynamic Binary Tree...", false, ACCENT_GREEN);
            while (minHeap.size() > 1) {
                HuffmanNode left = minHeap.poll();
                HuffmanNode right = minHeap.poll();
                HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
                minHeap.add(parent);
            }
            HuffmanNode root = minHeap.poll();

            Map<Character, String> huffmanCodes = new HashMap<>();
            generateCodes(root, "", huffmanCodes);

            updateProgress(85, "Packing Stream Bitwise Architectures...", false, ACCENT_GREEN);
            StringBuilder bitStream = new StringBuilder();
            for (char c : content.toCharArray()) {
                bitStream.append(huffmanCodes.get(c));
            }

            String outputFilePath = selectedFile.getAbsolutePath() + ".huff";
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFilePath))) {
                oos.writeObject(root);
                oos.writeInt(bitStream.length());
                byte[] packedBytes = packBits(bitStream.toString());
                oos.write(packedBytes);
            }

            long endTime = System.currentTimeMillis();
            File compressedFile = new File(outputFilePath);

            updateProgress(100, "Compression Sequence Secured.", false, ACCENT_GREEN);
            log("[COMPILATION SUCCESSFUL]");
            log("> Packed Ext: " + compressedFile.getName());
            log("> Original Weight: " + selectedFile.length() + " Bytes");
            log("> Encoded Weight: " + compressedFile.length() + " Bytes");
            double ratio = (1.0 - ((double) compressedFile.length() / selectedFile.length())) * 100;
            log(String.format("> Deficit Data Compaction Ratio: %.2f%%", ratio));
            log("> Algorithmic Latency Timings: " + (endTime - startTime) + " ms");

        } catch (Exception ex) {
            log("[CRITICAL RUNTIME ERROR]: " + ex.getMessage());
            updateProgress(0, "Fatal Engine Crash.", false, Color.RED);
            ex.printStackTrace();
        }
    }

    // --- Core Back-End Decompression Engine ---
    private void executeDecompression() {
        try {
            updateProgress(20, "Re-initializing Reverse Buffers...", false, ACCENT_BLUE);
            log("\n=======================================================");
            log("[RUNNING] INITIALIZING DATA PIPELINE INVERSION MODULE...");
            log("=======================================================");
            long startTime = System.currentTimeMillis();

            if (!selectedFile.getName().endsWith(".huff")) {
                log("[REJECTED] Incompatible structure. Module drops non-.huff variants.");
                updateProgress(0, "Process Aborted: Extension Mismatch.", false, Color.RED);
                return;
            }

            HuffmanNode root;
            int totalBits;
            byte[] packedBytes;

            updateProgress(50, "Deserializing Bitstream Header Core...", false, ACCENT_BLUE);
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
                root = (HuffmanNode) ois.readObject();
                totalBits = ois.readInt();
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = ois.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                packedBytes = baos.toByteArray();
            }

            updateProgress(75, "Unpacking Byte Blocks...", false, ACCENT_BLUE);
            String bitStream = unpackBits(packedBytes, totalBits);

            updateProgress(90, "Reconstructing Original File System...", false, ACCENT_BLUE);
            String outputFilePath = selectedFile.getAbsolutePath().replace(".huff", "_extracted.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                HuffmanNode current = root;
                for (int i = 0; i < bitStream.length(); i++) {
                    current = (bitStream.charAt(i) == '0') ? current.left : current.right;

                    if (current.isLeaf()) {
                        writer.write(current.data);
                        current = root;
                    }
                }
            }

            long endTime = System.currentTimeMillis();
            updateProgress(100, "Decompression Sequence Secured.", false, ACCENT_BLUE);
            log("[RECOVERY SUCCESSFUL]");
            log("> Output Destination: " + selectedFile.getName().replace(".huff", "_extracted.txt"));
            log("> Algorithmic Latency Timings: " + (endTime - startTime) + " ms");

        } catch (Exception ex) {
            log("[CRITICAL RUNTIME ERROR]: " + ex.getMessage());
            updateProgress(0, "Fatal Engine Crash.", false, Color.RED);
            ex.printStackTrace();
        }
    }

    private void generateCodes(HuffmanNode node, String code, Map<Character, String> huffmanCodes) {
        if (node == null) return;
        if (node.isLeaf()) {
            huffmanCodes.put(node.data, code.isEmpty() ? "0" : code);
            return;
        }
        generateCodes(node.left, code + "0", huffmanCodes);
        generateCodes(node.right, code + "1", huffmanCodes);
    }

    private byte[] packBits(String bitStream) {
        int byteCount = (bitStream.length() + 7) / 8;
        byte[] packed = new byte[byteCount];
        for (int i = 0; i < bitStream.length(); i++) {
            if (bitStream.charAt(i) == '1') {
                packed[i / 8] |= (1 << (7 - (i % 8)));
            }
        }
        return packed;
    }

    private String unpackBits(byte[] bytes, int totalBits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < totalBits; i++) {
            int bytePos = i / 8;
            int bitPos = 7 - (i % 8);
            int bit = (bytes[bytePos] >> bitPos) & 1;
            sb.append(bit);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HuffPressPro().setVisible(true));
    }
}