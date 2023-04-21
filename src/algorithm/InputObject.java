package algorithm;

public class InputObject
{
    private boolean[] arguments;
    private boolean classification;

    public InputObject(boolean[] a, boolean c)
    {
        this.arguments = a;
        this.classification = c;
    }

    public int size()
    {
        return this.arguments.length;
    }

    public boolean get(int i)
    {
        return this.arguments[i];
    }

    public boolean getClassification()
    {
        return this.classification;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for (boolean a : this.arguments) {
            sb.append(a ? 1 : 0);
            sb.append(' ');
        }

        sb.append("| ");
        sb.append(this.classification ? 1 : 0);

        return sb.toString();
    }
}
