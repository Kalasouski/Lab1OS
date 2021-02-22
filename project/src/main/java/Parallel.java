import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;


public class Parallel {

  private static class MyThread extends Thread {


    private int sum;
    private List<List<Integer>> subMatrix;

    MyThread(List<List<Integer>> subMatrix) {
      this.subMatrix = subMatrix;
    }

    @Override
    public void run() {
      for (List<Integer> row : subMatrix) {
        int maxValue = Integer.MIN_VALUE;
        for (int value : row)
          maxValue = Math.max(maxValue, value);
        sum += maxValue;
      }
    }

    public int getSum() {
      return sum;
    }

  }

  static int sumParallel(List<List<Integer>> matrix, int threadsNum) throws InterruptedException {

    int MyThreadNum = 0;
    int arraysTotal = matrix.size();

    int div = arraysTotal / threadsNum, rem = arraysTotal % threadsNum;

    if (div == 0) {
      MyThreadNum = rem;
    }
    else
      MyThreadNum = threadsNum;


    MyThread[] threads = new MyThread[MyThreadNum];

    int startIndex = 0;

    for (int i = 0; i < MyThreadNum; i++) {
      int arraysNum = div;// 3
      if (rem > 0) {
        arraysNum += 1;
        rem--;
      }
      threads[i] = new MyThread(matrix.subList(startIndex, startIndex + arraysNum));
      startIndex += arraysNum;
      threads[i].start();
    }

    int sum = 0;

    for (int i = 0; i < MyThreadNum; i++) {
      threads[i].join();
      sum += threads[i].getSum();
    }


    return sum;
  }

  static List<List<Integer>> generateMatrix(int rows, int cols) {

    List<List<Integer>> matrix = new ArrayList<>();
    Random r = new Random();
    int low = -10;
    int high = 10;

    for (int i = 0; i < rows; i++) {
      List<Integer> row = new ArrayList<>();
      for (int j = 0; j < cols; j++)
        row.add(r.nextInt(high - low) + low);
      matrix.add(row);
    }
    return matrix;
  }

  static int sumParallelStream(List<List<Integer>> matrix, int threadsNum) throws InterruptedException,
          ExecutionException {
    ForkJoinPool customThreadPool = new ForkJoinPool(threadsNum);
    return customThreadPool.submit(() -> matrix.parallelStream().map
            (list -> list.parallelStream().max(Integer::compare).get()).reduce(0, Integer::sum)).get();
  }

  public static void main(String[] args) {
    List<List<Integer>> matrix = generateMatrix(3, 3);
    try {
      long startTime = System.currentTimeMillis();


      System.out.println(sumParallel(matrix, 1));
      long estimatedTime = System.currentTimeMillis() - startTime;
      System.out.println(estimatedTime + "\n");

      startTime = System.currentTimeMillis();
      System.out.println(sumParallel(matrix, 2));
      estimatedTime = System.currentTimeMillis() - startTime;
      System.out.println(estimatedTime + "\n");
      startTime = System.currentTimeMillis();
      System.out.println(sumParallel(matrix, 7));
      estimatedTime = System.currentTimeMillis() - startTime;
      System.out.println(estimatedTime + "\n");

      System.out.println(sumParallelStream(matrix, 5));


    } catch (Exception e) {
    }
  }


}