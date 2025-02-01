import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

/** Testklass.
 * @author jaanus, Tanel Paal
 */
public class GraphTaskTest {

   @Test (timeout=20000)
   public void test1() { 
      GraphTask.main (null);
      assertTrue ("There are no tests", true);
   }

   @Test
   public void testEmptyGraph() {
      GraphTask.Graph g = new GraphTask().new Graph("EmptyGraph");
      try {
         int diameter = g.findDiameter();
         System.out.println("Diameter of empty graph: " + diameter);
         assertEquals("Diameter of an empty graph should be 0", 0, diameter);
      } catch (Exception e) {
         throw new RuntimeException("Failed to find diameter of empty graph", e);
      }
   }

   @Test
   public void testSmallGraph() {
      GraphTask.Graph g = new GraphTask().new Graph("SmallGraph");
      g.createRandomSimpleGraph(3, 3);
      int diameter = g.findDiameter();
      System.out.println("Diameter of small graph: " + diameter);
      assertTrue("Diameter should be a non-negative integer", diameter >= 0);
   }

   @Test
   public void testMediumGraph() {
      GraphTask.Graph g = new GraphTask().new Graph("MediumGraph");
      g.createRandomSimpleGraph(100, 150);
      int diameter = g.findDiameter();
      System.out.println("Diameter of medium graph: " + diameter);
      assertTrue("Diameter should be a non-negative integer", diameter >= 0);
   }

   @Test (timeout=20000)
   public void testLargeGraph() {
      GraphTask.Graph g = new GraphTask().new Graph("LargeGraph");
      g.createRandomSimpleGraph(2000, 3000);
      int diameter = g.findDiameter();
      System.out.println("Diameter of large graph: " + diameter);
      assertTrue("Diameter should be a non-negative integer", diameter >= 0);
   }

   @Test
   public void testSingleVertexGraph() {
      GraphTask.Graph g = new GraphTask().new Graph("SingleVertexGraph");
      g.createRandomSimpleGraph(1, 0);
      int diameter = g.findDiameter();
      System.out.println("Diameter of single vertex graph: " + diameter);
      assertEquals("Diameter of a single vertex graph should be 0", 0, diameter);
   }

   @Test
   public void testDisconnectedGraph() {
      GraphTask.Graph g = new GraphTask().new Graph("DisconnectedGraph");
      g.createVertex("v1");
      g.createVertex("v2");
      int diameter = g.findDiameter();
      System.out.println("Diameter of disconnected graph: " + diameter);
      assertEquals("Diameter of a disconnected graph should be 0", 0, diameter);
   }

   @Test
   public void testGraphWithDiameterTwo() {
      GraphTask.Graph g = new GraphTask().new Graph("GraphWithDiameterTwo");
      GraphTask.Vertex v1 = g.createVertex("A");
      GraphTask.Vertex v2 = g.createVertex("B");
      GraphTask.Vertex v3 = g.createVertex("C");
      g.createArc("A_B", v1, v2);
      g.createArc("B_A", v2, v1);
      g.createArc("B_C", v2, v3);
      g.createArc("C_B", v3, v2);
      int diameter = g.findDiameter();
      System.out.println("Diameter of graph: " + diameter);
      assertEquals("Diameter should be 2", 2, diameter);
      System.out.println("Vertices with diameter 2: " + v1 + " and " + v3);
   }


}

