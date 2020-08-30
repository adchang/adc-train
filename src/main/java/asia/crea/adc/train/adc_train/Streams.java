package asia.crea.adc.train.adc_train;

import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Streams {
  public static final <T> List<T> getSortedList(Collection<T> data) {
    return getSortedList(data, false);
  }
  
  public static final <T> List<T> getSortedList(Collection<T> data, boolean parallel) {
    return parallel
        ? data.parallelStream().sorted().collect(Collectors.toList())
        : data.stream().sorted().collect(Collectors.toList());
  }

  public static final <T> List<T> getDistinctList(Collection<T> data) {
    return getDistinctList(data, false);
  }
  
  public static final <T> List<T> getDistinctList(Collection<T> data, boolean parallel) {
    return parallel
        ? data.parallelStream().distinct().collect(Collectors.toList())
        : data.stream().distinct().collect(Collectors.toList());
  }

  public static final <T> String join(Collection<T> data, Function<T, String> toString,
      String seperator) {
    return join(data, toString, seperator, false);
  }
  
  public static final <T> String join(Collection<T> data, Function<T, String> toString,
      String seperator, boolean parallel) {
    return parallel
        ? data.parallelStream().map(toString).collect(Collectors.joining(seperator))
        : data.stream().map(toString).collect(Collectors.joining(seperator));
  }

  public static final <T> boolean exists(Collection<T> data, Predicate<T> predicate) {
    return exists(data, predicate, false);
  }
  
  public static final <T> boolean exists(Collection<T> data, Predicate<T> predicate,
      boolean parallel) {
    return parallel
        ? data.parallelStream().filter(predicate).findAny().isPresent()
        : data.stream().filter(predicate).findAny().isPresent();
  }

  public static final <T> Integer sum(Collection<T> data, ToIntFunction<T> mapper) {
    return sum(data, mapper, false);
  }

  public static final <T> Integer sum(Collection<T> data, ToIntFunction<T> mapper,
        boolean parallel) {
    return getIntStream(data, mapper, parallel).sum();
  }
  
  public static final <T> Double average(Collection<T> data, ToIntFunction<T> mapper) {
    return average(data, mapper, false);
  }
  
  public static final <T> Double average(Collection<T> data, ToIntFunction<T> mapper,
        boolean parallel) {
    return getIntStream(data, mapper, parallel).average().orElse(0);
  }
  
  public static final <T> IntStream getIntStream(Collection<T> data, ToIntFunction<T> mapper,
      boolean parallel) {
    return parallel
        ? data.parallelStream().mapToInt(mapper)
        : data.stream().mapToInt(mapper);
  }
  
  public static final <T> Long sum(Collection<T> data, ToLongFunction<T> mapper) {
    return sum(data, mapper, false);
  }

  public static final <T> Long sum(Collection<T> data, ToLongFunction<T> mapper,
        boolean parallel) {
    return getLongStream(data, mapper, parallel).sum();
  }

  public static final <T> Double average(Collection<T> data, ToLongFunction<T> mapper) {
    return average(data, mapper, false);
  }

  public static final <T> Double average(Collection<T> data, ToLongFunction<T> mapper,
        boolean parallel) {
    return getLongStream(data, mapper, parallel).average().orElse(0);
  }
  
  public static final <T> LongStream getLongStream(Collection<T> data, ToLongFunction<T> mapper,
        boolean parallel) {
    return parallel
        ? data.parallelStream().mapToLong(mapper)
        : data.stream().mapToLong(mapper);
  }
  
  public static final <T> Double sum(Collection<T> data, ToDoubleFunction<T> mapper) {
    return sum(data, mapper, false);
  }

  public static final <T> Double sum(Collection<T> data, ToDoubleFunction<T> mapper,
        boolean parallel) {
    return getDoubleStream(data, mapper, parallel).sum();
  }

  public static final <T> Double average(Collection<T> data, ToDoubleFunction<T> mapper) {
    return average(data, mapper, false);
  }

  public static final <T> Double average(Collection<T> data, ToDoubleFunction<T> mapper,
        boolean parallel) {
    return getDoubleStream(data, mapper, parallel).average().orElse(0);
  }
  
  public static final <T> DoubleStream getDoubleStream(Collection<T> data, ToDoubleFunction<T> mapper,
        boolean parallel) {
    return parallel
        ? data.parallelStream().mapToDouble(mapper)
        : data.stream().mapToDouble(mapper);
  }
}
