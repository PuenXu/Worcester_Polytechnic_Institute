
import java.lang.Math;

interface IBinTree {
 // determines whether element is in the tree
 boolean hasElt(int e);
 // returns number of nodes in the tree; counts duplicate elements as separate items
 int size();
 // returns depth of longest branch in the tree
 int height();
 
 ///////////////////////////////////////////////////////
 boolean isSmallest(int data);
 boolean isHeap();
 boolean allElementsExistIn(IBinTree aTree);
 boolean smallestEltExistIn(IBinTree aTree);
 ///////////////////////////////////////////////////////
}
