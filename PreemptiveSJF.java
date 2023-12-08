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
        Process current = null;

        super.initializeSchedule();
        
        for (int i=0; i<super.schedule.length; i++) {
            TreeSet<Process> arrivedProcess = new TreeSet<>(Collections.reverseOrder());
            for (Process p: processes) {
                if (p.getArrivalTime() == i) {
                    arrivedProcess.add(p);
                    current = p;
                }
            }
            
            for (Process ap: arrivedProcess) {
                readyPoll.add(ap);
                
            }
            arrivedProcess.clear();

            if (oldProcess != null) {
                readyPoll.add(oldProcess);
                oldProcess = null;
            }

            if (!runningProcess) {
                if (readyPoll.size() > 1) {
                    PriorityQueue<Process> pq = new PriorityQueue<>(readyPoll.size(), (p1, p2) -> p1.getBurstTime() - p2.getBurstTime());
                    pq.addAll(readyPoll);
                    readyPoll.clear();
                    readyPoll.addAll(pq);
                }
                if (readyPoll.iterator().hasNext()) {
                    Process nextProcess = readyPoll.iterator().next();
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
            System.out.println("current: " + current.getBurstTime());
            System.out.println("cpu: " + CPUprocess.getRemainingBurstTime());
            if ((current != CPUprocess && current.getRemainingBurstTime() > CPUprocess.getRemainingBurstTime()) || CPUprocess.getRemainingBurstTime() <= 0) {
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
        PreemptiveSJF npsjf = new PreemptiveSJF();
        //Example 1
        // np.addProcess(new Process(0, 6, 3));
        // np.addProcess(new Process(1, 4, 3));
        // np.addProcess(new Process(5, 6, 1));
        // np.addProcess(new Process(6, 6, 1));
        // np.addProcess(new Process(7, 6, 5));
        // np.addProcess(new Process(8, 6, 6));

        //Example 2
        npsjf.addProcess(new Process(0, 8, 2));
        npsjf.addProcess(new Process(0, 6, 1));
        npsjf.addProcess(new Process(4, 15, 5));
        npsjf.addProcess(new Process(9, 13, 4));
        npsjf.addProcess(new Process(7, 9, 3));
        npsjf.addProcess(new Process(13, 5, 1));

        npsjf.simulateSchedule();

        System.out.println(Arrays.toString(npsjf.getSchedule()));
        System.out.println("Average Turnaround Time: " + npsjf.calculateAveTurnaroundTime());
        System.out.println("Average Waiting Time: " + npsjf.calculateAveWaitingTime());
    }
}
