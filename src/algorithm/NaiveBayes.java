package algorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class NaiveBayes extends ClassifierAlgorithm
{
    private Map<Boolean, Map<Integer, Double>> probabilities;
    private long classSize;
    private long inputSize;

    public NaiveBayes(List<InputObject> input)
    {
        super(input);
    }

    @Override
    protected Function<InputObject, Boolean> generateClassifier(List<InputObject> input)
    {
        if (input == null || input.size() == 0)
            return null;

        int argumentNumber = input.get(0).size();
        this.inputSize = input.size();
        this.classSize = input.stream().filter(inputObject -> inputObject.getClassification()).count();
        this.probabilities = new HashMap<>();

        this.probabilities.put(true, new HashMap<>());
        this.probabilities.put(false, new HashMap<>());
        System.out.println(String.format("P(1) = %f",  1.0 * this.classSize / this.inputSize));
        System.out.println(String.format("P(0) = %f", 1.0 * (this.inputSize - this.classSize) / this.inputSize));

        for (boolean s : new boolean[]{ true, false })
        {
            Map<Integer, Double> conditionalProbabilities = this.probabilities.get(s);

            for (int q = 0; q < argumentNumber; ++q)
            {
                int finalQ = q;
                long c = input.stream().filter(inputObject -> inputObject.getClassification() == s && inputObject.get(finalQ)).count();

                conditionalProbabilities.put(q, 1.0 * c / (s ? this.classSize : this.inputSize - this.classSize));
                System.out.println(String.format("P(Q%d|%d) = %f", q, s ? 1 : 0, conditionalProbabilities.get(q)));
            }
        }

        return inputObject ->
        {
            double pT = 0;
            double pF = 0;

            for (int q = 0; q < argumentNumber; ++q)
            {
                if (inputObject.get(q))
                {
                    pT += Math.log(this.probabilities.get(true).get(q));
                    pF += Math.log(this.probabilities.get(false).get(q));
                }
                else
                {
                    pT += Math.log(1 - this.probabilities.get(true).get(q));
                    pF += Math.log(1 - this.probabilities.get(false).get(q));
                }
            }

            pT = Math.exp(pT) * this.classSize / this.inputSize;
            pF = Math.exp(pF) * (this.inputSize - this.classSize) / this.inputSize;

            return pT > pF;
        };
    }

    @Override
    public String toString()
    {
        return String.format("Naive-Bayes: P = %s", this.probabilities);
    }
}
