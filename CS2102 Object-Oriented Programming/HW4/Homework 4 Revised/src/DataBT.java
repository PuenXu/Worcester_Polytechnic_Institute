
class DataBT implements IBinTree {
 int data;
 IBinTree left;
 IBinTree right;

 DataBT(int data, IBinTree left, IBinTree right) {
  this.data = data;
  this.left = left;
  this.right = right;
 }
 
 // an alternate constructor for when both subtrees are empty
 DataBT(int data) {
   this.data = data;
   this.left = new MtBT();
   this.right = new MtBT();
 }

 // determines whether this node or node in subtree has given element
 public boolean hasElt(int e) {
  return this.data == e || this.left.hasElt(e) || this.right.hasElt(e) ;
 }

 // adds 1 to the number of nodes in the left and right subtrees
 public int size() {
  return 1 + this.left.size() + this.right.size();
 }

 // adds 1 to the height of the taller subtree
 public int height() {
  return 1 + Math.max(this.left.height(), this.right.height());
 }
 
 ///////////////////////////////////////////////////////
 /**
  * returns true if the input data is the smallest in this tree
  * @param data a value to compare
  * @return true if the input data is the smallest in this tree
  */
 public boolean isSmallest(int data) {
	 return data <= this.data &&
			this.left.isSmallest(data) &&
			this.right.isSmallest(data);
 }
 
 /**
  * returns true if the binary tree is an heap
  */
 public boolean isHeap() {
	 return this.isSmallest(this.data) &&
			this.left.isHeap() &&
			this.right.isHeap();
 }
 
 /**
  * checks if all elements in this tree are in the input tree
  * @param aTree a tree to compare
  * @return true if all elements in this tree are in the input tree
  */
 public boolean allElementsExistIn(IBinTree aTree) {
	 return aTree.hasElt(this.data) &&
			this.left.allElementsExistIn(aTree) &&
			this.right.allElementsExistIn(aTree);
 }
 
 /**
  * checks if the smallest elt in this tree is in the input tree
  * @param aTree a tree to compare
  * @return true if the smallest elt in this tree is in the input tree
  */
 public boolean smallestEltExistIn(IBinTree aTree) {
	 return aTree.hasElt(this.data);
 }
 ///////////////////////////////////////////////////////
}