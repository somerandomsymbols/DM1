package algorithm;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class OneRule extends ClassifierAlgorithm
{
    private int attributeIndex;
    private boolean attributeValue;
    private boolean classValue;

    public OneRule(List<InputObject> input)
    {
        super(input);
    }

    @Override
    protected Function<InputObject, Boolean> generateClassifier(List<InputObject> input)
    {
        if (input == null || input.size() == 0)
            return null;

        boolean[] domain = new boolean[]{ true, false };
        double v = 1;

        this.attributeIndex = 0;
        this.attributeValue = false;
        this.classValue = false;

        for (int i = 0; i < input.get(0).size(); ++i)
        {
            int finalI = i;

            for (boolean j : domain)
            {
                long t = input.stream().filter(inputObject -> inputObject.get(finalI) == j).count();

                for (boolean h : domain)
                {
                    long c = input.stream().filter(inputObject -> inputObject.get(finalI) == j && inputObject.getClassification() != h).count();

                    System.out.println(String.format("Q%d=%d -> S=%d E = %f", i, j ? 1 : 0, h ? 1 : 0, 1.0 * c / t));

                    if (1.0 * c / t < v)
                    {
                        v = 1.0 * c / t;
                        this.attributeIndex = i;
                        this.attributeValue = j;
                        this.classValue = h;
                    }
                }
            }
        }

        return inputObject -> (inputObject.get(this.attributeIndex) == this.attributeValue) == this.classValue;
    }

    @Override
    public String toString()
    {
        return String.format("1-Rule: Якщо Q%d = %d, то S = %d", this.attributeIndex, this.attributeValue ? 1 : 0, this.classValue ? 1 : 0);
    }
}
