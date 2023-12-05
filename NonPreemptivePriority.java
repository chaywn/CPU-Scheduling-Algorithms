import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class NonPreemptivePriority extends SchedulingAlgorithm {
    private Process CPUprocess;
    private LinkedHashSet<Process> readyPoll = new LinkedHashSet<>();
    private ArrayList<Process> sameArrivalTime = new ArrayList<>();
    
    @Override
    public void simulateSchedule() {
        boolean runningProcess = false;
        boolean arrived = false;
        int inCPUtime = 0;

        super.initializeSchedule();
        
        for (int i=0; i<super.schedule.length; i++) {
            TreeSet<Process> arrivedProcess = new TreeSet<>(Collections.reverseOrder());
            for (Process p: processes) {
                if (p.getArrivalTime() == i) {
                    arrived = true;
                    sameArrivalTime.add(p);
                }
            }
            if (arrived) {
                if (sameArrivalTime.size() > 1) {
                    PriorityQueue<Process> pq = new PriorityQueue<>(sameArrivalTime.size(), (p1, p2) -> p1.getPriority() - p2.getPriority());
                    pq.addAll(sameArrivalTime);
                    sameArrivalTime.clear();
                    sameArrivalTime.addAll(pq);
                }
                if (!runningProcess && readyPoll.isEmpty()) {
                    CPUprocess = sameArrivalTime.get(0);
                    sameArrivalTime.remove(0);
                    addProcessToSchedule(i, CPUprocess);
                    runningProcess = true;
                } else {
                    for (Process p: sameArrivalTime) {
                        arrivedProcess.add(p);
                    }
                }
                arrived = false;
            }

            for (Process ap: arrivedProcess) {
                readyPoll.add(ap);
            }
            arrivedProcess.clear();

            if (!runningProcess) {
                if (readyPoll.size() > 1) {
                    PriorityQueue<Process> pq = new PriorityQueue<>(readyPoll.size(), (p1, p2) -> p1.getPriority() - p2.getPriority());
                    pq.addAll(readyPoll);
                    readyPoll.clear();
                    readyPoll.addAll(pq);
                }
                if (readyPoll.iterator().hasNext()) {
                    Process nextProcess = readyPoll.iterator().next();
                    CPUprocess = nextProcess;
                    sameArrivalTime.remove(0);
                    addProcessToSchedule(i, CPUprocess);
                    readyPoll.remove(nextProcess);
                    runningProcess = true;
                } else {
                    continue;
                }
            }

            CPUprocess.execute(1);
            inCPUtime++;

            if (inCPUtime == CPUprocess.getBurstTime()) {
                CPUprocess.setFinishTime(i+1);
                runningProcess = false;
                inCPUtime = 0;
            }
        }
    }

    public static void main(String[] args) {
        NonPreemptivePriority np = new NonPreemptivePriority();
        //Example 1
        np.addProcess(new Process(0, 6, 3));
        np.addProcess(new Process(1, 4, 3));
        np.addProcess(new Process(5, 6, 1));
        np.addProcess(new Process(6, 6, 1));
        np.addProcess(new Process(7, 6, 5));
        np.addProcess(new Process(8, 6, 6));

        //Example 2
        // np.addProcess(new Process(0, 8, 2));
        // np.addProcess(new Process(4, 15, 5));
        // np.addProcess(new Process(7, 9, 3));
        // np.addProcess(new Process(13, 5, 1));
        // np.addProcess(new Process(9, 13, 4));
        // np.addProcess(new Process(0, 6, 1));

        np.simulateSchedule();

        System.out.println(Arrays.toString(np.getSchedule()));
        System.out.println("Average Turnaround Time: " + np.calculateAveTurnaroundTime());
        System.out.println("Average Waiting Time: " + np.calculateAveWaitingTime());
    }
}
