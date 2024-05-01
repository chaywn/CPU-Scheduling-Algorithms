# CPU Scheduling Algorithm

TSN2101 Operating System: Assignment (10%) ( Trimester 1, 23/24, 2310)

## Group Members
1. 1201103207 - Melody Koh Si Jie 
2. 1201103431 - Chay Wen Ning 
3. 1211307724 - Goh Shi Yi
4. 1211308798 - Choo Yun Yi

## .java Files

- [Main.java](Main.java) - The main program, integrates GUI interface
- [Process.java](Process.java) - The Process class to represent a process in the CPU
- [SchedulingAlgorithm.java](SchedulingAlgorithm.java) - Abstract class for all scheduling algorithm classes
- [RoundRobin.java](RoundRobin.java) - Scheduling algorithm class for Round Robin
- [NonPreemptivePriority.java](NonPreemptivePriority.java) - Scheduling algorithm class for Non-Preemptive Priority
- [NonPreemptiveSJF.java](NonPreemptiveSJF.java) - Scheduling algorithm class for Non-Preemptive Shortest Job First (SJF)
- [PreemptiveSJF.java](PreemptiveSJF.java) - Scheduling algorithm class for Preemptive Shortest Job First (SJF)


## Compile and Run Instructions
To compile and run the application, ensure that [Java JDK is installed with JavaFx](https://www.azul.com/downloads/?version=java-17-lts&package=jdk-fx). 

Below are the instructions for compiling and running the program:

1. Open your command prompt or terminal window and **cd** to the project folder (```../CPU-Scheduling-Algorithms```).

2. Run the following command to compile all .java files in the project folder: 
<br>
  ```javac *.java```

3. In the same directory, run the following command to start the program:
<br>
  ```java Main```

4. Optionally, run the [CPU-Scheduling-Algorithms](/CPU-Scheduling-Algorithms.jar) **JAR file** in the project folder to instantly start the program.



