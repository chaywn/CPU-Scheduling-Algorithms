import java.util.Objects;

public class Process implements Comparable<Process>{
    private static int count = 0;
    private int index;
    private int arrivalTime;
    private int finishTime;
    private int burstTime;
    private int remainingBurstTime;
    private int turnaroundTime, waitingTime;
    private int priority = 0;  

    public Process(int arrivalTime, int burstTime) {
        this.remainingBurstTime = this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        index = count++;
    }

    public Process(int arrivalTime, int burstTime, int priority) {
        this(arrivalTime, burstTime);
        this.priority = priority;
    }

    public static void resetIndex() {
        count = 0;
    }

    public int getIndex() { return index; }
    public int getArrivalTime() { return arrivalTime; }
    public int getFinishTime() { return finishTime; }
    public int getBurstTime() { return burstTime; }
    public int getRemainingBurstTime() { return remainingBurstTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    public int getWaitingTime() { return waitingTime; }
    public int getPriority() { return priority; }

    // set finish time when the process is finished executed
    public void setFinishTime(int finishTime) { 
        this.finishTime = finishTime; 
    }

    public void execute(int duation) {
        remainingBurstTime = (remainingBurstTime - duation >= 0) ? (remainingBurstTime - duation) : 0;
    }

    public int calculateTurnaroundTime() {
        return turnaroundTime = finishTime - arrivalTime;
    }

    public int calculateWaitingTime() {
        return waitingTime = turnaroundTime - burstTime;
    }

    @Override
    public int compareTo(Process p) {
        int compRes;
        // If the processes have priority, compare with priority (smaller priority number implies a higher priority)
        if (this.priority != 0 && p.priority != 0) {
            compRes = p.priority - this.priority;
        }
        // If not, compare processes using their remaining burst time (lower burst time implies a higher priority)
        else {
            compRes = p.remainingBurstTime - this.remainingBurstTime;
        }

        // If both processes have same priority and same arrival time, compare by their index order (lower index implies a higher priority)
        if (compRes == 0) {
            return p.index - index;
        }
        else {
            return compRes;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Process p = (Process) o;
        return index == p.index && priority == p.priority && arrivalTime == p.arrivalTime && burstTime == p.burstTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, arrivalTime, burstTime, priority);
    }

    @Override
    public String toString() {
        return "P" + index;
    }
}
