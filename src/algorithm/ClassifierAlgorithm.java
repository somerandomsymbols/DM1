package algorithm;

import java.util.List;
import java.util.function.Function;

public abstract class ClassifierAlgorithm
{
    private final Function<InputObject, Boolean> classifier;

    public ClassifierAlgorithm(List<InputObject> input)
    {
        this.classifier = this.generateClassifier(input);
    }

    protected abstract Function<InputObject, Boolean> generateClassifier(List<InputObject> input);

    public final boolean classify(InputObject o)
    {
        return this.classifier.apply(o);
    }

    @Override
    public abstract String toString();
}
