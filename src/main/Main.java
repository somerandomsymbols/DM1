package main;

import algorithm.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        List<List<String>> inputStrings = Arrays.asList(
                Arrays.asList(
                        "0 0 0 0 1",
                        "0 0 0 1 0",
                        "0 0 1 0 1",
                        "0 0 1 1 1",
                        "0 1 0 0 0",
                        "0 1 0 1 0",
                        "0 1 1 0 1",
                        "0 1 1 1 1",
                        "1 0 0 0 0",
                        "1 0 1 0 0"
                ),
                Arrays.asList(
                        "0 0 0 0 0",
                        "0 0 0 1 1",
                        "0 0 1 0 1",
                        "0 0 1 1 1",
                        "0 1 0 0 0",
                        "0 1 0 1 1",
                        "0 1 1 0 0",
                        "0 1 1 1 1",
                        "1 0 0 0 1",
                        "1 0 1 0 1"
                ),
                Arrays.asList(
                        "0 0 0 0 1",
                        "0 0 0 1 0",
                        "0 0 1 0 1",
                        "0 0 1 1 1",
                        "0 1 0 0 1",
                        "0 1 0 1 0",
                        "0 1 1 0 1",
                        "0 1 1 1 0",
                        "1 0 0 0 1",
                        "1 0 1 0 1"
                ),
                Arrays.asList(
                        "0 0 0 0 0",
                        "0 0 0 1 0",
                        "0 0 1 0 0",
                        "0 0 1 1 1",
                        "0 1 0 0 1",
                        "0 1 0 1 1",
                        "0 1 1 0 1",
                        "0 1 1 1 1",
                        "1 0 0 0 0",
                        "1 0 1 0 0"
                ),
                Arrays.asList(
                        "0 0 0 0 1",
                        "0 0 0 1 0",
                        "0 0 1 0 1",
                        "0 0 1 1 0",
                        "0 1 0 0 0",
                        "0 1 0 1 0",
                        "0 1 1 0 1",
                        "0 1 1 1 1",
                        "1 0 0 0 1",
                        "1 0 1 0 0"
                ),
                Arrays.asList(
                        "0 0 0 0 1",
                        "0 0 0 1 1",
                        "0 0 1 0 1",
                        "0 0 1 1 1",
                        "0 1 0 0 0",
                        "0 1 0 1 0",
                        "0 1 1 0 0",
                        "0 1 1 1 1",
                        "1 0 0 0 1",
                        "1 0 1 0 0"
                )
        );

        List<List<String>> unclassifiedStrings = Arrays.asList(
                Arrays.asList(
                        "1 1 1 1"
                ),
                Arrays.asList(
                        "1 1 1 1"
                ),
                Arrays.asList(
                        "1 1 1 1"
                ),
                Arrays.asList(
                        "1 1 1 1"
                ),
                Arrays.asList(
                        "1 1 1 1"
                ),
                Arrays.asList(
                        "1 1 1 1"
                )
        );

        for (int i = 0; i < inputStrings.size(); ++i)
        {
            List<InputObject> input = new ArrayList<>();

            for (String s : inputStrings.get(i))
            {
                String[] ss = s.split(" ");
                boolean[] a = new boolean[ss.length - 1];

                for (int j = 0; j < a.length; ++j)
                    a[j] = ss[j].equals("1");

                input.add(new InputObject(a, ss[ss.length - 1].equals("1")));
            }

            ClassifierAlgorithm oneRule = new OneRule(input);
            ClassifierAlgorithm naiveBayes = new NaiveBayes(input);
            ClassifierAlgorithm decisionTree = new DecisionTree(input);
            ClassifierAlgorithm kNearestNeighbors = new KNearestNeighbors(input);

            List<InputObject> outputOneRule = new ArrayList<>();
            List<InputObject> outputNaiveBayes = new ArrayList<>();
            List<InputObject> outputDecisionTree = new ArrayList<>();
            List<InputObject> outputKNearestNeighbors = new ArrayList<>();

            for (String s : unclassifiedStrings.get(i))
            {
                String[] ss = s.split(" ");
                boolean[] a = new boolean[ss.length];

                for (int j = 0; j < a.length; ++j)
                    a[j] = ss[j].equals("1");

                outputOneRule.add(new InputObject(a, oneRule.classify(new InputObject(a, false))));
                outputNaiveBayes.add(new InputObject(a, naiveBayes.classify(new InputObject(a, false))));
                outputDecisionTree.add(new InputObject(a, decisionTree.classify(new InputObject(a, false))));
                outputKNearestNeighbors.add(new InputObject(a, kNearestNeighbors.classify(new InputObject(a, false))));
            }

            System.out.println();

            for (InputObject object : input)
                System.out.println(object);

            System.out.println();
            System.out.println(oneRule);

            for (InputObject o : outputOneRule)
            {
                System.out.println(o);
                System.out.println();
            }

            System.out.println(naiveBayes);

            for (InputObject o : outputNaiveBayes)
            {
                System.out.println(o);
                System.out.println();
            }

            System.out.println(decisionTree);

            for (InputObject o : outputDecisionTree)
            {
                System.out.println(o);
                System.out.println();
            }

            System.out.println(kNearestNeighbors);

            for (InputObject o : outputKNearestNeighbors)
            {
                System.out.println(o);
                System.out.println();
            }
        }
    }
}
