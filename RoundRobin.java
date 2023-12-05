import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import java.util.LinkedHashSet;

public class RoundRobin extends SchedulingAlgorithm{
    private int timeQuantum;
    private Process CPUprocess;
    private LinkedHashSet<Process> readyPoll = new LinkedHashSet<>();

    public RoundRobin(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    @Override
    public void simulateSchedule() {
        boolean runningProcess = false;
        int inCPUtime = 0;
        Process oldProcess = null;

        super.initializeSchedule();

        for (int i = 0; i < super.schedule.length; i++) {
            TreeSet<Process> arrivedProcess = new TreeSet<>(Collections.reverseOrder());
            for (Process p: processes) {
                // check arrival of proccesses
                if (p.getArrivalTime() == i) {
                    arrivedProcess.add(p);
                }  
            }
            // add the sorted arrived processes into the ready poll
            for (Process ap: arrivedProcess) {
                readyPoll.add(ap);
            }
            arrivedProcess.clear();

            // add the old process that hasn't finished executing back to the ready poll
            if (oldProcess != null) {
                readyPoll.add(oldProcess);
                oldProcess = null;
            }

            // run process in ready poll if no process is running in the CPU
            if (!runningProcess) {
                if (readyPoll.iterator().hasNext()) {
                    Process nextProcess = readyPoll.iterator().next();
                    CPUprocess = nextProcess;
                    addProcessToSchedule(i, CPUprocess);
                    readyPoll.remove(nextProcess);
                    runningProcess = true;
                }
                // else if ready poll has no processes, skip this time
                else {
                    continue;
                }  
            }

            // execute process for current time and update its time stayed in CPU
            CPUprocess.execute(1);
            // schedule[i] = CPUprocess;
            inCPUtime++;

            // stop current process if the timeQuantum has elapsed or the process has finished executing
            if (inCPUtime >= timeQuantum || CPUprocess.getRemainingBurstTime() <= 0) {
                runningProcess = false;
                inCPUtime = 0;

                // Keep track of this process if it hasn't finish executing
                if (CPUprocess.getRemainingBurstTime() > 0) {
                    oldProcess = CPUprocess;
                }
                // Keep track of finish time if it has finished executed
                else {
                    CPUprocess.setFinishTime(i + 1);
                }
            }
        }
    }

    
    public static void main(String[] args) {
        // Example 1
        // RoundRobin rr = new RoundRobin(3);
        // rr.addProcess(new Process(0, 6, 3));
        // rr.addProcess(new Process(1, 4, 3));
        // rr.addProcess(new Process(5, 6, 1));
        // rr.addProcess(new Process(6, 6, 1));
        // rr.addProcess(new Process(7, 6, 5));
        // rr.addProcess(new Process(8, 6, 6));

        // Example 2
        // RoundRobin rr = new RoundRobin(4);
        // rr.addProcess(new Process(0, 24));
        // rr.addProcess(new Process(0, 3));
        // rr.addProcess(new Process(0, 3));

        // Example 3
        // RoundRobin rr = new RoundRobin(5);
        // rr.addProcess(new Process(0, 6));
        // rr.addProcess(new Process(0, 3));
        // rr.addProcess(new Process(0, 1));
        // rr.addProcess(new Process(0, 7));

        // Example 4
        RoundRobin rr = new RoundRobin(3);
        rr.addProcess(new Process(0, 8, 2));
        rr.addProcess(new Process(4, 15, 5));
        rr.addProcess(new Process(7, 9, 3));
        rr.addProcess(new Process(13, 5, 1));
        rr.addProcess(new Process(9, 13, 4));
        rr.addProcess(new Process(0, 6, 1));

        rr.simulateSchedule();

        System.out.println(Arrays.toString(rr.getSchedule()));
        System.out.println("Average Turnaround Time: " + rr.calculateAveTurnaroundTime());
        System.out.println("Average Waiting Time: " + rr.calculateAveWaitingTime());
    }
}
