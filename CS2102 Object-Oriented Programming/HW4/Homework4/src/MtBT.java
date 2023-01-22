class MtBT implements IBinTree {
 MtBT(){}

 // returns false since empty tree has no elements
 public boolean hasElt(int e) {
  return false;
 }

 // returns 0 since empty tree has no elements
 public int size() {
  return 0;
 }

 // returns 0 since empty tree has no branches
 public int height() {
  return 0;
 }
 
 /**
  * returns true because the heap is empty
  */
 public boolean isEmptyHeap() {
	 return true;
 }
 
 /**
  * returns the root of an empty tree, which would be null
  */
 public Integer getRoot() {
	 return null;
 }
 
 /**
  * get the left subtree of an empty tree
  * @return the left subtree, which is empty
  */
 public IBinTree getLeftTree() {
	 return new MtBT();
 }
 
 /**
  * get the right subtree of an empty tree
  * @return the right subtree, which is empty
  */
 public IBinTree getRightTree() {
	 return new MtBT();
 }
 
 /**
  * returns true because an empty tree is a valid heap, base case
  */
 public boolean isHeap() {
	 return true;
 }
 
 /**
  * checks if all elements in the array are in the binary tree
  * @return true since the binary tree is empty
  */
 public boolean hasAllElements(IBinTree aTree) {
	 if(aTree.isEmptyHeap()) {
		 return true;
	 }
	 else {
		 return false;
	 }
 }
}
