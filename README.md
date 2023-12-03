# CPU Scheduling Algorithm

TSN2101 Operating System: Assignment (10%) ( Trimester 1, 23/24, 2310)

## .java Files

- [Main.java](Main.java) - The main program, integrates GUI interface
- [Process.java](Process.java) - The Process class to represent a process in the CPU
- [SchedulingAlgorithm.java](SchedulingAlgorithm.java) - Abstract class for all scheduling algorithm classes
- [RoundRobin.java](RoundRobin.java) - Scheduling algorithm class for Round Robin
- [NonPreemptivePriority.java](NonPreemptivePriority.java) - Scheduling algorithm class for Non-Preemptive Priority
- [NonPreemptiveSJF.java](NonPreemptiveSJF.java) - Scheduling algorithm class for Non-Preemptive Shortest Job First (SJF)
- [PreemptiveSJF.java](PreemptiveSJF.java) - Scheduling algorithm class for Preemptive Shortest Job First (SJF)


## Usage

### Process.java
Each individual process to be executed in the CPU is represented by a Process object in ```Process.java```. Below are some of the important class components, for futher details refer to the Process class.

```java 
1. Process(int arrivalTime, int burstTime, int priority)
/* 
The constructor of Process class. Requires an arrival time, burst time and priority. if there is no priority for the algorithm, you can it leave it as 0 or use the alternative constructor `Process(int arrivalTime, int burstTime)`.
*/
```

```java 
2. public void execute(int duation)
/* 
The method for process execution. Call this during the simulation to update a process' remaining burst time for `duration`.  
*/
```

```java 
3. public void setFinishTime(int finishTime)
/* 
The method to store a process' finishing time. Call this after a process' remaining burst time becomes 0.
*/
```


### SchedulingAlgorithm.java
All algorithm classes extends the abstract class ``SchedulingAlgorithm.java``. Below are some of the important component you may use to implement your algorithm class. (Note: The methods for calculating turnaround time and waiting time are already implemented within the abstract class so you do not have to do it)

```java 
1. protected ArrayList<Process> processes = new ArrayList<>()
/* 
The arraylist for storing processes. Call `addProcess(Process p)` to add a process into the arraylist.
*/
```

```java 
2. protected Process[] schedule
/* 
The array to store the order of processes being executed. Call `initializeSchedule()` to initialize this array based on the total burst time of all processes. The time unit of the schedule is represented by the index of `schedule`.
*/
```

```java 
3. public void addProcess(Process p)
/*
The method for adding a process into the `processes` arraylist after initializing an object of your class.
*/
```

```java
4. public void initializeSchedule()
/* 
The method to initialize the `schedule` array. This method will calculate the total burst time of all processes and initialize an array of that length.
*/
```

```java
5. public void addProcessToSchedule(int time, Process p)
/* 
The method to record the time a process enters the CPU (schedule). This method will add the process into the `schedule` array at the specific index (representing time). 
IMPORTANT: You only need to add a process ino the schedule at its beginning execution time, e.g. if a process begins at time 1 and ends at time 3, you only need to call `addProcessToSchedule(1, p)` (at its beginning time) 
*/
```


## Group Members
1. Melody Koh Si Jie
2. Chay Wen Ning
3. Goh Shi Yi
4. Choo Yun Yi