import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ParallelTest {

  List<List<Integer>> matrix = (Arrays.asList(
          Arrays.asList(1, 2, 3, -3, 2, 0),
          Arrays.asList(1, 2, 4, -3, 2, 8),
          Arrays.asList(1, 0, 0, 0, -2, 0),
          Arrays.asList(1, 0, 11, 0, 6, 0)
  ));

  @Test
  public void sumParallel() throws InterruptedException {
    int result = Parallel.sumParallel(matrix, 3);
    assertEquals(result, 23);

  }

  @Test
  public void sumParallelStream() throws InterruptedException, ExecutionException {
    int result = Parallel.sumParallelStream(matrix, 5);
    assertEquals(result, 23);
  }
}
