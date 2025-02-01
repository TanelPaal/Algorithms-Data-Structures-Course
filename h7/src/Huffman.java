// Kasutatud materjal:
// https://www.geeksforgeeks.org/huffman-coding-greedy-algo-3/
// https://en.wikipedia.org/wiki/Huffman_coding#:~:text=In%20computer%20science%20and%20information,D.

import java.util.*;

/**
 * Prefix codes and Huffman tree.
 * Tree depends on source data.
 */
public class Huffman {

   // Node class represents each node in the Huffman tree
   private class Node implements Comparable<Node> {
      public Node left, right;
      public Byte symbol = null;
      public int freq;

      // Constructor for leaf nodes
      public Node(byte symbol, int freq) {
         this.symbol = symbol;
         this.freq = freq;
      }

      // Constructor for internal nodes
      public Node(Node left, Node right, int freq) {
         this.left = left;
         this.right = right;
         this.freq = freq;
      }

      @Override
      public int compareTo(Node o) {
         return Integer.compare(this.freq, o.freq);
      }
   }

   private Node huffmanTree; // Root of the Huffman tree
   private HashMap<Byte, Integer> charFreqs; // Frequency of each character
   private HashMap<Byte, String> huffmanCodes; // Huffman codes for each character

   /** Constructor to build the Huffman code for a given bytearray.
    * @param original source data
    */
   Huffman (byte[] original) {
      // Calculate the frequency of each character in the input data
      calculateCharFrequencies(original);
      // Build the Huffman tree based on character frequencies
      buildHuffmanTree(this.charFreqs);
      // Generate Huffman codes for each character
      createHuffmanCodes(this.huffmanTree);
   }

   // Calculate the frequency of each character in the input data
   private void calculateCharFrequencies(byte[] original) {
      charFreqs = new HashMap<>();
      for (byte b : original) {
         charFreqs.put(b, charFreqs.getOrDefault(b, 0) + 1);
      }
   }

   // Build the Huffman tree based on character frequencies
   private void buildHuffmanTree(HashMap<Byte, Integer> frequencies) {
      PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

      // Create a leaf node for each character and add it to the priority queue
      for (Map.Entry<Byte, Integer> entry : charFreqs.entrySet()) {
         priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
      }

      // Combine nodes until a single root node is left
      while (priorityQueue.size() > 1) {
         Node left = priorityQueue.poll();
         Node right = priorityQueue.poll();
         priorityQueue.add(new Node(left, right, left.freq + right.freq));
      }

      huffmanTree = priorityQueue.poll();
   }

   // Generate Huffman codes for each character by traversing the tree
   private void createHuffmanCodes(Node node, String code) {
      if (node == null) return;

      // Leaf node: assign code
      if (node.left == null && node.right == null) {
         huffmanCodes.put(node.symbol, code.isEmpty() ? "0" : code);
      } else {
         createHuffmanCodes(node.left, code + "0");
         createHuffmanCodes(node.right, code + "1");
      }
   }

   // Wrapper method to start the recursive generation of Huffman codes
   private void createHuffmanCodes(Node huffman) {
      this.huffmanCodes = new HashMap<>();
      createHuffmanCodes(huffman, "");
   }

   /** Length of encoded data in bits.
    * @return number of bits
    */
   public int bitLength() {
      int bitLength = 0;
      for (Map.Entry<Byte, Integer> entry : charFreqs.entrySet()) {
         bitLength += entry.getValue() * huffmanCodes.get(entry.getKey()).length();
      }
      return bitLength;
   }

   /** Encoding the byte array using this prefixcode.
    * @param origData original data
    * @return encoded data
    */
   public byte[] encode (byte[] origData) {
      byte[] encodedData = new byte[origData.length];

      // Encode each byte of the original data using the Huffman codes
      int i = 0;
      for (byte b : origData) {
         encodedData[i] = Byte.parseByte(this.huffmanCodes.get(b), 2);
         i++;
      }

      return encodedData;
   }

   /** Decoding the byte array using this prefixcode.
    * @param encodedData encoded data
    * @return decoded data (hopefully identical to original)
    */
   public byte[] decode (byte[] encodedData) {
      byte[] decodedData = new byte[encodedData.length];

      // Decode each byte of the encoded data by traversing the Huffman tree
      int i = 0;
      for (byte b : encodedData) {
         Huffman.Node currentNode = this.huffmanTree;

         // If there is only one node, it means the tree has only one character
         if (currentNode.left == null && currentNode.right == null) {
            decodedData[i] = currentNode.symbol;
            i++;
            continue;
         }

         // Traverse the tree based on the bits of the encoded byte
         String bString = Integer.toBinaryString(b);
         for (char bit : bString.toCharArray()) {
            currentNode = (bit == '1') ? currentNode.right : currentNode.left;
         }
         decodedData[i] = currentNode.symbol;
         i++;
      }

      return decodedData;
   }

   // Method to display the frequency of each character
   public void displayCharFrequencies() {
      for (Map.Entry<Byte, Integer> entry : this.charFreqs.entrySet()) {
         System.out.println((char) entry.getKey().byteValue() + ": " + entry.getValue());
      }
   }

   /** Main method. */
   public static void main (String[] params) {
      String tekst = "AAAAAAAAAAAAABBBBBBCCCDDEEF";
      byte[] orig = tekst.getBytes();
      Huffman huf = new Huffman(orig);
      byte[] kood = huf.encode (orig);
      byte[] orig2 = huf.decode (kood);
      // must be equal: orig, orig2
      System.out.println (Arrays.equals (orig, orig2));
      int lngth = huf.bitLength();
      System.out.println ("Length of encoded data in bits: " + lngth);
      // TODO!!! Your tests here!
      huf.displayCharFrequencies(); // Display character frequencies

      // Test with a longer text
      String text = "AAAAAAAAAAAAAAAAAAAAAAAAAAAADDDDDDDDDDDDDDDDDDDDDDDDDDSSSSSSSSSSSSSSSSSSSEEFFFFFFF";
      byte[] original = text.getBytes();
      Huffman huffman = new Huffman(original);
      byte[] encoded = huffman.encode(original);
      byte[] decoded = huffman.decode(encoded);
      System.out.println(Arrays.equals(original, decoded)); // Must be true
      int bitLength = huffman.bitLength();
      System.out.println("Length of encoded data in bits: " + bitLength);
      huffman.displayCharFrequencies(); // Display character frequencies
   }
}

