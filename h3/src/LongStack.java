import java.util.LinkedList;
import java.util.ListIterator;

public class LongStack{

   public static void main (String[] argum) {
      // TODO!!! Your tests here!
      // Test 1: Test push and pop
      LongStack stack = new LongStack();
      stack.push(10);
      stack.push(20);
      System.out.println("Test 1: " + (stack.pop() == 20 && stack.pop() == 10));

      // Test 2: Test stEmpty
      stack.push(30);
      stack.pop();
      System.out.println("Test 2: " + stack.stEmpty());

      // Test 3: Test tos
      stack.push(40);
      System.out.println("Test 3: " + (stack.tos() == 40));

      // Test 4: Test op
      stack.push(50);
      stack.push(10);
      stack.op("+");
      System.out.println("Test 4: " + (stack.pop() == 60));

      // Test 5: Test clone
      stack.push(70);
      stack.push(80);
      try {
         LongStack clonedStack = (LongStack) stack.clone();
         System.out.println("Test 5: " + stack.equals(clonedStack));
      } catch (CloneNotSupportedException e) {
         e.printStackTrace();
      }

      // Test 6: Test interpret
      String expression = "3 4 +";
      System.out.println("Test 6: " + (LongStack.interpret(expression) == 7));
   }

   private LinkedList<Long> stack;

   public LongStack() {
      // TODO!!! Your constructor here!
      stack = new LinkedList<>();
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      // TODO!!! Your code here!
      LongStack cloned = new LongStack();
      cloned.stack = new LinkedList<>(this.stack);
      return cloned;
   }

   public boolean stEmpty() {
      // TODO!!! Your code here!
      return stack.isEmpty();
   }

   public void push (long a) {
      // TODO!!! Your code here!
      stack.push(a);
   }

   public long pop() {
      // TODO!!! Your code here!
      if (stack.isEmpty()) {
         throw new RuntimeException("Stack is empty");
      }
        return stack.pop();
   } // pop

   public void op (String s) {
      // TODO!!!
      if (stack.size() < 2) {
         throw new RuntimeException("Stack underflow: not enough elements for operation " + s);
      }
      long b = stack.pop();
      long a = stack.pop();
      long result;
      switch (s) {
         case "+":
            result = a + b;
            break;
         case "-":
            result = a - b;
            break;
         case "*":
            result = a * b;
            break;
         case "/":
            if (b == 0) {
               throw new ArithmeticException("Division by zero: cannot divide " + a + " by zero.");
            }
            result = a / b;
            break;
         default:
            throw new RuntimeException("Illegal operation: " + s);
      }
      stack.push(result);
   }
  
   public long tos() {
      // TODO!!! Your code here!
      if (stack.isEmpty()) {
         throw new RuntimeException("Stack underflow: cannot read top of an empty stack.");
      }
      return stack.peek();
   }

   @Override
   public boolean equals (Object o) {
      // TODO!!! Your code here!
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      LongStack longStack = (LongStack) o;
      return stack.equals(longStack.stack);
   }

   @Override
   public String toString() {
      // TODO!!! Your code here!
      StringBuffer sb = new StringBuffer();
      ListIterator<Long> iterator = stack.listIterator(stack.size());
      while (iterator.hasPrevious()) {
         sb.append(iterator.previous()).append(" ");
      }
      return sb.toString().trim();
   }

   public static long interpret (String pol) {
      // TODO!!! Your code here!
      if (pol == null || pol.trim().isEmpty()) {
         throw new RuntimeException("Invalid expression: expression is null or empty. Expression: \"" + pol + "\"");
      }

      LongStack stack = new LongStack();
      String[] tokens = pol.trim().split("\\s+");

      for (String token : tokens) {
         if (token.isEmpty()) {
            continue;
         }
         try {
            long num = Long.parseLong(token);
            stack.push(num);
         } catch (NumberFormatException e) {
            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
               try {
                  stack.op(token);
               } catch (RuntimeException re) {
                  throw new RuntimeException("Error during operation: " + re.getMessage() + ". Expression: " + pol);
               }
            } else {
               throw new RuntimeException("Invalid token: " + token + " in expression: " + pol);
            }
         }
      }

      if (stack.stack.size() != 1) {
         throw new RuntimeException("Invalid expression: leaves redundant elements on the stack. Expression: \"" + pol + "\"");
      }

      return stack.pop();
   }

}

