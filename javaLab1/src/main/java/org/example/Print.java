package org.example;

public class Print  implements IOperation {
    @Override
    public void Do(Context context, String[] arguments){
        double pop = context.Pop();
        System.out.println(pop);
        context.Push(Double.toString(pop));
    }
}
