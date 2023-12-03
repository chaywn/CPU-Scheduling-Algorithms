import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class Main extends JFrame {
    private final int initialRowCount = 4;
    private int noOfRow = initialRowCount;
    private int timeQuantum = 0; // For Round Robin 

    class InputTableModel extends AbstractTableModel {
        private final int defaulArrivalTime = 0, defaultBurstTime = 1, defaultPriority = 0;

        private ArrayList<Object[]> data = new ArrayList<>();
        private String[] columnName = {
            "Process",
            "Arrival Time",
            "Burst Time",
            "Priority"
        };

        public InputTableModel() {
            // insert default rows
            for (int i = 0; i < initialRowCount; i++) {
                Integer[] r = {i, defaulArrivalTime, defaultBurstTime, defaultPriority};
                data.add(r);
            }
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnName.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnName[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data.get(row)[col];
        }
    
        @Override
        public void setValueAt(Object value, int row, int col) {
            data.get(row)[col] = value;
            fireTableCellUpdated(row, col);
        }

        @Override
        public Class<?> getColumnClass(int col) {
            return getValueAt(0, col).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col < 1) {
                return false;
            } else {
                return true;
            }
        }

        public void addRow() {
            Object[] r = {noOfRow++, defaulArrivalTime, defaultBurstTime, defaultPriority};
            data.add(r);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
        }

        public void deleteRow() {
            if (getRowCount() > 0) {
                data.remove(getRowCount() - 1);
                noOfRow--;
                fireTableRowsDeleted(getRowCount() - 1, getRowCount() - 1);
            }
        }
    }

    Main() {
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());


        // Table panel for inputting data
        JPanel tablePanel = new JPanel(new BorderLayout());
        JTable inputTable = new JTable(new InputTableModel());
        inputTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        inputTable.setFillsViewportHeight(true);
        tablePanel.add(inputTable, BorderLayout.CENTER);
        tablePanel.add(inputTable.getTableHeader(), BorderLayout.NORTH);
        

        // Button panel for adding and deleting rows
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addRowButton = new JButton("Add Row");
        JButton deleteRowButton = new JButton("Delete Row");
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputTableModel tm = (InputTableModel) inputTable.getModel();
                tm.addRow();
            }
        });
        deleteRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputTable.isEditing()) {
                    inputTable.getCellEditor().stopCellEditing();
                }
                InputTableModel tm = (InputTableModel) inputTable.getModel();
                tm.deleteRow();
            }
        });
        buttonPanel.add(addRowButton);
        buttonPanel.add(deleteRowButton);


        // Time Quantum Panel for entering time quantum if Round Robin is selected
        JPanel timeQuantumPanel = new JPanel(new FlowLayout());
        timeQuantumPanel.add(new JLabel("Time Quantum: "));
        JTextField timeQuantumField = new JTextField(2);
        timeQuantumField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                if(timeQuantumField.getText().length() >= 1 && !(key == KeyEvent.VK_DELETE || key == KeyEvent.VK_BACK_SPACE)) {
                    getToolkit().beep();
                    e.consume();
                }
                else if (key == KeyEvent.VK_DELETE || key == KeyEvent.VK_BACK_SPACE) {
                    timeQuantum = 0;
                }
                else if (!Character.isDigit(key)) {
                    e.consume();
                }
                else {
                    int value = key - '0';
                    if (value < 3 || value > 9) {
                        getToolkit().beep();
                        e.consume();
                    }
                    else {
                        timeQuantum = value;
                    }
                }
             }
        });
        timeQuantumPanel.add(timeQuantumField);
        timeQuantumPanel.add(new JLabel("(Enter a value between 3 and 9)"));
        timeQuantumPanel.setVisible(true);


        // Result panel to display gantt chart and process time
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));


        // Drop down menu to choose a scheduling algorithm
        JPanel dropDownPanel = new JPanel(new FlowLayout());
        dropDownPanel.add(new JLabel("Algorithm: "));
        String[] algorithmList = { "Round Robin", "Non-preemptive Priority", "Non-preemptive SJF", "Preemptive SJF" };
        JComboBox<String> comboBox = new JComboBox<>(algorithmList);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedIndex() == 0) {
                    timeQuantumPanel.setVisible(true);
                }
                else if (timeQuantumPanel.isVisible()) {
                    timeQuantumPanel.setVisible(false);
                }
            }
        });
        dropDownPanel.add(comboBox);
        // Simulate Button
        JButton simulateButton = new JButton("Simulate");
        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkInput(comboBox.getSelectedIndex(), inputTable)) {
                    displayResult(
                        performAlgorithm(comboBox.getSelectedIndex(), inputTable), 
                        resultPanel
                    );
                }
            }
        });
        dropDownPanel.add(simulateButton);

        
        // add components to main panel using grid bag constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;
        int TBpad = 30;   // top and bottom outer padding
        int LRpad = 50;   // left and right outer padding

        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.insets = new Insets(TBpad, LRpad, TBpad, LRpad);

        gbc.gridy = y++;
        mainPanel.add(new JLabel("CPU Scheduling Algorithm", SwingConstants.CENTER), gbc);

        gbc.gridy = y++;
        mainPanel.add(tablePanel, gbc);

        gbc.gridy = y++;
        mainPanel.add(buttonPanel, gbc);

        gbc.gridy = y++;
        mainPanel.add(dropDownPanel, gbc);

        gbc.gridy = y++;
        mainPanel.add(timeQuantumPanel, gbc);

        gbc.gridy = y++;
        mainPanel.add(resultPanel, gbc);
        
        // Add main panel to a scroll pane to make the window scrollable
        JScrollPane windowScrollPane = new JScrollPane(mainPanel);
        windowScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(windowScrollPane);

        // pack();
        setTitle("CPU Scheduling Algorithm");
        setSize(1000, 700);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private boolean checkInput(int algIndex, JTable table) {
        if (algIndex == 0 && timeQuantum == 0) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(this, "Enter a time quantum.");
            return false;
        }

        InputTableModel tm = (InputTableModel) table.getModel();
        int priorityCol = 3;
        boolean hasZero = false;

        for (int i = 0; i < noOfRow; i++) {
            int priority = (int) tm.getValueAt(i, priorityCol);
            if (priority == 0) {
                hasZero = true;
            }
            else if (hasZero) {
                JOptionPane.showMessageDialog(this, "Priority must be larger than 0.");
                return false;
            }
        }
        return true;
    }

    private SchedulingAlgorithm performAlgorithm(int algIndex, JTable table) {
        SchedulingAlgorithm alg = null;

        switch (algIndex) {
            case 0: {
                alg = new RoundRobin(timeQuantum);
                break;
            }
            case 1: {
                alg = new NonPreemptivePriority();
                break;
            }
            case 2: {
                alg = new NonPreemptiveSJF();
                break;
            }
            case 3: {
                alg = new PreemptiveSJF();
                break;
            }
        }
        
        InputTableModel tm = (InputTableModel) table.getModel();

        // Add processes based on table field
        for (int i = 0; i < noOfRow; i++) {
            int arrivalTime = (int) tm.getValueAt(i, 1);
            int burstTime = (int) tm.getValueAt(i, 2);
            int priority = (int) tm.getValueAt(i, 3);
            alg.addProcess(new Process(arrivalTime, burstTime, priority));
        }

        alg.simulateSchedule();

        System.out.println(Arrays.toString(alg.getSchedule()));
        System.out.println("Average Turnaround Time: " + alg.calculateAveTurnaroundTime());
        System.out.println("Average Waiting Time: " + alg.calculateAveWaitingTime());
        
        return alg;
    }

    public void displayResult(SchedulingAlgorithm alg, JPanel parentPanel) {    
        Process[] schedule = alg.getSchedule();
        Process[] processes = alg.getProcesses();

        if (schedule == null) return;

        parentPanel.removeAll();

        // Show Gantt Chart
        JPanel ganttChartPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 0.5;
        c.gridy = 0;

        // add process
        for (int i = 0; i < schedule.length; i++) {
            
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridx = i;

            if (schedule[i] != null) {
                Process p = schedule[i];
                c.gridwidth = getExecutionDuration(i, schedule);
                JLabel scheduleLabel = new JLabel(p.toString());
                scheduleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                ganttChartPanel.add(scheduleLabel, c);

                // add time unit
                c.gridy = 1;
                ganttChartPanel.add(new JLabel(i + ""), c);
            }
        }
        // add the last time unit
        c.gridy = 1;
        c.gridx = schedule.length;
        ganttChartPanel.add(new JLabel((schedule.length) + ""), c);



        // Table panel for displaying finishing time, turnaround time, and waiting time
        JPanel spreadTablePanel = new JPanel();
        spreadTablePanel.setLayout(new BoxLayout(spreadTablePanel, MAXIMIZED_HORIZ));
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = {
            "Process",
            "Arrival Time",
            "Burst Time",
            "Finishing Time",
            "Turnaround Time",
            "Waiting Time"
        };
        Object[][] data = new Object[processes.length][columnNames.length];
        // insert data
        for (int i = 0; i < processes.length; i++) {
            Process p = processes[i];
            Object[] r = {
                p.toString(), 
                p.getArrivalTime(), 
                p.getBurstTime(),
                p.getFinishTime(),
                p.getTurnaroundTime(),
                p.getWaitingTime()
            };
            data[i] = r;
        }
        JTable timeTable = new JTable(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // timeTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        timeTable.setFillsViewportHeight(true);
        tablePanel.add(timeTable, BorderLayout.CENTER);
        tablePanel.add(timeTable.getTableHeader(), BorderLayout.NORTH);
        spreadTablePanel.add(tablePanel);


        // Show average turnaround time and waiting time
        JPanel turnaroundTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String turnaroundTime = String.format("%.2f", alg.calculateAveTurnaroundTime());
        turnaroundTimePanel.add(new JLabel("Average Turnaround Time = " + turnaroundTime));

        JPanel waitingTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String waitingTime = String.format("%.2f", alg.calculateAveWaitingTime());
        waitingTimePanel.add(new JLabel("Average Waiting Time = " + waitingTime));


        parentPanel.add(ganttChartPanel);
        parentPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        parentPanel.add(spreadTablePanel);
        parentPanel.add(turnaroundTimePanel);
        parentPanel.add(waitingTimePanel);

        // refresh panel
        parentPanel.revalidate();
        parentPanel.repaint();
    }

    public int getExecutionDuration(int startingTime, Process[] schedule) {
        int duration = 1;

        if (startingTime == schedule.length - 1) return duration;

        for (int i = startingTime + 1; i < schedule.length; i++) {
            if (schedule[i] != null) {
                return duration;
            }
            duration++;
        }
        return duration;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
