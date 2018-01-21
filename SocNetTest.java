/**
  Program to perform basic testing of the SocNet class.  Expected output is below.
   
   The most popular one is caveman
   The top follower is daredevil
   The group leaders are: [bond, caveman]
   Graph density: 0.58
   Graph reciprocity: 0.29
   Shortest path from antman to caveman has 1 edge(s): [antman|caveman]
   Shortest path from bond to daredevil has 2147483647 edge(s): [NONE]
   Centrality of antman is 1.00 
   Users reachable from antman: [caveman, daredevil, bond]
   Centrality of bond is 1431655765.00 
   Users reachable from bond: [caveman]
   Centrality of caveman is 2147483647.00 
   Users reachable from caveman: []
   Centrality of daredevil is 1.00 
   Users reachable from daredevil: [caveman, antman, bond] 
*/
import java.util.*;
public class SocNetTest
{     
   public static void main (String[] args)
   {
      SocNet spynet = new SocNet("spies.txt");
      String[] spies = {"antman", "bond", "caveman", "daredevil" };
      
      System.out.println("The most popular one is " + spynet.mostPopular());
      System.out.println("The top follower is " + spynet.topFollower());
      System.out.println("The group leaders are: " + spynet.leaders());
      System.out.printf("Graph density: %4.2f\n", spynet.density());
      System.out.printf("Graph reciprocity: %4.2f\n", spynet.reciprocity());
      System.out.printf("Shortest path from %s to %s has %d edge(s): %s\n", 
                         spies[0], spies[2], spynet.distance(spies[0], spies[2]), 
                         spynet.path(spies[0], spies[2]));

      System.out.printf("Shortest path from %s to %s has %d edge(s): %s\n", 
                         spies[1], spies[3], spynet.distance(spies[1], spies[3]), 
                         spynet.path(spies[1], spies[3]));
      for (String spy: spies)
      {
         System.out.printf("Centrality of %s is %4.2f \n", spy, spynet.centrality(spy));
         System.out.printf("Users reachable from %s: %s\n", spy, spynet.reachable(spy));
      }
    }
}
