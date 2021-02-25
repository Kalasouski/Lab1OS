import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;


public class Parallel {

  private static class MaxSumThread extends Thread {


    private int sum;
    private List<List<Integer>> subMatrix;

    MaxSumThread(List<List<Integer>> subMatrix) {
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

    int arraySize = 0;
    int arraysTotal = matrix.size();

    int div = arraysTotal / threadsNum, rem = arraysTotal % threadsNum;

    if (div == 0) {
      arraySize = rem;
    } else
      arraySize = threadsNum;


    MaxSumThread[] threads = new MaxSumThread[arraySize];

    int startIndex = 0;

    for (int i = 0; i < arraySize; i++) {
      int arraysNum = div;// 3
      if (rem > 0) {
        arraysNum += 1;
        rem--;
      }
      threads[i] = new MaxSumThread(matrix.subList(startIndex, startIndex + arraysNum));
      startIndex += arraysNum;
      threads[i].start();
    }

    int sum = 0;

    for (int i = 0; i < arraySize; i++) {
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




}