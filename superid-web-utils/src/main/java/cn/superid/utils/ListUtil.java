package cn.superid.utils;

import cn.superid.utils.functions.Function2;
import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 维 on 2014/8/29.
 */
public class ListUtil {

    public static class ListProcessStream<T> {
        private List<T> source;
        public ListProcessStream(List<T> source) {
            this.source = source;
        }
        public <T2> ListProcessStream<T2> map(Function<T, T2> fn) {
            if(source == null || fn == null) {
                return null;
            }
            List<T2> result = new ArrayList<>();
            for (T item : source) {
                result.add(fn.apply(item));
            }
            return new ListProcessStream<>(result);
        }

        public void each(Function<T, Void> fn) {
            map(fn);
        }

        public ListProcessStream<T> filter(Function<T, Boolean> fn) {
            if (source == null || fn == null) {
                return new ListProcessStream<>(source);
            }
            List<T> result = new ArrayList<>();
            for (T item : source) {
                Boolean predResult = fn.apply(item);
                if (predResult != null && predResult) {
                    result.add(item);
                }
            }
            return new ListProcessStream<>(result);
        }

        public T first(Function<T, Boolean> fn) {
            if (source == null || fn == null) {
                return null;
            }
            for (T item : source) {
                Boolean predResult = fn.apply(item);
                if (predResult != null && predResult) {
                    return item;
                }
            }
            return null;
        }

        public T reduce(T start, Function2<T, T, T> fn) {
            if (fn == null || source == null) {
                return start;
            }
            T result = start;
            for (T item : source) {
                result = fn.apply(result, item);
            }
            return result;
        }
        public T reduce(Function2<T, T, T> fn) {
            if (fn == null || size(source) < 1) {
                return null;
            }
            return new ListProcessStream<>(ListUtil.rest(source)).reduce(ListUtil.first(source), fn);
        }

        public ListProcessStream<T> rest() {
            if (source == null) {
                return null;
            }
            List<T> result = ListUtil.clone(source);
            if (size(result) > 0) {
                result.remove(0);
            }
            return new ListProcessStream<>(result);
        }

        public T max(Comparator<T> comparator) {
            if (source == null || comparator == null) {
                return null;
            }
            List<T> sorted = ListUtil.clone(source);
            return Collections.max(sorted, comparator);
        }

        public T min(Comparator<T> comparator) {
            if (source == null || comparator == null) {
                return null;
            }
            List<T> sorted = ListUtil.clone(source);
            return Collections.min(sorted, comparator);
        }

        public ListProcessStream<T> sort(Comparator<T> comparator) {
            if(comparator == null) {
                return new ListProcessStream<>(source);
            }
            List<T> result = ListUtil.clone(source);
            Collections.sort(result, comparator);
            return new ListProcessStream<>(result);
        }

        public ListProcessStream<T> uniqueList(final Function2<T, T, Boolean> fn) {
            if(source == null || fn == null) {
                return new ListProcessStream<>(source);
            }
            List<T> result = new ArrayList<>();
            for(final T item : source) {
                T existedItem = ListUtil.first(result, new Function<T, Boolean>() {
                    @Override
                    public Boolean apply(T t) {
                        return fn.apply(item, t);
                    }
                });
                if(existedItem == null) {
                    result.add(item);
                }
            }
            return new ListProcessStream<>(result);
        }

        public List<T> value() {
            return source;
        }
    }

    public static List<Integer> intToList(int[] arr) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        return list;
    }

    public static <T1, T2> List<T2> map(List<T1> source, Function<T1, T2> fn) {
        return stream(source).map(fn).value();
    }

    public static <T> void each(List<T> source, Function<T, Void> fn) {
        stream(source).each(fn);
    }

    public static <T> ListProcessStream<T> stream(List<T> source) {
        return new ListProcessStream<>(source);
    }

    /**
     * 对于一个列表,如果里面有重复项,只保留第一个
     * @param source
     * @param fn 用来判断2个项是否是同一个的函数
     * @param <T>
     * @return
     */
    public static <T> List<T> uniqueList(List<T> source, final Function2<T, T, Boolean> fn) {
        return new ListProcessStream<>(source).uniqueList(fn).value();
    }

    public static <T> T reduce(List<T> source, T start, Function2<T, T, T> fn) {
        return stream(source).reduce(start, fn);
    }

    public static <T> T reduce(List<T> source, Function2<T, T, T> fn) {
        return stream(source).reduce(fn);
    }

    public static <T> List<T> rest(List<T> source) {
        return new ListProcessStream<>(source).rest().value();
    }

    public static <T> List<T> clone(List<T> source) {
        if (source == null) {
            return null;
        }
        return map(source, new Function<T, T>() {
            @Override
            public T apply(T t) {
                return t;
            }
        });
    }

    public static <T> List<T> filter(List<T> source, Function<T, Boolean> fn) {
        return new ListProcessStream<>(source).filter(fn).value();
    }

    public static <T> T first(List<T> source, Function<T, Boolean> fn) {
        return new ListProcessStream<>(source).first(fn);
    }

    public static <T> Function<T, Boolean> not(final Function<T, Boolean> fn) {
        if (fn == null) {
            return null;
        }
        return new Function<T, Boolean>() {
            @Override
            public Boolean apply(T t) {
                Boolean result = fn.apply(t);
                if (result == null) {
                    return true;
                }
                return !result;
            }
        };
    }

    public static <T> boolean any(List<T> source, Function<T, Boolean> fn) {
        return first(source, fn) != null;
    }

    public static <T> boolean all(List<T> source, Function<T, Boolean> fn) {
        return first(source, not(fn)) == null;
    }

    public static <T> List<T> list(T... items) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            result.add(item);
        }
        return result;
    }

    public static <T> boolean all(T[] source, Function<T, Boolean> fn) {
        return all(list(source), fn);
    }

    public static <T> T first(List<T> source) {
        if (source == null || source.size() < 1) {
            return null;
        }
        return source.get(0);
    }

    public static <T> int size(List<T> source) {
        if (source == null) {
            return 0;
        }
        return source.size();
    }

    public static <T> T max(List<T> source, Comparator<T> comparator) {
        return new ListProcessStream<>(source).max(comparator);
    }

    public static <T> T min(List<T> source, Comparator<T> comparator) {
        return new ListProcessStream<>(source).min(comparator);
    }

    public static <T> List<T> sort(List<T> source, Comparator<T> comparator) {
        return new ListProcessStream<>(source).sort(comparator).value();
    }
}
