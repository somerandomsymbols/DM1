package algorithm;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecisionTree extends ClassifierAlgorithm
{
    private Tree tree;

    public DecisionTree(List<InputObject> input)
    {
        super(input);
    }

    private static double entropy(List<InputObject> a)
    {
        long n = a.size();
        long m = a.stream().filter(inputObject -> inputObject.getClassification()).count();

        if (m == 0 || m == n)
            return 0;

        double mn = m, nmn = n - m;
        mn /= n;
        nmn /= n;

        return -(mn * Math.log(mn) + nmn * Math.log(nmn)) / Math.log(2);
    }

    private static double gain(List<InputObject> a, int q)
    {
        List<InputObject> aT = a.stream().filter(inputObject -> inputObject.get(q)).collect(Collectors.toList());
        List<InputObject> aF = a.stream().filter(inputObject -> !inputObject.get(q)).collect(Collectors.toList());

        return entropy(a) - (aT.size() * entropy(aT) + aF.size() * entropy(aF)) / a.size();
    }

    @Override
    protected Function<InputObject, Boolean> generateClassifier(List<InputObject> input)
    {
        this.tree = new Tree(input);

        return inputObject -> this.tree.classify(inputObject);
    }

    @Override
    public String toString()
    {
        return String.format("Decision Tree: %s", this.tree);
    }

    static class Tree
    {
        private final TreeNode root;

        public Tree(List<InputObject> objects)
        {
            this.root = generateNode(objects, IntStream.range(0, objects.get(0).size()).boxed().collect(Collectors.toSet()));
        }

        public static TreeNode generateNode(List<InputObject> objects, Set<Integer> attributes)
        {
            if (objects.stream().allMatch(inputObject -> inputObject.getClassification()))
                return new TreeNodeLeaf(true);

            if (objects.stream().allMatch(inputObject -> !inputObject.getClassification()))
                return new TreeNodeLeaf(false);

            List<InputObject> sT = objects.stream().filter(inputObject -> inputObject.getClassification()).collect(Collectors.toList());
            List<InputObject> sF = objects.stream().filter(inputObject -> !inputObject.getClassification()).collect(Collectors.toList());

            if (attributes.isEmpty())
                return new TreeNodeLeaf(sT.size() >= sF.size());

            int q = attributes.stream().max(Comparator.comparingDouble(a -> gain(objects, a))).get();
            List<InputObject> qT = objects.stream().filter(inputObject -> inputObject.get(q)).collect(Collectors.toList());
            List<InputObject> qF = objects.stream().filter(inputObject -> !inputObject.get(q)).collect(Collectors.toList());
            Set<Integer> unusedAttributes = new HashSet<>();

            unusedAttributes.addAll(attributes);
            unusedAttributes.remove(q);

            System.out.println(objects);

            for (int i : attributes)
                System.out.println(String.format("Gain(A,Q%d) = %f", i, gain(objects, i)));

            System.out.println(String.format("arg max = %d", q));
            System.out.println();

            return new TreeNodeAttribute(q, sT.size() >= sF.size(), unusedAttributes, qT, qF);
        }

        public boolean classify(InputObject o)
        {
            return this.root.classify(o);
        }

        @Override
        public String toString()
        {
            return this.root.toString();
        }
    }

    static abstract class TreeNode
    {
        public abstract boolean classify(InputObject o);

        @Override
        public abstract String toString();
    }

    static class TreeNodeAttribute extends TreeNode
    {
        private final int q;
        private final TreeNode qT;
        private final TreeNode qF;

        public TreeNodeAttribute(int a, boolean v, Set<Integer> attributes, List<InputObject> t, List<InputObject> f)
        {
            this.q = a;

            if (t.isEmpty())
                this.qT = new TreeNodeLeaf(v);
            else
                this.qT = Tree.generateNode(t, attributes);

            if (f.isEmpty())
                this.qF = new TreeNodeLeaf(v);
            else
                this.qF = Tree.generateNode(f, attributes);
        }

        @Override
        public boolean classify(InputObject o)
        {
            if (o.get(q))
                return this.qT.classify(o);
            else
                return this.qF.classify(o);
        }

        @Override
        public String toString()
        {
            return String.format("(Q%d ? %s : %s)", this.q, this.qT, this.qF);
        }
    }

    static class TreeNodeLeaf extends TreeNode
    {
        private final boolean value;

        public TreeNodeLeaf(boolean v)
        {
            this.value = v;
        }

        @Override
        public boolean classify(InputObject o)
        {
            return this.value;
        }

        @Override
        public String toString()
        {
            return this.value ? "1" : "0";
        }
    }
}
