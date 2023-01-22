
interface IBinTree {
 // determines whether element is in the tree
 boolean hasElt(int e);
 // returns number of nodes in the tree; counts duplicate elements as separate items
 int size();
 // returns depth of longest branch in the tree
 int height();
 // returns true if an heap or a binary tree is empty
 public boolean isEmptyHeap();
 // returns the root of a tree
 public Integer getRoot();
 //returns the left subtree
 public IBinTree getLeftTree();
 // returns the right subtree
 public IBinTree getRightTree();
 // returns true if a binary tree is a valid heap
 public boolean isHeap();
 // returns true if all elements in the array are in the binary tree
 public boolean hasAllElements(IBinTree aTree);
}
