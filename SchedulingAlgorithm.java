import java.util.ArrayList;

abstract class SchedulingAlgorithm {
    protected ArrayList<Process> processes = new ArrayList<>();
    protected Process[] schedule;  
    
    public SchedulingAlgorithm() {
        // reset the process index everytime a new scheduling algorithm is created
        Process.resetIndex();
    }

    public Process[] getSchedule() { return schedule; }

    // return an array of index-ordered processes
    public Process[] getProcesses() {
        Process[] processArr = new Process[processes.size()];

        for (int i = 0; i < processArr.length; i++) {
            for (Process p: processes) {
                if (p.getIndex() == i) {
                    processArr[i] = p;
                    break;
                }
            }
        }
        return processArr;
    }

    public void addProcess(Process p) {
        processes.add(p);
    }

    public void initializeSchedule() {
        int totalBurstTime = 0;

        // calculate total burst time
        for (Process p: processes) {
            totalBurstTime += p.getBurstTime();
        }
        schedule = new Process[totalBurstTime];
    }

    public void addProcessToSchedule(int time, Process p) {
        // the index of the process in the schedule array represents its beginning time
        schedule[time] = p;
    }

    public float calculateTotalTurnaroundTime() {
        float time = 0;
        for (Process p: processes) {
            time += p.calculateTurnaroundTime();
        }
        return time;
    }

    public float calculateTotalWaitingTime() {
        float time = 0;
        for (Process p: processes) {
            time += p.calculateWaitingTime();
        }
        return time;
    }

    public float calculateAveTurnaroundTime() {
        return calculateTotalTurnaroundTime()/processes.size();
    }

    public float calculateAveWaitingTime() {
        return calculateTotalWaitingTime()/processes.size();
    }

    // override this method to contain your algorithm
    abstract void simulateSchedule();

}
