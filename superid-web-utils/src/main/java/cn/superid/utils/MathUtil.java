package cn.superid.utils;

import cn.superid.utils.functions.Function2;
import com.google.common.base.Function;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Random;

/**
 * Created by zoowii on 2014/9/22.
 */
public class MathUtil {
    public static final Random random = new Random();

    /**
     * 按几何分布产生随机数(一段数值区间按权重划分线段长,随机数进入不同线段区间)
     *
     * @param valueWithWeights
     * @return
     */
    public static <T> T randomWithWeight(List<Pair<T, Double>> valueWithWeights) {
        if (valueWithWeights == null) {
            return null;
        }
        double r = random.nextDouble();
        double start = 0;
        double end = 0;
        List<Double> weights = ListUtil.map(valueWithWeights, new Function<Pair<T, Double>, Double>() {
            @Override
            public Double apply(Pair<T, Double> objectDoublePair) {
                return objectDoublePair.getRight();
            }
        });
        double weightSum = ListUtil.reduce(weights, new Function2<Double, Double, Double>() {
            @Override
            public Double apply(Double aDouble, Double aDouble2) {
                return aDouble + aDouble2;
            }
        });
        if (weightSum <= 0) {
            weightSum = 1;
        }
        for (Pair<T, Double> pair : valueWithWeights) {
            double curLen = pair.getRight() / weightSum;
            start = end;
            end = end + curLen;
            if (r >= start && r < end) {
                return pair.getLeft();
            }
        }
        return ListUtil.first(valueWithWeights).getLeft();
    }
}
