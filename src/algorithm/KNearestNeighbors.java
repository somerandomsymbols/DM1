package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KNearestNeighbors extends ClassifierAlgorithm
{
    private int k;

    public KNearestNeighbors(List<InputObject> input)
    {
        super(input);
    }

    @Override
    protected Function<InputObject, Boolean> generateClassifier(List<InputObject> input)
    {
        this.k = 0;
        int mv = 0;
        List<InputObject>[] lists = new ArrayList[input.size()];

        for (int i = 0; i < lists.length; ++i)
        {
            lists[i] = new ArrayList<>();
            lists[i].addAll(input);
            lists[i].remove(i);
            InputObject o = input.get(i);
            lists[i].sort(Comparator.comparingInt(inputObject -> distanceSqr(inputObject, o)));
        }

        for (int i = 1; i * i <= input.size(); ++i)
        {
            int c = 0;

            for (int j = 0; j < input.size(); ++j)
            {
                InputObject o = input.get(j);

                if (classifyByList(o, lists[j].subList(0, i)) == o.getClassification())
                    ++c;
            }

            if (c > mv)
            {
                mv = c;
                this.k = i;
            }

            System.out.println(String.format("k = %d -> p = %f", i, c / (input.size() - 1.0)));
        }

        return inputObject -> classifyByList(inputObject, getKClosest(input, inputObject, this.k));
    }

    private boolean classifyByList(InputObject object, List<InputObject> objects)
    {
        double vT = objects.stream().filter(inputObject -> inputObject.getClassification()).map(inputObject -> 1.0 / distanceSqr(inputObject, object)).reduce(0.0, Double::sum);
        double vF = objects.stream().filter(inputObject -> !inputObject.getClassification()).map(inputObject -> 1.0 / distanceSqr(inputObject, object)).reduce(0.0, Double::sum);

        return vT >= vF;
    }

    private List<InputObject> getKClosest(List<InputObject> objects, InputObject object, int k)
    {
        return objects.stream().sorted(Comparator.comparingInt(inputObject -> distanceSqr(inputObject, object))).limit(k).collect(Collectors.toList());
    }

    private static int distanceSqr(InputObject x, InputObject y)
    {
        int res = 0;

        for (int i = 0; i < x.size(); ++i)
            res += x.get(i) != y.get(i) ? 1 : 0;

        return res;
    }

    @Override
    public String toString()
    {
        return String.format("kNN: k = %d", this.k);
    }
}
