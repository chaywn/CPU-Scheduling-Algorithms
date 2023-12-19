import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class PreemptiveSJF extends SchedulingAlgorithm {
    private Process CPUprocess;
    private LinkedHashSet<Process> readyPoll = new LinkedHashSet<>();

    @Override
    public void simulateSchedule() {
        boolean runningProcess = false;
        int inCPUtime = 0;
        Process oldProcess = null;
        Process nextProcess = null;
        Process arrived = null;
        Process previous = null;
        ArrayList<Process> index = new ArrayList<Process>();

        super.initializeSchedule();

        for (int i = 0; i < super.schedule.length; i++) {
            TreeSet<Process> arrivedProcess = new TreeSet<>(Collections.reverseOrder());
            for (Process p : processes) {
                if (p.getArrivalTime() == i) {
                    
                    arrivedProcess.add(p);
                    arrived = p;
                    index.add(arrived);                    
                }
            }

            for (Process ap : arrivedProcess) {
                readyPoll.add(ap);
            }
            arrivedProcess.clear();

            if (oldProcess != null) {
                readyPoll.add(oldProcess);
                oldProcess = null;
                
            }

            if (!runningProcess || (arrived.getBurstTime() < nextProcess.getRemainingBurstTime()
                    && arrived.getRemainingBurstTime() != 0 )) {
                if (readyPoll.size() > 1) {
                    PriorityQueue<Process> pq = new PriorityQueue<>(readyPoll.size(),
                            (p1, p2) -> p1.getBurstTime() - p2.getBurstTime());
                    pq.addAll(readyPoll);
                    readyPoll.clear();
                    readyPoll.addAll(pq);
                }
                if (readyPoll.iterator().hasNext()) {
                    nextProcess = readyPoll.iterator().next();
                    if (arrived.getRemainingBurstTime()==nextProcess.getBurstTime()){
                        nextProcess = index.get(0);
                    }
                    if (arrived.getBurstTime() < nextProcess.getRemainingBurstTime()
                            && arrived.getRemainingBurstTime() != 0) {
                                nextProcess = arrived;
                        
                    }                    
                    previous = CPUprocess;
                    CPUprocess = nextProcess;
                    
                    addProcessToSchedule(i, CPUprocess);
                    readyPoll.remove(nextProcess);
                    runningProcess = true;
                } else {
                    continue;
                }
            }

            CPUprocess.execute(1);
            inCPUtime++;
            if ((CPUprocess!=previous)||CPUprocess.getRemainingBurstTime() <= 0) {
            
            runningProcess = false;
            inCPUtime = 0;         

            if (CPUprocess.getRemainingBurstTime() > 0) {
            oldProcess = CPUprocess;
            }

            else {
            CPUprocess.setFinishTime(i+1);
            index.remove(CPUprocess);
            }
            CPUprocess = arrived;

            }
            
        }
    }

    public static void main(String[] args) {
        PreemptiveSJF psjf = new PreemptiveSJF();
        // Example 1
        // np.addProcess(new Process(0, 6, 3));
        // np.addProcess(new Process(1, 4, 3));
        // np.addProcess(new Process(5, 6, 1));
        // np.addProcess(new Process(6, 6, 1));
        // np.addProcess(new Process(7, 6, 5));
        // np.addProcess(new Process(8, 6, 6));

        // Example 2
        psjf.addProcess(new Process(0, 8, 2));
        psjf.addProcess(new Process(0, 6, 1));
        psjf.addProcess(new Process(4, 15, 5));
        psjf.addProcess(new Process(9, 13, 4));
        psjf.addProcess(new Process(7, 9, 3));
        psjf.addProcess(new Process(13, 5, 1));

        psjf.simulateSchedule();

        System.out.println(Arrays.toString(psjf.getSchedule()));
        System.out.println("Average Turnaround Time: " + psjf.calculateAveTurnaroundTime());
        System.out.println("Average Waiting Time: " + psjf.calculateAveWaitingTime());
    }
}
