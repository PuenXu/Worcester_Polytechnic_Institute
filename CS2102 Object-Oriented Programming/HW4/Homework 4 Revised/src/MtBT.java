
class MtBT implements IBinTree {
 MtBT(){}

 // returns false since empty tree has no elements
 public boolean hasElt(int e) {
  return false;
 }

 // returns 0 since enpty tree has no elements
 public int size() {
  return 0;
 }

 // returns 0 since empty tree has no branches
 public int height() {
  return 0;
 }
 
 ///////////////////////////////////////////////////////
 /**
  * returns true if the input data is the smallest in this tree
  * @param data a value to compare
  * @return true if the input data is the smallest in this tree
  */
 public boolean isSmallest(int data) {
	 return true;
 }
 
 /**
  * returns true if the binary tree is an heap
  */
 public boolean isHeap() {
	 return true;
 }
 
 /**
  * checks if all elements in this tree are in the input tree
  * @param aTree a tree to compare
  * @return true if all elements in this tree are in the input tree
  */
 public boolean allElementsExistIn(IBinTree aTree) {
	 return true;
 }
 
 /**
  * checks if the smallest elt in this tree is in the input tree
  * @param aTree a tree to compare
  * @return true if the smallest elt in this tree is in the input tree
  */
 public boolean smallestEltExistIn(IBinTree aTree) {
	 return false;
 }
 ///////////////////////////////////////////////////////
}
