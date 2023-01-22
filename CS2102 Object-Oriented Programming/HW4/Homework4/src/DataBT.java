import java.lang.Math;

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
 
 /**
  * returns true if the heap or binary tree is empty
  */
 public boolean isEmptyHeap() {
	 return this.size() == 0;
 }
 
 /**
  * returns the root of a tree
  */
 public Integer getRoot() {
	 return this.data;
 }
 
 /**
  * get the left subtree of a tree
  * @return the left subtree
  */
 public IBinTree getLeftTree() {
	 return this.left;
 }
 
 /**
  * get the right subtree of a tree
  * @return the right subtree
  */
 public IBinTree getRightTree() {
	 return this.right;
 }
 
 /**
  * returns true if the binary tree is an heap
  */
 public boolean isHeap() {
	 if(this.left.isEmptyHeap() && this.right.isEmptyHeap()) {
		 return true;
	 }
	 else if(this.left.isEmptyHeap()) {
		 return this.data < this.right.getRoot();
	 }
	 else if(this.right.isEmptyHeap()) {
		 return this.data < this.left.getRoot();
	 }
	 else {
		 return this.left.isHeap() && this.right.isHeap() && this.data < this.left.getRoot() && this.data < this.right.getRoot();
	 }
 }
 
 /**
  * checks if all elements in the array are in the binary tree
  * @return true if all elements in the array are in the binary tree
  */
 public boolean hasAllElements(IBinTree aTree) {
	 if(aTree.isEmptyHeap()) {
		 return true;
	 }
	 return (this.hasElt(aTree.getRoot()) && 
				this.hasAllElements(aTree.getLeftTree()) &&
				this.hasAllElements(aTree.getRightTree()));
 }
}