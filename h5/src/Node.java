// Ideid ja mõtteid sain siit:
// https://docs.oracle.com/javase/7/docs/api/javax/swing/tree/TreeNode.html
// https://programtalk.com/java/java-tree-implementation/
// https://stackoverflow.com/questions/10290649/tree-of-nodet-explaination


import java.util.*;

public class Node {

   private String name;
   private Node firstChild;
   private Node nextSibling;
   private static LinkedList<String> items;
   private static String sb = null;

   Node (String n, Node d, Node r) {
      this.name = n;
      this.firstChild = d;
      this.nextSibling = r;
   }

   // Parses a postfix expression into a tree.
   public static Node parsePostfix (String s) {
      sb = s;
      items = new LinkedList<>(Arrays.asList(s.split("")));

      try {
         validateInput(s);
         Node root = buildTree(new Node(null, null, null), items);
         validateRoot(root, s);
         return root;
      } catch (RuntimeException e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   // Validates the input string for correct format.
   private static void validateInput(String s) {
      checkInvalidPattern(s);
      checkEmptyNodes(s);
      checkParenthesesBalance(s);
      checkCommaUsage(s);
   }

   private static void checkInvalidPattern(String s) {
      if (s.matches(".*\\(\\(.*\\)\\(.*\\).*\\).*")) {
         throw new RuntimeException("Invalid input: " + s);
      }
   }

   private static void checkEmptyNodes(String s) {
      for (String item : items) {
         if (item.trim().isEmpty()) {
            throw new RuntimeException("Node cannot be empty. Input: " + s);
         }
      }
   }

   private static void checkParenthesesBalance(String s) {
      int n = 0;
      for (String item : items) {
         if (item.equals("(")) {
            n++;
         } else if (item.equals(")")) {
            n--;
            if (n < 0) {
               throw new RuntimeException("Too many closing parentheses! Input: " + s);
            }
         }
      }
   }

   private static void checkCommaUsage(String s) {
      int n = 0;
      for (String item : items) {
         if (item.equals("(")) {
            n++;
         } else if (item.equals(")")) {
            n--;
         } else if (item.equals(",") && n == 0) {
            throw new RuntimeException("Missing parentheses: " + s);
         }
      }
   }

   // Validates the root node of the tree.
   private static void validateRoot(Node root, String s) {
      if (root.firstChild == null && root.nextSibling == null) {
         if (root.name != null) {
             return;
         }
      } else if (root.firstChild == null ) /*&& root.nextSibling != null*/{
         throw new RuntimeException("Missing parentheses! Input: " + s);
      }
   }

   // Builds the tree from the list of items.
   private static Node buildTree(Node root, LinkedList<String> items) {
      while (!items.isEmpty()) {
         String item = items.pop();
         if (item.equals("(")) {
            handleOpeningParenthesis(root, items);
         } else if (item.equals(",")) {
            handleComma(root, items);
            return root;
         } else if (item.equals(")")) {
            handleClosingParenthesis(root);
            return root;
         } else {
            appendNodeName(root, item);
         }
      }
      validateFinalNode(root);
      return root;
   }

   // Handles the opening parenthesis in the tree.
   private static void handleOpeningParenthesis(Node root, LinkedList<String> items) {
      root.firstChild = buildTree(new Node(null, null, null), items);
      if (!items.isEmpty() && root.name != null) {
         throw new RuntimeException("Node has no parent! Check " + sb);
      }
   }

   // Handles the comma in the tree.
   private static void handleComma(Node root, LinkedList<String> items) {
      root.nextSibling = buildTree(new Node(null, null, null), items);
      if (root.name == null) {
         throw new RuntimeException("Empty node or invalid comma usage. Check " + sb);
      }
   }

   // Validates the closing parenthesis.
   private static void handleClosingParenthesis(Node root) {
      if (root.name == null) {
         throw new RuntimeException("Empty node or invalid parentheses usage. Check " + sb);
      }
   }

   // Appends the item to the node name.
   private static void appendNodeName(Node root, String item) {
      if (root.name == null) {
         root.name = item;
      } else {
         root.name += item;
      }
   }

   // Validates the final node of the tree.
   private static void validateFinalNode(Node root) {
      if (root.name == null) {
         throw new RuntimeException("Empty node or invalid comma usage.");
      }
   }

   // Generates the left parenthetic representation of the tree.
   public String leftParentheticRepresentation() {
      StringBuilder stringBuilder = new StringBuilder();
      buildParentheticRepresentation(this, stringBuilder);
      return stringBuilder.toString();
   }

   // Helper method to build the parenthetic representation.
   private static void buildParentheticRepresentation(Node root, StringBuilder stringBuilder) {
      stringBuilder.append(root.name);
      if (root.firstChild != null) {
         stringBuilder.append("(");
         buildParentheticRepresentation(root.firstChild, stringBuilder);
         stringBuilder.append(")");
      }
      if (root.nextSibling != null) {
         stringBuilder.append(",");
         buildParentheticRepresentation(root.nextSibling, stringBuilder);
      }
   }

   // Main method for testing.
   public static void main (String[] param) {
      String s = "(B1,C)A";
      Node t = Node.parsePostfix (s);
      String v = t.leftParentheticRepresentation();
      System.out.println (s + " ==> " + v); // (B1,C)A ==> A(B1,C)
   }
}

